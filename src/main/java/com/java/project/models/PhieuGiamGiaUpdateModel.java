package com.java.project.models;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PhieuGiamGiaUpdateModel {
    @NotBlank(message = "Tên phiếu giảm giá không được để trống")
    @Size(max = 255, message = "Tên phiếu giảm giá không được quá 255 ký tự")
    private String tenPhieuGiamGia;

    private LocalDateTime thoiGianApDung;

    private LocalDateTime thoiGianHetHan;

    @NotNull(message = "Giá trị giảm không được để trống")
    @Positive(message = "Giá trị giảm phải là số dương")
    private Double giaTriGiam;

    @NotNull(message = "Số tiền tối thiểu hóa đơn không được để trống")
    @Positive(message = "Số tiền tối thiểu hóa đơn phải là số dương")
    private Double soTienToiThieuHd;

    @PositiveOrZero(message = "Số tiền giảm tối đa phải là số dương hoặc bằng 0")
    private Double soTienGiamToiDa;

    @NotNull(message = "Hình thức giảm giá không được để trống")
    @Min(value = 0, message = "Hình thức giảm giá  chỉ có thể là 0 hoặc 1")
    @Max(value = 1, message = "Hình thức giảm giá  chỉ có thể là 0 hoặc 1")
    private Integer hinhThucGiamGia;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    private Integer soLuong;
}
