package com.java.project.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.java.project.entities.SanPham}
 */
@Data
@Builder
public class SanPhamDto implements Serializable {
    Integer id;
    String tenSanPham;
    Integer soLuong;
    String maSanPham;
    String moTa;
    Boolean trangThai;
    Instant ngayTao;
}