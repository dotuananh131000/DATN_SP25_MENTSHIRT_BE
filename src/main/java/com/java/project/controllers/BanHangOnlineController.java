package com.java.project.controllers;

import com.java.project.dtos.KhachHangDto;
import com.java.project.dtos.PhieuGiamGiaDto;
import com.java.project.entities.HoaDon;
import com.java.project.request.APIRequestOrResponse;
import com.java.project.request.HoaDonModel;
import com.java.project.services.BanHangOnlineService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BanHangOnlineController {
    BanHangOnlineService banHangOnlineService;

    @GetMapping("/phieu-giam-gia")
    public APIRequestOrResponse<List<PhieuGiamGiaDto>>getPhieuGiamGia(@RequestParam(required = false) Integer idKH){
        List<PhieuGiamGiaDto> listPhieuGiamGia = banHangOnlineService.getPhieuGiamGiaByKH(idKH);
        return APIRequestOrResponse.<List<PhieuGiamGiaDto>>builder()
                .data(listPhieuGiamGia)
                .build();
    }

    @PostMapping("/creatOrder")
    public APIRequestOrResponse<HoaDon>createOrder(@Valid @RequestBody HoaDonModel hoaDonModel){
        HoaDon hoaDon = banHangOnlineService.addHoaDonOnline(hoaDonModel);
        return APIRequestOrResponse.<HoaDon>builder()
                .data(hoaDon)
                .build();
    }
}
