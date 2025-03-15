package com.java.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginResponse {
    private String token;
    private String maNhanVien;
    private String vaiTro;
}
