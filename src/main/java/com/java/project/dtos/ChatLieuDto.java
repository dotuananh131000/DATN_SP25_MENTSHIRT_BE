package com.java.project.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.java.project.entities.ChatLieu}
 */
@Data
@Builder
public class ChatLieuDto implements Serializable {
    Integer id;
    String tenChatLieu;
    Boolean trangThai;
    Instant ngayTao;
}