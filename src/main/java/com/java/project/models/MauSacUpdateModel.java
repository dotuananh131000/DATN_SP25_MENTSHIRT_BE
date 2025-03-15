package com.java.project.models;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MauSacUpdateModel {
    private String tenMauSac;
    private String maHex;
}
