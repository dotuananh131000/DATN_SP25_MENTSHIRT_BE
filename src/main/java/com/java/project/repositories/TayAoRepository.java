package com.java.project.repositories;

import com.java.project.entities.TayAo;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TayAoRepository extends JpaRepository<TayAo, Integer> {

    @Query("SELECT t FROM TayAo t WHERE t.tenTayAo LIKE %:search%")
    Page<TayAo> findAllWithSearch(String search, Pageable pageable);

    Optional<TayAo> findByTenTayAo(String tenTayAo);
}