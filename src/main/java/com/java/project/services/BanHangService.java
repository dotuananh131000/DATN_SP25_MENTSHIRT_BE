package com.java.project.services;

import com.java.project.dtos.HoaDonChiTietResponse;
import com.java.project.dtos.PhieuGiamGiaKhachHangResponse;
import com.java.project.dtos.PhieuGiamGiaResponse;
import com.java.project.dtos.SanPhamChiTietResponse;
import com.java.project.entities.*;
import com.java.project.helper.HoaDonHelper;
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
            if(hoaDon.getTrangThai() == 0){
                subtractNumberOfProduct(hoaDon.getId());
            }
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
        if(idHD != null){
            List<HoaDonChiTietResponse>ListProDuctDetail = hoaDonChiTietRepository.getAllByIDHD(idHD);
            if(ListProDuctDetail.size() > 0){
                for(HoaDonChiTietResponse hdct : ListProDuctDetail){
                    Optional<SanPhamChiTiet> spctOptional = sanPhamChiTietRepository.findById(hdct.getIdSPCT());
                    if(spctOptional.isPresent()){
                        SanPhamChiTiet spct = spctOptional.get();
                        int newQuantity = spct.getSoLuong() - hdct.getSoLuong();

                        if(newQuantity >= 0){
                            spct.setSoLuong(newQuantity);
                            sanPhamChiTietRepository.save(spct);
                        }else {
                            throw new IllegalArgumentException("Sản phẩm trong kho không đủ");
                        }
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

    public HoaDonChiTiet AddOrUpdateHoaDonChiTiet(HoaDonChiTietRequest hdctRequest){
        HoaDon hoaDon = hdctRequest.getIdHoaDon() != null
                ?hoaDonRepository.findById(hdctRequest.getIdHoaDon())
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy hóa đơn với id:"+ hdctRequest.getIdHoaDon()))
                : null;
        SanPhamChiTiet sanPhamChiTiet = hdctRequest.getIdSPCT() != null
                ?sanPhamChiTietRepository.findById(hdctRequest.getIdSPCT())
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy sản phẩm chi tiết với id:"+ hdctRequest.getIdSPCT()))
                :null;
        HoaDonChiTiet hoaDonChiTiet ;

        Optional<HoaDonChiTiet>getIsPresent = hoaDonChiTietRepository
                .findByHoaDon_IdAndSanPhamChiTiet_Id(hdctRequest.getIdHoaDon(),hdctRequest.getIdSPCT());

        if(getIsPresent.isPresent()){
            hoaDonChiTiet = getIsPresent.get();
            hoaDonChiTiet.setSoLuong(hoaDonChiTiet.getSoLuong() + hdctRequest.getSoLuong());
        }else {
            hoaDonChiTiet = new HoaDonChiTiet();
            BeanUtils.copyProperties(hdctRequest, hoaDonChiTiet);
            hoaDonChiTiet.setHoaDon(hoaDon);
            hoaDonChiTiet.setSanPhamChiTiet(sanPhamChiTiet);
            hoaDonChiTiet.setTrangThai(1);
        }

        if(hdctRequest.getSoLuong() != null && sanPhamChiTiet != null){
            BigDecimal donGia = sanPhamChiTiet.getDonGia();
            BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(hoaDonChiTiet.getSoLuong()));

            hoaDonChiTiet.setThanhTien(thanhTien.doubleValue());
        }else {
            throw  new IllegalArgumentException("Số lượng sản phẩm không hợp lệ");
        }
        updateSoLuongSPCT(sanPhamChiTiet.getId(),hdctRequest.getSoLuong());
        return hoaDonChiTietRepository.save(hoaDonChiTiet);
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

    public void updateTongTienHoaDon(Integer idHoaDon){
        List<HoaDonChiTiet>listHDCTByIdHD = hoaDonChiTietRepository.findByHoaDon_Id(idHoaDon);

        BigDecimal tongTien = listHDCTByIdHD.stream()
                .map(hdct -> BigDecimal.valueOf(hdct.getThanhTien()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        HoaDon hoaDon =  hoaDonRepository.findById(idHoaDon).orElseThrow();
        hoaDon.setTongTien(tongTien.doubleValue());
        hoaDonRepository.save(hoaDon);
    }

    public HoaDonChiTiet updateQuatityHDCT(Integer id, Integer newQuantity){
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id).
                orElseThrow(()->new EntityNotFoundException("Không tìm thấy hóa đơn chi tiết với id " + id));

        if(hoaDonChiTiet.getSoLuong() != newQuantity){
            Integer khoangSoLuongThayDoi = Math.abs(hoaDonChiTiet.getSoLuong() - newQuantity);
            if(hoaDonChiTiet.getSoLuong() > newQuantity){
                updateSoLuongSPCT(hoaDonChiTiet.getSanPhamChiTiet().getId(), - khoangSoLuongThayDoi);
            }else {
                updateSoLuongSPCT(hoaDonChiTiet.getSanPhamChiTiet().getId(), khoangSoLuongThayDoi);
            }

            hoaDonChiTiet.setSoLuong(newQuantity);
            BigDecimal donGia = hoaDonChiTiet.getSanPhamChiTiet().getDonGia();
            BigDecimal thanhTien = donGia.multiply(BigDecimal.valueOf(newQuantity));

            hoaDonChiTiet.setThanhTien(thanhTien.doubleValue());
            updateTongTienHoaDon(hoaDonChiTiet.getHoaDon().getId());

            return hoaDonChiTietRepository.save(hoaDonChiTiet);
        }

        return hoaDonChiTiet;
    }
    @Transactional
    public void deleteHoaDonChiTiet(Integer id){
        HoaDonChiTiet hoaDonChiTiet = hoaDonChiTietRepository.findById(id)
                        .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy hóa đơn chi tiết với id: "+ id));

        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(hoaDonChiTiet.getSanPhamChiTiet().getId())
                .orElseThrow(()->new EntityNotFoundException("Không thể tìm thấy spct"));
        Integer soLuongTraVe = hoaDonChiTiet.getSoLuong();
        if(soLuongTraVe != 0){
            updateSoLuongSPCT(sanPhamChiTiet.getId(), - soLuongTraVe);
        }

        hoaDonChiTietRepository.deleteById(id);
        updateTongTienHoaDon(hoaDonChiTiet.getHoaDon().getId());
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
