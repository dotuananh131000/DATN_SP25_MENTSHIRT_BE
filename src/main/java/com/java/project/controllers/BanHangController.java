package com.java.project.controllers;

import com.java.project.dtos.HoaDonHomNayResponse;
import com.java.project.dtos.KhachHangResponse;
import com.java.project.dtos.PhieuGiamGiaKhachHangResponse;
import com.java.project.entities.HoaDon;
import com.java.project.entities.HoaDonChiTiet;
import com.java.project.entities.PhieuGiamGia;
import com.java.project.entities.PhieuGiamGiaKhachHang;
import com.java.project.repositories.KhachHangRepository;
import com.java.project.repositories.PhieuGiamGiaKhachHangRepository;
import com.java.project.repositories.PhieuGiamGiaRepository;
import com.java.project.request.HoaDonChiTietRequest;
import com.java.project.request.HoaDonRequest;
import com.java.project.request.ThongTinDonHangRequest;
import com.java.project.services.BanHangService;
import com.java.project.services.HoaDonService;
import com.java.project.suportRespone.KhachHangReponse;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ban-hang")
public class BanHangController {

    @Autowired
    BanHangService banHangService;
    @Autowired
    HoaDonService hoaDonService;
    @Autowired
    private KhachHangRepository khachHangRepository;

    @Autowired
    private PhieuGiamGiaRepository phieuGiamGiaRepository;

    @Autowired
    private PhieuGiamGiaKhachHangRepository phieuGiamGiaKhachHangRepository;

