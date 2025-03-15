package com.java.project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "nhan_vien")
public class NhanVien {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "vai_tro_id", referencedColumnName = "id")
    private VaiTro vaiTro;

    @Column(name = "ma_nhan_vien")
    private String maNhanVien;

    @Column(name = "ten_nhan_vien")
    private String tenNhanVien;

    @Column(name = "ten_dang_nhap")
    private String tenDangNhap;

    @Column(name = "mat_khau")
    private String mat_khau;

    @Column(name = "email")
    private String email;

    @Column(name = "so_dien_thoai")
    private String soDienThoai;

    @Column(name = "dia_chi")
    private String diaChi;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "gioi_tinh")
    private Integer gioiTinh;
}
