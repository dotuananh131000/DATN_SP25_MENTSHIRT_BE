package com.java.project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "phieu_giam_gia")
public class PhieuGiamGia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ma_phieu_giam_gia", length = 50)
    private String maPhieuGiamGia;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ten_phieu_giam_gia")
    private String tenPhieuGiamGia;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private LocalDateTime ngayTao;

    @Column(name = "ngay_sua")
    private LocalDateTime ngaySua;

    @Column(name = "thoi_gian_ap_dung")
    private LocalDateTime thoiGianApDung;

    @Column(name = "thoi_gian_het_han")
    private LocalDateTime thoiGianHetHan;

    @Column(name = "gia_tri_giam")
    private Double giaTriGiam;

    @Column(name = "so_tien_toi_thieu_hd")
    private Double soTienToiThieuHd;

    @Column(name = "so_tien_giam_toi_da")
    private Double soTienGiamToiDa;

    @Column(name = "loai_giam")
    private Integer loaiGiam;

    @Column(name = "trang_thai")
    private Integer trangThai;

    @Column(name = "so_luong")
    private Integer soLuong;

    @ColumnDefault("0")
    @Column(name = "hinh_thuc_giam_gia")
    private Integer hinhThucGiamGia;

}