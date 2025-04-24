package com.java.project.services;

import com.java.project.configs.VNPayConfig;
import com.java.project.dtos.HoaDonChiTietResponse;
import com.java.project.entities.*;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.helper.HDPTTTHelper;
import com.java.project.repositories.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class VNPaymentService {
    BanHangOnlineService banHangOnlineService;

    HoaDonRepository hoaDonRepository;

    HoaDonChiTietRepository hoaDonChiTietRepository;

    SanPhamChiTietRepository sanPhamChiTietRepository;

    PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    HoaDonPhuongThucThanhToanRepository hoaDonPhuongThucThanhToanRepository;

    PhieuGiamGiaRepository phieuGiamGiaRepository;


    public String generatePaymentUrl(String maHoaDon) throws UnsupportedEncodingException {
        // Lấy hóa đơn từ database
        HoaDon hoaDon = hoaDonRepository.findByMaHoaDon(maHoaDon)
                .orElseThrow(() -> new EntityNotFoundException("Hóa đơn không tồn tại"));

        // Tổng tiền đơn hàng

        long totalAmount = (hoaDon.getTongTien().longValue()) + hoaDon.getPhiShip().longValue() - tinhTienGiam(hoaDon) ;
        long amount = totalAmount * 100;

        // Tạo tham số VNPay
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", ""); // Ngân hàng mặc định
        vnp_Params.put("vnp_TxnRef", maHoaDon);
        vnp_Params.put("vnp_OrderInfo", "Thanh toan hoa don " + maHoaDon);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", "127.0.0.1");
        vnp_Params.put("vnp_OrderType", "other");

        // Lấy thời gian tạo hóa đơn và hạn thanh toán
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String vnp_CreateDate = hoaDon.getNgayTao().atZone(java.time.ZoneId.systemDefault()).format(formatter);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        LocalDateTime expireTime = LocalDateTime.now().plusMinutes(15);
        String vnp_ExpireDate = expireTime.format(formatter);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        // Tạo chuỗi hash và URL thanh toán
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);

        for (String fieldName : fieldNames) {
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && !fieldValue.isEmpty()) {
                hashData.append(fieldName).append('=').append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII)).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (!fieldName.equals(fieldNames.get(fieldNames.size() - 1))) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        // Tạo Secure Hash
        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        // Trả về URL thanh toán
        return VNPayConfig.vnp_PayUrl + "?" + query.toString();
    }

    public String handleVnpayCallback(Map<String, String> payload) throws Exception {
        String vnpTxnRef = payload.get("vnp_TxnRef"); // Mã hóa đơn
        String vnpResponseCode = payload.get("vnp_ResponseCode"); // Trạng thái giao dịch
        String vnpAmount = payload.get("vnp_Amount"); // Tổng tiền

        if (vnpTxnRef == null || vnpResponseCode == null) {
            throw new IllegalArgumentException("Thiếu thông tin 'vnp_TxnRef' hoặc 'vnp_ResponseCode'.");
        }

        // Lấy thông tin hóa đơn từ DB
        HoaDon hoaDon = hoaDonRepository.findByMaHoaDon(vnpTxnRef)
                .orElseThrow(() -> new EntityNotFoundException("Hóa đơn không tồn tại."));
        BigDecimal tongTienSanPham =
                BigDecimal.valueOf(hoaDon.getTongTien() + hoaDon.getPhiShip() - tinhTienGiam(hoaDon));

        // Chuyển đổi số tiền từ VNPay về VNĐ
        BigDecimal amountFromVNPay = new BigDecimal(vnpAmount).divide(BigDecimal.valueOf(100));

        // Kiểm tra số tiền có khớp không
        if (tongTienSanPham.compareTo(amountFromVNPay) != 0) {
            throw new IllegalArgumentException("Số tiền thanh toán không khớp với hóa đơn.");
        }

        // Nếu giao dịch thành công
        if ("00".equals(vnpResponseCode)) {
            hoaDon.setTrangThaiGiaoHang(1); // Chuyển ngay trạng thái giao hàng là chờ xác nhận
            hoaDon.setTrangThai(1); // Chuyn tran thái hóa đơn là đã thanh toans
            processPaymentAndUpdateStock(hoaDon);
            addTotal(hoaDon.getId(), amountFromVNPay);
            hoaDonRepository.save(hoaDon);
            return "Giao dịch thành công";
        } else {
            return "Giao dịch thất bại, mã lỗi: " + vnpResponseCode;
        }
    }

    @Transactional
    protected void processPaymentAndUpdateStock(HoaDon hoaDon) {
        List<HoaDonChiTiet> list = hoaDonChiTietRepository.findByHoaDon_Id(hoaDon.getId());

        List<SanPhamChiTiet> sanPhamUpdate = new ArrayList<>();

        for(HoaDonChiTiet hoaDonChiTiet : list) {
            hoaDonChiTiet.setTrangThai(1);

            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();

            if(sanPhamChiTiet == null) continue;

            int soLuongMoi = sanPhamChiTiet.getSoLuong() - hoaDonChiTiet.getSoLuong();
            if(soLuongMoi < 0){
                throw new IllegalArgumentException("Số lượng ở trong kho không đủ");
            }
            sanPhamChiTiet.setSoLuong(soLuongMoi);
            sanPhamUpdate.add(sanPhamChiTiet);
        }

        hoaDonChiTietRepository.saveAll(list);
        sanPhamChiTietRepository.saveAll(sanPhamUpdate);

    }

    // Lưu lại thanh toán
    @Transactional
    protected void addTotal (Integer idHoaDon, BigDecimal total) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm tấy hóa đơn với mã là:" + idHoaDon));

        PhuongThucThanhToan pttt = phuongThucThanhToanRepository.findById(4)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy pttt"));

        HoaDonPhuongThucThanhToan hdpttt = new HoaDonPhuongThucThanhToan();
        hdpttt.setHoaDon(hoaDon);
        hdpttt.setMaGiaoDich(HDPTTTHelper.createHDPTTTHelper());
        hdpttt.setPhuongThucThanhToan(pttt);
        hdpttt.setNgayThucHienThanhToan(LocalDate.now());
        hdpttt.setSoTienThanhToan(total);

        hoaDonPhuongThucThanhToanRepository.save(hdpttt);
    }

    private long tinhTienGiam (HoaDon hoaDon) {
        PhieuGiamGia phieuGiamGia = hoaDon.getPhieuGiamGia();

        if(phieuGiamGia == null) return 0;

        Double tongTien = hoaDon.getTongTien();
        long soTienGiam = 0;

        if(phieuGiamGia.getHinhThucGiamGia() == 0) {
            soTienGiam = Math.round(tongTien * phieuGiamGia.getGiaTriGiam() / 100);
        } else {
            soTienGiam = Math.round(phieuGiamGia.getGiaTriGiam());
        }

        if(phieuGiamGia.getSoTienGiamToiDa() != null) {
            soTienGiam = Math.min(soTienGiam, phieuGiamGia.getSoTienGiamToiDa().longValue());
        }

        return soTienGiam;
    }

    public String generateHtml(String title, String message, String content) {
        return "<!DOCTYPE html>" +
                "<html lang=\"vi\">" +
                "<head>" +
                "<meta charset=\"UTF-8\">" +
                "<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "<title>" + title + "</title>" +
                "<link href=\"https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css\" rel=\"stylesheet\">" +
                "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css\">" +
                "<style>" +
                "  @import url('https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap');" +
                "  body { font-family: 'Inter', sans-serif; }" +
                "  .bg-orange { background-color: #FF7F00; }" +
                "  .text-orange { color: #FF7F00; }" +
                "  .border-orange { border-color: #FF7F00; }" +
                "  .btn-hover:hover { background-color: #FF6200; }" +
                "</style>" +
                "</head>" +
                "<body class=\"bg-gray-50\">" +
                "<div class=\"min-h-screen flex items-center justify-center p-6\">" +
                "  <div class=\"max-w-2xl w-full bg-white rounded-lg shadow-lg overflow-hidden\">" +
                "    <div class=\"bg-orange p-6 flex items-center justify-center\">" +
                "      <i class=\"fas fa-exclamation-circle text-white text-5xl\"></i>" +
                "    </div>" +
                "    <div class=\"p-8 text-center\">" +
                "      <h1 class=\"text-4xl font-bold text-orange mb-4\">" + title + "</h1>" +
                "      <h2 class=\"text-2xl font-semibold text-gray-800 mb-6\">" + message + "</h2>" +
                "      <div class=\"my-8 text-lg text-gray-600\">" +
                "        <p>" + content + "</p>" +
                "      </div>" +
                "      <div class=\"mt-10\">" +
                "        <a href=\"http://localhost:5173/\" class=\"bg-orange text-white px-10 py-4 rounded-lg font-medium inline-block btn-hover transition duration-300\">" +
                "          <i class=\"fas fa-home mr-2\"></i> Trở lại trang chủ" +
                "        </a>" +
                "      </div>" +
                "    </div>" +
                "    <div class=\"bg-gray-50 py-4 text-center text-gray-500 text-sm border-t border-gray-100\">" +
                "      <p>© 2025 - Thông báo hệ thống</p>" +
                "    </div>" +
                "  </div>" +
                "</div>" +
                "</body>" +
                "</html>";
    }

}
