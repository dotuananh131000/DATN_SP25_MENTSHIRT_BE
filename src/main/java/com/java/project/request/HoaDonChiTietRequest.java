package com.java.project.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class HoaDonChiTietRequest {
    private Integer id;
    private Integer idHoaDon;
    private Integer idSPCT;
    private Integer soLuong;
    private Double thanhTien;
}
