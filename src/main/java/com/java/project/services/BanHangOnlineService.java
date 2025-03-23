package com.java.project.services;

import com.java.project.dtos.PhieuGiamGiaDto;
import com.java.project.entities.PhieuGiamGia;
import com.java.project.mappers.PhieuGiamGiaMapper;
import com.java.project.repositories.PhieuGiamGiaRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BanHangOnlineService {
    PhieuGiamGiaRepository phieuGiamGiaRepository;

    public List<PhieuGiamGiaDto> getPhieuGiamGiaByKH(Integer idKH) {
        List<PhieuGiamGia> listPhieuGiamGia = phieuGiamGiaRepository.findPhieuGiamGiaByKhachHang(idKH);
        return listPhieuGiamGia.stream()
                .map(PhieuGiamGiaMapper ::toDTO)
                .toList();
    }
}
