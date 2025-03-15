package com.java.project.controllers;

import com.java.project.dtos.ThuongHieuDto;
import com.java.project.dtos.ApiResponse;
import com.java.project.models.ThuongHieuModel;
import com.java.project.services.ThuongHieuService;
import com.java.project.exceptions.EntityAlreadyExistsException;
import com.java.project.exceptions.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/thuong-hieu")
public class ThuongHieuController {

    @Autowired
    private ThuongHieuService thuongHieuService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllThuongHieu(
            @RequestParam(value = "search", defaultValue = "") String search,
            Pageable pageable) {
        try {
            Page<ThuongHieuDto> thuongHieuDtos = thuongHieuService.getAll(search, pageable);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy danh sách thương hiệu thành công", thuongHieuDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getThuongHieuById(@PathVariable Integer id) {
        try {
            ThuongHieuDto thuongHieuDto = thuongHieuService.getById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy thông tin thương hiệu thành công", thuongHieuDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addThuongHieu(@Valid @RequestBody ThuongHieuModel thuongHieuModel, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(new ApiResponse("error", "Thông tin thương hiệu không hợp lệ: " + errorMessage, null));
            }
            ThuongHieuDto newThuongHieu = thuongHieuService.addThuongHieu(thuongHieuModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", "Thương hiệu đã được thêm thành công", newThuongHieu));
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateThuongHieu(@PathVariable Integer id,
                                                        @Valid @RequestBody ThuongHieuModel thuongHieuModel, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(new ApiResponse("error", "Thông tin thương hiệu không hợp lệ: " + errorMessage, null));
            }
            ThuongHieuDto updatedThuongHieu = thuongHieuService.updateThuongHieu(id, thuongHieuModel);
            return ResponseEntity.ok(new ApiResponse("success", "Cập nhật thương hiệu thành công", updatedThuongHieu));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> toggleTrangThai(@PathVariable Integer id) {
        try {
            thuongHieuService.toggleTrangThai(id);
            return ResponseEntity.ok(new ApiResponse("success", "Cập nhật trạng thái thương hiệu thành công", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteThuongHieu(@PathVariable Integer id) {
        try {
            thuongHieuService.deleteThuongHieu(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }
}
