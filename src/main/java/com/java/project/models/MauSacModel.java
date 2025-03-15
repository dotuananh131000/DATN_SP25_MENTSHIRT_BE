package com.java.project.models;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MauSacModel {
    @NotBlank(message = "Tên màu sắc không được để trống")
    private String tenMauSac;

    @NotBlank(message = "Mã màu HEX không được để trống")
    private String maHex;
}
