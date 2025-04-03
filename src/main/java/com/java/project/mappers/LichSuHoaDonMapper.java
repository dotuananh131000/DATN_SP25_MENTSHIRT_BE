package com.java.project.mappers;

import com.java.project.dtos.LichSuHoaDonResponse;
import com.java.project.entities.HoaDon;
import com.java.project.entities.LichSuHoaDon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface LichSuHoaDonMapper {
    @Mapping(source = "hoaDon.id", target = "idHoaDon")
    LichSuHoaDonResponse toLichSuHoaDonResponse(LichSuHoaDon lichSuHoaDon);
}
