package com.java.project.mappers;

import com.java.project.dtos.SanPhamDto;
import com.java.project.entities.SanPham;

public class SanPhamMapper {
    public static SanPhamDto toDTO(SanPham sanPham, Integer tongSoLuong) {
        return SanPhamDto.builder()
                .id(sanPham.getId())
                .tenSanPham(sanPham.getTenSanPham())
                .soLuong(tongSoLuong == null ? 0 : tongSoLuong)
                .maSanPham(sanPham.getMaSanPham())
                .moTa(sanPham.getMoTa())
                .trangThai(sanPham.getTrangThai())
                .ngayTao(sanPham.getNgayTao())
                .build();
    }
}

