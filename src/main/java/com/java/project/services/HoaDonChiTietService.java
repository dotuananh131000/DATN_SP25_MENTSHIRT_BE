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
import org.springframework.beans.BeanUtils;
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

    @Autowired
    BanHangService banHangService;

    public List<HoaDonChiTietResponse> getAllHoaDonChiTiet(Integer idHD) {
        List<HoaDonChiTietResponse> getAll = hoaDonChiTietRepository.getAllByIDHD(idHD);
        return getAll;
    }


    @Transactional
    public HoaDonChiTietResponse add(HoaDonChiTietRequest hdctRequest){
        HoaDon hoaDon = hdctRequest.getIdHoaDon() != null
                ?hoaDonRepository.findById(hdctRequest.getIdHoaDon())
                .orElseThrow(()-> new jakarta.persistence.EntityNotFoundException("Không tìm thấy hóa đơn với id:"+ hdctRequest.getIdHoaDon()))
                : null;
        SanPhamChiTiet sanPhamChiTiet = hdctRequest.getIdSPCT() != null
                ?sanPhamChiTietRepository.findById(hdctRequest.getIdSPCT())
                .orElseThrow(()-> new jakarta.persistence.EntityNotFoundException("Không tìm thấy sản phẩm chi tiết với id:"+ hdctRequest.getIdSPCT()))
                :null;
        HoaDonChiTiet hoaDonChiTiet ;

        Optional<HoaDonChiTiet>getIsPresent = hoaDonChiTietRepository
                .findByHoaDon_IdAndSanPhamChiTiet_Id(hdctRequest.getIdHoaDon(),hdctRequest.getIdSPCT());

        Double tongTienHoaDon = hoaDon.getTongTien() != null ? hoaDon.getTongTien() : 0.0;

        if(getIsPresent.isPresent()){
            hoaDonChiTiet = getIsPresent.get();
            if(hoaDon.getLoaiDon() == 2){

                //ếu hóa đơn chi tiết đã thanh toán
                if(hoaDonChiTiet.getTrangThai() == 0){
                    hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + hdctRequest.getSoLuong());

                    BigDecimal donGia = sanPhamChiTiet.getDonGia();
                    BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(hdctRequest.getSoLuong()));

                    Double thanhTienMoi = hoaDonChiTiet.getThanhTien() + thanhTien.doubleValue();
                    hoaDonChiTiet.setThanhTien(thanhTienMoi);
                    hoaDon.setTongTien(tongTienHoaDon + thanhTien.doubleValue());
                }else {
                    hoaDonChiTiet = new HoaDonChiTiet();
                    BeanUtils.copyProperties(hdctRequest, hoaDonChiTiet);
                    hoaDonChiTiet.setHoaDon(hoaDon);
                    hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
                    BigDecimal donGia = sanPhamChiTiet.getDonGia();
                    BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(hoaDonChiTiet.getSoLuong()));

                    hoaDonChiTiet.setThanhTien(thanhTien.doubleValue());
                    hoaDon.setTongTien(tongTienHoaDon + thanhTien.doubleValue());
                }
            }else {
                hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + hdctRequest.getSoLuong());

                BigDecimal donGia = sanPhamChiTiet.getDonGia();
                BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(hdctRequest.getSoLuong()));

                hoaDonChiTiet.setThanhTien(hoaDonChiTiet.getThanhTien() + thanhTien.doubleValue());
                hoaDon.setTongTien(tongTienHoaDon + thanhTien.doubleValue());
            }

        }else {
            hoaDonChiTiet = new HoaDonChiTiet();
            BeanUtils.copyProperties(hdctRequest, hoaDonChiTiet);
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            BigDecimal donGia = sanPhamChiTiet.getDonGia();
            BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(hoaDonChiTiet.getSoLuong()));

            hoaDonChiTiet.setThanhTien(thanhTien.doubleValue());
            hoaDon.setTongTien(tongTienHoaDon + thanhTien.doubleValue());
        }

        if(hoaDon.getLoaiDon() != 2){
            hoaDonChiTiet.setTrangThai(1);
            banHangService.updateSoLuongSPCT(sanPhamChiTiet.getId(),hdctRequest.getSoLuong());
        }else {
            //Tổng tiền ộng thêm
            if(hoaDon.getTrangThai() == 1){
                BigDecimal thanhTien =
                        hoaDonChiTiet.getSanPhamChiTiet().getDonGia().multiply(BigDecimal.valueOf(hdctRequest.getSoLuong()));
                hoaDon.setPhuPhi(hoaDon.getPhuPhi().add(thanhTien));

            }

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

            if(hoaDon.getLoaiDon() == 2){ // loại đơn online
                if(hoaDonChiTiet.getTrangThai() != null && hoaDonChiTiet.getTrangThai() == 1) {
                    int soLuongMoi = sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong();
                    sanPhamChiTiet.setSoLuong(soLuongMoi);

                    BigDecimal phuPhiCu = hoaDon.getPhuPhi() != null ? hoaDon.getPhuPhi() : BigDecimal.ZERO;

                    hoaDon.setPhuPhi(phuPhiCu.subtract(BigDecimal.valueOf(hoaDonChiTiet.getThanhTien())));

                }
            }else {
                int soLuongMoi = sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong();
                sanPhamChiTiet.setSoLuong(soLuongMoi);
            }


            Double tongTienPre = hoaDon.getTongTien() != null ? hoaDon.getTongTien() : 0.0;
            hoaDon.setTongTien(tongTienPre - hoaDonChiTiet.getThanhTien());
            listUpdate.remove(hoaDonChiTiet);

            hoaDonRepository.save(hoaDon);
            sanPhamChiTietRepository.save(sanPhamChiTiet);
            hoaDonChiTietRepository.delete(hoaDonChiTiet);

            return listUpdate.stream()
                    .map(hoaDonChiTietMapper::toHoaDonChiTietResponse)
                    .toList();
    }

}
