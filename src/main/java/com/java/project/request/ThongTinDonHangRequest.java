package com.java.project.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ThongTinDonHangRequest {
    private Integer id;
    private String hoTenNguoiNhan;
    private String sdt;
    private String email;
    private String diaChiNhanHang;
    private Double phiShip;
}
