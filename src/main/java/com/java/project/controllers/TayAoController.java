package com.java.project.controllers;

import com.java.project.dtos.TayAoDto;
import com.java.project.dtos.ApiResponse;
import com.java.project.models.TayAoModel;
import com.java.project.services.TayAoService;
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
@RequestMapping("/api/tay-ao")
public class TayAoController {

    @Autowired
    private TayAoService tayAoService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllTayAo(
            @RequestParam(value = "search", defaultValue = "") String search,
            Pageable pageable) {
        try {
            Page<TayAoDto> tayAoDtos = tayAoService.getAll(search, pageable);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy danh sách tay áo thành công", tayAoDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getTayAoById(@PathVariable Integer id) {
        try {
            TayAoDto tayAoDto = tayAoService.getById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy thông tin tay áo thành công", tayAoDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addTayAo(@Valid @RequestBody TayAoModel tayAoModel, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(new ApiResponse("error", "Thông tin tay áo không hợp lệ: " + errorMessage, null));
            }
            TayAoDto newTayAo = tayAoService.addTayAo(tayAoModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", "Tay áo đã được thêm thành công", newTayAo));
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateTayAo(@PathVariable Integer id,
                                                   @Valid @RequestBody TayAoModel tayAoModel, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(new ApiResponse("error", "Thông tin tay áo không hợp lệ: " + errorMessage, null));
            }
            TayAoDto updatedTayAo = tayAoService.updateTayAo(id, tayAoModel);
            return ResponseEntity.ok(new ApiResponse("success", "Cập nhật tay áo thành công", updatedTayAo));
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
            tayAoService.toggleTrangThai(id);
            return ResponseEntity.ok(new ApiResponse("success", "Cập nhật trạng thái tay áo thành công", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteTayAo(@PathVariable Integer id) {
        try {
            tayAoService.deleteTayAo(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }
}
