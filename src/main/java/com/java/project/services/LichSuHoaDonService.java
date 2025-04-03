package com.java.project.services;

import com.java.project.dtos.LichSuHoaDonRequest;
import com.java.project.dtos.LichSuHoaDonResponse;
import com.java.project.entities.HoaDon;
import com.java.project.entities.LichSuHoaDon;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.mappers.LichSuHoaDonMapper;
import com.java.project.repositories.HoaDonRepository;
import com.java.project.repositories.LichSuHoaDonRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class LichSuHoaDonService {
    LichSuHoaDonRepository lichSuHoaDonRepository;
    HoaDonRepository hoaDonRepository;
    LichSuHoaDonMapper lichSuHoaDonMapper;

    @Transactional
    public LichSuHoaDonResponse createLichSuHoaDon (LichSuHoaDonRequest lichSuHoaDonRequest){
        HoaDon hoaDon =  hoaDonRepository.findById(lichSuHoaDonRequest.getIdHoaDon())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hóa đơn"));

        LichSuHoaDon lichSuHoaDon = new LichSuHoaDon();
        lichSuHoaDon.setHoaDon(hoaDon);
        lichSuHoaDon.setHanhDong(lichSuHoaDonRequest.getHanhDong());
        lichSuHoaDon.setNguoiThayDoi(lichSuHoaDonRequest.getNguoiThayDoi());
        lichSuHoaDon.setThoiGianThayDoi(LocalDateTime.now());



         return lichSuHoaDonMapper.toLichSuHoaDonResponse(lichSuHoaDonRepository.save(lichSuHoaDon));
    }

    public List<LichSuHoaDonResponse> getALlByIdHoaDon (Integer idHoaDon){
        return lichSuHoaDonRepository.getLichSuHoaDon(idHoaDon).stream()
                .map(lichSuHoaDonMapper ::toLichSuHoaDonResponse)
                .toList();
    }

}
