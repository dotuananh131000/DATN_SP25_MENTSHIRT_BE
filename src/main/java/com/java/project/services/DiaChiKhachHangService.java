package com.java.project.services;

import com.java.project.dtos.DiaChiKhachHangDto;
import com.java.project.entities.DiaChiKhachHang;
import com.java.project.entities.KhachHang;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.mappers.DiaChiKhachHangMapper;
import com.java.project.models.DiaChiKhachHangCreateModel;
import com.java.project.models.DiaChiKhachHangUpdateModel;
import com.java.project.repositories.DiaChiKhachHangRepository;
import com.java.project.repositories.KhachHangRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiaChiKhachHangService {

    @Autowired
    DiaChiKhachHangRepository diaChiKhachHangRepository;

    @Autowired
    KhachHangRepository khachHangRepository;

    public DiaChiKhachHangDto getById(Integer id) {
        DiaChiKhachHang diaChiKhachHang = diaChiKhachHangRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Địa chỉ khách hàng không tồn tại"));
        return DiaChiKhachHangMapper.toDTO(diaChiKhachHang);
    }

    public List<DiaChiKhachHangDto> getByKhachHangId(Integer khachHangId) {
        List<DiaChiKhachHang> diaChiKhachHangs = diaChiKhachHangRepository.findByKhachHangId(khachHangId);
        return diaChiKhachHangs.stream()
                .map(DiaChiKhachHangMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DiaChiKhachHangDto create(DiaChiKhachHangCreateModel model) {
        KhachHang khachHang = khachHangRepository.findById(model.getKhachHangId())
                .orElseThrow(() -> new EntityNotFoundException("Khách hàng không tồn tại"));

        DiaChiKhachHang diaChiKhachHang = new DiaChiKhachHang();
        diaChiKhachHang.setKhachHang(khachHang);
        diaChiKhachHang.setTinhThanhId(model.getTinhThanhId());
        diaChiKhachHang.setTinhThanh(model.getTinhThanh());
        diaChiKhachHang.setQuanHuyenId(model.getQuanHuyenId());
        diaChiKhachHang.setQuanHuyen(model.getQuanHuyen());
        diaChiKhachHang.setPhuongXaId(model.getPhuongXaId());
        diaChiKhachHang.setPhuongXa(model.getPhuongXa());
        diaChiKhachHang.setDiaChiChiTiet(model.getDiaChiChiTiet());
        diaChiKhachHang.setNgayTao(Instant.now());
        diaChiKhachHang.setTrangThai(true);

        diaChiKhachHang = diaChiKhachHangRepository.save(diaChiKhachHang);
        return DiaChiKhachHangMapper.toDTO(diaChiKhachHang);
    }

    @Transactional
    public DiaChiKhachHangDto update(Integer id, DiaChiKhachHangUpdateModel model) {
        DiaChiKhachHang diaChiKhachHang = diaChiKhachHangRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Địa chỉ khách hàng không tồn tại"));

        diaChiKhachHang.setTinhThanh(model.getTinhThanh());
        diaChiKhachHang.setQuanHuyenId(model.getQuanHuyenId());
        diaChiKhachHang.setQuanHuyen(model.getQuanHuyen());
        diaChiKhachHang.setPhuongXaId(model.getPhuongXaId());
        diaChiKhachHang.setPhuongXa(model.getPhuongXa());
        diaChiKhachHang.setDiaChiChiTiet(model.getDiaChiChiTiet());

        diaChiKhachHang = diaChiKhachHangRepository.save(diaChiKhachHang);
        return DiaChiKhachHangMapper.toDTO(diaChiKhachHang);
    }

    @Transactional
    public void delete(Integer id) {
        DiaChiKhachHang diaChiKhachHang = diaChiKhachHangRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Địa chỉ khách hàng không tồn tại"));
        diaChiKhachHangRepository.delete(diaChiKhachHang);
    }

    @Transactional
    public DiaChiKhachHangDto toggleStatus(Integer id) {
        DiaChiKhachHang diaChiKhachHang = diaChiKhachHangRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Địa chỉ khách hàng không tồn tại"));
        diaChiKhachHang.setTrangThai(!diaChiKhachHang.getTrangThai());
        diaChiKhachHang = diaChiKhachHangRepository.save(diaChiKhachHang);
        return DiaChiKhachHangMapper.toDTO(diaChiKhachHang);
    }
}