package com.java.project.mappers;

import com.java.project.dtos.KhachHangDto;
import com.java.project.entities.KhachHang;

public class KhachHangMapper {
    public static KhachHangDto toDTO(KhachHang khachHang) {
        return KhachHangDto.builder()
                .id(khachHang.getId())
                .maKhachHang(khachHang.getMaKhachHang())
                .tenKhachHang(khachHang.getTenKhachHang())
                .tenDangNhap(khachHang.getTenDangNhap())
                .email(khachHang.getEmail())
                .soDienThoai(khachHang.getSoDienThoai())
                .gioiTinh(khachHang.getGioiTinh())
                .ngaySinh(khachHang.getNgaySinh())
                .avatarUrl(khachHang.getAvatarUrl())
                .trangThai(khachHang.getTrangThai())
                .build();
    }
}
