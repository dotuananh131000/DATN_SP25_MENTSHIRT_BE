package com.java.project.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link com.java.project.entities.XuatXu}
 */
@Data
@Builder
public class XuatXuDto implements Serializable {
    Integer id;
    @NotNull
    @Size(max = 255)
    String tenXuatXu;
    @NotNull
    Boolean trangThai;
    Instant ngayTao;
}