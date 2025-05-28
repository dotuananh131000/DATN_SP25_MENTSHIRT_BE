package com.java.project.mappers;

import com.java.project.dtos.HoaDonResponse;
import com.java.project.entities.HoaDon;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HoaDonMapper {
    @Mapping(source = "nhanVien.id", target = "idNhanVien")
    @Mapping(source = "nhanVien.maNhanVien", target = "maNhanVien")
    @Mapping(source = "khachHang.tenKhachHang", target = "tenKhachHang")
    @Mapping(source = "khachHang.maKhachHang", target = "maKhachHang")
    @Mapping(source = "khachHang.id", target = "idKhachHang")
    @Mapping(source = "phieuGiamGia.maPhieuGiamGia", target = "maPhieuGiamGia")
    @Mapping(source = "phieuGiamGia.tenPhieuGiamGia", target = "tenPhieuGiamGia")
    @Mapping(source = "phieuGiamGia.soTienToiThieuHd", target = "soTienToiThieuHd")
    @Mapping(source = "phieuGiamGia.giaTriGiam", target = "giaTriGiam")
    @Mapping(source = "phieuGiamGia.hinhThucGiamGia", target = "hinhThucGiamGia")
    @Mapping(source = "phieuGiamGia.soTienGiamToiDa", target = "soTienGiamToiDa")
    @Mapping(source = "hoTenNguoiNhan", target = "hoTenNguoiNhan")
    HoaDonResponse toHoaDonResponse(HoaDon hoaDon);
}
