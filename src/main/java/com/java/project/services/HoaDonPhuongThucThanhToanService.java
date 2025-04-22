package com.java.project.services;

import com.java.project.dtos.HoaDonPhuongThucThanhToanResponse;
import com.java.project.entities.HoaDon;
import com.java.project.entities.HoaDonPhuongThucThanhToan;
import com.java.project.entities.PhuongThucThanhToan;
import com.java.project.helper.HDPTTTHelper;
import com.java.project.repositories.HoaDonPhuongThucThanhToanRepository;
import com.java.project.repositories.HoaDonRepository;
import com.java.project.repositories.PhuongThucThanhToanRepository;
import com.java.project.request.HoaDonPhuongThucThanhToanRequest;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HoaDonPhuongThucThanhToanService {
    @Autowired
    HoaDonPhuongThucThanhToanRepository hoaDonPhuongThucThanhToanRepository;

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    PhuongThucThanhToanRepository phuongThucThanhToanRepository;


    public List<HoaDonPhuongThucThanhToanResponse>getAll(Integer idHD){
        return hoaDonPhuongThucThanhToanRepository.getAllByIdHD(idHD);
    }

    public HoaDonPhuongThucThanhToan addHDPTTT(HoaDonPhuongThucThanhToanRequest request){
        HoaDon hoaDon = request.getHoaDonId() != null
                ? hoaDonRepository.findById(request.getHoaDonId())
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy hóa đơn với id là "+ request.getHoaDonId()))
                :null;

        PhuongThucThanhToan pttt = request.getPhuongThucThanhToanId() != null
                ? phuongThucThanhToanRepository.findById(request.getPhuongThucThanhToanId())
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy phương thức thanh toán "+ request.getPhuongThucThanhToanId()))
                :null;

        HoaDonPhuongThucThanhToan hoaDonPhuongThucThanhToan = new HoaDonPhuongThucThanhToan();
        BeanUtils.copyProperties(request,hoaDonPhuongThucThanhToan);
        hoaDonPhuongThucThanhToan.setMaGiaoDich(HDPTTTHelper.createHDPTTTHelper());
        hoaDonPhuongThucThanhToan.setHoaDon(hoaDon);
        hoaDonPhuongThucThanhToan.setPhuongThucThanhToan(pttt);
        hoaDonPhuongThucThanhToan.setNgayThucHienThanhToan(LocalDate.now());
        hoaDonPhuongThucThanhToan.setNguoiXacNhan(request.getNguoiXacNhan());
        return hoaDonPhuongThucThanhToanRepository.save(hoaDonPhuongThucThanhToan);
    }
}
