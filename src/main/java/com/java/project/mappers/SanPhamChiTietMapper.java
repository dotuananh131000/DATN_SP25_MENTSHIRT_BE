package com.java.project.mappers;

import com.java.project.dtos.SanPhamChiTietDto;
import com.java.project.dtos.SanPhamDto;
import com.java.project.entities.SanPhamChiTiet;

import java.util.Set;
import java.util.stream.Collectors;

public class SanPhamChiTietMapper {
    public static SanPhamChiTietDto toDTO(SanPhamChiTiet sanPhamChiTiet) {
        return SanPhamChiTietDto.builder()
                .id(sanPhamChiTiet.getId())
                .sanPham(SanPhamMapper.toDTO(sanPhamChiTiet.getSanPham(), 0))
                .soLuong(sanPhamChiTiet.getSoLuong())
                .donGia(sanPhamChiTiet.getDonGia())
                .ngayTao(sanPhamChiTiet.getNgayTao())
                .trangThai(sanPhamChiTiet.getTrangThai())
                .coAo(CoAoMapper.toDTO(sanPhamChiTiet.getCoAo()))
                .chatLieu(ChatLieuMapper.toDTO(sanPhamChiTiet.getChatLieu()))
                .kichThuoc(KichThuocMapper.toDTO(sanPhamChiTiet.getKichThuoc()))
                .mauSac(MauSacMapper.toDTO(sanPhamChiTiet.getMauSac()))
                .tayAo(TayAoMapper.toDTO(sanPhamChiTiet.getTayAo()))
                .thuongHieu(ThuongHieuMapper.toDTO(sanPhamChiTiet.getThuongHieu()))
                .xuatXu(XuatXuMapper.toDTO(sanPhamChiTiet.getXuatXu()))
                .hinhAnh(sanPhamChiTiet.getHinhAnh())
                .build();
    }
}
