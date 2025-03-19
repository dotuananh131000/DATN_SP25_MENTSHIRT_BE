package com.java.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class USanPhamResponse {
    Integer id;
    String maSanPham;
    String tenSanPham;
    Long soLuong;
    Long soLuongDaBan;
    String hinhAnh;
    BigDecimal gia;
    String moTa;
}