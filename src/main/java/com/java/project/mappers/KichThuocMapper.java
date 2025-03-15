package com.java.project.mappers;

import com.java.project.dtos.KichThuocDto;
import com.java.project.entities.KichThuoc;

public class KichThuocMapper {
    public static KichThuocDto toDTO(KichThuoc kichThuoc) {
        return KichThuocDto.builder()
                .id(kichThuoc.getId())
                .tenKichThuoc(kichThuoc.getTenKichThuoc())
                .trangThai(kichThuoc.getTrangThai())
                .ngayTao(kichThuoc.getNgayTao())
                .build();
    }
}
