package com.java.project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "san_pham")
public class SanPham {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ten_san_pham", nullable = false)
    private String tenSanPham;

    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "ma_san_pham", nullable = false, length = 50)
    private String maSanPham;

    @Nationalized
    @Lob
    @Column(name = "mo_ta")
    private String moTa;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "trang_thai", nullable = false)
    private Boolean trangThai = false;

    @NotNull
    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao", nullable = false)
    private Instant ngayTao;
}