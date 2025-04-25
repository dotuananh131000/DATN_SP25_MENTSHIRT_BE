package com.java.project.controllers;

import com.java.project.dtos.HoaDonChiTietResponse;
import com.java.project.entities.HoaDonChiTiet;
import com.java.project.repositories.HoaDonChiTietRepository;
import com.java.project.request.APIRequestOrResponse;
import com.java.project.request.HoaDonChiTietRequest;
import com.java.project.services.BanHangService;
import com.java.project.services.HoaDonChiTietService;
import com.java.project.services.HoaDonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/hdct")
public class HoaDonChiTietController {
    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    BanHangService banHangService;

    @Autowired
    HoaDonChiTietService hoaDonChiTietService;

    @Autowired
    HoaDonService hoaDonService;

    @GetMapping("/{id}")
    public List<HoaDonChiTietResponse>getAllHoaDonChiTietByHD(@PathVariable int id){
        return hoaDonChiTietRepository.getAllByIDHD(id);
    }

    @GetMapping("/count")
    public Map<Integer, Long>hoaDonChiTietCount(){
        return banHangService.getHoaDonChiTietCount();
    }

    @PostMapping
    public APIRequestOrResponse<HoaDonChiTietResponse> add (@RequestBody HoaDonChiTietRequest hoaDonChiTietRequest){
        return APIRequestOrResponse .<HoaDonChiTietResponse> builder()
                .data(hoaDonChiTietService.add(hoaDonChiTietRequest))
                .message("Đã thêm sản phẩm và giỏ hàng")
                .build();
    }

    @DeleteMapping("/{id}")
    public APIRequestOrResponse<List<HoaDonChiTietResponse>> delete (@PathVariable Integer id){
        List<HoaDonChiTietResponse>listHdct = hoaDonChiTietService.delete(id);
        return APIRequestOrResponse.<List<HoaDonChiTietResponse>>builder()
                .message("Đã xóa sản phẩm ra khỏi đơn hàng")
                .data(listHdct)
                .build();
    }

}
