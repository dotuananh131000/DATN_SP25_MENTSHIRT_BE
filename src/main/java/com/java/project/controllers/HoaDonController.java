package com.java.project.controllers;

import com.java.project.dtos.HoaDonBanHangResponse;
import com.java.project.dtos.HoaDonResponse;
import com.java.project.entities.HoaDon;
import com.java.project.mappers.HoaDonMapper;
import com.java.project.repositories.HoaDonChiTietRepository;
import com.java.project.repositories.HoaDonRepository;
import com.java.project.request.APIRequestOrResponse;
import com.java.project.request.ChonPhieuGiamGiaHoaDonRequest;
import com.java.project.request.ConfirmHoaDonRequest;
import com.java.project.request.ThongTinDonHangRequest;
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
import org.springframework.transaction.annotation.Transactional;
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

    @PostMapping
    public APIRequestOrResponse<HoaDonResponse>add(@RequestBody Integer idNhanVien){
        return APIRequestOrResponse .<HoaDonResponse>builder()
                .data(hoaDonService.add(idNhanVien))
                .build();
    }


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
    public APIRequestOrResponse<HoaDonResponse>getHoaDonById(
            @PathVariable("id") Integer id){
        HoaDonResponse hoaDon = hoaDonService.getHoaDonById(id);
        return APIRequestOrResponse.<HoaDonResponse>builder()
                .data(hoaDon)
                .build();
    }
    @GetMapping("/khach-hang/{id}")
    public APIRequestOrResponse<Page<HoaDonResponse>>getHoaDonKhachHangById(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String  keyword,
            @RequestParam(required = false) Integer trangThaiGiaoHang,
            @PathVariable("id") Integer id){
        Pageable pageable = PageRequest.of(page, size);
        Page<HoaDonResponse> hoaDons =
                hoaDonService.getHoaDonByIdKhachHang(pageable, id, keyword, trangThaiGiaoHang);
        return APIRequestOrResponse. <Page<HoaDonResponse>>builder()
                .data(hoaDons)
                .build();
    }

    @GetMapping("/cho")
    public APIRequestOrResponse<List<Object>>getHoaDonCho(){
        return APIRequestOrResponse.<List<Object>>builder()
                .data(hoaDonService.getHoaDonCho())
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

    @PutMapping("/{idHD}")
    public APIRequestOrResponse<HoaDonResponse> tiepNhanHoaDon(
            @PathVariable("idHD") Integer idHD,
            @RequestParam("idNhanVien") Integer idNhanVien
    ) {
        return APIRequestOrResponse .<HoaDonResponse>builder()
                .data(hoaDonService.tiepNhanHoaDon(idHD, idNhanVien))
                .message("Ok rồi nha")
                .build();
    }

    @GetMapping("/getByMa/{maHoaDon}")
    public  APIRequestOrResponse<HoaDonResponse>getHoaDonByMaHoaDon(@PathVariable String maHoaDon){
        return APIRequestOrResponse .<HoaDonResponse>builder()
                .data(hoaDonService.getHoaDonByMaHoaDon(maHoaDon))
                .message("Đã lấy thông tin hóa đơn.")
                .build();
    }

    @PutMapping("/paid/{idHD}")
    public APIRequestOrResponse<HoaDonResponse>paidInvoice(@PathVariable Integer idHD){
        return APIRequestOrResponse .<HoaDonResponse> builder()
                .data(hoaDonService.paidInvoice(idHD))
                .message("Hóa đơn đã được thanh toán")
                .build();
    }

    @PutMapping("/updateInfo")
    public APIRequestOrResponse<HoaDonResponse>updateInfoInvoice(@RequestBody ThongTinDonHangRequest request) {
        return APIRequestOrResponse .<HoaDonResponse>builder()
                .message("Đã cập nhật thông tin đn hàng thành công")
                .data(hoaDonService.upDateThongTinDonHang(request))
                .build();
    }

    @PutMapping("/cancel/{id}")
    public APIRequestOrResponse<HoaDonResponse>cancelInvoice(@PathVariable Integer id){
        return APIRequestOrResponse.<HoaDonResponse>builder()
                .data(hoaDonService.huyDonHang(id))
                .message("Đã hủy đơn hàng.")
                .build();
    }

    @PutMapping("/chon-khach-hang/{idHD}")
    public APIRequestOrResponse<HoaDonResponse>hoaDonChonKhachHang(
            @PathVariable Integer idHD,
            @RequestBody Integer idKH){
        return APIRequestOrResponse .<HoaDonResponse> builder()
                .data(hoaDonService.chonKhachHangVaoHoaDon(idHD, idKH))
                .message("Đã chọn khách hàng vào hóa đơn")
                .build();
    }

    @PutMapping("/bo-khach-hang/{idHD}")
    public APIRequestOrResponse<HoaDonResponse>boKhachHang(
            @PathVariable Integer idHD){
        return APIRequestOrResponse .<HoaDonResponse> builder()
                .data(hoaDonService.removeKhachHang(idHD))
                .message("Đã bỏ chọn khách hàng")
                .build();
    }

    @PutMapping("/chon-phieu-giam-gia/{idHD}")
    public APIRequestOrResponse<HoaDonResponse>hoaDonChonPhieuGiamGia(
            @PathVariable("idHD") Integer idHD, @RequestBody ChonPhieuGiamGiaHoaDonRequest request) {
        return APIRequestOrResponse .<HoaDonResponse> builder()
                .data(hoaDonService.chonPhieuGiamGiaKhac(idHD, request.getIdPGG()))
                .message("Đã chọn phiếu giảm giá mới")
                .build();
    }

    @PutMapping("/doi-loai-don/{idHD}")
    public APIRequestOrResponse<HoaDonResponse>doiLoaiDon(
            @PathVariable Integer idHD){
        return APIRequestOrResponse .<HoaDonResponse> builder()
                .data(hoaDonService.doiLoaiDon(idHD))
                .message("Đã đổi loại đơn")
                .build();
    }

    @PutMapping("/confirm-hoa-don/{idHD}")
    public APIRequestOrResponse<HoaDonResponse>confirmHoaDon(@PathVariable Integer idHD,
                                                             @RequestBody ConfirmHoaDonRequest request){
        return APIRequestOrResponse .<HoaDonResponse> builder()
                .data(hoaDonService.confirmHoaDon(idHD, request))
                .build();
    }
}
