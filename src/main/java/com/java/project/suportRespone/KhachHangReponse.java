package com.java.project.suportRespone;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KhachHangReponse {
    private Integer id;
    private String tenKH;
    private String hinhAnh;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDate ngaySinh;
    private Integer gioiTinh;
    private String soDienThoai;
    private String email;
}
