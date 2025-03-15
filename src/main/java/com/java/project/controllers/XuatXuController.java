package com.java.project.controllers;

import com.java.project.dtos.XuatXuDto;
import com.java.project.dtos.ApiResponse;
import com.java.project.models.XuatXuModel;
import com.java.project.services.XuatXuService;
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
@RequestMapping("/api/xuat-xu")
public class XuatXuController {

    @Autowired
    private XuatXuService xuatXuService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllXuatXu(
            @RequestParam(value = "search", defaultValue = "") String search,
            Pageable pageable) {
        try {
            Page<XuatXuDto> xuatXuDtos = xuatXuService.getAll(search, pageable);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy danh sách xuất xứ thành công", xuatXuDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getXuatXuById(@PathVariable Integer id) {
        try {
            XuatXuDto xuatXuDto = xuatXuService.getById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy thông tin xuất xứ thành công", xuatXuDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addXuatXu(@Valid @RequestBody XuatXuModel xuatXuModel, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(new ApiResponse("error", "Thông tin xuất xứ không hợp lệ: " + errorMessage, null));
            }
            XuatXuDto newXuatXu = xuatXuService.addXuatXu(xuatXuModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", "Xuất xứ đã được thêm thành công", newXuatXu));
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateXuatXu(@PathVariable Integer id,
                                                    @Valid @RequestBody XuatXuModel xuatXuModel, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(new ApiResponse("error", "Thông tin xuất xứ không hợp lệ: " + errorMessage, null));
            }
            XuatXuDto updatedXuatXu = xuatXuService.updateXuatXu(id, xuatXuModel);
            return ResponseEntity.ok(new ApiResponse("success", "Cập nhật xuất xứ thành công", updatedXuatXu));
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
            xuatXuService.toggleTrangThai(id);
            return ResponseEntity.ok(new ApiResponse("success", "Cập nhật trạng thái xuất xứ thành công", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteXuatXu(@PathVariable Integer id) {
        try {
            xuatXuService.deleteXuatXu(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }
}
