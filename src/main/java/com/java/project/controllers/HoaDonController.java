package com.java.project.controllers;

import com.java.project.dtos.HoaDonBanHangResponse;
import com.java.project.dtos.HoaDonResponse;
import com.java.project.services.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hoa-don")
@CrossOrigin(origins = "*")
public class HoaDonController {

    @Autowired
    HoaDonService hoaDonService;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * API hiển thị danh sách hóa đơn (có phân trang)
     */
    @GetMapping("/hien-thi")
    public Map<String, Object> hienThi(
            @RequestParam(value = "p", defaultValue = "0") int p,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(required = false) Integer loaiDon
    ) {
        return getHoaDonData(p, size,null, null, null, loaiDon, null, false);
    }

    /**
     * API tìm kiếm hóa đơn theo ngày, loại đơn, từ khóa (có phân trang)
     */
    @GetMapping("/search")
    public Map<String, Object> search(
            @RequestParam(value = "p", defaultValue = "0") int p,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(required = false) Integer trangThaiGiaoHang,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String ngayBatDau,
            @RequestParam(required = false) String ngayKetThuc,
            @RequestParam(required = false) Integer loaiDon
    ) {
        LocalDate startDate = parseDate(ngayBatDau);
        LocalDate endDate = parseDate(ngayKetThuc);

        return getHoaDonData(p, size,trangThaiGiaoHang, startDate, endDate, loaiDon, keyword, true);
    }

    /**
     * Phương thức xử lý chung cho `hienThi()` và `search()`
     */
    private Map<String, Object> getHoaDonData(
            int p, int size,Integer trangThaiGiaoHang, LocalDate ngayBatDau, LocalDate ngayKetThuc,
            Integer loaiDon, String keyword, boolean isSearch
    ) {
        List<HoaDonResponse> listHD;
        Pageable pageable = PageRequest.of(p, size);

        if (size == -1) {
            // Nếu size = -1 -> Lấy tất cả dữ liệu không phân trang
            
            listHD = isSearch
                    ? hoaDonService.getAllSearch(trangThaiGiaoHang, keyword, ngayBatDau, ngayKetThuc, loaiDon)
                    : hoaDonService.getAllHoaDon(loaiDon);
        } else {
            // Phân trang nếu size khác -1
            Page<HoaDonResponse> pageHD = isSearch
                    ? hoaDonService.getPhanTrangSearch(pageable,trangThaiGiaoHang, keyword, ngayBatDau, ngayKetThuc, loaiDon)
                    : hoaDonService.GetPhanTrangHoaDon(pageable, loaiDon);

            listHD = pageHD.getContent();

            return buildResponse(listHD, pageHD.getTotalPages(), pageHD.getTotalElements(), pageHD.getNumber(), pageHD.getSize());
        }

        return buildResponse(listHD, 1, listHD.size(), 0, listHD.size());
    }

    /**
     * Chuyển đổi dữ liệu từ HoaDonResponse thành Map<String, Object>
     */
    private Map<String, Object> convertToMap(HoaDonResponse hoaDonResponse) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("id", hoaDonResponse.getId());
        map.put("tenKhachHang", hoaDonResponse.getTenKhachHang());
        map.put("maNhanVien", hoaDonResponse.getMaNhanVien());
        map.put("maHoaDon", hoaDonResponse.getMaHoaDon());
        map.put("loaiDon", hoaDonResponse.getLoaiDon() == 0 ? "Online" : "Tại quầy");
        map.put("ghiChu", hoaDonResponse.getGhiChu());
        map.put("hoTenNguoiNhan", hoaDonResponse.getHoTenNguoiNhan());
        map.put("soDienThoai", hoaDonResponse.getSoDienThoai());
        map.put("email", hoaDonResponse.getEmail());
        map.put("diaChiNhanHang", hoaDonResponse.getDiaChiNhanHang());
        map.put("ngayMongMuonNhanHang", hoaDonResponse.getNgayNhanMongMuon());
        map.put("ngayDuKienNhan", hoaDonResponse.getNgayDuKienNhan());
        map.put("trangThaiGiaoHang", getTrangThaiGiaoHang(hoaDonResponse.getTrangThaiGiaoHang()));
        map.put("phiShip", hoaDonResponse.getPhiShip());
        if (hoaDonResponse.getTongTien() != null) {
            map.put("tongTien", NumberFormat.getInstance(new Locale("vi", "VN")).format(hoaDonResponse.getTongTien()));
        } else {
            map.put("tongTien", 0);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss dd/MM/yyyy");
        map.put("ngayTao", hoaDonResponse.getNgayTao().format(formatter));
        map.put("trangThai", hoaDonResponse.getTrangThai() == 0 ? "Chưa Thanh Toán"
                :hoaDonResponse.getTrangThai() == 1 ? "Đã Thanh Toán":"Đã hủy");
        return map;
    }

    /**
     * Hàm trả về chuỗi trạng thái giao hàng
     */
    private String getTrangThaiGiaoHang(int status) {
        return switch (status) {
            case 1 -> "Chờ Xác Nhận";
            case 2 -> "Xác Nhận";
            case 3 -> "Chờ Vận Chuyển";
            case 4 -> "Vận Chuyển";
            case 5 -> "Thành Công";
            case 6 -> "Hoàn Hàng";
            case 7 -> "Đã Hủy";
            case 9 -> "Đã thanh toán";
            default -> "Tạo hóa đơn";
        };
    }

    /**
     * Chuyển đổi dữ liệu thành response JSON
     */
    private Map<String, Object> buildResponse(List<HoaDonResponse> listHD, int totalPages, long totalElements, int currentPage, int size) {
        List<Map<String, Object>> mapList = listHD.stream().map(this::convertToMap).collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("content", mapList);
        response.put("totalElements", totalElements);
        response.put("currentPage", currentPage);
        response.put("totalPages", totalPages);
        response.put("size", size);

        return response;
    }

    /**
     * Chuyển đổi String sang LocalDate
     */
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        return LocalDate.parse(dateStr, dateFormatter);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getOrderCounts(
            @RequestParam(required = false) String ngayBatDau,
            @RequestParam(required = false) String ngayKetThuc,
            @RequestParam(required = false) Integer loaiDon
    ) {

        // Nếu không có ngày, mặc định lấy dữ liệu hôm nay
        LocalDate startDate = (ngayBatDau != null && !ngayBatDau.isEmpty())
                ? LocalDate.parse(ngayBatDau)
                : LocalDate.now();

        LocalDate endDate = (ngayKetThuc != null && !ngayKetThuc.isEmpty())
                ? LocalDate.parse(ngayKetThuc)
                : LocalDate.now();

        Map<String, Long> counts = hoaDonService.getOrderCounts(startDate, endDate, loaiDon);
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/{maHoaDon}")
    public ResponseEntity<HoaDonBanHangResponse> getHoaDonByMaHoaDon(@PathVariable String maHoaDon) {
        Optional<HoaDonBanHangResponse>hoaDon = hoaDonService.getHoaDonByMaHoaDon(maHoaDon);
        if(hoaDon.isPresent()) {
            return ResponseEntity.ok(hoaDon.get());
        }else {
            return ResponseEntity.notFound().build();
        }
    }
}
