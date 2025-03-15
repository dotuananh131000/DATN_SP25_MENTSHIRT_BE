package com.java.project.repositories;

import com.java.project.dtos.SanPhamChiTietResponse;
import com.java.project.entities.SanPham;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SanPhamRepository extends JpaRepository<SanPham, Integer> {
    @Query("select coalesce(sum(spct.soLuong), 0) from SanPhamChiTiet spct where spct.sanPham.id =:id")
    Integer countSLByID(Integer id);

    @Query("SELECT sp FROM SanPham sp WHERE LOWER(sp.tenSanPham) LIKE LOWER(CONCAT('%', :search, '%')) OR LOWER(sp.maSanPham) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<SanPham> searchByNameOrCode(String search, Pageable pageable);

    @Query("SELECT sp FROM SanPham sp WHERE LOWER(sp.tenSanPham) = LOWER(:tenSanPham)")
    Optional<SanPham> findByTenSanPham(String tenSanPham);

    @Query("SELECT sp FROM SanPham sp WHERE LOWER(sp.maSanPham) = LOWER(:maSanPham)")
    Optional<SanPham> findByMaSanPham(String maSanPham);


}