package com.java.project.repositories;

import com.java.project.entities.LichSuHoaDon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LichSuHoaDonRepository extends JpaRepository<LichSuHoaDon, Integer> {
    @Query("SELECT lshd FROM LichSuHoaDon lshd WHERE lshd.hoaDon.id = :idHD")
    List<LichSuHoaDon> getLichSuHoaDon(@Param("idHD") Integer idHD);
}