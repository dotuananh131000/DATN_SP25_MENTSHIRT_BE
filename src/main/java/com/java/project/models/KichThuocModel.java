package com.java.project.models;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class KichThuocModel {
    @NotBlank(message = "Tên kích thước không được để trống")
    private String tenKichThuoc;
}