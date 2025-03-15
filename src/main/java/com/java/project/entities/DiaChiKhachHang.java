package com.java.project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "dia_chi_khach_hang")
public class DiaChiKhachHang {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "khach_hang_id", nullable = false)
    private KhachHang khachHang;

    @NotNull
    @Column(name = "tinh_thanh_id", nullable = false)
    private Integer tinhThanhId;

    @NotNull
    @Column(name = "quan_huyen_id", nullable = false)
    private Integer quanHuyenId;

    @NotNull
    @Column(name = "phuong_xa_id", nullable = false)
    private Integer phuongXaId;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "tinh_thanh", nullable = false)
    private String tinhThanh;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "quan_huyen", nullable = false)
    private String quanHuyen;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "phuong_xa", nullable = false)
    private String phuongXa;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "dia_chi_chi_tiet", nullable = false)
    private String diaChiChiTiet;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "trang_thai", nullable = false)
    private Boolean trangThai = false;

}