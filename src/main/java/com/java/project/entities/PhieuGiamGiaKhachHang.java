package com.java.project.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "phieu_giam_gia_khach_hang")
public class PhieuGiamGiaKhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_khach_hang")
    private KhachHang idKhachHang;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_voucher")
    private PhieuGiamGia idVoucher;

    @Column(name = "trang_thai", columnDefinition = "tinyint")
    private Short trangThai;

    @ColumnDefault("CONVERT([date],getdate())")
    @Column(name = "ngay_tao")
    private LocalDate ngayTao;

    @Column(name = "ngay_sua")
    private LocalDate ngaySua;
}