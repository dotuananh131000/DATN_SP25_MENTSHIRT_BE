package com.java.project.services;

import com.java.project.dtos.HoaDonResponse;
import com.java.project.dtos.PhieuGiamGiaDto;
import com.java.project.entities.HoaDon;
import com.java.project.entities.HoaDonChiTiet;
import com.java.project.entities.PhieuGiamGia;
import com.java.project.entities.SanPhamChiTiet;
import com.java.project.helper.HoaDonHelper;
import com.java.project.mappers.HoaDonMapper;
import com.java.project.mappers.PhieuGiamGiaMapper;
import com.java.project.repositories.*;
import com.java.project.request.HoaDonChiTietModel;
import com.java.project.request.HoaDonModel;
import com.java.project.request.HoaDonRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BanHangOnlineService {
    PhieuGiamGiaRepository phieuGiamGiaRepository;
    KhachHangRepository khachHangRepository;
    HoaDonRepository hoaDonRepository;
    HoaDonMapper hoaDonMapper;
    SanPhamChiTietRepository sanPhamChiTietRepository;
    HoaDonChiTietRepository hoaDonChiTietRepository;


    public List<PhieuGiamGia> getPhieuGiamGiaByKH(Integer idKH) {
        List<PhieuGiamGia> listPhieuGiamGia = phieuGiamGiaRepository.findPhieuGiamGiaByKhachHang(idKH);
        return listPhieuGiamGia;
    }

    // Lấy phiếu giảm giá tốt nhất cho khách hàng
    @Transactional
    public PhieuGiamGia theBestVoucher (Integer idKH, Integer idHD, Double tongTien) {
        List<PhieuGiamGia> list = getPhieuGiamGiaByKH(idKH);

        HoaDon hoaDon = hoaDonRepository.findById(idHD)
                .orElseThrow(() -> new EntityNotFoundException("Order not found by id" + idHD));

        // Trả lại phiếu giảm gias trước đó khi cập nhật phiếu giảm giá tốt nhất
        PhieuGiamGia phieuGiamGia = hoaDon.getPhieuGiamGia();
        if(phieuGiamGia != null) {
            phieuGiamGia.setSoLuong(phieuGiamGia.getSoLuong() + 1);
            hoaDon.setPhieuGiamGia(null);
            phieuGiamGiaRepository.save(phieuGiamGia);
        }

        PhieuGiamGia phieuGiamGiaTotNhat = null;

        Double tienGiamTotNhat = 0.0;

        for (PhieuGiamGia pgg : list) {
            Double tienGiam = 0.0;

            // Bỏ qua các phiếu không đủ điều kieenj
            if(tongTien < pgg.getSoTienToiThieuHd()){
                continue;
            }

            if(pgg.getHinhThucGiamGia() == 0) {
                Double tinhTienGiam = tongTien * (pgg.getGiaTriGiam() / 100);

                tienGiam = Math.min(tinhTienGiam, pgg.getSoTienGiamToiDa());


            }else if(pgg.getHinhThucGiamGia() == 1) {
                tienGiam = pgg.getGiaTriGiam();
            }

            if(tienGiam.compareTo(tienGiamTotNhat) > 0) {
                tienGiamTotNhat = tienGiam;
                phieuGiamGiaTotNhat = pgg;
            }

        }
        if(phieuGiamGiaTotNhat.getSoLuong() > 0){
            phieuGiamGiaTotNhat.setSoLuong(phieuGiamGiaTotNhat.getSoLuong() - 1);
            hoaDon.setPhieuGiamGia(phieuGiamGiaTotNhat);
            phieuGiamGiaRepository.save(phieuGiamGiaTotNhat);
            hoaDonRepository.save(hoaDon);
        }else {
            throw new IllegalStateException("Phếu giảm giá ã hết lượt sử dụng");
        }


        return phieuGiamGiaTotNhat;
    }

    @Transactional
    public HoaDonResponse hoanPhieuGiam(Integer idHD, Double tongTien) {
        HoaDon hoaDon = hoaDonRepository.findById(idHD)
                .orElseThrow(() -> new EntityNotFoundException("Order not found by id" + idHD));
        PhieuGiamGia phieuGiamGia = hoaDon.getPhieuGiamGia();
        if(phieuGiamGia != null) {
            if(tongTien <= 0.0){
                phieuGiamGia.setSoLuong(phieuGiamGia.getSoLuong() + 1);
                hoaDon.setPhieuGiamGia(null);
            }
        }
        phieuGiamGiaRepository.save(phieuGiamGia);
        return hoaDonMapper.toHoaDonResponse(hoaDonRepository.save(hoaDon));
    }


    public HoaDon addHoaDonOnline(HoaDonModel hoaDonModel){
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon(HoaDonHelper.createHoaDonHelper());
        hoaDon.setLoaiDon(2); // Ddown hang f online
        hoaDon.setHoTenNguoiNhan(hoaDonModel.getHoTenNguoiNhan());
        hoaDon.setSoDienThoai(hoaDonModel.getSoDienThoai());
        hoaDon.setEmail(hoaDonModel.getEmail());
        hoaDon.setDiaChiNhanHang(hoaDonModel.getDiaChiNhanHang());
        hoaDon.setGhiChu(hoaDonModel.getGhiChu());
        hoaDon.setPhiShip(hoaDonModel.getPhiShip());

        //Lấy Thông tin khách hàng nếu có
        if(hoaDonModel.getIdKhachHang() != null){
            hoaDon.setKhachHang(khachHangRepository.findById(hoaDonModel.getIdKhachHang())
                    .orElseThrow(()-> new EntityNotFoundException("Khách hagnf không tồn tại")));
        }

        hoaDon.setNhanVien(null);

        hoaDon.setTrangThaiGiaoHang(1);
        hoaDon.setNgayTao(LocalDateTime.now());
        hoaDon.setNgaySua(LocalDateTime.now());

        hoaDon = hoaDonRepository.save(hoaDon);

        //Xử lý chi tiết hóa đơn
        List<HoaDonChiTiet> danhSachChiTiet = processOrderDetail(hoaDonModel.getDanhSachChiTiet(),hoaDon);

        PhieuGiamGia phieuGiamGia = null;
        Integer idPhieuGiamGia = hoaDonModel.getIdPhieuGiamGia();
         if(idPhieuGiamGia != null){
            phieuGiamGia = phieuGiamGiaRepository.findById(idPhieuGiamGia)
                    .orElseThrow(()-> new EntityNotFoundException("Not found idPhieuGiamGia"));
            hoaDon.setPhieuGiamGia(phieuGiamGia);
            phieuGiamGia.setSoLuong(phieuGiamGia.getSoLuong() - 1);
         }

         hoaDon.setTrangThai(0);

         hoaDon.setTongTien(caculatorTongTien(danhSachChiTiet));//Trả về hóa đơn chưa thanh toans

        return hoaDonRepository.save(hoaDon);

    }

    private Double caculatorTongTien(List<HoaDonChiTiet>listSanPham){
        return listSanPham.stream()
                .mapToDouble(hoaDonChiTiet -> hoaDonChiTiet.getThanhTien().doubleValue())
                .sum();
    }

    private List<HoaDonChiTiet>processOrderDetail(List<HoaDonChiTietModel>danhSachChitiet, HoaDon hoaDon){
        return danhSachChitiet.stream().map(chiTietModel -> {
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(chiTietModel.getSanPhamChiTietId())
                    .orElseThrow(() -> new EntityNotFoundException("SanPham Chi Tiet Not Found"));

            HoaDonChiTiet hoaDonChiTiet = new HoaDonChiTiet();
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTiet.setSoLuong(chiTietModel.getSoLuong());
            hoaDonChiTiet.setThanhTien(sanPhamChiTiet.getDonGia().doubleValue() * chiTietModel.getSoLuong());
            hoaDonChiTiet.setHoaDon(hoaDon);
            return hoaDonChiTietRepository.save(hoaDonChiTiet);
        }).collect(Collectors.toList());
    }

}
