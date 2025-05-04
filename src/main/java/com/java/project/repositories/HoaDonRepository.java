package com.java.project.repositories;

import com.java.project.dtos.HoaDonBanHangResponse;
import com.java.project.dtos.HoaDonHomNayResponse;
import com.java.project.dtos.HoaDonResponse;
import com.java.project.dtos.TopSellingResponse;
import com.java.project.entities.HoaDon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface HoaDonRepository extends JpaRepository<HoaDon, Integer> {

    @Query("SELECT hd FROM  HoaDon hd " +
            "LEFT JOIN KhachHang kh ON hd.khachHang.id = kh.id " +
            "LEFT JOIN NhanVien nv ON hd.nhanVien.id = nv.id " +
            "LEFT JOIN PhieuGiamGia pgg ON hd.phieuGiamGia.id = pgg.id " +
            "WHERE (:ngayBatDau IS NULL OR CAST(hd.ngayTao AS DATE) >= :ngayBatDau) " +
            "AND (:ngayKetThuc IS NULL OR CAST(hd.ngayTao AS DATE) <= :ngayKetThuc) " +
            "AND (:keyword IS NULL OR LOWER(hd.maHoaDon) LIKE LOWER(CONCAT('%', :keyword , '%' ))) " +
            "AND (:trangThaiGiaoHang IS NULL OR hd.trangThaiGiaoHang = :trangThaiGiaoHang) " +
            "AND (:loaiDon IS NULL OR hd.loaiDon = :loaiDon) " +
            "ORDER BY hd.ngayTao ASC ")
    Page<HoaDon>getListHoaDon(Pageable pageable,
                          @Param("ngayBatDau") LocalDate ngayBatDau,
                          @Param("ngayKetThuc") LocalDate ngayKetThuc,
                          @Param("keyword") String keyword,
                          @Param("loaiDon") Integer loaiDon,
                          @Param("trangThaiGiaoHang") Integer trangThaiGiaoHang);

    @Query("SELECT hd FROM  HoaDon hd " +
            "LEFT JOIN KhachHang kh ON hd.khachHang.id = kh.id " +
            "LEFT JOIN NhanVien nv ON hd.nhanVien.id = nv.id " +
            "LEFT JOIN PhieuGiamGia pgg ON hd.phieuGiamGia.id = pgg.id " +
            "WHERE hd.khachHang.id = :id " +
            "AND (:keyword IS NULL OR LOWER(hd.maHoaDon) LIKE LOWER(CONCAT('%', :keyword , '%' ))) " +
            "AND (:trangThaiGiaoHang IS NULL OR hd.trangThaiGiaoHang = :trangThaiGiaoHang ) " +
            "AND hd.loaiDon = 0 " +
            "ORDER BY hd.ngayTao DESC ")
    Page<HoaDon>getListHoaDonByIdKH(Pageable pageable, @Param("id") Integer id,
                                    @Param("keyword") String keyword,
                                    @Param("trangThaiGiaoHang") Integer trangThaiGiaoHang);


    @Query("""
                SELECT hd.trangThaiGiaoHang, COUNT(hd) 
                FROM HoaDon hd 
                WHERE
                (:ngayBatDau IS NULL OR CAST(hd.ngayTao AS DATE ) >=  :ngayBatDau)
                and (:ngayKetThuc IS NULL OR CAST(hd.ngayTao AS DATE ) <=  :ngayKetThuc)
                AND (:loaiDon IS NULL OR hd.loaiDon = :loaiDon)
                GROUP BY hd.trangThaiGiaoHang
            """)
    List<Object[]> countOrdersByStatus(
            @Param("ngayBatDau") LocalDate ngayBatDau,
            @Param("ngayKetThuc") LocalDate ngayKetThuc,
            @Param("loaiDon") Integer loaiDon
    );

    @Query("""
            select new com.java.project.dtos.HoaDonHomNayResponse(
            hd.id,
            kh.id,
            kh.soDienThoai,
            pgg.id,
            hd.maHoaDon,
            hd.ngayTao,
            kh.tenKhachHang,
            kh.ngaySinh,
            kh.email,
            kh.soDienThoai,
            pgg.maPhieuGiamGia,
            pgg.loaiGiam,
            pgg.hinhThucGiamGia,
            pgg.soTienGiamToiDa,
            pgg.soTienToiThieuHd,
            pgg.giaTriGiam,
            hd.tongTien,
            hd.loaiDon
            )from HoaDon hd left join KhachHang kh on hd.khachHang.id = kh.id
            left join PhieuGiamGia pgg on hd.phieuGiamGia.id = pgg.id
            left join NhanVien nv on hd.nhanVien.id = nv.id
            where cast(hd.ngayTao as date ) = cast(current_date as date ) 
            and hd.trangThaiGiaoHang =8
            """)
    List<HoaDonHomNayResponse> getHoaDonHomNay();

    @Query("""
               SELECT hd FROM HoaDon hd WHERE hd.maHoaDon = :maHoaDon
            """)
    Optional<HoaDon> getHoaDonByMaHoaDon(@Param("maHoaDon") String maHoaDon);

    //Tổng doanh thu đơn hàng trong ngày hôm nay
    @Query("select SUM(hd.tongTien) from HoaDon hd where cast(hd.ngayTao as date ) = cast(current_date as date ) " +
            "and hd.trangThai = 1")
    Double getTongDoanhThuHomNay();

    //Tổng số lượng bán ra trong ngày hôm nay
    @Query("select SUM(hdct.soLuong) from HoaDonChiTiet hdct join HoaDon hd " +
            "on hdct.hoaDon.id = hd.id where cast(hd.ngayTao as date ) = cast(current_date as date) ")
    Integer getSoluongBanRaHomNay();

    //Số lượng hóa đơn
    @Query("select COUNT(hd) from HoaDon hd where cast(hd.ngayTao as date ) = cast(current_date as date) ")
    Integer soLuongHoaDonHomNay();

    //Tổng doanh thu đơn hàng trong ...
    @Query("select SUM(hd.tongTien) from HoaDon hd where " +
            "cast(hd.ngayTao as date ) >= :startDate and cast(hd.ngayTao as date ) <= :endDate " +
            "and hd.trangThai = 1")
    Double getTongDoanhThuTrong(@Param("startDate") LocalDate startDate,
                                @Param("endDate") LocalDate endDate);

    //Tổng số lượng bán ra trong ...
    @Query("select SUM(hdct.soLuong) from HoaDonChiTiet hdct join HoaDon hd " +
            "on hdct.hoaDon.id = hd.id where " +
            "cast(hd.ngayTao as date ) >= :startDate and cast(hd.ngayTao as date ) <= :endDate ")
    Integer getSoluongBanRaTrong(@Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate);

    //Số lượng hóa đơn ...
    @Query("select COUNT(hd) from HoaDon hd where " +
            "cast(hd.ngayTao as date ) >= :startDate and cast(hd.ngayTao as date ) <= :endDate")
    Integer soLuongHoaDonTrong(@Param("startDate") LocalDate startDate,
                               @Param("endDate") LocalDate endDate);

    @Query("""
            select new com.java.project.dtos.TopSellingResponse (
            spct.hinhAnh,
            sp.tenSanPham,
            spct.mauSac.tenMauSac,
            spct.kichThuoc.tenKichThuoc,
            sum(hdct.soLuong),
            spct.donGia
            )from HoaDonChiTiet hdct join SanPhamChiTiet spct 
            on hdct.sanPhamChiTiet.id = spct.id
            join SanPham sp on spct.sanPham.id = sp.id
            join HoaDon hd on hdct.hoaDon.id = hd.id
            where cast(hd.ngayTao as date) >= :startDate and cast(hd.ngayTao as date) <= :endDate
            group by 
            spct.hinhAnh,
            spct.donGia,
            spct.mauSac.tenMauSac,
            spct.kichThuoc.tenKichThuoc,
            sp.tenSanPham
            order by sum(hdct.soLuong) desc
            """)
    Page<TopSellingResponse> getTopSellingResponse(@Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate,
                                                   Pageable pageable);

    @Query("""
            select new com.java.project.dtos.TopSellingResponse(
            spct.hinhAnh,
            spct.sanPham.tenSanPham,
            spct.mauSac.tenMauSac,
            spct.kichThuoc.tenKichThuoc,
            cast(spct.soLuong as long ),
            spct.donGia
            )from SanPhamChiTiet spct where spct.soLuong <= 20 and spct.trangThai = true 
            """)
    Page<TopSellingResponse> GetSanPhamSapHet(Pageable pageable);


    @Query("""
                SELECT hd.trangThaiGiaoHang, COUNT(hd) 
                FROM HoaDon hd 
                WHERE
                (:ngayBatDau IS NULL OR CAST(hd.ngayTao AS DATE ) >=  :ngayBatDau)
                and (:ngayKetThuc IS NULL OR CAST(hd.ngayTao AS DATE ) <=  :ngayKetThuc)
                AND hd.loaiDon = 0
                GROUP BY hd.trangThaiGiaoHang
            """)
    List<Object[]> SoLuongTrangThaiDonHang(
            @Param("ngayBatDau") LocalDate ngayBatDau,
            @Param("ngayKetThuc") LocalDate ngayKetThuc
    );

    Optional<HoaDon>findByMaHoaDon(String maHoaDon);
}


