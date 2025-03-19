package com.java.project.repositories;

import com.java.project.dtos.SanPhamChiTietResponse;
import com.java.project.entities.SanPham;
import com.java.project.dtos.USanPhamResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
     SELECT new com.java.project.dtos.USanPhamResponse(
         sp.id,
         sp.maSanPham,
         sp.tenSanPham,
         SUM(spct.soLuong),
         CAST(COALESCE(SUM(hdct.soLuong), 0) AS long),
         MIN(spct.hinhAnh),
         MIN(spct.donGia),
         sp.moTa
     )
     FROM SanPham sp
     JOIN SanPhamChiTiet spct ON spct.sanPham.id = sp.id
     LEFT JOIN HoaDonChiTiet hdct ON hdct.sanPhamChiTiet.id = spct.id AND hdct.trangThai = 1
     WHERE sp.trangThai = true 
     AND spct.trangThai = true
     AND (:search IS NULL OR sp.tenSanPham LIKE %:search% OR sp.maSanPham LIKE %:search%)
     AND (:thuongHieuIds IS NULL OR spct.thuongHieu.id IN :thuongHieuIds)
     AND (:xuatXuIds IS NULL OR spct.xuatXu.id IN :xuatXuIds)
     AND (:chatLieuIds IS NULL OR spct.chatLieu.id IN :chatLieuIds)
     AND (:coAoIds IS NULL OR spct.coAo.id IN :coAoIds)
     AND (:tayAoIds IS NULL OR spct.tayAo.id IN :tayAoIds)
     AND (:mauSacIds IS NULL OR spct.mauSac.id IN :mauSacIds)
     AND (:kichThuocIds IS NULL OR spct.kichThuoc.id IN :kichThuocIds)
     AND (:minPrice IS NULL OR spct.donGia >= :minPrice)
     AND (:maxPrice IS NULL OR spct.donGia <= :maxPrice)
     GROUP BY sp.id, sp.maSanPham, sp.tenSanPham, sp.moTa
     ORDER BY 
         CASE WHEN :sortBy = 'id' AND :sortDir = 'asc' THEN sp.id END ASC,
         CASE WHEN :sortBy = 'id' AND :sortDir = 'desc' THEN sp.id END DESC,
         
         CASE WHEN :sortBy = 'tenSanPham' AND :sortDir = 'asc' THEN sp.tenSanPham END ASC,
         CASE WHEN :sortBy = 'tenSanPham' AND :sortDir = 'desc' THEN sp.tenSanPham END DESC,
         
         CASE WHEN :sortBy = 'soLuong' AND :sortDir = 'asc' THEN SUM(spct.soLuong) END ASC,
         CASE WHEN :sortBy = 'soLuong' AND :sortDir = 'desc' THEN SUM(spct.soLuong) END DESC,
         
         CASE WHEN :sortBy = 'gia' AND :sortDir = 'asc' THEN MIN(spct.donGia) END ASC,
         CASE WHEN :sortBy = 'gia' AND :sortDir = 'desc' THEN MIN(spct.donGia) END DESC,
         
         CASE WHEN :sortBy = 'soLuongDaBan' AND :sortDir = 'asc' THEN CAST(COALESCE(SUM(hdct.soLuong), 0) AS long) END ASC,
         CASE WHEN :sortBy = 'soLuongDaBan' AND :sortDir = 'desc' THEN CAST(COALESCE(SUM(hdct.soLuong), 0) AS long) END DESC
 """)
    Page<USanPhamResponse> findAllSanPhamWithFilter(
            @Param("search") String search,
            @Param("thuongHieuIds") List<Integer> thuongHieuIds,
            @Param("xuatXuIds") List<Integer> xuatXuIds,
            @Param("chatLieuIds") List<Integer> chatLieuIds,
            @Param("coAoIds") List<Integer> coAoIds,
            @Param("tayAoIds") List<Integer> tayAoIds,
            @Param("mauSacIds") List<Integer> mauSacIds,
            @Param("kichThuocIds") List<Integer> kichThuocIds,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            @Param("sortBy") String sortBy,
            @Param("sortDir") String sortDir,
            Pageable pageable
    );

}