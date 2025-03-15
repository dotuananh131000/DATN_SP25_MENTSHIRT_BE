package com.java.project.services;

import com.java.project.dtos.KhachHangDto;
import com.java.project.entities.KhachHang;
import com.java.project.entities.NhanVien;
import com.java.project.exceptions.EntityAlreadyExistsException;
import com.java.project.exceptions.ResourceNotFoundException;
import com.java.project.exceptions.RuntimeException;
import com.java.project.mappers.KhachHangMapper;
import com.java.project.models.KhachHangCreateModel;
import com.java.project.models.KhachHangUpdateModel;
import com.java.project.repositories.KhachHangRepository;
import com.java.project.utils.RandomUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class KhachHangService {

    @Autowired
    KhachHangRepository khachHangRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MailService mailService;

    public KhachHangDto getKhachHangById(Integer id) {
        return KhachHangMapper.toDTO(
                khachHangRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Khách hàng với ID " + id + " không tồn tại."))
        );
    }

    public Page<KhachHangDto> getKhachHangByVoucher(Integer voucherId, String search, Pageable pageable) {
        Page<KhachHang> khachHangsPage = khachHangRepository.findInPhieuGiamGiaKhachHangByVoucherId(voucherId, search, pageable);
        return khachHangsPage.map(KhachHangMapper::toDTO);
    }

    public Page<KhachHangDto> getAllKhachHang(String keyword, Integer trangThai, int page, int size, String sortBy, String sortDirection) {
        sortBy = (sortBy == null || sortBy.trim().isEmpty()) ? "id" : sortBy;
        Sort sort = "asc".equalsIgnoreCase(sortDirection) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return khachHangRepository.findAllWithFilters(keyword, trangThai, pageable)
                .map(KhachHangMapper::toDTO);
    }

    @Transactional
    public KhachHangDto createKhachHang(KhachHangCreateModel model) {
        validateUniqueFields(model.getTenDangNhap(), model.getEmail(), model.getSoDienThoai());

        KhachHang khachHang = new KhachHang();
        khachHang.setMaKhachHang(generateMaKhachHang(model.getMaKhachHang()));
        khachHang.setTenKhachHang(model.getTenKhachHang());
        khachHang.setTenDangNhap(model.getTenDangNhap());
        khachHang.setEmail(model.getEmail());
        khachHang.setSoDienThoai(model.getSoDienThoai());
        khachHang.setGioiTinh(model.getGioiTinh());
        khachHang.setNgaySinh(model.getNgaySinh());
        khachHang.setAvatarUrl(model.getAvatarUrl());
        khachHang.setTrangThai(1);
        String generatedPassword = RandomUtil.generateRandomPassword();
        khachHang.setMat_khau(passwordEncoder.encode(generatedPassword));
        KhachHang saveKhachHang = khachHangRepository.save(khachHang);
        mailService.sendNewPasswordMail(khachHang.getTenDangNhap(), khachHang.getEmail(), generatedPassword);
        return KhachHangMapper.toDTO(saveKhachHang);
    }


    @Transactional
    public KhachHangDto updateKhachHang(Integer id, KhachHangUpdateModel model) {
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khách hàng với ID " + id + " không tồn tại."));

        validateUniqueFieldsForUpdate(id, model.getEmail(), model.getSoDienThoai());

        khachHang.setTenKhachHang(model.getTenKhachHang());
        khachHang.setEmail(model.getEmail());
        khachHang.setSoDienThoai(model.getSoDienThoai());
        khachHang.setGioiTinh(model.getGioiTinh());
        khachHang.setNgaySinh(model.getNgaySinh());
        khachHang.setAvatarUrl(model.getAvatarUrl());

        return KhachHangMapper.toDTO(khachHangRepository.save(khachHang));
    }

    public KhachHangDto toggleTrangThai(Integer id) {
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Khách hàng với ID " + id + " không tồn tại."));

        khachHang.setTrangThai(khachHang.getTrangThai() == 1 ? 0 : 1);
        return KhachHangMapper.toDTO(khachHangRepository.save(khachHang));
    }


    public void resetPassword(Integer id) throws MessagingException {
        KhachHang khachHang = khachHangRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nhân viên với ID " + id + " không tồn tại."));

        String newPassword = RandomUtil.generateRandomPassword();
        khachHang.setMat_khau(passwordEncoder.encode(newPassword));
        khachHangRepository.save(khachHang);

        mailService.sendNewPasswordMail(khachHang.getTenDangNhap(), khachHang.getEmail(), newPassword);
    }

    private String generateMaKhachHang(String existingMa) {
        return existingMa != null ? existingMa : UUID.randomUUID().toString();
    }

    private void validateUniqueFields(String tenDangNhap, String email, String soDienThoai) {
        if (khachHangRepository.existsByTenDangNhap(tenDangNhap)) {
            throw new EntityAlreadyExistsException("Tên đăng nhập đã tồn tại.");
        }
        if (khachHangRepository.existsByEmail(email)) {
            throw new EntityAlreadyExistsException("Email đã tồn tại.");
        }
        if (khachHangRepository.existsBySoDienThoai(soDienThoai)) {
            throw new EntityAlreadyExistsException("Số điện thoại đã tồn tại.");
        }
    }

    private void validateUniqueFieldsForUpdate(Integer id, String email, String soDienThoai) {
        khachHangRepository.findByEmail(email).ifPresent(kh -> {
            if (!kh.getId().equals(id)) {
                throw new EntityAlreadyExistsException("Email đã tồn tại.");
            }
        });

        khachHangRepository.findBySoDienThoai(soDienThoai).ifPresent(kh -> {
            if (!kh.getId().equals(id)) {
                throw new EntityAlreadyExistsException("Số điện thoại đã tồn tại.");
            }
        });
    }
}
