package com.java.project.controllers;

import com.java.project.dtos.ApiResponse;
import com.java.project.dtos.ChatLieuDto;
import com.java.project.dtos.SanPhamChiTietDto;
import com.java.project.dtos.UThuocTinhSanPhamListDto;
import com.java.project.services.USanPhamChiTietService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/san-pham-chi-tiet")
public class USanPhamChiTietController {

    @Autowired
    private USanPhamChiTietService uSanPhamChiTietService;

    @GetMapping("/thuoc-tinh")
    public ApiResponse getThuocTinhBySanPhamId(@RequestParam Integer sanPhamId) {
        UThuocTinhSanPhamListDto thuocTinh = uSanPhamChiTietService.getThuocTinhbySanPhamId(sanPhamId);

        return new ApiResponse("success", "Lấy danh sách thành công", thuocTinh);
    }

    @GetMapping("/chi-tiet")
    public ApiResponse getSanPhamChiTiet(
            @RequestParam Integer sanPhamId,
            @RequestParam Integer chatLieuId,
            @RequestParam Integer coAoId,
            @RequestParam Integer kichThuocId,
            @RequestParam Integer mauSacId,
            @RequestParam Integer tayAoId,
            @RequestParam Integer thuongHieuId,
            @RequestParam Integer xuatXuId
    ) {
        SanPhamChiTietDto sanPhamChiTiet = uSanPhamChiTietService.getByThuocTinh(
                sanPhamId, chatLieuId, coAoId, kichThuocId, mauSacId, tayAoId, thuongHieuId, xuatXuId);

        if (sanPhamChiTiet == null) {
            return new ApiResponse("error", "Không tìm thấy sản phẩm chi tiết với thuộc tính này", null);
        }

        return new ApiResponse("success", "Lây danh sách thành công", sanPhamChiTiet);
    }
}