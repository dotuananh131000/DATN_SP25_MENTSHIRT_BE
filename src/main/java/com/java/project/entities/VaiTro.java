package com.java.project.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter

@Entity
@Table( name = "vai_tro")
public class VaiTro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ma_vai_tro")
    private String maVaiTro;

    @Column(name = "ten_vai_tro")
    private String tenVaiTro;

    @Column(name = "trang_thai")
    private Integer trangThai;

}
