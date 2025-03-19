package com.java.project.controllers;

import com.java.project.dtos.ApiResponse;
import com.java.project.dtos.SanPhamChiTietDto;
import com.java.project.dtos.USanPhamResponse;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.services.USanPhamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/san-pham")
public class USanPhamController {

    @Autowired
    private USanPhamService uSanPhamService;

    @GetMapping
    public ApiResponse getAllSanPhamsWithFilter(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "thuongHieuIds", required = false) List<Integer> thuongHieuIds,
            @RequestParam(value = "xuatXuIds", required = false) List<Integer> xuatXuIds,
            @RequestParam(value = "chatLieuIds", required = false) List<Integer> chatLieuIds,
            @RequestParam(value = "coAoIds", required = false) List<Integer> coAoIds,
            @RequestParam(value = "tayAoIds", required = false) List<Integer> tayAoIds,
            @RequestParam(value = "mauSacIds", required = false) List<Integer> mauSacIds,
            @RequestParam(value = "kichThuocIds", required = false) List<Integer> kichThuocIds,
            @RequestParam(value = "minPrice", required = false) Double minPrice,
            @RequestParam(value = "maxPrice", required = false) Double maxPrice,
            @RequestParam(value = "sortBy", required = false, defaultValue = "gia") String sortBy,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
            Pageable pageable
    ) {
        // Các trường hợp sắp xếp hợp lệ
        List<String> validSortFields = Arrays.asList("gia", "soLuongDaBan", "tenSanPham", "soLuong", "id");

        // Kiểm tra và gán giá trị mặc định nếu tham số không hợp lệ
        if (!validSortFields.contains(sortBy)) {
            sortBy = "gia"; // Mặc định sắp xếp theo giá
        }
        if (!"asc".equalsIgnoreCase(sortDir) && !"desc".equalsIgnoreCase(sortDir)) {
            sortDir = "asc"; // Mặc định sắp xếp tăng dần
        }

        Page<USanPhamResponse> sanPhams = uSanPhamService.getAllSanPhamsWithFilter(
                search,
                thuongHieuIds,
                xuatXuIds,
                chatLieuIds,
                coAoIds,
                tayAoIds,
                mauSacIds,
                kichThuocIds,
                minPrice,
                maxPrice,
                sortBy,
                sortDir,
                pageable
        );
        return new ApiResponse("success", "Lấy danh sách sản phẩm thành công", sanPhams);
    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<ApiResponse> getSanPhamChiTietById(@PathVariable Integer id) {
        try {
            List<SanPhamChiTietDto> sanPhamChiTietDtos = uSanPhamService.getSanPhamChiTietById(id);
            return ResponseEntity.ok(new ApiResponse("success", "Lấy danh sách chi tiết sản phẩm thành công", sanPhamChiTietDtos));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("error", e.getMessage(), null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("error", "Lỗi hệ thống: " + e.getMessage(), null));
        }
    }

}