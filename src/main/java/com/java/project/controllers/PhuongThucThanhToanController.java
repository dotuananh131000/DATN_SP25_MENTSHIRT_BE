package com.java.project.controllers;

import com.java.project.entities.PhuongThucThanhToan;
import com.java.project.repositories.PhuongThucThanhToanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/phuong-thuc-thanh-toan")
public class PhuongThucThanhToanController {
    @Autowired
    PhuongThucThanhToanRepository PhuongThucThanhToanRepository;
    @Autowired
    private PhuongThucThanhToanRepository phuongThucThanhToanRepository;

    @GetMapping
    public List<PhuongThucThanhToan>getAll(){
        return phuongThucThanhToanRepository.findAll();
    }
    @PostMapping("/add")
    public PhuongThucThanhToan add(@RequestBody PhuongThucThanhToan pttt){
        return phuongThucThanhToanRepository.save(pttt);
    }
}
