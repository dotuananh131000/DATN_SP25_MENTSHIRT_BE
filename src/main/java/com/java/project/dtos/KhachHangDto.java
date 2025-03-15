package com.java.project.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * DTO for {@link com.java.project.entities.KhachHang}
 */
@Data
@Builder
public class KhachHangDto implements Serializable {
    Integer id;
    String maKhachHang;
    String tenKhachHang;
    String tenDangNhap;
    String email;
    String soDienThoai;
    Integer gioiTinh;
    LocalDate ngaySinh;
    String avatarUrl;
    Integer trangThai;
}