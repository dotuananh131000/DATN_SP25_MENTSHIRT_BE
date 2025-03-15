package com.java.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SanPhamChiTietResponse {
    private Integer id;
    private String hinhAnh;
    private String maSanPham;
    private String tenSanPham;
    private String tenMauSac;
    private String tenKichThuoc;
    private String tenCoAo;
    private String tenTayAo;
    private String tenChatLieu;
    private Integer soLuong;
    private BigDecimal donGia;
}
