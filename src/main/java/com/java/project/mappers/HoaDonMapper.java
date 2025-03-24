package com.java.project.mappers;

import com.java.project.dtos.HoaDonResponse;
import com.java.project.entities.HoaDon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HoaDonMapper {
    @Mapping(source = "nhanVien.maNhanVien", target = "maNhanVien")
    @Mapping(source = "khachHang.tenKhachHang", target = "tenKhachHang")
    HoaDonResponse toHoaDonResponse(HoaDon hoaDon);
}
