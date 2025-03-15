package com.java.project.models;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CoAoModel {
    @NotBlank(message = "Tên cổ áo không được để trống")
    private String tenCoAo;
}