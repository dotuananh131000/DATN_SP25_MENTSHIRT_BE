package com.java.project.controllers;

import com.java.project.dtos.HoaDonChiTietResponse;
import com.java.project.entities.HoaDonChiTiet;
import com.java.project.repositories.HoaDonChiTietRepository;
import com.java.project.services.BanHangService;
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

    @GetMapping("/{id}")
    public List<HoaDonChiTietResponse>getAllHoaDonChiTietByHD(@PathVariable int id){
        return hoaDonChiTietRepository.getAllByIDHD(id);
    }

    @GetMapping("/count")
    public Map<Integer, Long>hoaDonChiTietCount(){
        return banHangService.getHoaDonChiTietCount();
    }

//    @GetMapping("/ht/{idHD}&&{idSPCT}")
//    public Optional<HoaDonChiTiet> getHDCT(@PathVariable int idHD, @PathVariable int idSPCT){
//        return hoaDonChiTietRepository.findByHoaDon_IdAndSanPhamChiTiet_Id(idHD,idSPCT);
//    }
}
