package com.java.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ApiResponse {
    private String status;
    private String message;
    private Object data;
}
