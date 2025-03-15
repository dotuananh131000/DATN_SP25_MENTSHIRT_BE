package com.java.project.services;

import com.java.project.dtos.NhanVienDto;
import com.java.project.entities.NhanVien;
import com.java.project.entities.VaiTro;
import com.java.project.exceptions.EntityAlreadyExistsException;
import com.java.project.exceptions.ResourceNotFoundException;
import com.java.project.exceptions.RuntimeException;
import com.java.project.mappers.NhanVienMapper;
import com.java.project.models.NhanVienCreateModel;
import com.java.project.models.NhanVienUpdateModel;
import com.java.project.repositories.NhanVienRepository;
import com.java.project.repositories.VaiTroRepository;
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

import java.util.UUID;

@Service
public class NhanVienService {

    @Autowired
    private NhanVienRepository nhanVienRepository;

    @Autowired
    private VaiTroRepository vaiTroRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MailService mailService;

    public NhanVienDto getNhanVienById(Integer id) {
        return NhanVienMapper.toNhanVienDTO(
                nhanVienRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Nhân viên với ID " + id + " không tồn tại."))
        );
    }

    public Page<NhanVienDto> getAllNhanVien(String keyword, Integer trangThai, int page, int size, String sortBy, String sortDirection) {
        if (sortBy == null || sortBy.trim().isEmpty()) {
            sortBy = "id";
        }

        Sort sort = sortDirection.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        return nhanVienRepository.findAllWithFilters(keyword, trangThai, pageable)
                .map(NhanVienMapper::toNhanVienDTO);
    }

    @Transactional
    public NhanVienDto createNhanVien(NhanVienCreateModel model) {
        validateUniqueFields(model.getTenDangNhap(), model.getEmail(), model.getSoDienThoai());

        VaiTro vaiTro = vaiTroRepository.findById(model.getVaiTro())
                .orElseThrow(() -> new ResourceNotFoundException("Vai trò với ID " + model.getVaiTro() + " không tồn tại."));

        NhanVien nhanVien = new NhanVien();
        nhanVien.setMaNhanVien(generateMaNhanVien(model.getMaNhanVien()));
        nhanVien.setTenNhanVien(model.getTenNhanVien());
        nhanVien.setTenDangNhap(model.getTenDangNhap());
        nhanVien.setEmail(model.getEmail());
        nhanVien.setSoDienThoai(model.getSoDienThoai());
        nhanVien.setDiaChi(model.getDiaChi());
        nhanVien.setGioiTinh(model.getGioiTinh());
        nhanVien.setVaiTro(vaiTro);
        nhanVien.setTrangThai(1);
        nhanVien.setAvatarUrl(model.getAvatarUrl());
        String generatedPassword = RandomUtil.generateRandomPassword();
        nhanVien.setMat_khau(passwordEncoder.encode(generatedPassword));

        NhanVien savedNhanVien = nhanVienRepository.save(nhanVien);

        mailService.sendNewPasswordMail(nhanVien.getTenDangNhap(), nhanVien.getEmail(), generatedPassword);

        return NhanVienMapper.toNhanVienDTO(savedNhanVien);
    }


    @Transactional
    public NhanVienDto updateNhanVien(Integer id, NhanVienUpdateModel model) {
        NhanVien nhanVien = nhanVienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nhân viên với ID " + id + " không tồn tại."));

        validateUniqueFieldsForUpdate(id, model.getEmail(), model.getSoDienThoai());

        VaiTro vaiTro = vaiTroRepository.findById(model.getVaiTro())
                .orElseThrow(() -> new ResourceNotFoundException("Vai trò với ID " + model.getVaiTro() + " không tồn tại."));

        nhanVien.setTenNhanVien(model.getTenNhanVien());
        nhanVien.setEmail(model.getEmail());
        nhanVien.setSoDienThoai(model.getSoDienThoai());
        nhanVien.setDiaChi(model.getDiaChi());
        nhanVien.setGioiTinh(model.getGioiTinh());
        nhanVien.setAvatarUrl(model.getAvatarUrl());
        nhanVien.setVaiTro(vaiTro);
        return NhanVienMapper.toNhanVienDTO(nhanVienRepository.save(nhanVien));
    }

    public void resetPassword(Integer id) throws MessagingException {
        NhanVien nhanVien = nhanVienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nhân viên với ID " + id + " không tồn tại."));

        String newPassword = RandomUtil.generateRandomPassword();
        nhanVien.setMat_khau(passwordEncoder.encode(newPassword));
        nhanVienRepository.save(nhanVien);

        mailService.sendNewPasswordMail(nhanVien.getTenDangNhap(), nhanVien.getEmail(), newPassword);
    }

    public NhanVienDto toggleTrangThai(Integer id) {
        NhanVien nhanVien = nhanVienRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Nhân viên với ID " + id + " không tồn tại."));

        nhanVien.setTrangThai(nhanVien.getTrangThai() == 1 ? 0 : 1);
        return NhanVienMapper.toNhanVienDTO(nhanVienRepository.save(nhanVien));
    }

    private String generateMaNhanVien(String existingMa) {
        return existingMa != null ? existingMa : UUID.randomUUID().toString();
    }

    private void updateFields(NhanVien nhanVien, NhanVien updatedNhanVien) {
        nhanVien.setTenNhanVien(updatedNhanVien.getTenNhanVien());
        nhanVien.setEmail(updatedNhanVien.getEmail());
        nhanVien.setSoDienThoai(updatedNhanVien.getSoDienThoai());
        nhanVien.setDiaChi(updatedNhanVien.getDiaChi());
        nhanVien.setGioiTinh(updatedNhanVien.getGioiTinh());

        VaiTro vaiTro = vaiTroRepository.findById(updatedNhanVien.getVaiTro().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vai trò với ID " + updatedNhanVien.getVaiTro().getId() + " không tồn tại."));
        nhanVien.setVaiTro(vaiTro);
    }

    private void validateUniqueFields(String tenDangNhap, String email, String soDienThoai) {
        if (nhanVienRepository.existsByTenDangNhap(tenDangNhap)) {
            throw new EntityAlreadyExistsException("Tên đăng nhập đã tồn tại.");
        }
        if (nhanVienRepository.existsByEmail(email)) {
            throw new EntityAlreadyExistsException("Email đã tồn tại.");
        }
        if (nhanVienRepository.existsBySoDienThoai(soDienThoai)) {
            throw new EntityAlreadyExistsException("Số điện thoại đã tồn tại.");
        }
    }

    private void validateUniqueFieldsForUpdate(Integer id, String email, String soDienThoai) {
        nhanVienRepository.findByEmail(email).ifPresent(nv -> {
            if (!nv.getId().equals(id)) {
                throw new EntityAlreadyExistsException("Email đã tồn tại.");
            }
        });

        nhanVienRepository.findBySoDienThoai(soDienThoai).ifPresent(nv -> {
            if (!nv.getId().equals(id)) {
                throw new EntityAlreadyExistsException("Số điện thoại đã tồn tại.");
            }
        });
    }

}