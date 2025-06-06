package com.java.project.repositories;

import com.java.project.dtos.HoaDonChiTietResponse;
import com.java.project.entities.HoaDonChiTiet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HoaDonChiTietRepository extends JpaRepository<HoaDonChiTiet, Integer> {

    Optional<HoaDonChiTiet>findByHoaDon_IdAndSanPhamChiTiet_Id(Integer idHoaDon, Integer idSPCT);

    @Query("""
            select new com.java.project.dtos.HoaDonChiTietResponse(
            hdct.id,
            hdct.hoaDon.id,
            spct.id,
            sp.tenSanPham,
            spct.hinhAnh,
            spct.mauSac.tenMauSac,
            spct.kichThuoc.tenKichThuoc,
            spct.donGia,
            spct.donGiaCu,
            spct.soLuong,
            hdct.soLuong,
            hdct.thanhTien,
            hdct.trangThai
            )from HoaDonChiTiet hdct left join SanPhamChiTiet spct on hdct.sanPhamChiTiet.id = spct.id
            left join SanPham sp on spct.sanPham.id = sp.id
            where hdct.hoaDon.id =:idHD
            """)
    List<HoaDonChiTietResponse>getAllByIDHD(@Param("idHD") Integer idHD);

    List<HoaDonChiTiet>findByHoaDon_Id(Integer idHoaDon);

    @Query("select hdct.hoaDon.id, count(hdct.id) from HoaDonChiTiet hdct group by hdct.hoaDon.id")
    List<Object[]> getHoaDonChiTietCount();

//    @Query("SELECT hdct FROM HoaDonChiTiet hdct " +
//            "WHERE hdct.hoaDon.id = :idHD")
//    Page<HoaDonChiTiet>getHoaDonChiTiet(Pageable pageable, @Param("idHD") Integer idHD);

}