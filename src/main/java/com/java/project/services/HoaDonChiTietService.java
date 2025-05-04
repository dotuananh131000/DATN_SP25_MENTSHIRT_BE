package com.java.project.services;

import com.java.project.dtos.HoaDonChiTietResponse;
import com.java.project.dtos.HoaDonResponse;
import com.java.project.entities.HoaDon;
import com.java.project.entities.HoaDonChiTiet;
import com.java.project.entities.SanPhamChiTiet;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.mappers.HoaDonChiTietMapper;
import com.java.project.mappers.HoaDonMapper;
import com.java.project.repositories.HoaDonChiTietRepository;
import com.java.project.repositories.HoaDonRepository;
import com.java.project.repositories.SanPhamChiTietRepository;
import com.java.project.request.HoaDonChiTietRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class HoaDonChiTietService {
    @Autowired
    HoaDonChiTietRepository hoaDonChiTietRepository;

    @Autowired
    HoaDonRepository hoaDonRepository;

    @Autowired
    SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    HoaDonChiTietMapper hoaDonChiTietMapper;

    public List<HoaDonChiTietResponse> getAllHoaDonChiTiet(Integer idHD) {
        List<HoaDonChiTietResponse> getAll = hoaDonChiTietRepository.getAllByIDHD(idHD);
        return getAll;
    }


    @Transactional
    public HoaDonChiTietResponse add(HoaDonChiTietRequest hoaDonChiTietRequest){
        HoaDon hoaDon = hoaDonRepository.findById(hoaDonChiTietRequest.getIdHoaDon())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với ID: "
                        + hoaDonChiTietRequest.getIdHoaDon()));

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(hoaDonChiTietRequest.getIdSPCT())
                .orElseThrow(() -> new EntityNotFoundException("Not found ProductDetail"
                        + hoaDonChiTietRequest.getIdSPCT()));

        Optional<HoaDonChiTiet>hoaDonChiTietOptional = hoaDonChiTietRepository
                .findByHoaDon_IdAndSanPhamChiTiet_Id(hoaDon.getId(), sanPhamChiTiet.getId());

        HoaDonChiTiet hoaDonChiTiet;

        // Cộng dồn khi sản phâ đã có trong giỏ hàng
        if(hoaDonChiTietOptional.isPresent() && hoaDonChiTietOptional.get().getTrangThai() == 0){
            hoaDonChiTiet = hoaDonChiTietOptional.get();
            hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + hoaDonChiTietRequest.getSoLuong());
            double newAddTotal = sanPhamChiTiet.getDonGia().doubleValue() *  hoaDonChiTietRequest.getSoLuong();
            hoaDonChiTiet.setThanhTien(hoaDonChiTiet.getThanhTien() + newAddTotal);
            hoaDon.setTongTien(hoaDon.getTongTien() + newAddTotal);
        }else {
            hoaDonChiTiet = new HoaDonChiTiet();
            Double thanhTien = (sanPhamChiTiet.getDonGia().doubleValue()) * hoaDonChiTietRequest.getSoLuong();

            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTiet.setSoLuong(hoaDonChiTietRequest.getSoLuong());
            hoaDonChiTiet.setThanhTien(thanhTien);
            hoaDonChiTiet.setTrangThai(0);
            hoaDon.setTongTien(hoaDon.getTongTien() + thanhTien);
        }
        // Cộng dồn phụ phí
        if(hoaDon.getTrangThai() == 1) {
            BigDecimal phiHienTai = hoaDon.getPhuPhi() == null ? BigDecimal.ZERO : hoaDon.getPhuPhi();
            hoaDon.setPhuPhi(phiHienTai.add(
                    sanPhamChiTiet.getDonGia().multiply(BigDecimal.valueOf(hoaDonChiTietRequest.getSoLuong()))
            ));
        }

        hoaDonRepository.save(hoaDon);
        return hoaDonChiTietMapper.toHoaDonChiTietResponse(hoaDonChiTietRepository.save(hoaDonChiTiet));

    }

    @Transactional
    public List<HoaDonChiTietResponse> delete (Integer idHoaDonChiTiet){

            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(idHoaDonChiTiet)
                    .orElseThrow(() -> new EntityNotFoundException("Not found ProductDetail"));

            HoaDon hoaDon = hoaDonChiTiet.getHoaDon();

            List<HoaDonChiTiet> listUpdate =
                    hoaDonChiTietRepository.findByHoaDon_Id(hoaDon.getId());

            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();

            if(hoaDonChiTiet.getTrangThai() != null && hoaDonChiTiet.getTrangThai() == 1) {
                int soLuongMoi = sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong();
                sanPhamChiTiet.setSoLuong(soLuongMoi);
                hoaDon.setPhuPhi(hoaDon.getPhuPhi().subtract(BigDecimal.valueOf(hoaDonChiTiet.getThanhTien())));
            }

            hoaDon.setTongTien(hoaDon.getTongTien() - hoaDonChiTiet.getThanhTien());
            listUpdate.remove(hoaDonChiTiet);

            hoaDonRepository.save(hoaDon);
            sanPhamChiTietRepository.save(sanPhamChiTiet);
            hoaDonChiTietRepository.delete(hoaDonChiTiet);

            return listUpdate.stream()
                    .map(hoaDonChiTietMapper::toHoaDonChiTietResponse)
                    .toList();
    }

}
