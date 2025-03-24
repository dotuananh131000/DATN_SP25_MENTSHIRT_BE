package com.java.project.services;

import com.java.project.dtos.HoaDonBanHangResponse;
import com.java.project.dtos.HoaDonHomNayResponse;
import com.java.project.dtos.HoaDonResponse;
import com.java.project.entities.HoaDon;
import com.java.project.mappers.HoaDonMapper;
import com.java.project.repositories.HoaDonRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class HoaDonService {
    HoaDonRepository hoaDonRepository;

    HoaDonMapper hoaDonMapper;

    LocalDateTime startDate = LocalDate.now().atStartOfDay();
    LocalDateTime endDate = startDate.plusDays(1);


    public Page<HoaDonResponse>getHoaDonList(Pageable pageable,
                                             LocalDate ngayBatDau,
                                             LocalDate ngayKetThuc,
                                             String keyword,
                                             Integer loaiDon,
                                             Integer trangThaiGiaoHang) {
        return hoaDonRepository.getListHoaDon(pageable,ngayBatDau, ngayKetThuc,keyword,loaiDon,trangThaiGiaoHang)
                .map(hoaDonMapper::toHoaDonResponse);
    }


    public Map<String, Long> getOrderCounts(
            LocalDate ngayBatDau, LocalDate ngayKetThuc,
            Integer loaiDon) {

        List<Object[]> results = hoaDonRepository.countOrdersByStatus(ngayBatDau, ngayKetThuc, loaiDon);

        // Tạo một map với giá trị mặc định là 0
        Map<String, Long> counts = new LinkedHashMap<>();
        counts.put("tong", 0L);
        counts.put("cho_xac_nhan", 0L);
        counts.put("xac_nhan", 0L);
        counts.put("cho_van_chuyen", 0L);
        counts.put("van_chuyen", 0L);
        counts.put("thanh_cong", 0L);
        counts.put("hoan_hang", 0L);
        counts.put("da_huy", 0L);

        // Cập nhật giá trị từ kết quả truy vấn
        for (Object[] result : results) {
            Integer trangThai = (Integer) result[0];
            Long soLuong = (Long) result[1];

            switch (trangThai) {
                case 1 -> counts.put("cho_xac_nhan", soLuong);
                case 2 -> counts.put("xac_nhan", soLuong);
                case 3 -> counts.put("cho_van_chuyen", soLuong);
                case 4 -> counts.put("van_chuyen", soLuong);
                case 5 -> counts.put("thanh_cong", soLuong);
                case 6 -> counts.put("hoan_hang", soLuong);
                case 7 -> counts.put("da_huy", soLuong);
                default -> counts.put("tao_hoa_don", soLuong);    
            }
            counts.put("tong", counts.get("tong") + soLuong);
        }

        return counts;
    }

    public List<HoaDonHomNayResponse>getHoaDonHomNay() {
        return hoaDonRepository.getHoaDonHomNay();
    }

    public Optional<HoaDonBanHangResponse>getHoaDonByMaHoaDon(String maHoaDon){
        return hoaDonRepository.getHoaDonByMaHoaDon(maHoaDon);
    }


}
