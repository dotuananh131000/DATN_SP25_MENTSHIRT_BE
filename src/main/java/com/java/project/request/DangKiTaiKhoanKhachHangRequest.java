package com.java.project.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DangKiTaiKhoanKhachHangRequest {
    @NotBlank(message = "Tên khách hàng không được để trống")
    String tenKhachHang;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0[0-9]{9,10})$", message = "Số điện thoại không hợp lệ (phải có 10 hoặc 11 chữ số và bắt đầu bằng số 0)")
    String soDienThoai;

    @Email(message = "Email không hợp lệ")
    String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=.*[a-zA-Z]).{8,}$", message = "Mật khẩu phải có 8 kí tự, và ít nhất 1 chữ hoa")
    String password;

    String confirmPassword;
}
