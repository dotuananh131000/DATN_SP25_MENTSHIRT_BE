package com.java.project.services;

import com.java.project.dtos.PhieuGiamGiaDto;
import com.java.project.entities.HoaDon;
import com.java.project.entities.HoaDonChiTiet;
import com.java.project.entities.PhieuGiamGia;
import com.java.project.entities.SanPhamChiTiet;
import com.java.project.helper.HoaDonHelper;
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
    SanPhamChiTietRepository sanPhamChiTietRepository;
    HoaDonChiTietRepository hoaDonChiTietRepository;


    public List<PhieuGiamGiaDto> getPhieuGiamGiaByKH(Integer idKH) {
        List<PhieuGiamGia> listPhieuGiamGia = phieuGiamGiaRepository.findPhieuGiamGiaByKhachHang(idKH);
        return listPhieuGiamGia.stream()
                .map(PhieuGiamGiaMapper ::toDTO)
                .toList();
    }

    public HoaDon addHoaDonOnline(HoaDonModel hoaDonModel){
        HoaDon hoaDon = new HoaDon();
        hoaDon.setMaHoaDon(HoaDonHelper.createHoaDonHelper());
        hoaDon.setLoaiDon(0);
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
