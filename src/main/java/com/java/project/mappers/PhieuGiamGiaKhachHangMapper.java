package com.java.project.mappers;

import com.java.project.dtos.PhieuGiamGiaKhachHangDto;
import com.java.project.entities.PhieuGiamGiaKhachHang;

public class PhieuGiamGiaKhachHangMapper {
    public static PhieuGiamGiaKhachHangDto toDTO(PhieuGiamGiaKhachHang phieuGiamGiaKhachHang) {
        return PhieuGiamGiaKhachHangDto.builder()
                .id(phieuGiamGiaKhachHang.getId())
                .idKhachHang(KhachHangMapper.toDTO(phieuGiamGiaKhachHang.getIdKhachHang()))
                .idVoucher(PhieuGiamGiaMapper.toDTO(phieuGiamGiaKhachHang.getIdVoucher()))
                .trangThai(phieuGiamGiaKhachHang.getTrangThai())
                .ngayTao(phieuGiamGiaKhachHang.getNgayTao())
                .ngaySua(phieuGiamGiaKhachHang.getNgaySua())
                .build();
    }
}
