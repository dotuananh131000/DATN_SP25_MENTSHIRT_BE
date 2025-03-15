package com.java.project.services;

import com.java.project.dtos.TopSellingResponse;
import com.java.project.repositories.HoaDonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ThongKeService {
    @Autowired
    HoaDonRepository hoaDonRepository;

    LocalDate thuHai = LocalDate.now().with(DayOfWeek.MONDAY);
    LocalDate cuoiTuan = thuHai.plusDays(6);

    LocalDate dauThang = LocalDate.now().withDayOfMonth(1);
    LocalDate cuoiThang = dauThang.plusMonths(1).minusDays(1);

    LocalDate dauNam = LocalDate.now().withDayOfYear(1);
    LocalDate cuoiNam = dauNam.plusYears(1).minusDays(1);

    public Double doanhThuHomNay(){
        return hoaDonRepository.getTongDoanhThuHomNay();
    }

    public Integer soLuongDaBanTrongNgayHomNay(){
        return hoaDonRepository.getSoluongBanRaHomNay();
    }

    public Integer soLuongHoaDonNgayHomNay(){
        return hoaDonRepository.soLuongHoaDonHomNay();
    }

    public Double doanhThuTrongTuan(){
        return hoaDonRepository.getTongDoanhThuTrong(thuHai, cuoiTuan);
    }

    public Integer soLuongDaBanTrongTuan(){
        return hoaDonRepository.getSoluongBanRaTrong(thuHai, cuoiTuan);
    }

    public Integer soLuongHoaDonTrongTuan(){
        return hoaDonRepository.soLuongHoaDonTrong(thuHai, cuoiTuan);
    }
    //trong tháng
    public Double doanhThuTrongThang(){
        return hoaDonRepository.getTongDoanhThuTrong(dauThang, cuoiThang);
    }

    public Integer soLuongDaBanTrongThang(){
        return hoaDonRepository.getSoluongBanRaTrong(dauThang, cuoiThang);
    }

    public Integer soLuongHoaDonTrongThang(){
        return hoaDonRepository.soLuongHoaDonTrong(dauThang, cuoiThang);
    }
    //Trong năm
    public Double doanhThuTrongNam(){
        return hoaDonRepository.getTongDoanhThuTrong(dauNam, cuoiNam);
    }

    public Integer soLuongDaBanTrongNam(){
        return hoaDonRepository.getSoluongBanRaTrong(dauNam, cuoiNam);
    }

    public Integer soLuongHoaDonTrongNam(){
        return hoaDonRepository.soLuongHoaDonTrong(dauNam, cuoiNam);
    }

    public Page<TopSellingResponse>getTopSellingOnMonth(Pageable pageable){
        return hoaDonRepository.getTopSellingResponse(dauThang, cuoiThang, pageable);
    }

    public Page<TopSellingResponse>getSPGanHetHang(Pageable pageable){
        return hoaDonRepository.GetSanPhamSapHet(pageable);
    }

    public Map<String, Long> getOrderCounts(
            LocalDate ngayBatDau, LocalDate ngayKetThuc) {

        List<Object[]> results = hoaDonRepository.SoLuongTrangThaiDonHang(ngayBatDau, ngayKetThuc);

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
}
