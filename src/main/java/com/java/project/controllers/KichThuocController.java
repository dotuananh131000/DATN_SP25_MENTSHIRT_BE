package com.java.project.controllers;

import com.java.project.dtos.KichThuocDto;
import com.java.project.models.KichThuocModel;
import com.java.project.services.KichThuocService;
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
@RequestMapping("/api/kich-thuoc")
public class KichThuocController {

    @Autowired
    private KichThuocService kichThuocService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllKichThuoc(
            @RequestParam(value = "search", defaultValue = "") String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "id") String sort,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
        try {
            Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.by(sort).with(Sort.Direction.fromString(direction))));
            Page<KichThuocDto> kichThuocDtos = kichThuocService.getAll(search, pageable);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy dữ liệu thành công", kichThuocDtos));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", null));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getKichThuocById(@PathVariable Integer id) {
        try {
            KichThuocDto kichThuocDto = kichThuocService.getById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy thông tin kích thước thành công", kichThuocDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> addKichThuoc(@Valid @RequestBody KichThuocModel kichThuocModel, BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(new ApiResponse("error", "Validation Error: " + errorMessage, null));
            }

            KichThuocDto newKichThuoc = kichThuocService.addKichThuoc(kichThuocModel);
            return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse("success", "Kích thước đã được thêm thành công", newKichThuoc));

        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse("error", "Kích thước đã tồn tại", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateKichThuoc(@PathVariable Integer id,
                                                       @Valid @RequestBody KichThuocModel kichThuocModel,
                                                       BindingResult bindingResult) {
        try {
            if (bindingResult.hasErrors()) {
                String errorMessage = bindingResult.getAllErrors().stream()
                        .map(ObjectError::getDefaultMessage)
                        .collect(Collectors.joining(", "));
                return ResponseEntity.badRequest().body(new ApiResponse("error", "Validation Error: " + errorMessage, null));
            }

            KichThuocDto updatedKichThuoc = kichThuocService.updateKichThuoc(id, kichThuocModel);
            return ResponseEntity.ok(new ApiResponse("success", "Cập nhật kích thước thành công", updatedKichThuoc));

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", "Kích thước không tồn tại", null));
        } catch (EntityAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse("error", "Kích thước đã tồn tại", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", null));
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse> toggleTrangThai(@PathVariable Integer id) {
        try {
            kichThuocService.toggleTrangThai(id);
            return ResponseEntity.ok(new ApiResponse("success", "Trạng thái đã được cập nhật", null));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", "Kích thước không tồn tại", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteKichThuoc(@PathVariable Integer id) {
        try {
            kichThuocService.deleteKichThuoc(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse("error", "Kích thước không tồn tại", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra trong quá trình xử lý", null));
        }
    }
}
