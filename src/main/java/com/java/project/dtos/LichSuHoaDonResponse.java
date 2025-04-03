package com.java.project.dtos;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Builder
public class LichSuHoaDonResponse {
    Integer id;
    Integer idHoaDon;
    String hanhDong;
    String nguoiThayDoi;
    LocalDateTime thoiGianThayDoi;
    String ghiChu;
}
