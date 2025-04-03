package com.java.project.dtos;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
@Data
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LichSuHoaDonRequest {

    Integer idHoaDon;

    String hanhDong;

    String nguoiThayDoi;

}
