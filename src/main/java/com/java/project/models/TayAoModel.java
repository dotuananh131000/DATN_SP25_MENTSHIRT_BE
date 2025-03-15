package com.java.project.models;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TayAoModel {
    @NotBlank(message = "Tên tay áo không được để trống")
    private String tenTayAo;
}