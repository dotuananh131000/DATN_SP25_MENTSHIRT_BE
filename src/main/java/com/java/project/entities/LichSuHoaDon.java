package com.java.project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Nationalized;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "lich_su_hoa_don")
public class LichSuHoaDon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_hoa_don")
    private HoaDon idHoaDon;

    @Size(max = 255)
    @Nationalized
    @Column(name = "hanh_dong")
    private String hanhDong;

    @Size(max = 255)
    @Nationalized
    @Column(name = "nguoi_thay_doi")
    private String nguoiThayDoi;

    @ColumnDefault("CONVERT([date],getdate())")
    @Column(name = "thoi_gian_thay_doi")
    private LocalDate thoiGianThayDoi;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ghi_chu")
    private String ghiChu;

}