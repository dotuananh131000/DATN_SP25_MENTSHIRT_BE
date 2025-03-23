package com.java.project.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true)
@RequiredArgsConstructor
@Builder
public class HoaDonModel {
    String ghiChu;
    Integer idKhachHang;
    Integer idPhieuGiamGia;

    @NotBlank(message = "Họ tên người nhận không được để trống")
    @Size(max = 255, message = "Họ tên không được vượt quá 255 ký tự")
    String hoTenNguoiNhan;

    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^0\\d{9,10}$", message = "Số điện thoại không hợp lệ")
    String soDienThoai;

    @Email(message = "Email không hợp lệ")
    String email;

    @NotBlank(message = "Địa chỉ nhận hàng không được để trống")
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    String diaChiNhanHang;

    Integer phuongThucThanhToan;

    private Double phiShip;

    @NotNull(message = "Danh sách chi tiết đơn hàng không được để trống")
    @Size(min = 1, message = "Phải có ít nhất một sản phẩm trong đơn hàng")
    private List<@Valid HoaDonChiTietModel> danhSachChiTiet;
}
