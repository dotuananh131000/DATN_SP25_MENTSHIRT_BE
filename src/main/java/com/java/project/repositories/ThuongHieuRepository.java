package com.java.project.repositories;

import com.java.project.entities.ThuongHieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ThuongHieuRepository extends JpaRepository<ThuongHieu, Integer> {
    @Query("SELECT t FROM ThuongHieu t WHERE t.tenThuongHieu LIKE %:search%")
    Page<ThuongHieu> findAllWithSearch(String search, Pageable pageable);

    Optional<ThuongHieu> findByTenThuongHieu(String tenThuongHieu);
}