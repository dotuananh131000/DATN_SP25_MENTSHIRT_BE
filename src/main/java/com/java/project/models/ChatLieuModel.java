package com.java.project.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ChatLieuModel {
    @NotBlank(message = "Tên chất liệu không được để trống")
    private String tenChatLieu;
}
