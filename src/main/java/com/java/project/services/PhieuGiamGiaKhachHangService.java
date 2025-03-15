package com.java.project.services;

import com.java.project.dtos.PhieuGiamGiaKhachHangDto;
import com.java.project.entities.KhachHang;
import com.java.project.entities.PhieuGiamGia;
import com.java.project.entities.PhieuGiamGiaKhachHang;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.mappers.PhieuGiamGiaKhachHangMapper;
import com.java.project.models.PhieuGiamGiaKhachHangModel;
import com.java.project.repositories.KhachHangRepository;
import com.java.project.repositories.PhieuGiamGiaKhachHangRepository;
import com.java.project.repositories.PhieuGiamGiaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhieuGiamGiaKhachHangService {

    @Autowired
    private PhieuGiamGiaKhachHangRepository phieuGiamGiaKhachHangRepository;

    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Autowired
    private MailService mailService;

    public PhieuGiamGiaKhachHangDto getById(Integer id) {
        PhieuGiamGiaKhachHang phieuGiamGiaKhachHang = phieuGiamGiaKhachHangRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Phiếu giảm giá khách hàng không tồn tại"));
        return PhieuGiamGiaKhachHangMapper.toDTO(phieuGiamGiaKhachHang);
    }

    public List<PhieuGiamGiaKhachHangDto> getByKhachHangId(Integer khachHangId) {
        List<PhieuGiamGiaKhachHang> phieuGiamGiaKhachHangs = phieuGiamGiaKhachHangRepository.findByKhachHangId(khachHangId);
        return phieuGiamGiaKhachHangs.stream()
                .map(PhieuGiamGiaKhachHangMapper::toDTO)
                .collect(Collectors.toList());
    }

    public List<PhieuGiamGiaKhachHangDto> getByVoucherId(Integer voucherId) {
        List<PhieuGiamGiaKhachHang> phieuGiamGiaKhachHangs = phieuGiamGiaKhachHangRepository.findByVoucherId(voucherId);
        return phieuGiamGiaKhachHangs.stream()
                .map(PhieuGiamGiaKhachHangMapper::toDTO)
                .collect(Collectors.toList());
    };

    @Transactional
    public List<PhieuGiamGiaKhachHangDto> createMultiple(List<PhieuGiamGiaKhachHangModel> models) {
        return models.stream().map(this::createSingle).collect(Collectors.toList());
    }

    @Transactional
    public PhieuGiamGiaKhachHangDto createSingle(PhieuGiamGiaKhachHangModel model) {
        KhachHang khachHang = khachHangRepository.findById(model.getIdKhachHang())
                .orElseThrow(() -> new EntityNotFoundException("Khách hàng không tồn tại"));

        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(model.getIdVoucher())
                .orElseThrow(() -> new EntityNotFoundException("Phiếu giảm giá không tồn tại"));

        PhieuGiamGiaKhachHang phieuGiamGiaKhachHang = new PhieuGiamGiaKhachHang();
        phieuGiamGiaKhachHang.setIdKhachHang(khachHang);
        phieuGiamGiaKhachHang.setIdVoucher(phieuGiamGia);
        phieuGiamGiaKhachHang.setTrangThai(Short.parseShort("1"));
        phieuGiamGiaKhachHang.setNgayTao(LocalDate.now());

        phieuGiamGiaKhachHang = phieuGiamGiaKhachHangRepository.save(phieuGiamGiaKhachHang);

        mailService.sendVoucherReceivedMail(khachHang.getTenKhachHang(), khachHang.getEmail(),
                phieuGiamGia.getMaPhieuGiamGia(), phieuGiamGia.getTenPhieuGiamGia(),
                String.valueOf(phieuGiamGia.getGiaTriGiam()),
                phieuGiamGia.getThoiGianApDung().toString(),
                phieuGiamGia.getThoiGianHetHan().toString());

        return PhieuGiamGiaKhachHangMapper.toDTO(phieuGiamGiaKhachHang);
    }

    @Transactional
    public void updateVoucherList(Integer phieuGiamGiaId, List<Integer> khachHangIds) {
        List<PhieuGiamGiaKhachHang> existingEntries = phieuGiamGiaKhachHangRepository.findByVoucherId(phieuGiamGiaId);
        phieuGiamGiaKhachHangRepository.deleteAll(existingEntries);

        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(phieuGiamGiaId)
                .orElseThrow(() -> new EntityNotFoundException("Phiếu giảm giá không tồn tại"));

        for (Integer khachHangId : khachHangIds) {
            KhachHang khachHang = khachHangRepository.findById(khachHangId)
                    .orElseThrow(() -> new EntityNotFoundException("Khách hàng không tồn tại"));

            PhieuGiamGiaKhachHang phieuGiamGiaKhachHang = new PhieuGiamGiaKhachHang();
            phieuGiamGiaKhachHang.setIdKhachHang(khachHang);
            phieuGiamGiaKhachHang.setIdVoucher(phieuGiamGia);
            phieuGiamGiaKhachHang.setTrangThai(Short.parseShort("1"));
            phieuGiamGiaKhachHang.setNgayTao(LocalDate.now());

            phieuGiamGiaKhachHangRepository.save(phieuGiamGiaKhachHang);

            mailService.sendVoucherReceivedMail(khachHang.getTenKhachHang(), khachHang.getEmail(),
                    phieuGiamGia.getMaPhieuGiamGia(), phieuGiamGia.getTenPhieuGiamGia(),
                    String.valueOf(phieuGiamGia.getGiaTriGiam()),
                    phieuGiamGia.getThoiGianApDung().toString(),
                    phieuGiamGia.getThoiGianHetHan().toString());
        }
    }

    @Transactional
    public void deleteMultiple(List<Integer> ids) {
        ids.forEach(this::deleteSingle);
    }

    @Transactional
    public void deleteSingle(Integer id) {
        PhieuGiamGiaKhachHang phieuGiamGiaKhachHang = phieuGiamGiaKhachHangRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Phiếu giảm giá khách hàng không tồn tại"));

        KhachHang khachHang = phieuGiamGiaKhachHang.getIdKhachHang();
        PhieuGiamGia phieuGiamGia = phieuGiamGiaKhachHang.getIdVoucher();

        phieuGiamGiaKhachHangRepository.delete(phieuGiamGiaKhachHang);

        mailService.sendVoucherDeletionMail(khachHang.getTenKhachHang(), khachHang.getEmail(),
                phieuGiamGia.getMaPhieuGiamGia(), phieuGiamGia.getTenPhieuGiamGia());
    }
}
