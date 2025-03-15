package com.java.project.mappers;

import com.java.project.dtos.NhanVienDto;
import com.java.project.entities.NhanVien;

public class NhanVienMapper {
    public static NhanVienDto toNhanVienDTO(NhanVien nhanVien) {
        return NhanVienDto.builder()
                .id(nhanVien.getId())
                .vaiTro(VaiTroMapper.toVaiTroDTO(nhanVien.getVaiTro()))
                .maNhanVien(nhanVien.getMaNhanVien())
                .tenNhanVien(nhanVien.getTenNhanVien())
                .tenDangNhap(nhanVien.getTenDangNhap())
                .email(nhanVien.getEmail())
                .soDienThoai(nhanVien.getSoDienThoai())
                .diaChi(nhanVien.getDiaChi())
                .gioiTinh(nhanVien.getGioiTinh())
                .avatarUrl(nhanVien.getAvatarUrl())
                .trangThai(nhanVien.getTrangThai())
                .build();
    }
}
