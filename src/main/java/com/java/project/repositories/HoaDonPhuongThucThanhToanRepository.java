package com.java.project.repositories;

import com.java.project.dtos.HoaDonPhuongThucThanhToanResponse;
import com.java.project.entities.HoaDonPhuongThucThanhToan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HoaDonPhuongThucThanhToanRepository extends JpaRepository<HoaDonPhuongThucThanhToan, Integer> {
    @Query("""
            select new com.java.project.dtos.HoaDonPhuongThucThanhToanResponse(
            hdpttt.id,
            hdpttt.maGiaoDich,
            hd.maHoaDon,
            pttt.tenPhuongThuc,
            hdpttt.ngayThucHienThanhToan,
            hdpttt.soTienThanhToan,
            hdpttt.ghiChu,
            hdpttt.nguoiXacNhan
            )from HoaDonPhuongThucThanhToan hdpttt 
            left join HoaDon hd on hdpttt.hoaDon.id = hd.id
            left join PhuongThucThanhToan pttt on hdpttt.phuongThucThanhToan.id = pttt.id
            where hdpttt.hoaDon.id = :idHD
            """)
    List<HoaDonPhuongThucThanhToanResponse>getAllByIdHD(@Param("idHD") Integer idHD);
}