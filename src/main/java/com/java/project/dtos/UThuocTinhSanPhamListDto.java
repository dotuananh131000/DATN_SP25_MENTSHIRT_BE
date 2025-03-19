package com.java.project.dtos;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UThuocTinhSanPhamListDto {
    List<ChatLieuDto> chatLieus;
    List<CoAoDto> coAos;
    List<KichThuocDto> kichThuocs;
    List<MauSacDto> mauSacs;
    List<TayAoDto> tayAos;
    List<ThuongHieuDto> thuongHieus;
    List<XuatXuDto> xuatXus;
}