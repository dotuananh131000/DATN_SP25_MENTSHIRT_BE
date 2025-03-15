package com.java.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonChiTietResponse {
    private Integer id;
    private Integer idHoaDon;
    private Integer idSPCT;
    private String tenSanPham;
    private String hinhAnh;
    private String tenMauSac;
    private String tenKichThuoc;
    private BigDecimal donGia;
    private Integer soLuongTon;
    private Integer soLuong;
    private Double thanhTien;
    private Integer trangThai;
}
