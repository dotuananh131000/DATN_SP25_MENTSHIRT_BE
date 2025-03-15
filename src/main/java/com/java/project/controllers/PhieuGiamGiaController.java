package com.java.project.controllers;

import com.java.project.dtos.ApiResponse;
import com.java.project.dtos.PhieuGiamGiaDto;
import com.java.project.exceptions.EntityAlreadyExistsException;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.models.PhieuGiamGiaCreateModel;
import com.java.project.models.PhieuGiamGiaUpdateModel;
import com.java.project.services.PhieuGiamGiaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/phieu-giam-gia")
public class PhieuGiamGiaController {

    @Autowired
    private PhieuGiamGiaService phieuGiamGiaService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Integer id) {
        try {
            PhieuGiamGiaDto phieuGiamGiaDto = phieuGiamGiaService.getById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Phiếu giảm giá được tìm thấy", phieuGiamGiaDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getAll(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) LocalDateTime endTime,
            @RequestParam(required = false) Integer loaiGiam,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortKey,
            @RequestParam(defaultValue = "desc") String sortDirection) {
        try {
            var phieuGiamGiaPage = phieuGiamGiaService.getAll(keyword, startTime, endTime, loaiGiam, page, size, sortKey, sortDirection);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy danh sách phiếu giảm giá thành công", phieuGiamGiaPage));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody PhieuGiamGiaCreateModel model) {
        try {
            PhieuGiamGiaDto phieuGiamGiaDto = phieuGiamGiaService.create(model);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", "Phiếu giảm giá được tạo thành công", phieuGiamGiaDto));
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Integer id, @Valid @RequestBody PhieuGiamGiaUpdateModel model) {
        try {
            PhieuGiamGiaDto phieuGiamGiaDto = phieuGiamGiaService.update(id, model);
            return ResponseEntity.ok(new ApiResponse("success", "Phiếu giảm giá được cập nhật thành công", phieuGiamGiaDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @PatchMapping("/toggle-trang-thai/{id}")
    public ResponseEntity<ApiResponse> toggleStatus(@PathVariable Integer id) {
        try {
            PhieuGiamGiaDto phieuGiamGiaDto = phieuGiamGiaService.toggleTrangThai(id);
            String message = phieuGiamGiaDto.getTrangThai() == 1 ? "Voucher đã được mở lại" : "Voucher đã bị khóa";
            return ResponseEntity.ok(new ApiResponse("success", message, phieuGiamGiaDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        }
    }
}
