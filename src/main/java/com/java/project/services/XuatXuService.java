package com.java.project.services;

import com.java.project.dtos.XuatXuDto;
import com.java.project.entities.XuatXu;
import com.java.project.exceptions.EntityAlreadyExistsException;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.mappers.XuatXuMapper;
import com.java.project.models.XuatXuModel;
import com.java.project.repositories.XuatXuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class XuatXuService {

    @Autowired
    private XuatXuRepository xuatXuRepository;

    public Page<XuatXuDto> getAll(String search, Pageable pageable) {
        Page<XuatXu> xuatXus = xuatXuRepository.findAllWithSearch(search, pageable);
        return xuatXus.map(XuatXuMapper::toDTO);
    }

    @Transactional
    public XuatXuDto getById(Integer id) {
        XuatXu xuatXu = xuatXuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy xuất xứ"));
        return XuatXuMapper.toDTO(xuatXu);
    }

    @Transactional
    public XuatXuDto addXuatXu(XuatXuModel xuatXuModel) {
        if (xuatXuRepository.findByTenXuatXu(xuatXuModel.getTenXuatXu()).isPresent()) {
            throw new EntityAlreadyExistsException("Xuất xứ đã tồn tại");
        }
        XuatXu xuatXu = new XuatXu();
        xuatXu.setTenXuatXu(xuatXuModel.getTenXuatXu());
        xuatXu.setTrangThai(true);
        xuatXu.setNgayTao(Instant.now());
        xuatXu = xuatXuRepository.save(xuatXu);
        return XuatXuMapper.toDTO(xuatXu);
    }

    @Transactional
    public XuatXuDto updateXuatXu(Integer id, XuatXuModel xuatXuModel) {
        XuatXu xuatXu = xuatXuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy xuất xứ"));

        if (xuatXuRepository.findByTenXuatXu(xuatXuModel.getTenXuatXu()).isPresent()) {
            throw new EntityAlreadyExistsException("Xuất xứ đã tồn tại");
        }

        xuatXu.setTenXuatXu(xuatXuModel.getTenXuatXu());
        xuatXu = xuatXuRepository.save(xuatXu);
        return XuatXuMapper.toDTO(xuatXu);
    }

    @Transactional
    public void toggleTrangThai(Integer id) {
        XuatXu xuatXu = xuatXuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy xuất xứ"));
        xuatXu.setTrangThai(!xuatXu.getTrangThai());
        xuatXuRepository.save(xuatXu);
    }

    @Transactional
    public void deleteXuatXu(Integer id) {
        XuatXu xuatXu = xuatXuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy xuất xứ"));
        xuatXuRepository.delete(xuatXu);
    }
}

