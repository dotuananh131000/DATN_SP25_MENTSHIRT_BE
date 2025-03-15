package com.java.project.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonHomNayResponse {
    private Integer id;
    private Integer idKhachHang;
    private Integer idPhieuGiamGia;
    private String maHoaDon;
    private LocalDateTime ngayTao;
    private String tenKhachHang;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate ngaySinh;
    private String email;
    private String soDienThoai;
    private String maPhieuGiamGia;
    private Integer loaiGiam;
    private Integer hinhThucPhieuGiamGia;
    private Double soTienGiamToiDa;
    private Double soTienToiThieu;
    private Double giaTriGiam;
    private Double tongTien;
    private Integer loaiDon;

}
