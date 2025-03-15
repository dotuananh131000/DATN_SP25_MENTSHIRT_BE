package com.java.project.repositories;

import com.java.project.entities.NhanVien;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NhanVienRepository extends JpaRepository<NhanVien, Integer> {

    @Query("SELECT nv FROM NhanVien nv WHERE " +
            "(:keyword IS NULL OR nv.maNhanVien LIKE CONCAT('%', :keyword, '%') " +
            "OR nv.tenDangNhap LIKE CONCAT('%', :keyword, '%') " +
            "OR nv.tenNhanVien LIKE CONCAT('%', :keyword, '%') " +
            "OR nv.email LIKE CONCAT('%', :keyword, '%') " +
            "OR nv.soDienThoai LIKE CONCAT('%', :keyword, '%')) " +
            "AND (:trangThai IS NULL OR nv.trangThai = :trangThai)")
    Page<NhanVien> findAllWithFilters(@Param("keyword") String keyword,
                                      @Param("trangThai") Integer trangThai,
                                      Pageable pageable);


    Optional<NhanVien> findByTenDangNhap(String tenDangNhap);

    boolean existsByTenDangNhap(String tenDangNhap);

    boolean existsByEmail(String email);

    boolean existsBySoDienThoai(String soDienThoai);

    Optional<NhanVien> findByEmail(String email);

    Optional<NhanVien> findBySoDienThoai(String soDienThoai);
}