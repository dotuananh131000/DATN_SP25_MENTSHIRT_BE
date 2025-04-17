package com.java.project.mappers;

import com.java.project.dtos.ThongBaoResponse;
import com.java.project.entities.ThongBao;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ThongBaoMapper {

    ThongBaoResponse toThongBaoResponse(ThongBao thongBao);
}
