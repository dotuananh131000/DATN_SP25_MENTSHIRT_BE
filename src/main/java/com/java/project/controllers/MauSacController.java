package com.java.project.controllers;

import com.java.project.dtos.MauSacDto;
import com.java.project.models.MauSacModel;
import com.java.project.models.MauSacUpdateModel;
import com.java.project.services.MauSacService;
import com.java.project.exceptions.EntityAlreadyExistsException;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.dtos.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/mau-sac")
public class MauSacController {

    @Autowired
    private MauSacService mauSacService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllMauSac(
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.by(sort).with(Sort.Direction.fromString(direction))));
            Page<MauSacDto> mauSacDtos = mauSacService.getAll(search, pageable);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy dữ liệu thành công", mauSacDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getMauSacById(@PathVariable Integer id) {
        try {
            MauSacDto mauSacDto = mauSacService.getById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy thông tin màu sắc thành công", mauSacDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addMauSac(@Valid @RequestBody MauSacModel mauSacModel, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(new ApiResponse("error", "Validation Error: " + errorMessage, null));
            }

            MauSacDto newMauSac = mauSacService.addMauSac(mauSacModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", "Màu sắc đã được thêm thành công", newMauSac));

        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error", "Màu sắc đã tồn tại", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateMauSac(@PathVariable Integer id,
                                                    @Valid @RequestBody MauSacUpdateModel mauSacModel,
                                                    BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(new ApiResponse("error", "Validation Error: " + errorMessage, null));
            }

            MauSacDto updatedMauSac = mauSacService.updateMauSac(id, mauSacModel);
            return ResponseEntity.ok(new ApiResponse("success", "Cập nhật màu sắc thành công", updatedMauSac));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", "Màu sắc không tồn tại", null));
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("error", "Màu sắc đã tồn tại", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", e.getMessage()));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> toggleTrangThai(@PathVariable Integer id) {
        try {
            mauSacService.toggleTrangThai(id);
            return ResponseEntity.ok(new ApiResponse("success", "Trạng thái đã được cập nhật", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", "Màu sắc không tồn tại", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteMauSac(@PathVariable Integer id) {
        try {
            mauSacService.deleteMauSac(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", "Màu sắc không tồn tại", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", null));
        }
    }
}