    @PostMapping
    public ResponseEntity<Map<String, Object>> addHD(@Valid @RequestBody HoaDonRequest hoaDonRequest,
                                                   BindingResult bindingResult) {
        Map<String, Object> response = new HashMap<>();

        //Xử lý lỗi Binding
        if(bindingResult.hasErrors()) {
            response.put("succsess", false);
            List<String> errMessage = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("error", errMessage);
            return ResponseEntity.badRequest().body(response);
        }

        try {
            HoaDon hoaDon = banHangService.createHoaDon(hoaDonRequest);
            response.put("succsess", true);
            response.put("message", "Đã thêm hóa đơn mới");
            response.put("id", hoaDon.getId());
            response.put("Mã hóa đơn", hoaDon.getMaHoaDon());
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("success", false);
            response.put("message", "Thêm hóa đơn thất bại: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update-hoa-don/{id}")
    @Transactional
    public ResponseEntity<Object> updateHoaDon(@PathVariable("id") Integer id,@RequestBody Integer idKH){
        HoaDon hoaDonUpdate = null;
        try {
            hoaDonUpdate = banHangService.updateKHOfHoaDon(id,idKH);
            return new ResponseEntity<>(hoaDonUpdate, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(hoaDonUpdate, HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/update-trang-thai/{id}")
    @Transactional
    public ResponseEntity<Object> updateHoaDon(@PathVariable("id") Integer id){
        HoaDon hoaDonUpdate = null;
        try {
            hoaDonUpdate = banHangService.upDateTrangThaiHoaDon(id);
            return new ResponseEntity<>(hoaDonUpdate, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(hoaDonUpdate, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update-loai-don-online/{id}")
    public ResponseEntity<Object> upDateLoaiDonOn(@PathVariable("id") Integer id){
        HoaDon hoaDonUpdate = null;
        try {
            hoaDonUpdate = banHangService.upDateLoaiDonOnline(id);
            return new ResponseEntity<>(hoaDonUpdate, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(hoaDonUpdate, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update-loai-don-offline/{id}")
    public ResponseEntity<Object> upDateLoaiDonOff(@PathVariable("id") Integer id){
        HoaDon hoaDonUpdate = null;
        try {
            hoaDonUpdate = banHangService.upDateLoaiDonOffline(id);
            return new ResponseEntity<>(hoaDonUpdate, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(hoaDonUpdate, HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/update-trang-thai-don-hang/{id}")
    public ResponseEntity<Object> updateTrangThaiDonHang(@PathVariable("id") Integer id){
        HoaDon hoaDonUpdate = null;
        try {
            hoaDonUpdate = banHangService.updateTrangThaiDonHang(id);
            return new ResponseEntity<>(hoaDonUpdate, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(hoaDonUpdate, HttpStatus.NOT_FOUND);
        }
    }



    @PostMapping("/addHdct")
    public ResponseEntity<Map<String, Object>>addHDCT(@Valid @RequestBody HoaDonChiTietRequest hoaDonChiTietRequest,
                                                      BindingResult bindingResult){
        Map<String, Object> response = new HashMap<>();

        if(bindingResult.hasErrors()) {
            response.put("succsess", false);
            List<String>errMessage = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("error", errMessage);
            return ResponseEntity.badRequest().body(response);
        }

        try{
            HoaDonChiTiet hdct =
                    banHangService.AddOrUpdateHoaDonChiTiet(hoaDonChiTietRequest);
            banHangService.updateTongTienHoaDon(hdct.getHoaDon().getId());
            response.put("succsess", true);
            response.put("message", "Đã thêm hóa đơn chi tiết mới");
            response.put("Mã hóa đơn ", hdct.getHoaDon().getMaHoaDon());
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("success", false);
            response.put("message", "Thêm hóa đơn chi tiết thất bại: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PutMapping("/update-quantity/{id}")
    public ResponseEntity<Object>updateQuantityHDCT(@PathVariable("id") Integer id,@RequestBody Integer newQuantity) {
        HoaDonChiTiet upDateHoaDonChiTiet = null;
        try {
            upDateHoaDonChiTiet = banHangService.updateQuatityHDCT(id, newQuantity);
            return new ResponseEntity<>(upDateHoaDonChiTiet, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(upDateHoaDonChiTiet, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteHDCT/{id}")
    @Transactional
    public ResponseEntity<String>deleteHDCT(@PathVariable("id") Integer id) {
        try {
            banHangService.deleteHoaDonChiTiet(id);
            return new ResponseEntity<>("Xóa hóa đơn chi tiết thành công ", HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>("Xóa hóa đơn chi tiết thất bại",HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping("/hoa-don-hom-nay")
    public List<HoaDonHomNayResponse>getHoaDonHomNay(){
        return hoaDonService.getHoaDonHomNay();
    }

    @GetMapping("/khach-hang")
    public List<KhachHangReponse>getAll(){
        return khachHangRepository.getAllKHByHD();
    }

    @GetMapping("/phieu-giam-gia")
    public List<PhieuGiamGia>getAllPhieuGiamGia(){
        return phieuGiamGiaRepository.getAll();
    };

    @GetMapping("/phieu-giam-gia-khach-hang/{idkh}")
    public List<PhieuGiamGiaKhachHangResponse>getAllPGGKH(@PathVariable("idkh") Integer idkh){
        return phieuGiamGiaKhachHangRepository.phieuGiamGiaKhachHang(idkh);
    }

    @PutMapping("/auto-pggtn/{id}")
    public ResponseEntity<Object>autoPhieuGiamGiaTotNhat(@PathVariable("id") Integer id){
        HoaDon hoaDonAutoGG = null;
        try{
            hoaDonAutoGG = banHangService.autoUpdatePGGTotNhat(id);
            return new ResponseEntity<>(hoaDonAutoGG, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(hoaDonAutoGG, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/deleteKhachHang/{id}")
    public ResponseEntity<Object>deleteKhachHang(@PathVariable("id") Integer idHD){
        HoaDon hoaDonDelete = null;
        try{
            hoaDonDelete = banHangService.deleteKhachHangOfHoaDon(idHD);
            return new ResponseEntity<>(hoaDonDelete, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(hoaDonDelete, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mapGiaSPCT")
    public ResponseEntity<Map<Integer, BigDecimal>>getMapGiaSPCT(){
        try{
            Map<Integer,BigDecimal> mapSPCT =banHangService.getMapGiaSanPham();
            return new ResponseEntity<>(mapSPCT, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/reloadHDCT/{id}")
    public ResponseEntity<HoaDonChiTiet> reloadHDCT(@PathVariable("id") Integer id) {
        HoaDonChiTiet hoaDonChiTiet = null;
        try{
            hoaDonChiTiet = banHangService.reloadSPGioHang(id);
            return new ResponseEntity<>(hoaDonChiTiet, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(hoaDonChiTiet, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/updateThongTinDonHang/{id}")
    public ResponseEntity<HoaDon> updateThongTinDonHang(@PathVariable("id") Integer id,
                                                        @RequestBody ThongTinDonHangRequest request){
        HoaDon hoaDon = null;
        try{
            hoaDon = banHangService.updateThongTinDonHang(id,request);
            return new ResponseEntity<>(hoaDon, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(hoaDon, HttpStatus.NOT_FOUND);
        }
    }


}
