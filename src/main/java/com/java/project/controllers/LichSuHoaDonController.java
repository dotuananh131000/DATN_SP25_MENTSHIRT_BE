package com.java.project.controllers;

import com.java.project.dtos.LichSuHoaDonRequest;
import com.java.project.dtos.LichSuHoaDonResponse;
import com.java.project.entities.LichSuHoaDon;
import com.java.project.request.APIRequestOrResponse;
import com.java.project.services.LichSuHoaDonService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/lich-su-hoa-don")
public class LichSuHoaDonController {
    LichSuHoaDonService lichSuHoaDonService;

    @PostMapping
    public APIRequestOrResponse<LichSuHoaDonResponse>create(@RequestBody LichSuHoaDonRequest lichSuHoaDonRequest){
        LichSuHoaDonResponse response = lichSuHoaDonService.createLichSuHoaDon(lichSuHoaDonRequest);
        return APIRequestOrResponse.<LichSuHoaDonResponse>builder()
                .data(response)
                .build();
    }

    @GetMapping("/{idHD}")
    public APIRequestOrResponse<List<LichSuHoaDonResponse>>getAll (@PathVariable("idHD") Integer idHD){
        return APIRequestOrResponse .<List<LichSuHoaDonResponse>>builder()
                .data(lichSuHoaDonService.getALlByIdHoaDon(idHD))
                .build();
    }
}
