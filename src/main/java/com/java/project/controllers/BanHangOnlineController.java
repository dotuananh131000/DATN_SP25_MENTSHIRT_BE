package com.java.project.controllers;

import com.java.project.dtos.HoaDonResponse;
import com.java.project.dtos.KhachHangDto;
import com.java.project.dtos.PhieuGiamGiaDto;
import com.java.project.entities.HoaDon;
import com.java.project.entities.PhieuGiamGia;
import com.java.project.mappers.HoaDonMapper;
import com.java.project.mappers.PhieuGiamGiaMapper;
import com.java.project.request.APIRequestOrResponse;
import com.java.project.request.HoaDonModel;
import com.java.project.services.BanHangOnlineService;
import com.java.project.services.NotificationService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BanHangOnlineController {
    BanHangOnlineService banHangOnlineService;

    NotificationService notificationService;

    SimpMessagingTemplate messagingTemplate;

    HoaDonMapper hoaDonMapper;


    @GetMapping("/phieu-giam-gia")
    public APIRequestOrResponse<List<PhieuGiamGiaDto>>getPhieuGiamGia(@RequestParam(required = false) Integer idKH){
        List<PhieuGiamGia> listPhieuGiamGia = banHangOnlineService.getPhieuGiamGiaByKH(idKH);

        // Mapping từ Entity -> DTO
        List<PhieuGiamGiaDto> listDto = listPhieuGiamGia.stream()
                .map(PhieuGiamGiaMapper::toDTO)
                .collect(Collectors.toList());

        return APIRequestOrResponse.<List<PhieuGiamGiaDto>>builder()
                .data(listDto)
                .build();
    }

    @GetMapping("/phieu-giam-gia-tot-nhat")
    public APIRequestOrResponse<PhieuGiamGiaDto>getTheBestVoucher(
            @RequestParam(required = false) Integer idKH,
            @RequestParam(required = true) Integer idHD,
            @RequestParam(required = false) Double tongTien){
        PhieuGiamGia phieuGiamGia = banHangOnlineService.theBestVoucher(idKH, idHD, tongTien);
        return APIRequestOrResponse .<PhieuGiamGiaDto> builder()
                .data(PhieuGiamGiaMapper.toDTO(phieuGiamGia))
                .build();
    }

    @PutMapping("/hoanPhieuGiam/{idHD}")
    public APIRequestOrResponse<HoaDonResponse>getTheBestVoucher(
            @PathVariable Integer idHD,
            @RequestBody Double tongTien){
        HoaDonResponse hoaDonResponse = banHangOnlineService.hoanPhieuGiam(idHD, tongTien);
        return APIRequestOrResponse .<HoaDonResponse> builder()
                .data(hoaDonResponse)
                .build();
    }

    @PostMapping("/creatOrder")
    public APIRequestOrResponse<HoaDon>createOrder(@Valid @RequestBody HoaDonModel hoaDonModel){
        HoaDon hoaDon = banHangOnlineService.addHoaDonOnline(hoaDonModel);
//        notificationService.notifyNewOrder(hoaDon);
        HoaDonResponse hoaDonResponse = hoaDonMapper.toHoaDonResponse(hoaDon);
        notificationService.guiThongBaoChoNhanVien(hoaDonResponse);
        return APIRequestOrResponse.<HoaDon>builder()
                .data(hoaDon)
                .build();
    }
}
