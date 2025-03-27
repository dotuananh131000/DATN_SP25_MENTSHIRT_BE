package com.java.project.controllers;

import com.java.project.dtos.HoaDonBanHangResponse;
import com.java.project.dtos.HoaDonResponse;
import com.java.project.entities.HoaDon;
import com.java.project.mappers.HoaDonMapper;
import com.java.project.repositories.HoaDonChiTietRepository;
import com.java.project.repositories.HoaDonRepository;
import com.java.project.request.APIRequestOrResponse;
import com.java.project.services.HoaDonService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.logging.Level;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hoa-don")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class HoaDonController {

    HoaDonService hoaDonService;

    HoaDonRepository hoaDonRepository;

    HoaDonMapper hoaDonMapper;

    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @GetMapping
    public APIRequestOrResponse<Page<HoaDonResponse>>
    getHoaDons(@RequestParam(defaultValue = "0") int page,
               @RequestParam(defaultValue = "10") int size,
               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayBatDau,
               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate ngayKetThuc,
               @RequestParam(required = false) String keyword,
               @RequestParam(required = false) Integer loaiDon,
               @RequestParam(required = false) Integer trangThaiGiaoHang)
    {
        Pageable pageable = PageRequest.of(page, size);
        LocalDate today = LocalDate.now();
        if(ngayBatDau == null){
            ngayBatDau = today;
        }
        if(ngayKetThuc == null){
            ngayKetThuc = today;
        }
        Page<HoaDonResponse> hoaDons =
                hoaDonService.getHoaDonList(pageable,ngayBatDau,ngayKetThuc,keyword,loaiDon,trangThaiGiaoHang);
        return APIRequestOrResponse.<Page<HoaDonResponse>>builder()
                .data(hoaDons)
                .build();
    }

    @GetMapping("/{id}")
    public APIRequestOrResponse<HoaDonResponse>getHoaDonById(@PathVariable("id") Integer id){
        HoaDonResponse hoaDon = hoaDonService.getHoaDonById(id);
        return APIRequestOrResponse.<HoaDonResponse>builder()
                .data(hoaDon)
                .build();
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getOrderCounts(
            @RequestParam(required = false) String ngayBatDau,
            @RequestParam(required = false) String ngayKetThuc,
            @RequestParam(required = false) Integer loaiDon
    ) {

        // Nếu không có ngày, mặc định lấy dữ liệu hôm nay
        LocalDate startDate = (ngayBatDau != null && !ngayBatDau.isEmpty())
                ? LocalDate.parse(ngayBatDau)
                : LocalDate.now();

        LocalDate endDate = (ngayKetThuc != null && !ngayKetThuc.isEmpty())
                ? LocalDate.parse(ngayKetThuc)
                : LocalDate.now();

        Map<String, Long> counts = hoaDonService.getOrderCounts(startDate, endDate, loaiDon);
        return ResponseEntity.ok(counts);
    }

    @GetMapping("/{idHD}/hoa-don")
    public HoaDonResponse getHoaDonByMaHoaDon(@PathVariable Integer idHD) {
        HoaDon hoaDon = hoaDonRepository.findById(idHD).orElse(null);
       return hoaDonMapper.toHoaDonResponse(hoaDon);
    }
}
