package com.java.project.mappers;

import com.java.project.dtos.XuatXuDto;
import com.java.project.entities.XuatXu;

public class XuatXuMapper {
    public static XuatXuDto toDTO(XuatXu xuatXu) {
        return XuatXuDto.builder()
                .id(xuatXu.getId())
                .tenXuatXu(xuatXu.getTenXuatXu())
                .trangThai(xuatXu.getTrangThai())
                .ngayTao(xuatXu.getNgayTao())
                .build();
    }
}
