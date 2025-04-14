package com.java.project.repositories;

import com.java.project.entities.DiaChiKhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DiaChiKhachHangRepository extends JpaRepository<DiaChiKhachHang, Integer> {
    @Query("SELECT dck FROM DiaChiKhachHang dck WHERE dck.khachHang.id = :khachHangId")
    List<DiaChiKhachHang> findByKhachHangId(Integer khachHangId);

    @Query("SELECT dckh FROM DiaChiKhachHang dckh WHERE dckh.khachHang.id = :khachHangId " +
            "AND dckh.trangThai = TRUE ")
    Optional<DiaChiKhachHang>getDiaChiMacDinh(Integer khachHangId);
}