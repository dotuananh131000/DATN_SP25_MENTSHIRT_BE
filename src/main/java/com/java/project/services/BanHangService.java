package com.java.project.services;

import com.java.project.dtos.HoaDonChiTietResponse;
import com.java.project.dtos.PhieuGiamGiaKhachHangResponse;
import com.java.project.dtos.PhieuGiamGiaResponse;
import com.java.project.dtos.SanPhamChiTietResponse;
import com.java.project.entities.*;
import com.java.project.exceptions.RuntimeException;
import com.java.project.helper.HoaDonHelper;
import com.java.project.mappers.HoaDonChiTietMapper;
import com.java.project.repositories.*;
import com.java.project.request.HoaDonChiTietRequest;
import com.java.project.request.HoaDonRequest;
import com.java.project.request.ThongTinDonHangRequest;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BanHangService {

    @Autowired
    HoaDonRepository hoaDonRepository;
    @Autowired
    KhachHangRepository khachHangRepository;
    @Autowired
    NhanVienRepository nhanVienRepository;
    @Autowired
    PhieuGiamGiaRepository phieuGiamGiaRepository;
    @Autowired
    PhieuGiamGiaKhachHangRepository phieuGiamGiaKhachHangRepository;
    @Autowired
    private HoaDonChiTietRepository hoaDonChiTietRepository;
    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private HoaDonChiTietMapper hoaDonChiTietMapper;

    public HoaDon createHoaDon(HoaDonRequest hoaDonRequest){
        KhachHang khachHang = hoaDonRequest.getIdKhachHang() != null
                ? khachHangRepository.findById(hoaDonRequest.getIdKhachHang())
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy khách hàng với id "+ hoaDonRequest.getIdKhachHang()))
                :null;

        NhanVien nhanVien = hoaDonRequest.getIdNhanVien() != null
                ? nhanVienRepository.findById(hoaDonRequest.getIdNhanVien())
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy nhân viên với id "+ hoaDonRequest.getIdNhanVien()))
                :null;

        PhieuGiamGia phieuGiamGia = hoaDonRequest.getIdPhieuGiamGia() != null
                ? phieuGiamGiaRepository.findById(hoaDonRequest.getIdPhieuGiamGia())
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy phiếu giảm giá với id "+ hoaDonRequest.getIdPhieuGiamGia()))
                :null;

        HoaDon hoaDon = new HoaDon();
        BeanUtils.copyProperties(hoaDonRequest, hoaDon);
        hoaDon.setKhachHang(khachHang);
        hoaDon.setNhanVien(nhanVien);
        hoaDon.setPhieuGiamGia(phieuGiamGia);
        hoaDon.setMaHoaDon(HoaDonHelper.createHoaDonHelper());
        hoaDon.setLoaiDon(1);
        hoaDon.setTrangThaiGiaoHang(8);
        hoaDon.setNgayTao(LocalDateTime.now());
        hoaDon.setTrangThai(0);

        return hoaDonRepository.save(hoaDon);
    }

    public HoaDon updateKHOfHoaDon(Integer idHD, Integer idKH){
        KhachHang khachHang = khachHangRepository.findById(idKH)
                .orElseThrow(()->new EntityNotFoundException("Không tìm thấy khách hàng với Id: " + idKH));
        HoaDon hoaDon = hoaDonRepository.findById(idHD).get();
        hoaDon.setKhachHang(khachHang);
        return hoaDonRepository.save(hoaDon);
    }

    public HoaDon upDateTrangThaiHoaDon(Integer idHD){
        HoaDon hoaDon = hoaDonRepository.findById(idHD).get();
        hoaDon.setTrangThai(1);
        if(hoaDon.getLoaiDon() ==1){
            hoaDon.setTrangThaiGiaoHang(9);
        }

        return hoaDonRepository.save(hoaDon);
    }

    public HoaDon upDateLoaiDonOnline(Integer idHD){
        HoaDon hoaDon = hoaDonRepository.findById(idHD).get();
        hoaDon.setLoaiDon(0);
        return hoaDonRepository.save(hoaDon);
    }

    public HoaDon upDateLoaiDonOffline(Integer idHD){
        HoaDon hoaDon = hoaDonRepository.findById(idHD).get();
        hoaDon.setLoaiDon(1);
        return hoaDonRepository.save(hoaDon);
    }

    public HoaDon updateTrangThaiDonHang(Integer idHD){

        if(idHD == null){
            throw new IllegalArgumentException("Không tìm thấy id");
        }
        HoaDon hoaDon = hoaDonRepository.findById(idHD)
                .orElseThrow(()-> new EntityNotFoundException("Không timg thấy id hóa đơn"+ idHD));

        if(hoaDon.getTrangThaiGiaoHang() == 8){
            hoaDon.setTrangThai(1);
            hoaDon.setTrangThaiGiaoHang(2);
            mailService.sendBillStatus(hoaDon.getHoTenNguoiNhan(), hoaDon.getEmail(),hoaDon.getMaHoaDon()
            ,"Đang chờ xác nhận");

            return hoaDonRepository.save(hoaDon); //Từ tạo hóa đơn sang chờ chờ xác nhận

        }else if(hoaDon.getTrangThaiGiaoHang() == 1){
            hoaDon.setTrangThaiGiaoHang(2);

            mailService.sendBillStatus(hoaDon.getHoTenNguoiNhan(), hoaDon.getEmail(),hoaDon.getMaHoaDon()
                    ,"Đã xác nhận");

            subtractNumberOfProduct(hoaDon.getId());
            
            return hoaDonRepository.save(hoaDon); //Từ chờ xác nhận, sang xác nhận
        }else if(hoaDon.getTrangThaiGiaoHang() == 2){
            hoaDon.setTrangThaiGiaoHang(3);
            mailService.sendBillStatus(hoaDon.getHoTenNguoiNhan(), hoaDon.getEmail(),hoaDon.getMaHoaDon()
                    ,"Đang chờ vận chuyển");

            return hoaDonRepository.save(hoaDon); //Từ xác nhận, sang chờ vận chuyển

        }else if(hoaDon.getTrangThaiGiaoHang() == 3){
            hoaDon.setTrangThaiGiaoHang(4);
            mailService.sendBillStatus(hoaDon.getHoTenNguoiNhan(), hoaDon.getEmail(),hoaDon.getMaHoaDon()
                    ,"Đã được vận chuyển");

            return hoaDonRepository.save(hoaDon); //Từ chờ vận chuyển, sang vận chuyển

        }else if(hoaDon.getTrangThaiGiaoHang() == 4){
            hoaDon.setTrangThaiGiaoHang(5);
//            hoaDon.setTrangThai(1);
            mailService.sendBillStatus(hoaDon.getHoTenNguoiNhan(), hoaDon.getEmail(),hoaDon.getMaHoaDon()
                    ,"Đơn hàng của bạn đã thành công");

            return hoaDonRepository.save(hoaDon); //Từ vận chuyển sang thành công
        }
        return hoaDonRepository.save(hoaDon);
    }

    private void subtractNumberOfProduct(Integer idHD){
        if(idHD == null)
            throw new RuntimeException("Order not found.");

        List<HoaDonChiTietResponse>ListProductDetail = hoaDonChiTietRepository.getAllByIDHD(idHD);

        if(ListProductDetail.size() > 0) {
            for (HoaDonChiTietResponse hdct : ListProductDetail) {
                if(hdct.getTrangThai() == 1) continue;

                Optional<SanPhamChiTiet> spctOptional = sanPhamChiTietRepository.findById(hdct.getIdSPCT());
                if(spctOptional.isPresent()){
                    SanPhamChiTiet spct = spctOptional.get();
                    int newQuantity = spct.getSoLuong() - hdct.getSoLuong();

                    if(newQuantity >= 0){
                        spct.setSoLuong(newQuantity);
                        sanPhamChiTietRepository.save(spct);
                    }else {
                        throw new IllegalArgumentException("Sản phẩm ID: " + hdct.getIdSPCT() + " trong kho không đủ");
                    }
                }
            }
        }

    }

    public HoaDonChiTiet reloadSPGioHang(Integer id){

        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Không tìm thấy hóa đơn chi tiết với Id"+ id));
        BigDecimal donGia = hoaDonChiTiet.getSanPhamChiTiet().getDonGia();
        BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(hoaDonChiTiet.getSoLuong()));

        hoaDonChiTiet.setThanhTien(thanhTien.doubleValue());
        return hoaDonChiTietRepository.save(hoaDonChiTiet);
    }

    public HoaDonChiTietResponse AddOrUpdateHoaDonChiTiet(HoaDonChiTietRequest hdctRequest){
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
           updateSoLuongSPCT(sanPhamChiTiet.getId(),hdctRequest.getSoLuong());
        }

        hoaDonRepository.save(hoaDon);
        return hoaDonChiTietMapper.toHoaDonChiTietResponse(hoaDonChiTietRepository.save(hoaDonChiTiet));
    }

    public void updateSoLuongSPCT(Integer idSPCT, Integer soLuongMua){
            SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(idSPCT)
                    .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy idSPct"));
            Integer soLuongCu = sanPhamChiTiet.getSoLuong();
            Integer soLuongCon = soLuongMua != null ? soLuongCu - soLuongMua : soLuongCu;

            if(soLuongCon <= 0){
                sanPhamChiTiet.setTrangThai(false);
            }else {
                sanPhamChiTiet.setTrangThai(true);
            }

            sanPhamChiTiet.setSoLuong(soLuongCon);

            sanPhamChiTietRepository.save(sanPhamChiTiet);
    }

