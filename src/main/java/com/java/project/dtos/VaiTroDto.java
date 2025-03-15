package com.java.project.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;

/**
 * DTO for {@link com.java.project.entities.VaiTro}
 */
@Data
@Builder
public class VaiTroDto implements Serializable {
    Integer id;
    String maVaiTro;
    String tenVaiTro;
    Integer trangThai;
}