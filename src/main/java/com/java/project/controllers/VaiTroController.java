package com.java.project.controllers;

import com.java.project.dtos.VaiTroDto;
import com.java.project.exceptions.ResourceNotFoundException;
import com.java.project.services.VaiTroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vaitro")
public class VaiTroController {

    @Autowired
    private VaiTroService vaiTroService;

    @GetMapping
    public ResponseEntity<List<VaiTroDto>> getAllVaiTro() {
        try {
            List<VaiTroDto> vaiTroList = vaiTroService.getAllVaiTro();
            return ResponseEntity.ok(vaiTroList);
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<VaiTroDto> getVaiTroById(@PathVariable Integer id) {
        try {
            VaiTroDto vaiTro = vaiTroService.getVaiTroById(id);
            return ResponseEntity.ok(vaiTro);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
