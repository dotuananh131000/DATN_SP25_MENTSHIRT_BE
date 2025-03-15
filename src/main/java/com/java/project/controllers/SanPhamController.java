package com.java.project.controllers;

import com.java.project.dtos.SanPhamChiTietDto;
import com.java.project.dtos.SanPhamDto;
import com.java.project.dtos.ApiResponse;
import com.java.project.exceptions.EntityAlreadyExistsException;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.models.SanPhamModel;
import com.java.project.services.SanPhamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/san-pham")
public class SanPhamController {

    @Autowired
    private SanPhamService sanPhamService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllSanPham(
            @RequestParam(required = false, defaultValue = "") String search,
            Pageable pageable) {
        try {
            Page<SanPhamDto> sanPhams = sanPhamService.getAllSanPham(search, pageable);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy danh sách sản phẩm thành công", sanPhams));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getSanPhamById(@PathVariable Integer id) {
        try {
            SanPhamDto sanPhamDto = sanPhamService.getById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy thông tin sản phẩm thành công", sanPhamDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<ApiResponse> getSanPhamChiTietById(@PathVariable Integer id) {
        try {
            List<SanPhamChiTietDto> sanPhamChiTietDtos = sanPhamService.getSanPhamChiTietById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy danh sách chi tiết sản phẩm thành công", sanPhamChiTietDtos));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createSanPham(@Valid @RequestBody SanPhamModel sanPhamModel, BindingResult result) {
        try {
            if (result.hasErrors()) {
                String errorMessage = result.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(new ApiResponse("error", "Thông tin sản phẩm không hợp lệ: " + errorMessage, null));
            }

            SanPhamDto createdSanPham = sanPhamService.createSanPham(sanPhamModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", "Sản phẩm đã được tạo thành công", createdSanPham));

        } catch (EntityAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("error", ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateSanPham(@PathVariable Integer id, @Valid @RequestBody SanPhamModel sanPhamModel, BindingResult result) {
        try {
            if (result.hasErrors()) {
                String errorMessage = result.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(new ApiResponse("error", "Thông tin sản phẩm không hợp lệ: " + errorMessage, null));
            }

            SanPhamDto updatedSanPham = sanPhamService.updateSanPham(id, sanPhamModel);
            return ResponseEntity.ok(new ApiResponse("success", "Cập nhật sản phẩm thành công", updatedSanPham));

        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", ex.getMessage(), null));
        } catch (EntityAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ApiResponse("error", ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> toggleTrangThai(@PathVariable Integer id) {
        try {
            SanPhamDto sanPhamDto = sanPhamService.toggleTrangThai(id);
            return ResponseEntity.ok(new ApiResponse("success", "Cập nhật trạng thái sản phẩm thành công", sanPhamDto));
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSanPham(@PathVariable Integer id) {
        try {
            sanPhamService.deleteSanPham(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", ex.getMessage(), null));
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + ex.getMessage(), null));
        }
    }
}
