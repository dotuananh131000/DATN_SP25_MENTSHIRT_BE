package com.java.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HoaDonPhuongThucThanhToanResponse {
    private Integer hoaDonPhuongThucThanhToan_id;
    private String maGiaoDich;
    private String maHoaDon;
    private String tenPhuongThuc;
    private LocalDate ngayThucHienThanhToan;
    private BigDecimal soTienThanhToan;
    private String ghiChu;
    private String nguoiXacNhan;
}
