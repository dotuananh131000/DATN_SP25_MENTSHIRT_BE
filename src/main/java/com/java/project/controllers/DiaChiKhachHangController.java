package com.java.project.controllers;

import com.java.project.dtos.ApiResponse;
import com.java.project.dtos.DiaChiKhachHangDto;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.models.DiaChiKhachHangCreateModel;
import com.java.project.models.DiaChiKhachHangUpdateModel;
import com.java.project.services.DiaChiKhachHangService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/dia-chi")
public class DiaChiKhachHangController {

    @Autowired
    private DiaChiKhachHangService diaChiKhachHangService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Integer id) {
        try {
            DiaChiKhachHangDto diaChiKhachHangDto = diaChiKhachHangService.getById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Địa chỉ khách hàng được tìm thấy", diaChiKhachHangDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @GetMapping("/khach-hang/{khachHangId}")
    public ResponseEntity<ApiResponse> getByKhachHangId(@PathVariable Integer khachHangId) {
        List<DiaChiKhachHangDto> diaChiKhachHangDtos = diaChiKhachHangService.getByKhachHangId(khachHangId);
        return ResponseEntity.ok(new ApiResponse("success", "Danh sách địa chỉ khách hàng", diaChiKhachHangDtos));
    }

    @PostMapping
    public ResponseEntity<ApiResponse> create(@Valid @RequestBody DiaChiKhachHangCreateModel model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(buildValidationErrorResponse(bindingResult));
        }
        try {
            DiaChiKhachHangDto diaChiKhachHangDto = diaChiKhachHangService.create(model);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("success", "Địa chỉ khách hàng được tạo thành công", diaChiKhachHangDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> update(@PathVariable Integer id, @Valid @RequestBody DiaChiKhachHangUpdateModel model, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(buildValidationErrorResponse(bindingResult));
        }
        try {
            DiaChiKhachHangDto diaChiKhachHangDto = diaChiKhachHangService.update(id, model);
            return ResponseEntity.ok(new ApiResponse("success", "Địa chỉ khách hàng được cập nhật thành công", diaChiKhachHangDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> delete(@PathVariable Integer id) {
        try {
            diaChiKhachHangService.delete(id);
            return ResponseEntity.ok(new ApiResponse("success", "Địa chỉ khách hàng được xóa thành công", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        }
    }

//    @PatchMapping("/toggleStatus/{id}")
//    public ResponseEntity<ApiResponse> toggleStatus(@PathVariable Integer id) {
//        try {
//            DiaChiKhachHangDto diaChiKhachHangDto = diaChiKhachHangService.toggleStatus(id);
//            return ResponseEntity.ok(new ApiResponse("success", "Trạng thái địa chỉ khách hàng được thay đổi thành công", diaChiKhachHangDto));
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
//        }
//    }

    private ApiResponse buildValidationErrorResponse(BindingResult bindingResult) {
        List<String> errors = bindingResult.getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.toList());
        return new ApiResponse("error", "Dữ liệu không hợp lệ", errors);
    }
}