//    public void updateTongTienHoaDon(Integer idHoaDon){
//        List<HoaDonChiTiet>listHDCTByIdHD = hoaDonChiTietRepository.findByHoaDon_Id(idHoaDon);
//
//        BigDecimal tongTien = listHDCTByIdHD.stream()
//                .map(hdct -> BigDecimal.valueOf(hdct.getThanhTien()))
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//        HoaDon hoaDon =  hoaDonRepository.findById(idHoaDon).orElseThrow();
//        hoaDon.setTongTien(tongTien.doubleValue());
//
//        hoaDonRepository.save(hoaDon);
//    }

    public HoaDonChiTietResponse updateQuatityHDCT(Integer id, Integer newQuantity){
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id)
                .orElseThrow(() -> new com.java.project.exceptions.EntityNotFoundException("Not found ProductDetail"));

        SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();

        if(newQuantity <= 0){
            throw new IllegalArgumentException("Số lượng phải lớn hơn không");
        }

        // Kiểm tra số lượng hợp lệ khi tăng
        Integer checkSoLuongHopLe = newQuantity - hoaDonChiTiet.getSoLuong();

        if(checkSoLuongHopLe > sanPhamChiTiet.getSoLuong()){
            throw new IllegalArgumentException("Số lượng vượt quá số lượng tồn");
        }

        Integer khoangThayDoi = Math.abs(hoaDonChiTiet.getSoLuong() - newQuantity);
        //Số tiền lẹch
        Double soTienLech = sanPhamChiTiet.getDonGia().doubleValue() * khoangThayDoi;

        HoaDon hoaDon = hoaDonChiTiet.getHoaDon();

        BigDecimal phuPhi = hoaDon.getPhuPhi() != null ? hoaDon.getPhuPhi() : BigDecimal.ZERO;

        // Nếu giảm số lợng của sản phaarm
        if(hoaDonChiTiet.getSoLuong() > newQuantity){
            hoaDonChiTiet.setThanhTien(hoaDonChiTiet.getThanhTien() - soTienLech);
        } else if (hoaDonChiTiet.getSoLuong() < newQuantity){
            if(newQuantity == 1){
                hoaDonChiTiet.setThanhTien(sanPhamChiTiet.getDonGia().doubleValue());
            }else {
                hoaDonChiTiet.setThanhTien(hoaDonChiTiet.getThanhTien() + soTienLech);
            }

        }

        //Xử lý khi hóa đơn là bán hàng tại quầy
        if(hoaDon.getLoaiDon() != 2){
            if(hoaDonChiTiet.getSoLuong() > newQuantity){
                // Khi giảm số lượng
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() + khoangThayDoi);
            } else if (hoaDonChiTiet.getSoLuong() < newQuantity){
                // Khi tăn số lượng
                if(sanPhamChiTiet.getSoLuong() <= 0) {
                    throw new IllegalArgumentException("Số lượng vượt quá số lượng tồn");
                }
                sanPhamChiTiet.setSoLuong(sanPhamChiTiet.getSoLuong() - khoangThayDoi);
            }

            sanPhamChiTietRepository.save(sanPhamChiTiet);
        }else {
            if(hoaDon.getTrangThai() == 0) {
                if(hoaDonChiTiet.getSoLuong() > newQuantity){
                    hoaDon.setTongTien(hoaDon.getTongTien() - soTienLech);
                }else if (hoaDonChiTiet.getSoLuong() < newQuantity){
                    hoaDon.setTongTien(hoaDon.getTongTien() + soTienLech);
                }
            }else {
                if(hoaDonChiTiet.getSoLuong() > newQuantity){
                    hoaDon.setPhuPhi(phuPhi.subtract(BigDecimal.valueOf(soTienLech)));
                }else if (hoaDonChiTiet.getSoLuong() < newQuantity){
                    hoaDon.setPhuPhi(phuPhi.add(BigDecimal.valueOf(soTienLech)));
                }
            }
        }


        hoaDonChiTiet.setSoLuong(newQuantity);

        hoaDonRepository.save(hoaDon);

        return hoaDonChiTietMapper.toHoaDonChiTietResponse(hoaDonChiTietRepository.save(hoaDonChiTiet));

    }
    @Transactional
    public List<HoaDonChiTietResponse>  deleteHoaDonChiTiet(Integer id){
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id)
                .orElseThrow(() -> new com.java.project.exceptions.EntityNotFoundException("Not found ProductDetail"));

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

    public Map<Integer, Long>getHoaDonChiTietCount(){
        List<Object[]>result = hoaDonChiTietRepository.getHoaDonChiTietCount();
        Map<Integer, Long>hoaDonChiTietMap = new HashMap<>();
        for (Object[] row : result){
            Integer hoaDonId = (Integer) row[0];
            Long soLuong = (Long) row[1];
            hoaDonChiTietMap.put(hoaDonId, soLuong);
        }
        return hoaDonChiTietMap;
    }
    public HoaDon autoUpdatePGGTotNhat(Integer id){
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy hóa đơn với id"+ id));

        Integer idKhachHang = hoaDon.getKhachHang() != null ? hoaDon.getKhachHang().getId() : null;
        Integer idPhieuGiamGia = filterPhieuGiamGiaTotNhat(hoaDon.getTongTien(), idKhachHang);
        PhieuGiamGia phieuGiamGia = null;
        if(hoaDon.getTongTien() != null ){
            phieuGiamGia = (idPhieuGiamGia != null)
                    ? phieuGiamGiaRepository.findById(idPhieuGiamGia)
                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy phiếu giảm giá"))
                    : null;
        }
        if(hoaDon.getPhieuGiamGia() != null ){
            updateSoLuongPhieuGiamGiaKhiKoSD(hoaDon.getPhieuGiamGia().getId());
        }
       if(idPhieuGiamGia != null){
           updateSoLuongPhieuGiamGiaKhiSD(idPhieuGiamGia);
       }
        hoaDon.setPhieuGiamGia(phieuGiamGia);
        return hoaDonRepository.save(hoaDon);
    }

    @Transactional
    public HoaDon choosePhieuGiamGia(Integer idHD, Integer idPGG){
        HoaDon hoaDon = hoaDonRepository.findById(idHD)
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy hóa đơn với id"+ idHD));

        //Hóa đơn được chọn để thay đổi
        PhieuGiamGia phieuGiamGia = phieuGiamGiaRepository.findById(idPGG)
                .orElseThrow(() -> new EntityNotFoundException(("Không tìm thấy phiếu giảm giá với Id là: " + idPGG)));

        Integer idPGGPrev = hoaDon.getPhieuGiamGia() != null ? hoaDon.getPhieuGiamGia().getId():null;
        if(idPGGPrev != null){
            updateSoLuongPhieuGiamGiaKhiKoSD(hoaDon.getPhieuGiamGia().getId());
        }
        updateSoLuongPhieuGiamGiaKhiSD(phieuGiamGia.getId());


        hoaDon.setPhieuGiamGia(phieuGiamGia);
        return hoaDonRepository.save(hoaDon);
    }

    private void updateSoLuongPhieuGiamGiaKhiSD(Integer idPGG){
        if(idPGG != null){
        PhieuGiamGia pgg = phieuGiamGiaRepository.findById(idPGG)
                .orElseThrow(()-> new EntityNotFoundException("Không thể tìm thấy phiếu giảm giá với id"+ idPGG));
        pgg.setSoLuong(pgg.getSoLuong()-1);
        }
    }
    private void updateSoLuongPhieuGiamGiaKhiKoSD(Integer idPGG){
        if(idPGG != null){
            PhieuGiamGia pgg = phieuGiamGiaRepository.findById(idPGG)
                    .orElseThrow(()-> new EntityNotFoundException("Không thể tìm thấy phiếu giảm giá với id"+ idPGG));
            pgg.setSoLuong(pgg.getSoLuong()+1);
        }

    }

    private Integer filterPhieuGiamGiaTotNhat(Double tongTien, Integer idKH) {
        Integer idPhieuGiamGiaTotNhat = null;
        Double giamGia = 0.0;

        // Lấy danh sách phiếu giảm giá cá nhân nếu có idKH
        List<PhieuGiamGiaKhachHangResponse> listPGGCaNhan = (idKH != null) ? phieuGiamGiaKhachHangRepository.phieuGiamGiaKhachHang(idKH) : new ArrayList<>();

        // Lấy danh sách tất cả phiếu giảm giá công khai
        List<PhieuGiamGia> listPhieuGiamGia = phieuGiamGiaRepository.getAll();

        // Kiểm tra và lọc phiếu giảm giá cá nhân
        for (PhieuGiamGiaKhachHangResponse pggkh : listPGGCaNhan) {
            Double discount = calculateDiscount(tongTien, pggkh.getSoTienToiThieu(), pggkh.getHinhThucGiamGia(), pggkh.getGiaTriGiam(), pggkh.getSoTienGiamToiDa());
            // Kiểm tra nếu discount lớn hơn giá trị giảm hiện tại
            if (discount > giamGia) {
                giamGia = discount;
                idPhieuGiamGiaTotNhat = pggkh.getIdPGG(); // Lấy id phiếu giảm giá
            }
        }

        // Kiểm tra và lọc phiếu giảm giá công khai
        for (PhieuGiamGia pgg : listPhieuGiamGia) {
            Double discount = calculateDiscount(tongTien, pgg.getSoTienToiThieuHd(), pgg.getHinhThucGiamGia(), pgg.getGiaTriGiam(), pgg.getSoTienGiamToiDa());
            // Kiểm tra nếu discount lớn hơn giá trị giảm hiện tại
            if (discount > giamGia) {
                giamGia = discount;
                idPhieuGiamGiaTotNhat = pgg.getId(); // Lấy id phiếu giảm giá
            }
        }

        return idPhieuGiamGiaTotNhat;
    }

    private Double calculateDiscount(Double tongTien, Double soTienToiThieu, Integer hinhThucGiamGia, Double giaTriGiam, Double soTienToiDa) {
        if (tongTien >= soTienToiThieu) {
            if (hinhThucGiamGia == 1) {
                // Giảm giá cố định
                return Math.min(giaTriGiam, soTienToiDa);  // Không vượt quá soTienToiDa
            } else {
                // Giảm giá theo phần trăm
                Double soTienGiam = tongTien * giaTriGiam / 100;
                return Math.min(soTienGiam, soTienToiDa);  // Không vượt quá soTienToiDa
            }
        }
        return 0.0;  // Không đủ điều kiện giảm giá
    }

    public HoaDon deleteKhachHangOfHoaDon(Integer idHoaDon){
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(()->new EntityNotFoundException("Không thể tìm thấy hóa đơn với Id là:"+ idHoaDon));
        hoaDon.setKhachHang(null);
        hoaDon.setLoaiDon(1);
        return hoaDonRepository.save(hoaDon);
    }

    public Map<Integer, BigDecimal>getMapGiaSanPham(){
        List<SanPhamChiTiet>listSPCT = sanPhamChiTietRepository.findAll();
        Map<Integer, BigDecimal>mapGiaSanPham = new HashMap<>();

        for(SanPhamChiTiet spct : listSPCT){
            mapGiaSanPham.put(spct.getId(), spct.getDonGia());
        }
        return mapGiaSanPham;
    }

    public HoaDon updateThongTinDonHang(Integer id, ThongTinDonHangRequest request){
        HoaDon hoaDon = hoaDonRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy hóa đơn với id"+ id));

        hoaDon.setHoTenNguoiNhan(request.getHoTenNguoiNhan());
        hoaDon.setSoDienThoai(request.getSdt());
        hoaDon.setEmail(request.getEmail());
        hoaDon.setDiaChiNhanHang(request.getDiaChiNhanHang());
        hoaDon.setPhiShip(request.getPhiShip());
        return hoaDonRepository.save(hoaDon);
    }

}
