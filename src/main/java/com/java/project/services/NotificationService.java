package com.java.project.services;

import com.java.project.dtos.HoaDonResponse;
import com.java.project.dtos.ThongBaoResponse;
import com.java.project.entities.HoaDon;
import com.java.project.entities.KhachHang;
import com.java.project.entities.NhanVien;
import com.java.project.entities.ThongBao;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.mappers.ThongBaoMapper;
import com.java.project.repositories.KhachHangRepository;
import com.java.project.repositories.NhanVienRepository;
import com.java.project.repositories.ThongBaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {
    @Autowired
    private SimpMessagingTemplate template;

    @Autowired
    ThongBaoRepository thongBaoRepository;

    @Autowired
    NhanVienRepository nhanVienRepository;

    @Autowired
    KhachHangRepository khachHangRepository;

    @Autowired
    ThongBaoMapper thongBaoMapper;

    public void notifyNewOrder(ThongBao thongBao) {
        template.convertAndSend("/topic/notification", thongBao);
    }

    public void guiThongBaoChoNhanVien(HoaDonResponse hoaDonResponse){
        List<NhanVien> lists = nhanVienRepository.findAll();

        for(NhanVien nhanVien : lists){
            ThongBao tb = new ThongBao();
            tb.setNoiDung("Đơn hàng mới #: " + hoaDonResponse.getMaHoaDon() + " đã đươc tạo");
            tb.setThoiGianTao(LocalDateTime.now());
            tb.setDaDoc(false);
            tb.setNhanVien(nhanVien);
            thongBaoRepository.save(tb);

            template.convertAndSend("/topic/nhan-vien/"+nhanVien.getId(), tb);
        }
    }

    public List<ThongBaoResponse>getThongBaoListByNhanVien(Integer id){
        return thongBaoRepository.findByNhanVien_Id(id).stream()
                .map(thongBaoMapper::toThongBaoResponse)
                .toList();
    }

    public void Seen (Integer id){
        ThongBao thongBao = thongBaoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Thong Bao Not Found"));
        thongBao.setDaDoc(true);
        thongBaoRepository.save(thongBao);
    }
    public void guiThongBaoChoKhachHang(HoaDonResponse hoaDonResponse){
        KhachHang khachHang = khachHangRepository.findById(hoaDonResponse.getIdKhachHang())
                .orElseThrow(()-> new EntityNotFoundException("Không tìm thấy khách hàng"));

        ThongBao tb = new ThongBao();
        tb.setNoiDung("Đơn hàng #: " + hoaDonResponse.getId() + " đã được cập nhật trạng thái.");
        tb.setKhachHang(khachHang);
        tb.setThoiGianTao(LocalDateTime.now());
        thongBaoRepository.save(tb);

        template.convertAndSend("/topic/khach-hang/" + khachHang.getId(), tb);
    }
}
