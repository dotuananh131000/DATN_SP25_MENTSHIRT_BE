package com.java.project.mappers;

import com.java.project.dtos.VaiTroDto;
import com.java.project.entities.VaiTro;

public class VaiTroMapper {
    public static VaiTroDto toVaiTroDTO(VaiTro vaiTro) {
        return VaiTroDto.builder()
                .id(vaiTro.getId())
                .maVaiTro(vaiTro.getMaVaiTro())
                .tenVaiTro(vaiTro.getTenVaiTro())
                .trangThai(vaiTro.getTrangThai())
                .build();
    }
}
