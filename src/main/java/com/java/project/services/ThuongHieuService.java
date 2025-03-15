package com.java.project.services;

import com.java.project.dtos.ThuongHieuDto;
import com.java.project.entities.ThuongHieu;
import com.java.project.exceptions.EntityAlreadyExistsException;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.mappers.ThuongHieuMapper;
import com.java.project.models.ThuongHieuModel;
import com.java.project.repositories.ThuongHieuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class ThuongHieuService {

    @Autowired
    private ThuongHieuRepository thuongHieuRepository;

    public Page<ThuongHieuDto> getAll(String search, Pageable pageable) {
        Page<ThuongHieu> thuongHieus = thuongHieuRepository.findAllWithSearch(search, pageable);
        return thuongHieus.map(ThuongHieuMapper::toDTO);
    }

    @Transactional
    public ThuongHieuDto getById(Integer id) {
        ThuongHieu thuongHieu = thuongHieuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thương hiệu"));
        return ThuongHieuMapper.toDTO(thuongHieu);
    }

    @Transactional
    public ThuongHieuDto addThuongHieu(ThuongHieuModel thuongHieuModel) {
        if (thuongHieuRepository.findByTenThuongHieu(thuongHieuModel.getTenThuongHieu()).isPresent()) {
            throw new EntityAlreadyExistsException("Tên thương hiệu đã tồn tại");
        }
        ThuongHieu thuongHieu = new ThuongHieu();
        thuongHieu.setTenThuongHieu(thuongHieuModel.getTenThuongHieu());
        thuongHieu.setTrangThai(true);
        thuongHieu.setNgayTao(Instant.now());
        thuongHieu = thuongHieuRepository.save(thuongHieu);
        return ThuongHieuMapper.toDTO(thuongHieu);
    }

    @Transactional
    public ThuongHieuDto updateThuongHieu(Integer id, ThuongHieuModel thuongHieuModel) {
        ThuongHieu thuongHieu = thuongHieuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thương hiệu"));

        if (!thuongHieu.getTenThuongHieu().equalsIgnoreCase(thuongHieuModel.getTenThuongHieu()) && thuongHieuRepository.findByTenThuongHieu(thuongHieuModel.getTenThuongHieu()).isPresent()) {
            throw new EntityAlreadyExistsException("Tên thương hiệu đã tồn tại");
        }

        thuongHieu.setTenThuongHieu(thuongHieuModel.getTenThuongHieu());
        thuongHieu = thuongHieuRepository.save(thuongHieu);
        return ThuongHieuMapper.toDTO(thuongHieu);
    }

    @Transactional
    public void toggleTrangThai(Integer id) {
        ThuongHieu thuongHieu = thuongHieuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thương hiệu"));
        thuongHieu.setTrangThai(!thuongHieu.getTrangThai());
        thuongHieuRepository.save(thuongHieu);
    }

    @Transactional
    public void deleteThuongHieu(Integer id) {
        ThuongHieu thuongHieu = thuongHieuRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thương hiệu"));
        thuongHieuRepository.delete(thuongHieu);
    }
}
