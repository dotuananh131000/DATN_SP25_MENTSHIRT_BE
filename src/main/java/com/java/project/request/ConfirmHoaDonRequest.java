package com.java.project.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfirmHoaDonRequest {
    String hoTenNguoiNhan;
    String soDienThoai;
    String email;
    String diaChiNhanHang;
    Double phiShip;

    @NotNull(message = " Tổng tiền hóa đơn không được để trống")
    Double tongTien;
}
