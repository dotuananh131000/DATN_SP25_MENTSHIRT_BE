package com.java.project.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateSanPhamChiTietModel {
    @NotNull(message = "Thương hiệu không được để trống.")
    private Integer thuongHieu;

    @NotNull(message = "Xuất xứ không được để trống.")
    private Integer xuatXu;

    @NotNull(message = "Chất liệu không được để trống.")
    private Integer chatLieu;

    @NotNull(message = "Cỡ áo không được để trống.")
    private Integer coAo;

    @NotNull(message = "Tay áo không được để trống.")
    private Integer tayAo;

    @NotNull(message = "Màu sắc không được để trống.")
    private Integer mauSac;

    @NotNull(message = "Kích thước không được để trống.")
    private Integer kichThuoc;

    @NotNull(message = "Số lượng không được để trống.")
    @Positive(message = "Số lượng phải là số dương.")
    private Integer soLuong;

    @NotNull(message = "Đơn giá không được để trống.")
    @Positive(message = "Đơn giá phải là số dương.")
    private BigDecimal donGia;

    private String hinhAnh;
}

