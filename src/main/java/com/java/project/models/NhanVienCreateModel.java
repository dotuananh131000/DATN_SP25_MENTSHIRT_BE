    package com.java.project.models;

    import lombok.AllArgsConstructor;
    import lombok.Data;
    import lombok.NoArgsConstructor;

    import jakarta.validation.constraints.*;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class NhanVienCreateModel {

        @Size(max = 20, message = "Mã nhân viên không được vượt quá 20 ký tự")
        private String maNhanVien;

        @NotBlank(message = "Tên nhân viên không được để trống")
        @Size(max = 50, message = "Tên nhân viên không được vượt quá 50 ký tự")
        private String tenNhanVien;

        @NotBlank(message = "Tên đăng nhập không được để trống")
        @Size(min = 5, max = 30, message = "Tên đăng nhập phải có độ dài từ 5 đến 30 ký tự")
        private String tenDangNhap;

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

