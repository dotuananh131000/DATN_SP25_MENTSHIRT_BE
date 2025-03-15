package com.java.project.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

@Getter
@Setter
@Entity
@Table(name = "phuong_thuc_thanh_toan")
public class PhuongThucThanhToan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Size(max = 50)
    @Nationalized
    @Column(name = "ma_phuong_thuc", length = 50)
    private String maPhuongThuc;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ten_phuong_thuc")
    private String tenPhuongThuc;

    @Size(max = 255)
    @Nationalized
    @Column(name = "ghi_chu")
    private String ghiChu;

    @Column(name = "trang_thai", columnDefinition = "tinyint")
    private Short trangThai;

}