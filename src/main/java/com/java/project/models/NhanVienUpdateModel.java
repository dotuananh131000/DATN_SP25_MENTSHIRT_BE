package com.java.project.models;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NhanVienUpdateModel {
    @NotBlank(message = "Tên nhân viên không được để trống")
    @Size(max = 50, message = "Tên nhân viên không được vượt quá 50 ký tự")
    private String tenNhanVien;

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    private String email;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0[0-9]{9,10})$", message = "Số điện thoại không hợp lệ")
    private String soDienThoai;

    @NotBlank(message = "Địa chỉ không được để trống")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    private String diaChi;

    @NotNull(message = "Giới tính không được để trống")
    @Min(value = 0, message = "Giới tính phải là 0 hoặc 1")
    @Max(value = 1, message = "Giới tính phải là 0 hoặc 1")
    private Integer gioiTinh;

    @NotNull(message = "Vai trò không được để trống")
    @Positive(message = "Vai trò phải là số dương")
    private Integer vaiTro;

    @Size(max = 255, message = "URL avatar không được vượt quá 255 ký tự")
    private String avatarUrl;
}

