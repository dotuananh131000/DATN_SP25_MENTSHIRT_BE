package com.java.project.controllers;

import com.java.project.dtos.ThongBaoResponse;
import com.java.project.entities.ThongBao;
import com.java.project.request.APIRequestOrResponse;
import com.java.project.services.NotificationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)

@RequestMapping("/api/thong-bao")
public class ThongBaoController {

    NotificationService notificationService;

    @GetMapping("/{id}")
    public APIRequestOrResponse<List<ThongBaoResponse>>getThongBaoByNhanVien(@PathVariable("id") Integer id){
        List<ThongBaoResponse> lists = notificationService.getThongBaoListByNhanVien(id);
        return APIRequestOrResponse .<List<ThongBaoResponse>>builder()
                .data(lists)
                .build();
    }

    @PutMapping("/{id}")
    public APIRequestOrResponse<String>Seen (@PathVariable("id") Integer id){
        notificationService.Seen(id);
        return APIRequestOrResponse .<String>builder()
                .data("Đã đọc")
                .build();
    }
}
