package com.java.project.mappers;

import com.java.project.dtos.TayAoDto;
import com.java.project.dtos.ThuongHieuDto;
import com.java.project.entities.TayAo;
import com.java.project.entities.ThuongHieu;

public class TayAoMapper {
    public static TayAoDto toDTO(TayAo tayAo) {
        return TayAoDto.builder()
                .id(tayAo.getId())
                .tenTayAo(tayAo.getTenTayAo())
                .trangThai(tayAo.getTrangThai())
                .ngayTao(tayAo.getNgayTao())
                .build();
    }
}
