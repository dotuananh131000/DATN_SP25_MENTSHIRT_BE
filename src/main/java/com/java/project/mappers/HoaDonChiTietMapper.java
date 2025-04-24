package com.java.project.mappers;

import com.java.project.dtos.HoaDonChiTietResponse;
import com.java.project.entities.HoaDonChiTiet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HoaDonChiTietMapper {
    @Mapping(source = "hoaDon.id", target = "idHoaDon")
    @Mapping(source = "sanPhamChiTiet.id", target = "idSPCT")
    @Mapping(source = "sanPhamChiTiet.sanPham.tenSanPham", target = "tenSanPham")
    @Mapping(source = "sanPhamChiTiet.mauSac.tenMauSac", target = "tenMauSac")
    @Mapping(source = "sanPhamChiTiet.kichThuoc.tenKichThuoc", target = "tenKichThuoc")
    @Mapping(source = "sanPhamChiTiet.donGia", target = "donGia")
    @Mapping(source = "sanPhamChiTiet.soLuong", target = "soLuongTon")
    @Mapping(source = "sanPhamChiTiet.hinhAnh", target = "hinhAnh")
    HoaDonChiTietResponse toHoaDonChiTietResponse(HoaDonChiTiet hoaDonChiTiet);
}
