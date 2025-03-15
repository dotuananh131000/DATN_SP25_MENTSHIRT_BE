package com.java.project.controllers;

import com.java.project.dtos.TopSellingResponse;
import com.java.project.services.HoaDonService;
import com.java.project.services.ThongKeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/thong-ke")
public class ThongKeController {

    @Autowired
    ThongKeService thongKeService;
    @Autowired
    private HoaDonService hoaDonService;

    @GetMapping("/doanh-thu-hom-nay")
    public Double doanhThuHomNay(){
        return thongKeService.doanhThuHomNay();
    }

    @GetMapping("/so-luong-da-ban-hom-nay")
    public Integer soLuongBanRaHomNay(){
        return thongKeService.soLuongDaBanTrongNgayHomNay();
    }

    @GetMapping("/so-luong-hoa-don-hom-nay")
    public Integer soLuongHoaDonHomNay(){
        return thongKeService.soLuongHoaDonNgayHomNay();
    }
    //Trong tuần
    @GetMapping("/doanh-thu-trong-tuan")
    public Double doanhThuTrongTuan(){
        return thongKeService.doanhThuTrongTuan();
    }

    @GetMapping("/so-luong-da-ban-trong-tuan")
    public Integer soLuongBanRaTrongTuan(){
        return thongKeService.soLuongDaBanTrongTuan();
    }

    @GetMapping("/so-luong-hoa-don-trong-tuan")
    public Integer soLuongHoaDonTrongTuan(){
        return thongKeService.soLuongHoaDonTrongTuan();
    }
    //Trong thangs
    @GetMapping("/doanh-thu-trong-thang")
    public Double doanhThuTrongThang(){
        return thongKeService.doanhThuTrongThang();
    }

    @GetMapping("/so-luong-da-ban-trong-thang")
    public Integer soLuongBanRaTrongThang(){
        return thongKeService.soLuongDaBanTrongThang();
    }

    @GetMapping("/so-luong-hoa-don-trong-thang")
    public Integer soLuongHoaDonTrongThang(){
        return thongKeService.soLuongHoaDonTrongThang();
    }
    //Trong năm
    @GetMapping("/doanh-thu-trong-nam")
    public Double doanhThuTrongNam(){
        return thongKeService.doanhThuTrongNam();
    }

    @GetMapping("/so-luong-da-ban-trong-nam")
    public Integer soLuongBanRaTrongNam(){
        return thongKeService.soLuongDaBanTrongNam();
    }

    @GetMapping("/so-luong-hoa-don-trong-nam")
    public Integer soLuongHoaDonTrongNam(){
        return thongKeService.soLuongHoaDonTrongNam();
    }

    @GetMapping("/top-san-pham")
    public ResponseEntity<Page<TopSellingResponse>>GetTopSelling(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<TopSellingResponse>TopSelling = thongKeService.getTopSellingOnMonth(pageable);

        return ResponseEntity.ok(TopSelling);
    }

    @GetMapping("/san-pham-gan-het")
    public ResponseEntity<Page<TopSellingResponse>>GetSPGanHet(
            @RequestParam(defaultValue = "0")int page,
            @RequestParam(defaultValue = "5") int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<TopSellingResponse>SPGanHet = thongKeService.getSPGanHetHang(pageable);

        return ResponseEntity.ok(SPGanHet);
    }

    @GetMapping("/so-luong-trang-thai-don-hang")
    public ResponseEntity<Map<String, Long>>countTrangThaiDonHang(
            @RequestParam(value = "ngayBatDau", required = false)LocalDate ngayBatDau,
            @RequestParam(value = "ngayKetThuc", required = false)LocalDate ngayKetThuc
            ){
        if(ngayBatDau == null){
            ngayBatDau = LocalDate.now();
        }
        if(ngayKetThuc == null){
            ngayKetThuc = LocalDate.now();
        }
        Map<String, Long>mapCount = thongKeService.getOrderCounts(ngayBatDau,ngayKetThuc);
        return ResponseEntity.ok(mapCount);
    }

}
