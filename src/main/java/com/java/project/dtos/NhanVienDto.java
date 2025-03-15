package com.java.project.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.java.project.entities.NhanVien}
 */
@Builder
@Data
public class NhanVienDto implements Serializable {
    Integer id;
    VaiTroDto vaiTro;
    String maNhanVien;
    String tenNhanVien;
    String tenDangNhap;
    String email;
    String soDienThoai;
    String diaChi;
    String avatarUrl;
    Integer gioiTinh;
    Integer trangThai;
}