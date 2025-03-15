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
@Table(name = "mau_sac")
public class MauSac {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 7)
    @NotNull
    @Nationalized
    @Column(name = "ma_hex", nullable = false, length = 7)
    private String maHex;

    @Size(max = 255)
    @NotNull
    @Nationalized
    @Column(name = "ten_mau_sac", nullable = false)
    private String tenMauSac;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "trang_thai", nullable = false)
    private Boolean trangThai = false;

    @NotNull
    @ColumnDefault("getdate()")
    @Column(name = "ngay_tao", nullable = false)
    private Instant ngayTao;
}