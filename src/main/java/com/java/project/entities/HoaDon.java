package com.java.project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "hoa_don")
public class HoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_khach_hang", referencedColumnName = "id")
    private KhachHang khachHang;

    @ManyToOne
    @JoinColumn(name = "id_nhan_vien", referencedColumnName = "id")
    private NhanVien nhanVien;

    @ManyToOne
    @JoinColumn(name = "id_phieu_giam_gia", referencedColumnName = "id")
    private PhieuGiamGia phieuGiamGia;

    @Column(name = "ma_hoa_don")
    private String maHoaDon;

    @Column(name = "loai_don")
    private Integer loaiDon;

    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "ho_ten_nguoi_nhan")
    private String hoTenNguoiNhan;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "email")
    private String email;

    @Column(name = "dia_chi_nhan_hang")
    private String diaChiNhanHang;

    @Column(name = "ngay_nhan_mong_muon")
    private LocalDate ngayNhanMongMuon;

    @Column(name = "ngay_du_kien_nhan")
    private LocalDate ngayDuKienNhan;

    @Column(name = "trang_thai_giao_hang")
    private Integer trangThaiGiaoHang;

    @Column(name = "phi_ship")
    private Double phiShip;

    @Column(name = "tong_tien")
    private Double tongTien;

    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "trang_thai")
    private Integer trangThai;
}
