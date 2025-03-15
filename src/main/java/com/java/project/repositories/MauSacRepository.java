package com.java.project.repositories;

import com.java.project.entities.MauSac;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MauSacRepository extends JpaRepository<MauSac, Integer> {
    @Query("SELECT m FROM MauSac m WHERE m.tenMauSac LIKE %:search% ")
    Page<MauSac> findAllWithSearch(String search, Pageable pageable);

    Optional<MauSac> findByTenMauSac(String tenMauSac);

    Optional<MauSac> findByMaHex(String maHex);
}