package com.java.project.repositories;

import com.java.project.entities.CoAo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CoAoRepository extends JpaRepository<CoAo, Integer> {

    @Query("SELECT c FROM CoAo c WHERE c.tenCoAo LIKE %:search%")
    Page<CoAo> findAllWithSearch(String search, Pageable pageable);

    Optional<CoAo> findByTenCoAo(String tenCoAo);
}
