package com.java.project.services;

import com.java.project.dtos.SanPhamChiTietDto;
import com.java.project.dtos.UThuocTinhSanPhamListDto;
import com.java.project.entities.*;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.mappers.*;
import com.java.project.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class USanPhamChiTietService {
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    public UThuocTinhSanPhamListDto getThuocTinhbySanPhamId(Integer sanPhamId) {
        return UThuocTinhSanPhamListDto.builder()
                .chatLieus(sanPhamChiTietRepository.findChatLieuBySanPhamId(sanPhamId)
                        .stream().map(ChatLieuMapper::toDTO).collect(Collectors.toList()))
                .coAos(sanPhamChiTietRepository.findCoAoBySanPhamId(sanPhamId)
                        .stream().map(CoAoMapper::toDTO).collect(Collectors.toList()))
                .kichThuocs(sanPhamChiTietRepository.findKichThuocBySanPhamId(sanPhamId)
                        .stream().map(KichThuocMapper::toDTO).collect(Collectors.toList()))
                .mauSacs(sanPhamChiTietRepository.findMauSacBySanPhamId(sanPhamId)
                        .stream().map(MauSacMapper::toDTO).collect(Collectors.toList()))
                .tayAos(sanPhamChiTietRepository.findTayAoBySanPhamId(sanPhamId)
                        .stream().map(TayAoMapper::toDTO).collect(Collectors.toList()))
                .thuongHieus(sanPhamChiTietRepository.findThuongHieuBySanPhamId(sanPhamId)
                        .stream().map(ThuongHieuMapper::toDTO).collect(Collectors.toList()))
                .xuatXus(sanPhamChiTietRepository.findXuatXuBySanPhamId(sanPhamId)
                        .stream().map(XuatXuMapper::toDTO).collect(Collectors.toList()))
                .build();
    }

    public SanPhamChiTietDto getByThuocTinh(Integer sanPhamId, Integer chatLieuId, Integer coAoId, Integer kichThuocId, Integer mauSacId, Integer tayAoId, Integer thuongHieuId, Integer xuatXuId) {
        List<SanPhamChiTiet> danhSachSanPham = sanPhamChiTietRepository.findBySanPhamIdActive(sanPhamId);

        Optional<SanPhamChiTiet> ketQua = danhSachSanPham.stream()
                .filter(sp -> sp.getChatLieu().getId().equals(chatLieuId))
                .filter(sp -> sp.getCoAo().getId().equals(coAoId))
                .filter(sp -> sp.getKichThuoc().getId().equals(kichThuocId))
                .filter(sp -> sp.getMauSac().getId().equals(mauSacId))
                .filter(sp -> sp.getTayAo().getId().equals(tayAoId))
                .filter(sp -> sp.getThuongHieu().getId().equals(thuongHieuId))
                .filter(sp -> sp.getXuatXu().getId().equals(xuatXuId))
                .findFirst();

        return ketQua.map(SanPhamChiTietMapper::toDTO).orElse(null);
    }


}