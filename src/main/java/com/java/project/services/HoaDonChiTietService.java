package com.java.project.services;

import com.java.project.dtos.HoaDonChiTietResponse;
import com.java.project.dtos.HoaDonResponse;
import com.java.project.entities.HoaDon;
import com.java.project.entities.HoaDonChiTiet;
import com.java.project.entities.SanPhamChiTiet;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.exceptions.RuntimeException;
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

        BigDecimal phuPhi = hoaDon.getPhuPhi() != null ? hoaDon.getPhuPhi() : BigDecimal.ZERO;

        Optional<HoaDonChiTiet>getIsPresent = hoaDonChiTietRepository
                .findByHoaDon_IdAndSanPhamChiTiet_Id(hdctRequest.getIdHoaDon(),hdctRequest.getIdSPCT());

        if(getIsPresent.isPresent()){
            hoaDonChiTiet = getIsPresent.get();

            //Với hóa đơn online
            if(hoaDon.getLoaiDon() == 2){

                // nếu hóa đơn chi tiết chưa thanh toán
                if(hoaDonChiTiet.getTrangThai() == null || hoaDonChiTiet.getTrangThai() == 0){
                    hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + hdctRequest.getSoLuong());

                    BigDecimal donGia = sanPhamChiTiet.getDonGia();
                    BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(hdctRequest.getSoLuong()));

                    if(hoaDonChiTiet.getSoLuong() > sanPhamChiTiet.getSoLuong()) {
                        throw new IllegalArgumentException("Số lượng sản phẩm trong kho không đủ");
                    }

                    Double thanhTienMoi = hoaDonChiTiet.getThanhTien() + thanhTien.doubleValue();
                    hoaDonChiTiet.setThanhTien(thanhTienMoi);

                    if(hoaDon.getTrangThai() == 0) {
                        hoaDon.setTongTien(hoaDon.getTongTien() +thanhTien.doubleValue());
                    } else {
                        hoaDon.setPhuPhi(thanhTien);
                    }


                }else {
                    hoaDonChiTiet = new HoaDonChiTiet();
                    BeanUtils.copyProperties(hdctRequest, hoaDonChiTiet);
                    hoaDonChiTiet.setHoaDon(hoaDon);
                    hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
                    BigDecimal donGia = sanPhamChiTiet.getDonGia();
                    BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(hoaDonChiTiet.getSoLuong()));

                    hoaDonChiTiet.setThanhTien(thanhTien.doubleValue());

                    if(hoaDon.getTrangThai() == 0) {
                        hoaDon.setTongTien(hoaDon.getTongTien() +thanhTien.doubleValue());
                    } else {
                        hoaDon.setPhuPhi(hoaDon.getPhuPhi().add(thanhTien));
                    }
                }
            // với hóa đơn offline
            }else {
                hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + hdctRequest.getSoLuong());

                BigDecimal donGia = sanPhamChiTiet.getDonGia();
                BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(hdctRequest.getSoLuong()));

                hoaDonChiTiet.setThanhTien(hoaDonChiTiet.getThanhTien() + thanhTien.doubleValue());
            }

        // nếu như sản phẩm chưa có trong giỏ hàng
        }else {
            hoaDonChiTiet = new HoaDonChiTiet();
            BeanUtils.copyProperties(hdctRequest, hoaDonChiTiet);
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            BigDecimal donGia = sanPhamChiTiet.getDonGia();
            BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(hoaDonChiTiet.getSoLuong()));

            hoaDonChiTiet.setThanhTien(thanhTien.doubleValue());

            if(hoaDon.getLoaiDon() == 2) {
                if(hoaDon.getTrangThai() == 0){
                    hoaDon.setTongTien(hoaDon.getTongTien() + thanhTien.doubleValue());
                }else {
                    hoaDon.setPhuPhi(phuPhi.add(thanhTien));
                }
            }
        }

        if(hoaDon.getLoaiDon() != 2){
            hoaDonChiTiet.setTrangThai(1);
            banHangService.updateSoLuongSPCT(sanPhamChiTiet.getId(),hdctRequest.getSoLuong());
        }

        hoaDonRepository.save(hoaDon);
        return hoaDonChiTietMapper.toHoaDonChiTietResponse(hoaDonChiTietRepository.save(hoaDonChiTiet));
    }

    @Transactional
    public List<HoaDonChiTietResponse> delete (Integer idHoaDonChiTiet){

            HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(idHoaDonChiTiet)
                    .orElseThrow(() -> new EntityNotFoundException("Not found ProductDetail"));

            if(hoaDonChiTiet.getTrangThai() != null && hoaDonChiTiet.getTrangThai() == 1)
                throw new RuntimeException("Không thể xóa sản phẩm đã thanh toán");

            HoaDon hoaDon = hoaDonChiTiet.getHoaDon();

            List<HoaDonChiTiet> listUpdate =
                    hoaDonChiTietRepository.findByHoaDon_Id(hoaDon.getId());

            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();

            if(hoaDon.getLoaiDon() == 2){ // loại đơn online

                // Xử lý xoas khi hóa đơn chưa thanh toán
                if(hoaDon.getTrangThai() == 0) {

                    hoaDon.setTongTien(hoaDon.getTongTien() - hoaDonChiTiet.getThanhTien());


                }else {

                    BigDecimal phuPhiCu = hoaDon.getPhuPhi() != null ? hoaDon.getPhuPhi() : BigDecimal.ZERO;

                    hoaDon.setPhuPhi(phuPhiCu.subtract(BigDecimal.valueOf(hoaDonChiTiet.getThanhTien())));

                }
            }else {
                int soLuongMoi = sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong();
                sanPhamChiTiet.setSoLuong(soLuongMoi);
            }


            listUpdate.remove(hoaDonChiTiet);

            hoaDonRepository.save(hoaDon);
            sanPhamChiTietRepository.save(sanPhamChiTiet);
            hoaDonChiTietRepository.delete(hoaDonChiTiet);

            return listUpdate.stream()
                    .map(hoaDonChiTietMapper::toHoaDonChiTietResponse)
                    .toList();
    }

    @Transactional
    public HoaDonChiTietResponse capNhatSoLuong(Integer idHoaDonChiTiet, Integer soLuong){
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(idHoaDonChiTiet)
                .orElseThrow(() -> new EntityNotFoundException("Not found ProductDetail"));

        SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();

        if(soLuong <= 0){
            throw new IllegalArgumentException("Số lượng phải lớn hơn không");
        }

        // Kiểm tra số lượng hợp lệ khi tăng
        Integer checkSoLuongHopLe = soLuong - hoaDonChiTiet.getSoLuong();

        if(checkSoLuongHopLe > sanPhamChiTiet.getSoLuong()){
            throw new IllegalArgumentException("Số lượng vượt quá số lượng tồn");
        }

        Integer khoangThayDoi = Math.abs(hoaDonChiTiet.getSoLuong() - soLuong);
        //Số tiền lẹch
        Double soTienLech = sanPhamChiTiet.getDonGia().doubleValue() * khoangThayDoi;

        HoaDon hoaDon = hoaDonChiTiet.getHoaDon();

        BigDecimal phuPhi = hoaDon.getPhuPhi() != null ? hoaDon.getPhuPhi() : BigDecimal.ZERO;

        // Nếu giảm số lợng của sản phaarm
        if(hoaDonChiTiet.getSoLuong() > soLuong){
            hoaDonChiTiet.setThanhTien(hoaDonChiTiet.getThanhTien() - soTienLech);
        } else if (hoaDonChiTiet.getSoLuong() < soLuong){
            if(soLuong == 1){
                hoaDonChiTiet.setThanhTien(sanPhamChiTiet.getDonGia().doubleValue());
            }else {
                hoaDonChiTiet.setThanhTien(hoaDonChiTiet.getThanhTien() + soTienLech);
            }

        }

        //Xử lý khi hóa đơn là bán hàng tại quầy
        if(hoaDon.getLoaiDon() != 2){
            if(hoaDonChiTiet.getSoLuong() > soLuong){
                // Khi giảm số lượng
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + khoangThayDoi);
            } else if (hoaDonChiTiet.getSoLuong() < soLuong){
                // Khi tăn số lượng
                if(sanPhamChiTiet.getSoLuong() <= 0) {
                    throw new IllegalArgumentException("Số lượng vượt quá số lượng tồn");
                }
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - khoangThayDoi);
            }

            sanPhamChiTietRepository.save(sanPhamChiTiet);
        }else {
            if(hoaDon.getTrangThai() == 0) {
                if(hoaDonChiTiet.getSoLuong() > soLuong){
                    hoaDon.setTongTien(hoaDon.getTongTien() - soTienLech);
                }else if (hoaDonChiTiet.getSoLuong() < soLuong){
                    hoaDon.setTongTien(hoaDon.getTongTien() + soTienLech);
                }
            }else {
                if(hoaDonChiTiet.getSoLuong() > soLuong){
                    hoaDon.setPhuPhi(phuPhi.subtract(BigDecimal.valueOf(soTienLech)));
                }else if (hoaDonChiTiet.getSoLuong() < soLuong){
                    hoaDon.setPhuPhi(phuPhi.add(BigDecimal.valueOf(soTienLech)));
                }
            }
        }


        hoaDonChiTiet.setSoLuong(soLuong);

        hoaDonRepository.save(hoaDon);

        return hoaDonChiTietMapper.toHoaDonChiTietResponse(hoaDonChiTietRepository.save(hoaDonChiTiet));

    }

}
