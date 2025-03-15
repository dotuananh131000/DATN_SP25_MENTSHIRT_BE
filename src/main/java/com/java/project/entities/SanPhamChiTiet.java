package com.java.project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "san_pham_chi_tiet")
public class SanPhamChiTiet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "san_pham_id", nullable = false)
    private SanPham sanPham;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "thuong_hieu_id", nullable = false)
    private ThuongHieu thuongHieu;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "xuat_xu_id", nullable = false)
    private XuatXu xuatXu;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_lieu_id", nullable = false)
    private ChatLieu chatLieu;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "co_ao_id", nullable = false)
    private CoAo coAo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tay_ao_id", nullable = false)
    private TayAo tayAo;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "mau_sac_id", nullable = false)
    private MauSac mauSac;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "kich_thuoc_id", nullable = false)
    private KichThuoc kichThuoc;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "so_luong", nullable = false)
    private Integer soLuong;

    @NotNull
    @Column(name = "don_gia", nullable = false, precision = 18, scale = 2)
    private BigDecimal donGia;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "hinh_anh", nullable = false)
    private String hinhAnh;

    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao")
    private Instant ngayTao;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "trang_thai", nullable = false)
    private Boolean trangThai = false;

}