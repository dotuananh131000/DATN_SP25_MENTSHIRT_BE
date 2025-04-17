package com.java.project.repositories;

import com.java.project.entities.ThongBao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public interface ThongBaoRepository extends JpaRepository<ThongBao, Integer> {
    @Query("SELECT tb FROM ThongBao tb WHERE tb.nhanVien.id = :id ORDER BY tb.thoiGianTao DESC ")
    List<ThongBao>findByNhanVien_Id(@Param("id") Integer id);

    List<ThongBao>findByKhachHang_Id(Integer id);
}
