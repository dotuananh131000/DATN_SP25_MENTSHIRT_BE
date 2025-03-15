package com.java.project.services;

import com.java.project.dtos.MauSacDto;
import com.java.project.entities.MauSac;
import com.java.project.exceptions.EntityAlreadyExistsException;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.mappers.MauSacMapper;
import com.java.project.models.MauSacModel;
import com.java.project.models.MauSacUpdateModel;
import com.java.project.repositories.MauSacRepository;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class MauSacService {

    @Autowired
    private MauSacRepository mauSacRepository;

    public Page<MauSacDto> getAll(String search, Pageable pageable) {
        Page<MauSac> mauSacs = mauSacRepository.findAllWithSearch(search, pageable);
        return mauSacs.map(MauSacMapper::toDTO);
    }

    @Transactional
    public MauSacDto getById(Integer id) {
        MauSac mauSac = mauSacRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy màu sắc"));
        return MauSacMapper.toDTO(mauSac);
    }


    @Transactional
    public MauSacDto addMauSac(MauSacModel mauSacModel){
        if (mauSacRepository.findByTenMauSac(mauSacModel.getTenMauSac()).isPresent()) {
            throw new EntityAlreadyExistsException("Tên màu sắc đã tồn tại");
        }

        if (mauSacRepository.findByMaHex(mauSacModel.getMaHex()).isPresent()) {
            throw new EntityAlreadyExistsException("Mã màu HEX đã tồn tại");
        }

        MauSac mauSac = new MauSac();

        mauSac.setMaHex(mauSacModel.getMaHex());
        mauSac.setTenMauSac(mauSacModel.getTenMauSac());
        mauSac.setTrangThai(true);
        mauSac.setNgayTao(Instant.now());
        mauSac = mauSacRepository.save(mauSac);
        return MauSacMapper.toDTO(mauSac);
    }

    @Transactional
    public MauSacDto updateMauSac(Integer id, MauSacUpdateModel mauSacModel) {
        MauSac mauSac = mauSacRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy màu sắc"));

        if (StringUtils.isNotEmpty(mauSacModel.getTenMauSac()) && !mauSacModel.getTenMauSac().equals(mauSac.getTenMauSac())) {
            if (mauSacRepository.findByTenMauSac(mauSacModel.getTenMauSac()).isPresent()) {
                throw new EntityAlreadyExistsException("Tên màu sắc đã tồn tại");
            }
            mauSac.setTenMauSac(mauSacModel.getTenMauSac());
        }

        if (StringUtils.isNotEmpty(mauSacModel.getMaHex()) && !mauSacModel.getMaHex().equals(mauSac.getMaHex())) {
            if (mauSacRepository.findByMaHex(mauSacModel.getMaHex()).isPresent()) {
                throw new EntityAlreadyExistsException("Mã màu HEX đã tồn tại");
            }
            mauSac.setMaHex(mauSacModel.getMaHex());
        }

        mauSac = mauSacRepository.save(mauSac);
        return MauSacMapper.toDTO(mauSac);
    }


    @Transactional
    public void toggleTrangThai(Integer id) {
        MauSac mauSac = mauSacRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy màu sắc"));
        mauSac.setTrangThai(!mauSac.getTrangThai());
        mauSacRepository.save(mauSac);
    }

    @Transactional
    public void deleteMauSac(Integer id) {
        MauSac mauSac = mauSacRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy màu sắc"));
        mauSacRepository.delete(mauSac);
    }
}

