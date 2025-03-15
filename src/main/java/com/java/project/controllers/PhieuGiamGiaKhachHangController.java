package com.java.project.controllers;

import com.java.project.dtos.ApiResponse;
import com.java.project.dtos.PhieuGiamGiaKhachHangDto;
import com.java.project.models.PhieuGiamGiaKhachHangModel;
import com.java.project.services.PhieuGiamGiaKhachHangService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phieu-giam-gia-khach-hang")
public class PhieuGiamGiaKhachHangController {

    @Autowired
    private PhieuGiamGiaKhachHangService phieuGiamGiaKhachHangService;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse> getById(@PathVariable Integer id) {
        try {
            PhieuGiamGiaKhachHangDto dto = phieuGiamGiaKhachHangService.getById(id);
            return ResponseEntity.ok(new ApiResponse("thanh_cong", "Tìm thấy phiếu giảm giá", dto));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ApiResponse("loi", e.getMessage(), null));
        }
    }

    @GetMapping("/khach-hang/{id}")
    public ResponseEntity<ApiResponse> getByKhachHangId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(new ApiResponse("thanh_cong", "Danh sách phiếu giảm giá của khách hàng", phieuGiamGiaKhachHangService.getByKhachHangId(id)));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ApiResponse("loi", e.getMessage(), null));
        }
    }

    @GetMapping("/voucher/{id}")
    public ResponseEntity<ApiResponse> getByVoucherId(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(new ApiResponse("thanh_cong", "Danh sách phiếu giảm giá của voucher", phieuGiamGiaKhachHangService.getByVoucherId(id)));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ApiResponse("loi", e.getMessage(), null));
        }
    }

    @PostMapping("/batch")
    public ResponseEntity<ApiResponse> createMultiple(@Valid @RequestBody List<PhieuGiamGiaKhachHangModel> models) {
        if (models == null || models.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse("loi", "Danh sách phiếu giảm giá không được để trống", null));
        }
        try {
            List<PhieuGiamGiaKhachHangDto> dtos = phieuGiamGiaKhachHangService.createMultiple(models);
            return ResponseEntity.ok(new ApiResponse("thanh_cong", "Tạo phiếu giảm giá thành công", dtos));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse("loi", e.getMessage(), null));
        }
    }

    @DeleteMapping("/batch")
    public ResponseEntity<ApiResponse> deleteMultiple(@RequestBody List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return ResponseEntity.badRequest().body(new ApiResponse("loi", "Danh sách ID không được để trống", null));
        }
        try {
            phieuGiamGiaKhachHangService.deleteMultiple(ids);
            return ResponseEntity.ok(new ApiResponse("thanh_cong", "Xóa phiếu giảm giá thành công", null));
        } catch (Exception e) {
            return ResponseEntity.status(404).body(new ApiResponse("loi", e.getMessage(), null));
        }
    }
}
