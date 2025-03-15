package com.java.project.models;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class KhachHangUpdateModel {
    @NotBlank(message = "Tên khách hàng không được để trống")
    @Size(max = 255, message = "Tên khách hàng không được vượt quá 255 ký tự")
    private String tenKhachHang;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 255, message = "Email không được vượt quá 255 ký tự")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0[0-9]{9,10})$", message = "Số điện thoại không hợp lệ")
    private String soDienThoai;

    @NotNull(message = "Giới tính không được để trống")
    @Min(value = 0, message = "Giới tính phải là 0 hoặc 1")
    @Max(value = 1, message = "Giới tính phải là 0 hoặc 1")
    private Integer gioiTinh;

    @NotNull(message = "Ngày sinh không được để trống")
    private LocalDate ngaySinh;

    @Size(max = 255, message = "URL avatar không được vượt quá 255 ký tự")
    private String avatarUrl;
}
