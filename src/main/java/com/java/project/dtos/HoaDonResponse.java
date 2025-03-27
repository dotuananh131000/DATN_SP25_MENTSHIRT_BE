package com.java.project.dtos;


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
public class HoaDonResponse {
    private Integer id;

    private String maKhachHang;

    private String tenKhachHang;

    private String maNhanVien;

    private String maHoaDon;

    private Integer loaiDon;

    private String hoTenNguoiNhan;

    private String soDienThoai;

    private String email;

    private String diaChiNhanHang;

    private Integer trangThaiGiaoHang;

    private Double phiShip;

    private Integer hinhThucGiamGia;

    private Double soTienGiamToiDa;

    private Double giaTriGiam;

    private Double tongTien;

    private LocalDateTime ngayTao;

    private Integer trangThai;
}
