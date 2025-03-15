package com.java.project.models;

import com.java.project.entities.KhachHang;
import com.java.project.entities.PhieuGiamGia;

import java.util.Date;

public class PhieuGiamGiaKhachHangRequest {
    private Integer id;
    private Byte trangThai;
    private Date ngayTao;
    private Date ngaySua;
    private Integer soLuong;
    private KhachHang khachHang;
    private PhieuGiamGia phieuGiamGia;

}
