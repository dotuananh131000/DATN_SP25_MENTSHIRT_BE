package com.java.project.models;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DiaChiKhachHangUpdateModel {
    @NotNull(message = "Mã tỉnh thành không được để trống")
    private Integer tinhThanhId;

    @NotBlank(message = "Tỉnh thành không được để trống")
    @Size(max = 255, message = "Tỉnh thành không được vượt quá 255 ký tự")
    private String tinhThanh;

    @NotNull(message = "Mã quận huyện không được để trống")
    private Integer quanHuyenId;

    @NotBlank(message = "Quận huyện không được để trống")
    private String quanHuyen;

    @NotNull(message = "Mã phường xã không được để trống")
    private Integer phuongXaId;

    @NotBlank(message = "Phường xã không được để trống")
    @Size(max = 255, message = "Phường xã không được vượt quá 255 ký tự")
    private String phuongXa;

    @NotBlank(message = "Địa chỉ chi tiết không được để trống")
    @Size(max = 255, message = "Địa chỉ chi tiết không được vượt quá 255 ký tự")
    private String diaChiChiTiet;
}