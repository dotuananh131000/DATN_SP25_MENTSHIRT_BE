package com.java.project.repositories;

import com.java.project.dtos.PhieuGiamGiaKhachHangResponse;
import com.java.project.entities.PhieuGiamGiaKhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhieuGiamGiaKhachHangRepository extends JpaRepository<PhieuGiamGiaKhachHang, Integer> {
    @Query("SELECT p FROM PhieuGiamGiaKhachHang p WHERE p.idKhachHang.id = :khachHangId")
    List<PhieuGiamGiaKhachHang> findByKhachHangId(Integer khachHangId);

    @Query("SELECT p FROM PhieuGiamGiaKhachHang p WHERE p.idVoucher.id = :phieuGiamGiaId")
    List<PhieuGiamGiaKhachHang> findByVoucherId(Integer phieuGiamGiaId);

   @Query("""
           select new com.java.project.dtos.PhieuGiamGiaKhachHangResponse (
           pggkh.id,
           pggkh.idKhachHang.id,
           pgg.id,
           pgg.maPhieuGiamGia,
           pgg.tenPhieuGiamGia,
           pgg.giaTriGiam,
           pgg.soTienToiThieuHd,
           pgg.soTienGiamToiDa,
           pgg.loaiGiam,
           pgg.hinhThucGiamGia,
           pgg.thoiGianHetHan,
           pgg.trangThai
           )from PhieuGiamGiaKhachHang pggkh left join PhieuGiamGia pgg 
           on pggkh.idVoucher.id = pgg.id
           where pgg.trangThai = 1 and pgg.loaiGiam = 1 and pggkh.idKhachHang.id = :idKH
           and pgg.thoiGianApDung <= current_timestamp 
           and pgg.thoiGianHetHan >= current_timestamp 
           and pgg.soLuong >0
           """)
    List<PhieuGiamGiaKhachHangResponse>phieuGiamGiaKhachHang(@Param("idKH") Integer idKH);
}