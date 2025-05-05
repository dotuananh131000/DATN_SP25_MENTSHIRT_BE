package com.java.project.repositories;

import com.java.project.entities.PhieuGiamGia;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PhieuGiamGiaRepository extends JpaRepository<PhieuGiamGia, Integer> {
    @Query("SELECT p FROM PhieuGiamGia p WHERE " +
            "(p.maPhieuGiamGia LIKE %:keyword% OR :keyword IS NULL) AND " +
            "(p.tenPhieuGiamGia LIKE %:keyword% OR :keyword IS NULL) AND " +
            "(p.thoiGianApDung BETWEEN :startTime AND :endTime OR (:startTime IS NULL AND :endTime IS NULL)) AND " +
            "(p.thoiGianHetHan BETWEEN :startTime AND :endTime OR (:startTime IS NULL AND :endTime IS NULL)) AND " +
            "(p.loaiGiam = :loaiGiam OR :loaiGiam IS NULL)")
    Page<PhieuGiamGia> findAllByCriteria(
            @Param("keyword") String keyword,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("loaiGiam") Integer loaiGiam,
            Pageable pageable);

    boolean existsByMaPhieuGiamGia(String maPhieuGiamGia);

    boolean existsByMaPhieuGiamGiaAndIdNot(String maPhieuGiamGia, Integer id);

    boolean existsByTenPhieuGiamGia(String tenPhieuGiamGia);

    PhieuGiamGia getByMaPhieuGiamGia(String maPhieuGiamGia);

    PhieuGiamGia getByTenPhieuGiamGia(String tenPhieuGiamGia);

    PhieuGiamGia getByMaPhieuGiamGiaAndIdNot(String maPhieuGiamGia, Integer id);

    PhieuGiamGia getByTenPhieuGiamGiaAndIdNot(String tenPhieuGiamGia, Integer id);

    @Query("SELECT p FROM PhieuGiamGia p WHERE " +
            "(p.trangThai =1 and p.loaiGiam = 0 and p.soLuong >0)" +
            "and p.thoiGianApDung <= current_timestamp " +
            "and p.thoiGianHetHan >= current_timestamp ")
    List<PhieuGiamGia>getAll();

    @Query("SELECT pgg " +
            "FROM PhieuGiamGia pgg " +
            "LEFT JOIN PhieuGiamGiaKhachHang pggkh " +
            "ON pgg.id = pggkh.idVoucher.id AND (pggkh.idKhachHang.id = :idKH OR pggkh.idKhachHang IS NULL) " +
            "WHERE (pgg.loaiGiam = 0 OR pggkh.idKhachHang IS NOT NULL)" +
            "AND pgg.thoiGianApDung <= current_timestamp " +
            "AND pgg.thoiGianHetHan >= current_timestamp ")
    List<PhieuGiamGia>findPhieuGiamGiaByKhachHang(@Param("idKH") Integer idKH);
}