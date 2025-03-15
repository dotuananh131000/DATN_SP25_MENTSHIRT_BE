package com.java.project.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonRequest {
    private Integer id;
    private Integer idKhachHang;
    private Integer idNhanVien;
    private Integer idPhieuGiamGia;
    private String maHoaDon;
    private Integer loaiDon;
    private String ghiChu;
    private String hoTenNguoiNhan;
    private String soDienThoai;
    private String email;
    private String diaChiNhanHang;
    private LocalDate ngayNhanMongMuon;
    private LocalDate ngayDuKienNhan;
    private Integer trangThaiGiaoHang;
    private Double phiShip;
    private Double tongTien;
    private LocalDateTime ngayTao;
    private Integer trangThai;
}
