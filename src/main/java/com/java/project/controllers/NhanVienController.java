package com.java.project.controllers;

import com.java.project.dtos.ApiResponse;
import com.java.project.dtos.NhanVienDto;
import com.java.project.exceptions.EntityAlreadyExistsException;
import com.java.project.exceptions.ResourceNotFoundException;
import com.java.project.exceptions.RuntimeException;
import com.java.project.models.NhanVienCreateModel;
import com.java.project.models.NhanVienUpdateModel;
import com.java.project.services.NhanVienService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nhan-vien")
public class NhanVienController {

    @Autowired
    private NhanVienService nhanVienService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getNhanVienById(@PathVariable Integer id) {
        try {
            NhanVienDto nhanVienDto = nhanVienService.getNhanVienById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy thông tin nhân viên thành công", nhanVienDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAllNhanVien(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer trangThai,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "desc") String direction) {

        Page<NhanVienDto> nhanViens = nhanVienService.getAllNhanVien(keyword, trangThai, page, size, sort, direction);
        return ResponseEntity.ok(new ApiResponse("success", "Lấy danh sách nhân viên thành công", nhanViens));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createNhanVien(@Valid @RequestBody NhanVienCreateModel model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", "Dữ liệu không hợp lệ", bindingResult.getAllErrors()));
        }
        try {
            NhanVienDto nhanVienDto = nhanVienService.createNhanVien(model);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", "Nhân viên được tạo thành công", nhanVienDto));
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("error", e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateNhanVien(@PathVariable Integer id, @Valid @RequestBody NhanVienUpdateModel model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new ApiResponse("error", "Dữ liệu không hợp lệ", bindingResult.getAllErrors()));
        }
        try {
            NhanVienDto nhanVienDto = nhanVienService.updateNhanVien(id, model);
            return ResponseEntity.ok(new ApiResponse("success", "Cập nhật nhân viên thành công", nhanVienDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @PostMapping("/reset-password/{id}")
    public ResponseEntity<ApiResponse> resetPassword(@PathVariable Integer id) {
        try {
            nhanVienService.resetPassword(id);
            return ResponseEntity.ok(new ApiResponse("success", "Đặt lại mật khẩu thành công", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @PatchMapping("/toggle-trang-thai/{id}")
    public ResponseEntity<ApiResponse> toggleTrangThai(@PathVariable Integer id) {
        try {
            NhanVienDto nhanVienDto = nhanVienService.toggleTrangThai(id);
            return ResponseEntity.ok(new ApiResponse("success", "Chuyển trạng thái thành công", nhanVienDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        }
    }
}
