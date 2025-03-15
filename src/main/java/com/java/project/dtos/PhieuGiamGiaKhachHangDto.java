package com.java.project.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@Builder
public class PhieuGiamGiaKhachHangDto implements Serializable {
    Integer id;
    KhachHangDto idKhachHang;
    PhieuGiamGiaDto idVoucher;
    Short trangThai;
    LocalDate ngayTao;
    LocalDate ngaySua;
}