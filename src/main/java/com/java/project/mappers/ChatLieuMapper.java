package com.java.project.mappers;

import com.java.project.dtos.ChatLieuDto;
import com.java.project.entities.ChatLieu;

public class ChatLieuMapper {
    public static ChatLieuDto toDTO(ChatLieu chatLieu) {
        return ChatLieuDto.builder()
                .id(chatLieu.getId())
                .tenChatLieu(chatLieu.getTenChatLieu())
                .trangThai(chatLieu.getTrangThai())
                .ngayTao(chatLieu.getNgayTao())
                .build();
    }
}

