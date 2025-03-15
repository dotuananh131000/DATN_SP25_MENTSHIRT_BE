package com.java.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TopSellingResponse {
    private String anh;
    private String tenSanPham;
    private String mauSac;
    private String kichThuoc;
    private Long soLuong;
    private BigDecimal giaBan;
}
