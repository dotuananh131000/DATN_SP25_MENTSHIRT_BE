package com.java.project.services;

import com.java.project.dtos.SanPhamChiTietDto;
import com.java.project.dtos.USanPhamResponse;
import com.java.project.entities.SanPham;
import com.java.project.entities.SanPhamChiTiet;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.mappers.SanPhamChiTietMapper;
import com.java.project.repositories.SanPhamChiTietRepository;
import com.java.project.repositories.SanPhamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class USanPhamService {
    @Autowired
    SanPhamRepository sanPhamRepository;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    public Page<USanPhamResponse> getAllSanPhamsWithFilter(
            String search,
            List<Integer> thuongHieuIds,
            List<Integer> xuatXuIds,
            List<Integer> chatLieuIds,
            List<Integer> coAoIds,
            List<Integer> tayAoIds,
            List<Integer> mauSacIds,
            List<Integer> kichThuocIds,
            Double minPrice,
            Double maxPrice,
            String sortBy,
            String sortDir,
            Pageable pageable
    ) {
        return sanPhamRepository.findAllSanPhamWithFilter(
                search,
                thuongHieuIds == null || thuongHieuIds.isEmpty() ? null : thuongHieuIds,
                xuatXuIds == null || xuatXuIds.isEmpty() ? null : xuatXuIds,
                chatLieuIds == null || chatLieuIds.isEmpty() ? null : chatLieuIds,
                coAoIds == null || coAoIds.isEmpty() ? null : coAoIds,
                tayAoIds == null || tayAoIds.isEmpty() ? null : tayAoIds,
                mauSacIds == null || mauSacIds.isEmpty() ? null : mauSacIds,
                kichThuocIds == null || kichThuocIds.isEmpty() ? null : kichThuocIds,
                minPrice,
                maxPrice,
                sortBy,
                sortDir,
                pageable
        );
    }

    @Transactional
    public List<SanPhamChiTietDto> getSanPhamChiTietById(Integer id) {
        SanPham sanPham = sanPhamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm"));

        List<SanPhamChiTiet> sanPhamChiTiets = sanPhamChiTietRepository.findByActiveSanPhamId(id);
        if (sanPhamChiTiets.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy chi tiết sản phẩm");
        }

        return sanPhamChiTiets.stream()
                .map(SanPhamChiTietMapper::toDTO)
                .collect(Collectors.toList());
    }
}