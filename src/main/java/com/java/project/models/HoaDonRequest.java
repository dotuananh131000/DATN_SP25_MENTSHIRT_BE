package com.java.project.models;

import com.java.project.entities.KhachHang;
import com.java.project.entities.NhanVien;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class HoaDonRequest {
    private Integer id;
    private KhachHang khachHang;
    private NhanVien nhanVien;
    private String maHoaDon;
    private String loaiDon;
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
