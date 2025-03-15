package com.java.project.dtos;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class SanPhamChiTietGenerateDTO {
    private Integer sanPham;
    private String tenSanPham;

    private Integer thuongHieu;
    private String tenThuongHieu;

    private Integer xuatXu;
    private String tenXuatXu;

    private Integer chatLieu;
    private String tenChatLieu;

    private Integer coAo;
    private String tenCoAo;

    private Integer tayAo;
    private String tenTayAo;

    private Integer mauSac;
    private String tenMauSac;

    private Integer kichThuoc;
    private String tenKichThuoc;

    private Integer soLuong = 10;

    private BigDecimal donGia = BigDecimal.valueOf(50000);

    private String hinhAnh = null;
}

