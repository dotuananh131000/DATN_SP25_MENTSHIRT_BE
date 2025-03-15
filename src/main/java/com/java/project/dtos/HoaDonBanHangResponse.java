package com.java.project.dtos;

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
public class HoaDonBanHangResponse {
    private Integer id;

    private String tenKhachHang;

    private String maNhanVien;

    private String maHoaDon;

    private Integer loaiGiam;

    private Double giaTriGiam;

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

    private Integer hinhThucPhieuGiamGia;
}
