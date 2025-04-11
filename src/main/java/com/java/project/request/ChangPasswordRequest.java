package com.java.project.request;

import jakarta.validation.constraints.NotEmpty;
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
public class ChangPasswordRequest {
    String oldPassword;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[@#$%^&+=!])[A-Za-z0-9@#$%^&+=!]{8,}$",
            message = "Mật khẩu ít nhất là 8 kí tự, phải có chữ viết hoa và kí tự đặc biệt.")
    String newPassword;
}
