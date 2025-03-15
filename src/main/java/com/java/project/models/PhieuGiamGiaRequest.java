package com.java.project.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhieuGiamGiaRequest {
    private Integer id;
    private String maPhieuGiamGia;
    private String tenPhieuGiamGia;
    private Integer hinhThucPhieuGiamGia;
    private Date ngayTao;
    private Date ngaySua;
    private Date thoiGianApDung;
    private Date thoiGianHetHan;
    private Double giaTriGiam;
    private Double soTienToiThieuHD;
    private Double soTienGiamToiDa;
    private Integer trangThai;
    private Integer soLuong;
    private Integer loaiGiam;
}
