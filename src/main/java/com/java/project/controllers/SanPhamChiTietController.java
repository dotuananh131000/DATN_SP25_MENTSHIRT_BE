package com.java.project.controllers;

import com.java.project.dtos.ApiResponse;
import com.java.project.dtos.SanPhamChiTietDto;
import com.java.project.dtos.SanPhamChiTietPhanLoaiDTO;
import com.java.project.dtos.SanPhamChiTietResponse;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.models.SanPhamChiTietGenerateModel;
import com.java.project.models.SanPhamChiTietModel;
import com.java.project.models.UpdateSanPhamChiTietModel;
import com.java.project.services.SanPhamChiTietService;
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
@RequestMapping("/api/san-pham-chi-tiet")
public class SanPhamChiTietController {

    @Autowired
    private SanPhamChiTietService sanPhamChiTietService;

    @GetMapping
    public ResponseEntity<ApiResponse> getAllSanPhamChiTiet(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) List<Integer> thuongHieuIds,
            @RequestParam(required = false) List<Integer> xuatXuIds,
            @RequestParam(required = false) List<Integer> chatLieuIds,
            @RequestParam(required = false) List<Integer> coAoIds,
            @RequestParam(required = false) List<Integer> tayAoIds,
            @RequestParam(required = false) List<Integer> mauSacIds,
            @RequestParam(required = false) List<Integer> kichThuocIds,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            Pageable pageable) {
        try {
            if (thuongHieuIds != null && thuongHieuIds.isEmpty()) thuongHieuIds = null;
            if (xuatXuIds != null && xuatXuIds.isEmpty()) xuatXuIds = null;
            if (chatLieuIds != null && chatLieuIds.isEmpty()) chatLieuIds = null;
            if (coAoIds != null && coAoIds.isEmpty()) coAoIds = null;
            if (tayAoIds != null && tayAoIds.isEmpty()) tayAoIds = null;
            if (mauSacIds != null && mauSacIds.isEmpty()) mauSacIds = null;
            if (kichThuocIds != null && kichThuocIds.isEmpty()) kichThuocIds = null;

            Page<SanPhamChiTietDto> result = sanPhamChiTietService.getAllSanPhamChiTiet(
                    search, thuongHieuIds, xuatXuIds, chatLieuIds, coAoIds, tayAoIds,
                    mauSacIds, kichThuocIds, minPrice, maxPrice, pageable);

            return ResponseEntity.ok(new ApiResponse("success", "Lấy danh sách chi tiết sản phẩm thành công", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse("error", "Có lỗi xảy ra: " + e.getMessage(), null));
        }
    }





    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getSanPhamChiTietById(@PathVariable Integer id) {
        try {
            SanPhamChiTietDto sanPhamChiTietDto = sanPhamChiTietService.getById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy thông tin chi tiết sản phẩm thành công", sanPhamChiTietDto));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createSanPhamChiTiet(@RequestBody @Valid List<SanPhamChiTietModel> sanPhamChiTietModels, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new ApiResponse("error", "Dữ liệu không hợp lệ", errorMessages));
        }
        try {
            List<SanPhamChiTietDto> result = sanPhamChiTietService.createSanPhamChiTietList(sanPhamChiTietModels);
            return ResponseEntity.ok(new ApiResponse("success", "Tạo chi tiết sản phẩm thành công", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Có lỗi xảy ra: " + e.getMessage(), null));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse> updateSanPhamChiTiet(@PathVariable Integer id,
                                                            @RequestBody @Valid UpdateSanPhamChiTietModel model,
                                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(ObjectError::getDefaultMessage)
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(new ApiResponse("error", "Dữ liệu không hợp lệ", errorMessages));
        }
        try {
            SanPhamChiTietDto result = sanPhamChiTietService.updateSanPhamChiTiet(id, model);
            return ResponseEntity.ok(new ApiResponse("success", "Cập nhật chi tiết sản phẩm thành công", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Có lỗi xảy ra: " + e.getMessage(), null));
        }
    }


    @PatchMapping("/{id}/toggle")
    public ResponseEntity<ApiResponse> toggleTrangThai(@PathVariable Integer id) {
        try {
            SanPhamChiTietDto result = sanPhamChiTietService.toggleTrangThai(id);
            return ResponseEntity.ok(new ApiResponse("success", "Cập nhật trạng thái thành công", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Có lỗi xảy ra: " + e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteSanPhamChiTiet(@PathVariable Integer id) {
        try {
            sanPhamChiTietService.deleteSanPhamChiTiet(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Có lỗi xảy ra: " + e.getMessage(), null));
        }
    }

    @PostMapping("/generate")
    public ResponseEntity<ApiResponse> generateSanPhamChiTiet(@RequestBody SanPhamChiTietGenerateModel generateModel) {
        try {
            List<SanPhamChiTietPhanLoaiDTO> result = sanPhamChiTietService.generateSanPhamChiTietGroupedByMauSac(generateModel);
            return ResponseEntity.ok(new ApiResponse("success", "Tạo chi tiết sản phẩm thành công", result));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Có lỗi xảy ra: " + e.getMessage(), null));
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi hệ thống: " + e.getMessage());
    }

    @GetMapping("/hien-thi")
    public List<SanPhamChiTietResponse>getAll(){
        return sanPhamChiTietService.getAll();
    }
}
