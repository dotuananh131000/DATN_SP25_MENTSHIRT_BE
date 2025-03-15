package com.java.project.mappers;

import com.java.project.dtos.PhieuGiamGiaDto;
import com.java.project.entities.PhieuGiamGia;

public class PhieuGiamGiaMapper {
    public static PhieuGiamGiaDto toDTO(PhieuGiamGia phieuGiamGia) {
        return PhieuGiamGiaDto.builder()
                .id(phieuGiamGia.getId())
                .maPhieuGiamGia(phieuGiamGia.getMaPhieuGiamGia())
                .tenPhieuGiamGia(phieuGiamGia.getTenPhieuGiamGia())
                .ngayTao(phieuGiamGia.getNgayTao())
                .ngaySua(phieuGiamGia.getNgaySua())
                .thoiGianApDung(phieuGiamGia.getThoiGianApDung())
                .thoiGianHetHan(phieuGiamGia.getThoiGianHetHan())
                .giaTriGiam(phieuGiamGia.getGiaTriGiam())
                .soTienToiThieuHd(phieuGiamGia.getSoTienToiThieuHd())
                .soTienGiamToiDa(phieuGiamGia.getSoTienGiamToiDa())
                .loaiGiam(phieuGiamGia.getLoaiGiam())
                .trangThai(phieuGiamGia.getTrangThai())
                .soLuong(phieuGiamGia.getSoLuong())
                .hinhThucGiamGia(phieuGiamGia.getHinhThucGiamGia())
                .build();
    }
}
