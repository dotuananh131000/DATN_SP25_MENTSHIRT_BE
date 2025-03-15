package com.java.project.repositories;

import com.java.project.entities.ChatLieu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ChatLieuRepository extends JpaRepository<ChatLieu, Integer> {
    @Query("SELECT c FROM ChatLieu c WHERE c.tenChatLieu LIKE %:search%")
    Page<ChatLieu> findByTenChatLieu(@Param("search") String search, Pageable pageable);

    // Kiểm tra xem tên chất liệu đã tồn tại chưa (dùng trong validation)
    boolean existsByTenChatLieu(String tenChatLieu);
}