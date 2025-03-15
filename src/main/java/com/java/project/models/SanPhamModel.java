package com.java.project.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SanPhamModel {
    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String tenSanPham;

    private String maSanPham;

    private String moTa;
}
