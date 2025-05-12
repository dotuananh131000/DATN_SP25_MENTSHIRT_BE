package com.java.project.services;

import com.java.project.dtos.HoaDonBanHangResponse;
import com.java.project.dtos.HoaDonHomNayResponse;
import com.java.project.dtos.HoaDonResponse;
import com.java.project.entities.*;
import com.java.project.helper.HoaDonHelper;
import com.java.project.mappers.HoaDonMapper;
import com.java.project.repositories.*;
import com.java.project.request.ConfirmHoaDonRequest;
import com.java.project.request.ThongTinDonHangRequest;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class HoaDonService {
    HoaDonRepository hoaDonRepository;

    NhanVienRepository nhanVienRepository;

    HoaDonMapper hoaDonMapper;

    HoaDonChiTietRepository hoaDonChiTietRepository;

    SanPhamChiTietRepository sanPhamChiTietRepository;

    KhachHangRepository khachHangRepository;

    public HoaDonResponse add(Integer idNhanVien) {

        NhanVien nhanVien = nhanVienRepository.findById(idNhanVien)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấynhaann viên với id là " + idNhanVien));

        HoaDon hoaDon = new HoaDon();
        hoaDon.setNhanVien(nhanVien);
        hoaDon.setMaHoaDon(HoaDonHelper.createHoaDonHelper());
        hoaDon.setLoaiDon(1); // Default hóa đơn tại quầy
        hoaDon.setTrangThaiGiaoHang(8); // Default hóa đơn đang chờ
        hoaDon.setNgayTao(LocalDateTime.now());
        hoaDon.setTrangThai(0); // Default hóa đơn chưa thanh toán

        return hoaDonMapper.toHoaDonResponse(hoaDonRepository.save(hoaDon));
     }


    public Page<HoaDonResponse>getHoaDonList(Pageable pageable,
                                             LocalDate ngayBatDau,
                                             LocalDate ngayKetThuc,
                                             String keyword,
                                             Integer loaiDon,
                                             Integer trangThaiGiaoHang) {
        return hoaDonRepository.getListHoaDon(pageable,ngayBatDau, ngayKetThuc,keyword,loaiDon,trangThaiGiaoHang)
                .map(hoaDonMapper::toHoaDonResponse);
    }

    public HoaDonResponse getHoaDonById(Integer id) {
        var HoaDon = hoaDonRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy hóa đơn"));
        return hoaDonMapper.toHoaDonResponse(HoaDon) ;
    }

    public Page<HoaDonResponse>getHoaDonByIdKhachHang(Pageable pageable, Integer id, String keyword, Integer trangThaiGiaoHang){
        return hoaDonRepository.getListHoaDonByIdKH(pageable, id, keyword, trangThaiGiaoHang)
                .map(hoaDonMapper::toHoaDonResponse);
    }

    public List<Object>getHoaDonCho(){
        List<Object>list = new ArrayList<>();

        for (Object[] obj : hoaDonRepository.getHoaDonCho()){
            Map<String, Object> map = new HashMap<>();
            map.put("id", obj[0]);
            map.put("soLuong", obj[1]);
            list.add(map);
        }
        return list;
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

    public HoaDonResponse getHoaDonByMaHoaDon(String maHoaDon){
        return hoaDonRepository.getHoaDonByMaHoaDon(maHoaDon)
                .map(hoaDonMapper::toHoaDonResponse)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy hóa đơn với mã: " + maHoaDon));
    }

    @Transactional
    public HoaDonResponse tiepNhanHoaDon(Integer idHD, Integer idNhanVien) {
        HoaDon hoaDon = hoaDonRepository.findById(idHD)
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy hóa đơn với id: " + idHD));

        NhanVien nhanVien = nhanVienRepository.findById(idNhanVien)
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy hóa đơn với id: " + idHD));
        hoaDon.setNhanVien(nhanVien);

        return hoaDonMapper.toHoaDonResponse(hoaDonRepository.save(hoaDon));
    }

    @Transactional
    public HoaDonResponse paidInvoice (Integer idHD) {
        HoaDon hoaDon = hoaDonRepository.findById(idHD)
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy hóa đơn với id: " + idHD));
        hoaDon.setTrangThai(1);
        return hoaDonMapper.toHoaDonResponse(hoaDonRepository.save(hoaDon));
    }

    @Transactional
    public HoaDonResponse upDateThongTinDonHang(ThongTinDonHangRequest thongTinDonHangRequest) {
        HoaDon hoaDon = hoaDonRepository.findById(thongTinDonHangRequest.getId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found for id " + thongTinDonHangRequest.getId()));

        hoaDon.setHoTenNguoiNhan(thongTinDonHangRequest.getHoTenNguoiNhan());
        hoaDon.setSoDienThoai(thongTinDonHangRequest.getSdt());
        hoaDon.setEmail(thongTinDonHangRequest.getEmail());
        hoaDon.setDiaChiNhanHang(thongTinDonHangRequest.getDiaChiNhanHang());

        //Tiền chênh lệch khi đổi địa chỉ
        if(hoaDon.getPhuPhi() == null){
            hoaDon.setPhuPhi(BigDecimal.ZERO);
        }
        BigDecimal chenhLech = BigDecimal.valueOf(thongTinDonHangRequest.getPhiShip() - hoaDon.getPhiShip());
        hoaDon.setPhuPhi(hoaDon.getPhuPhi().add(chenhLech));
        hoaDon.setPhiShip(thongTinDonHangRequest.getPhiShip());

        return hoaDonMapper.toHoaDonResponse(hoaDonRepository.save(hoaDon));
    }

    @Transactional
    public HoaDonResponse huyDonHang (Integer idHD) {
        HoaDon hoaDon = hoaDonRepository.findById(idHD)
                .orElseThrow(() -> new EntityNotFoundException("Order not found for id " + idHD));
        hoaDon.setTrangThaiGiaoHang(7);

        List<HoaDonChiTiet> listCartItem = hoaDonChiTietRepository.findByHoaDon_Id(hoaDon.getId());

        List<SanPhamChiTiet> sanPhamUpdate = new ArrayList<>();

        for(HoaDonChiTiet hoaDonChiTiet : listCartItem) {

            SanPhamChiTiet sanPhamChiTiet = hoaDonChiTiet.getSanPhamChiTiet();

            if(sanPhamChiTiet == null) continue;

            if (hoaDonChiTiet.getTrangThai() != null && hoaDonChiTiet.getTrangThai() == 1){
                int soLuongMoi = sanPhamChiTiet.getSoLuong() + hoaDonChiTiet.getSoLuong();
                sanPhamChiTiet.setSoLuong(soLuongMoi);
            }else {
                continue;
            }
            sanPhamUpdate.add(sanPhamChiTiet);
        }

//        hoaDonChiTietRepository.saveAll(listCartItem);
        sanPhamChiTietRepository.saveAll(sanPhamUpdate);
        return hoaDonMapper.toHoaDonResponse(hoaDon);
    }

    public HoaDonResponse chonKhachHangVaoHoaDon (Integer idHoaDon, Integer idKH) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new EntityNotFoundException("Order not found for id " + idHoaDon));
        KhachHang khachHang = khachHangRepository.findById(idKH)
                        .orElseThrow(() -> new EntityNotFoundException("Customer not found for id " + idKH));
        hoaDon.setKhachHang(khachHang);
        return hoaDonMapper.toHoaDonResponse(hoaDonRepository.save(hoaDon));
    }

    public HoaDonResponse removeKhachHang (Integer idHoaDon) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new EntityNotFoundException("Order not found for id " + idHoaDon));
        hoaDon.setKhachHang(null);
        return hoaDonMapper.toHoaDonResponse(hoaDonRepository.save(hoaDon));
    }

    public HoaDonResponse doiLoaiDon (Integer idHoaDon) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new EntityNotFoundException("Order not found for id " + idHoaDon));
        hoaDon.setLoaiDon(hoaDon.getLoaiDon() == 0? 1:0);
        return hoaDonMapper.toHoaDonResponse(hoaDonRepository.save(hoaDon));
    }

    @Transactional
    public HoaDonResponse confirmHoaDon (Integer idHoaDon, ConfirmHoaDonRequest confirmHoaDonRequest) {
        HoaDon hoaDon = hoaDonRepository.findById(idHoaDon)
                .orElseThrow(() -> new EntityNotFoundException("Order not found for id " + idHoaDon));
        hoaDon.setHoTenNguoiNhan(confirmHoaDonRequest.getHoTenNguoiNhan());
        hoaDon.setSoDienThoai(confirmHoaDonRequest.getSoDienThoai());
        hoaDon.setEmail(confirmHoaDonRequest.getEmail());
        hoaDon.setDiaChiNhanHang(confirmHoaDonRequest.getDiaChiNhanHang());
        hoaDon.setPhiShip(confirmHoaDonRequest.getPhiShip());
        hoaDon.setTongTien(confirmHoaDonRequest.getTongTien());
        hoaDon.setTrangThai(1);
        if(hoaDon.getLoaiDon() == 0){
            hoaDon.setTrangThaiGiaoHang(2);
        }else {
            hoaDon.setTrangThaiGiaoHang(9);
        }

        return hoaDonMapper.toHoaDonResponse(hoaDonRepository.save(hoaDon));
    }




}
