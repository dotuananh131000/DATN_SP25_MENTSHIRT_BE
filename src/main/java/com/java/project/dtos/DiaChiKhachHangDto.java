package com.java.project.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.java.project.entities.DiaChiKhachHang}
 */
@Data
@Builder
public class DiaChiKhachHangDto implements Serializable {
    Integer id;
    KhachHangDto khachHang;
    Integer tinhThanhId;
    String tinhThanh;
    Integer quanHuyenId;
    String quanHuyen;
    Integer phuongXaId;
    String phuongXa;
    String diaChiChiTiet;
    Instant ngayTao;
    Boolean trangThai;
}