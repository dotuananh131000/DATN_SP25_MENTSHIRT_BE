package com.java.project.services;

import com.java.project.dtos.PhieuGiamGiaDto;
import com.java.project.entities.KhachHang;
import com.java.project.entities.PhieuGiamGia;
import com.java.project.entities.PhieuGiamGiaKhachHang;
import com.java.project.exceptions.EntityAlreadyExistsException;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.mappers.PhieuGiamGiaMapper;
import com.java.project.models.PhieuGiamGiaCreateModel;
import com.java.project.models.PhieuGiamGiaUpdateModel;
import com.java.project.repositories.KhachHangRepository;
import com.java.project.repositories.PhieuGiamGiaKhachHangRepository;
import com.java.project.repositories.PhieuGiamGiaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PhieuGiamGiaService {

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Autowired
    KhachHangRepository khachHangRepository;

    @Autowired
    PhieuGiamGiaKhachHangRepository phieuGiamGiaKhachHangRepository;

    @Autowired
    MailService mailService;

    public PhieuGiamGiaDto getById(Integer id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Phiếu giảm giá không tồn tại"));
        return PhieuGiamGiaMapper.toDTO(phieuGiamGia);
    }

    public Page<PhieuGiamGiaDto> getAll(String keyword, LocalDateTime startTime, LocalDateTime endTime, Integer loaiGiam, int page, int size, String sortBy, String sortDirection) {
        Sort sort = "asc".equalsIgnoreCase(sortDirection) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        LocalDateTime defaultStartTime = (startTime != null) ? startTime : LocalDateTime.of(1970, 1, 1, 0, 0, 0, 0);
        LocalDateTime defaultEndTime = (endTime != null) ? endTime : LocalDateTime.of(9999, 12, 31, 23, 59, 59, 999999999);

        Page<PhieuGiamGia> phieuGiamGias = phieuGiamGiaRepository.findAllByCriteria(
                keyword, defaultStartTime, defaultEndTime, loaiGiam, pageable);

        return phieuGiamGias.map(PhieuGiamGiaMapper::toDTO);
    }


    @Transactional
    public PhieuGiamGiaDto create(PhieuGiamGiaCreateModel model) {
        validateDuplicate(model.getMaPhieuGiamGia(), model.getTenPhieuGiamGia());

        PhieuGiamGia phieuGiamGia = new PhieuGiamGia();
        phieuGiamGia.setMaPhieuGiamGia(model.getMaPhieuGiamGia());
        phieuGiamGia.setTenPhieuGiamGia(model.getTenPhieuGiamGia());
        phieuGiamGia.setThoiGianApDung(model.getThoiGianApDung());
        phieuGiamGia.setThoiGianHetHan(model.getThoiGianHetHan());
        phieuGiamGia.setGiaTriGiam(model.getGiaTriGiam());
        phieuGiamGia.setSoTienToiThieuHd(model.getSoTienToiThieuHd());
        phieuGiamGia.setSoTienGiamToiDa(model.getSoTienGiamToiDa());
        phieuGiamGia.setLoaiGiam(model.getLoaiGiam());
        phieuGiamGia.setHinhThucGiamGia(model.getHinhThucGiamGia());
        phieuGiamGia.setSoLuong(model.getSoLuong());
        phieuGiamGia.setNgayTao(LocalDateTime.now());
        phieuGiamGia.setTrangThai(1);

        if(model.getLoaiGiam()==0){
            List<Integer> khachHangIds = khachHangRepository.getAllId();
            phieuGiamGia.setSoLuong(khachHangIds.size());
            model.setKhachHangId(khachHangIds);
        }

        phieuGiamGia = phieuGiamGiaRepository.save(phieuGiamGia);

        for (Integer khachHangId : model.getKhachHangId()) {
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

        return PhieuGiamGiaMapper.toDTO(phieuGiamGia);
    }

    @Transactional
    public PhieuGiamGiaDto update(Integer id, PhieuGiamGiaUpdateModel model) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Phiếu giảm giá không tồn tại"));

        validateDuplicateForUpdate(id, model.getTenPhieuGiamGia());

        phieuGiamGia.setTenPhieuGiamGia(model.getTenPhieuGiamGia());
        phieuGiamGia.setThoiGianApDung(model.getThoiGianApDung());
        phieuGiamGia.setThoiGianHetHan(model.getThoiGianHetHan());
        phieuGiamGia.setGiaTriGiam(model.getGiaTriGiam());
        phieuGiamGia.setSoTienToiThieuHd(model.getSoTienToiThieuHd());
        phieuGiamGia.setSoTienGiamToiDa(model.getSoTienGiamToiDa());
        phieuGiamGia.setSoLuong(model.getSoLuong());
        phieuGiamGia.setNgaySua(LocalDateTime.now());
        phieuGiamGia.setHinhThucGiamGia(model.getHinhThucGiamGia());

        phieuGiamGia = phieuGiamGiaRepository.save(phieuGiamGia);

        // Gửi email thông báo cho khách hàng
        List<KhachHang> khachHangs = khachHangRepository.findInPhieuGiamGiaKhachHangByVoucherId(id);
        for (KhachHang khachHang : khachHangs) {
            mailService.sendVoucherUpdateMail(khachHang.getTenDangNhap(), khachHang.getEmail(),
                    phieuGiamGia.getMaPhieuGiamGia(), phieuGiamGia.getTenPhieuGiamGia(),
                    phieuGiamGia.getGiaTriGiam().toString(),
                    phieuGiamGia.getThoiGianApDung().toString(),
                    phieuGiamGia.getThoiGianHetHan().toString(),
                    phieuGiamGia.getTrangThai() == 1 ? "Hoạt động" : "Không hoạt động");
        }

        return PhieuGiamGiaMapper.toDTO(phieuGiamGia);
    }


    public PhieuGiamGiaDto toggleTrangThai(Integer id) {
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Phiếu giảm giá không tồn tại"));

        boolean isVoucherActive = phieuGiamGia.getTrangThai() == 1;
        phieuGiamGia.setTrangThai(isVoucherActive ? 0 : 1);
        phieuGiamGia = phieuGiamGiaRepository.save(phieuGiamGia);

        List<KhachHang> khachHangs = khachHangRepository.findInPhieuGiamGiaKhachHangByVoucherId(id);

        for (KhachHang khachHang : khachHangs) {
            if (isVoucherActive) {
                mailService.sendVoucherInactiveMail(
                        khachHang.getTenDangNhap(),
                        khachHang.getEmail(),
                        phieuGiamGia.getMaPhieuGiamGia(),
                        LocalDateTime.now().toString()
                );
            } else {
                mailService.sendVoucherActiveMail(
                        khachHang.getTenDangNhap(),
                        khachHang.getEmail(),
                        phieuGiamGia.getMaPhieuGiamGia(),
                        LocalDateTime.now().toString()
                );
            }
        }

        return PhieuGiamGiaMapper.toDTO(phieuGiamGia);
    }

    private void validateDuplicate(String maPhieuGiamGia, String tenPhieuGiamGia) {
        if (phieuGiamGiaRepository.existsByMaPhieuGiamGia(maPhieuGiamGia)) {
            throw new EntityAlreadyExistsException("Mã phiếu giảm giá đã tồn tại");
        }
        if (phieuGiamGiaRepository.existsByTenPhieuGiamGia(tenPhieuGiamGia)) {
            throw new EntityAlreadyExistsException("Tên phiếu giảm giá đã tồn tại");
        }
    }

    private void validateDuplicateForUpdate(Integer id, String tenPhieuGiamGia) {
        if (phieuGiamGiaRepository.existsByTenPhieuGiamGia(tenPhieuGiamGia) &&
                phieuGiamGiaRepository.getByTenPhieuGiamGiaAndIdNot(tenPhieuGiamGia, id) != null) {
            throw new EntityAlreadyExistsException("Tên phiếu giảm giá đã tồn tại");
        }
    }
}
