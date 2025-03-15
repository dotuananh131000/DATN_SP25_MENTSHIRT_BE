package com.java.project.repositories;

import com.java.project.entities.DiaChiKhachHang;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiaChiKhachHangRepository extends JpaRepository<DiaChiKhachHang, Integer> {
    @Query("SELECT dck FROM DiaChiKhachHang dck WHERE dck.khachHang.id = :khachHangId")
    List<DiaChiKhachHang> findByKhachHangId(Integer khachHangId);
}