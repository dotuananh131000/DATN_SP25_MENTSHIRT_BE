package com.java.project.mappers;

import com.java.project.dtos.MauSacDto;
import com.java.project.entities.MauSac;

public class MauSacMapper {
    public static MauSacDto toDTO(MauSac mauSac) {
        return MauSacDto.builder()
                .id(mauSac.getId())
                .maHex(mauSac.getMaHex())
                .tenMauSac(mauSac.getTenMauSac())
                .trangThai(mauSac.getTrangThai())
                .ngayTao(mauSac.getNgayTao())
                .build();
    }
}
