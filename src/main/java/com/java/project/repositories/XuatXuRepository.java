package com.java.project.repositories;

import com.java.project.entities.XuatXu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface XuatXuRepository extends JpaRepository<XuatXu, Integer> {
    @Query("SELECT x FROM XuatXu x WHERE x.tenXuatXu LIKE %:search%")
    Page<XuatXu> findAllWithSearch(String search, Pageable pageable);

    Optional<XuatXu> findByTenXuatXu(String tenXuatXu);
}