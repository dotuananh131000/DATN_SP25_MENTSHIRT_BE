package com.java.project.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonPhuongThucThanhToanRequest {
    private Integer id;
    private Integer hoaDonId;
    private Integer phuongThucThanhToanId;
    private BigDecimal soTienThanhToan;
    private LocalDate ngayThanhToan;
    private String ghiChu;
    private String nguoiXacNhan;
}
