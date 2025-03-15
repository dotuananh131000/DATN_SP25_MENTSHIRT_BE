package com.java.project.controllers;

import com.java.project.dtos.ApiResponse;
import com.java.project.dtos.KhachHangDto;
import com.java.project.exceptions.RuntimeException;
import com.java.project.models.KhachHangCreateModel;
import com.java.project.models.KhachHangUpdateModel;
import com.java.project.services.KhachHangService;
import com.java.project.exceptions.EntityAlreadyExistsException;
import com.java.project.exceptions.ResourceNotFoundException;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api/khach-hang")
public class KhachHangConroller {
    @Autowired
    private KhachHangService khachHangService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Integer id) {
        try {
            KhachHangDto khachHangDto = khachHangService.getKhachHangById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Khách hàng được tìm thấy", khachHangDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @GetMapping("/voucher/{voucherId}")
    public ResponseEntity<ApiResponse> getKhachHangByVoucher(@PathVariable Integer voucherId,
                                                             @RequestParam(required = false) String search,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);

            Page<KhachHangDto> khachHangDtos = khachHangService.getKhachHangByVoucher(voucherId, search, pageable);
            return ResponseEntity.ok(new ApiResponse("success", "Danh sách khách hàng được tìm thấy", khachHangDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage(), null));
        }
    }



    @GetMapping
    public ResponseEntity<ApiResponse> getAllKhachHang(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer trangThai,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            var khachHangPage = khachHangService.getAllKhachHang(keyword, trangThai, page, size, sortBy, sortDirection);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy danh sách khách hàng thành công", khachHangPage));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody KhachHangCreateModel model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(buildValidationErrorResponse(bindingResult));
        }
        try {
            KhachHangDto khachHangDto = khachHangService.createKhachHang(model);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", "Khách hàng được tạo thành công", khachHangDto));
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Integer id, @Valid @RequestBody KhachHangUpdateModel model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(buildValidationErrorResponse(bindingResult));
        }
        try {
            KhachHangDto khachHangDto = khachHangService.updateKhachHang(id, model);
            return ResponseEntity.ok(new ApiResponse("success", "Khách hàng được cập nhật thành công", khachHangDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("error", e.getMessage(), null));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @PatchMapping("/toggle-trang-thai/{id}")
    public ResponseEntity<ApiResponse> toggleStatus(@PathVariable Integer id) {
        try {
            KhachHangDto khachHangDto = khachHangService.toggleTrangThai(id);
            return ResponseEntity.ok(new ApiResponse("success", "Trạng thái khách hàng được thay đổi thành công", khachHangDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @PostMapping("/reset-password/{id}")
    public ResponseEntity<ApiResponse> resetPassword(@PathVariable Integer id) {
        try {
            khachHangService.resetPassword(id);
            return ResponseEntity.ok(new ApiResponse("success", "Đặt lại mật khẩu thành công", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    private ApiResponse buildValidationErrorResponse(BindingResult bindingResult) {
        List<String> errors = bindingResult.getAllErrors()
                .stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
        return new ApiResponse("error", "Dữ liệu không hợp lệ", errors);
    }

}
