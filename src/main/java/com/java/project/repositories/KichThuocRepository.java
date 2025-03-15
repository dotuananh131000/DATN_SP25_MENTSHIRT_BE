package com.java.project.repositories;

import com.java.project.entities.KichThuoc;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface KichThuocRepository extends JpaRepository<KichThuoc, Integer> {
    @Query("SELECT k FROM KichThuoc k WHERE k.tenKichThuoc LIKE %:search%")
    Page<KichThuoc> findAllWithSearch(String search, Pageable pageable);

    Optional<KichThuoc> findByTenKichThuoc(String tenKichThuoc);
}