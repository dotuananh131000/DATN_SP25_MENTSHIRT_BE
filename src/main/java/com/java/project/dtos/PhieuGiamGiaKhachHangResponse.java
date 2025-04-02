package com.java.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PhieuGiamGiaKhachHangResponse {
    private Integer id;
    private Integer idKH;
    private Integer idPGG;
    private String maPhieuGiamGia;
    private String tenPhieuGiamGia;
    private Double giaTriGiam;
    private Double soTienToiThieu;
    private Double soTienGiamToiDa;
    private Integer loaiGiam;
    private Integer hinhThucGiamGia;
    private LocalDateTime ngayKetThuc;
    private Integer trangThai;
}
