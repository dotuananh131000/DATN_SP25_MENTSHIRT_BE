package com.java.project.mappers;

import com.java.project.dtos.DiaChiKhachHangDto;
import com.java.project.entities.DiaChiKhachHang;

public class DiaChiKhachHangMapper {
    public static DiaChiKhachHangDto toDTO(DiaChiKhachHang diaChiKhachHang) {
        return DiaChiKhachHangDto.builder()
                .id(diaChiKhachHang.getId())
                .khachHang(KhachHangMapper.toDTO(diaChiKhachHang.getKhachHang()))
                .tinhThanhId(diaChiKhachHang.getTinhThanhId())
                .tinhThanh(diaChiKhachHang.getTinhThanh())
                .quanHuyenId(diaChiKhachHang.getQuanHuyenId())
                .quanHuyen(diaChiKhachHang.getQuanHuyen())
                .phuongXaId(diaChiKhachHang.getPhuongXaId())
                .phuongXa(diaChiKhachHang.getPhuongXa())
                .diaChiChiTiet(diaChiKhachHang.getDiaChiChiTiet())
                .ngayTao(diaChiKhachHang.getNgayTao())
                .trangThai(diaChiKhachHang.getTrangThai())
                .build();
    }
}