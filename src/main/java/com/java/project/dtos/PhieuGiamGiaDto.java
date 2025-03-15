package com.java.project.dtos;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.LocalDateTime;

/**
 * DTO for {@link com.java.project.entities.PhieuGiamGia}
 */
@Data
@Builder
public class PhieuGiamGiaDto implements Serializable {
    Integer id;
    String maPhieuGiamGia;
    String tenPhieuGiamGia;
    LocalDateTime ngayTao;
    LocalDateTime ngaySua;
    LocalDateTime thoiGianApDung;
    LocalDateTime thoiGianHetHan;
    Double giaTriGiam;
    Double soTienToiThieuHd;
    Double soTienGiamToiDa;
    Integer loaiGiam;
    Integer trangThai;
    Integer soLuong;
    Integer hinhThucGiamGia;
}