package com.java.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhieuGiamGiaResponse {
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
