// Entity class for HoaDonChiTiet
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
@Table(name = "hoa_don_chi_tiet")
public class HoaDonChiTiet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "hoa_don_id", referencedColumnName = "id")
    private HoaDon hoaDon;

    @ManyToOne
    @JoinColumn(name = "san_pham_chi_tiet_id", referencedColumnName = "id")
    private SanPhamChiTiet sanPhamChiTiet;

    @Column(name = "so_luong")
    private Integer soLuong;

    @Column(name = "thanh_tien")
    private Double thanhTien;

    @Column(name = "trang_thai")
    private Integer trangThai;
}
