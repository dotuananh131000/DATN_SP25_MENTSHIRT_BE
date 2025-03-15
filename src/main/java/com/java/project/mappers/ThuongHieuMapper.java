package com.java.project.mappers;

import com.java.project.dtos.ThuongHieuDto;
import com.java.project.entities.ThuongHieu;

public class ThuongHieuMapper {
    public static ThuongHieuDto toDTO(ThuongHieu thuongHieu) {
        return ThuongHieuDto.builder()
                .id(thuongHieu.getId())
                .tenThuongHieu(thuongHieu.getTenThuongHieu())
                .trangThai(thuongHieu.getTrangThai())
                .ngayTao(thuongHieu.getNgayTao())
                .build();
    }
}
