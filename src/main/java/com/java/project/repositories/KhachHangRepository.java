package com.java.project.repositories;

import com.java.project.entities.KhachHang;
import com.java.project.suportRespone.KhachHangReponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface KhachHangRepository extends JpaRepository<KhachHang, Integer> {
    boolean existsByTenDangNhap(String tenDangNhap);

    boolean existsByEmail(String email);

    boolean existsBySoDienThoai(String soDienThoai);

    Optional<KhachHang> findByEmail(String email);

    Optional<KhachHang> findBySoDienThoai(String soDienThoai);

    @Query("SELECT kh FROM KhachHang kh WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR kh.maKhachHang LIKE %:keyword% OR " +
            "kh.tenDangNhap LIKE %:keyword% OR kh.tenKhachHang LIKE %:keyword% OR " +
            "kh.email LIKE %:keyword% OR kh.soDienThoai LIKE %:keyword%) " +
            "AND (:trangThai IS NULL OR kh.trangThai = :trangThai)")
    Page<KhachHang> findAllWithFilters(@org.springframework.lang.Nullable String keyword,
                                       @org.springframework.lang.Nullable Integer trangThai,
                                       Pageable pageable);

    @Query("SELECT pgkh.idKhachHang FROM PhieuGiamGiaKhachHang pgkh WHERE pgkh.idVoucher.id = :voucherId " +
            "AND (:search IS NULL OR :search = '' OR pgkh.idKhachHang.tenDangNhap LIKE %:search% " +
            "OR pgkh.idKhachHang.email LIKE %:search% " +
            "OR pgkh.idKhachHang.tenKhachHang LIKE %:search%)")
    Page<KhachHang> findInPhieuGiamGiaKhachHangByVoucherId(@Param("voucherId") Integer voucherId,
                                                           @Param("search") String search,
                                                           Pageable pageable);

    @Query("SELECT pgkh.idKhachHang FROM PhieuGiamGiaKhachHang pgkh where pgkh.idVoucher.id = :voucherId")
    List<KhachHang> findInPhieuGiamGiaKhachHangByVoucherId(Integer voucherId);


    @Query("SELECT kh.id FROM KhachHang kh")
    List<Integer> getAllId();

    @Query("""
            select new com.java.project.suportRespone.KhachHangReponse(
            kh.id,
            kh.tenKhachHang,
            kh.avatarUrl,
            kh.ngaySinh,
            kh.gioiTinh,
            kh.soDienThoai,
            kh.email
            )from KhachHang kh 
            where kh.trangThai =1
            """)
    List<KhachHangReponse> getAllKHByHD();
}