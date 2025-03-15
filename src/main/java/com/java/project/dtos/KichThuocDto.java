package com.java.project.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.java.project.entities.KichThuoc}
 */
@Data
@Builder
public class KichThuocDto implements Serializable {
    Integer id;
    @NotNull
    @Size(max = 255)
    String tenKichThuoc;
    @NotNull
    Boolean trangThai;
    Instant ngayTao;
}