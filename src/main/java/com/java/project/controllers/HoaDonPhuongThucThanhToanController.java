package com.java.project.controllers;

import com.java.project.dtos.HoaDonPhuongThucThanhToanResponse;
import com.java.project.entities.HoaDonPhuongThucThanhToan;
import com.java.project.request.HoaDonPhuongThucThanhToanRequest;
import com.java.project.services.HoaDonPhuongThucThanhToanService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/hdpttt")
public class HoaDonPhuongThucThanhToanController {
    @Autowired
    HoaDonPhuongThucThanhToanService hoaDonPhuongThucThanhToanService;

    @GetMapping("/{idHD}")
    public List<HoaDonPhuongThucThanhToanResponse>getAllByIdHD(@PathVariable Integer idHD){
        return hoaDonPhuongThucThanhToanService.getAll(idHD);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> add(@Valid @RequestBody HoaDonPhuongThucThanhToanRequest rq,
                                                   BindingResult bindingResult){
        Map<String, Object>response = new HashMap<>();
        if(bindingResult.hasErrors()){
            response.put("success", false);
            List<String>errMassage = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("errors", errMassage);
            return ResponseEntity.badRequest().body(response);
        }
        try {
            HoaDonPhuongThucThanhToan hoaDonPhuongThucThanhToan = hoaDonPhuongThucThanhToanService.addHDPTTT(rq);
            response.put("success", true);
            response.put("success", "Đã thêm hóa đơn phương thức thanh toán");
            return ResponseEntity.ok(response);
        }catch (Exception e){
            response.put("success", false);
            response.put("succes","Thêm hóa đơn phương thức thanh toán thất bại");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
