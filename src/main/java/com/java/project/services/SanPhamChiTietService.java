package com.java.project.services;

import com.java.project.dtos.SanPhamChiTietDto;
import com.java.project.dtos.SanPhamChiTietGenerateDTO;
import com.java.project.dtos.SanPhamChiTietPhanLoaiDTO;
import com.java.project.dtos.SanPhamChiTietResponse;
import com.java.project.entities.SanPhamChiTiet;
import com.java.project.exceptions.EntityAlreadyExistsException;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.mappers.SanPhamChiTietMapper;
import com.java.project.models.SanPhamChiTietGenerateModel;
import com.java.project.models.SanPhamChiTietModel;
import com.java.project.models.UpdateSanPhamChiTietModel;
import com.java.project.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SanPhamChiTietService {

    @Autowired
    private SanPhamChiTietRepository sanPhamChiTietRepository;

    @Autowired
    private SanPhamRepository sanPhamRepository;

    @Autowired
    private ThuongHieuRepository thuongHieuRepository;

    @Autowired
    private XuatXuRepository xuatXuRepository;

    @Autowired
    private ChatLieuRepository chatLieuRepository;

    @Autowired
    private CoAoRepository coAoRepository;

    @Autowired
    private TayAoRepository tayAoRepository;

    @Autowired
    private MauSacRepository mauSacRepository;

    @Autowired
    private KichThuocRepository kichThuocRepository;

    public Page<SanPhamChiTietDto> getAllSanPhamChiTiet(
            String search,
            List<Integer> thuongHieuIds, List<Integer> xuatXuIds,
            List<Integer> chatLieuIds, List<Integer> coAoIds, List<Integer> tayAoIds,
            List<Integer> mauSacIds, List<Integer> kichThuocIds,
            Double minPrice, Double maxPrice,  // Thêm tham số lọc giá
            Pageable pageable) {

        Page<SanPhamChiTiet> sanPhamChiTiets = sanPhamChiTietRepository.findBySearchAndFilter(
                search, thuongHieuIds, xuatXuIds, chatLieuIds, coAoIds, tayAoIds,
                mauSacIds, kichThuocIds, minPrice, maxPrice, pageable);

        return sanPhamChiTiets.map(SanPhamChiTietMapper::toDTO);
    }


    @Transactional
    public SanPhamChiTietDto getById(Integer id) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chi tiết sản phẩm không tồn tại"));
        return SanPhamChiTietMapper.toDTO(sanPhamChiTiet);
    }

    @Transactional
    public List<SanPhamChiTietDto> createSanPhamChiTietList(List<SanPhamChiTietModel> sanPhamChiTietModels) {
        List<SanPhamChiTietDto> result = new ArrayList<>();

        for (SanPhamChiTietModel model : sanPhamChiTietModels) {
            // Kiểm tra dữ liệu có tồn tại không
            validateExistence(model);

            // Kiểm tra uniqueness
            checkUniqueSanPhamChiTiet(model);

            // Map từ model sang entity
            SanPhamChiTiet sanPhamChiTiet = new SanPhamChiTiet();
            mapToEntity(model, sanPhamChiTiet);

            // Lưu entity vào database
            sanPhamChiTietRepository.save(sanPhamChiTiet);

            // Thêm kết quả trả về
            result.add(SanPhamChiTietMapper.toDTO(sanPhamChiTiet));
        }

        return result;
    }

    @Transactional
    public SanPhamChiTietDto updateSanPhamChiTiet(Integer id, UpdateSanPhamChiTietModel model) {

        // Tìm chi tiết sản phẩm theo ID
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Chi tiết sản phẩm không tồn tại"));

        // Cập nhật Thương hiệu nếu có
        if (model.getThuongHieu() != null) {
            sanPhamChiTiet.setThuongHieu(thuongHieuRepository.findById(model.getThuongHieu())
                    .orElseThrow(() -> new EntityNotFoundException("Thương hiệu không tồn tại")));
        }

        // Cập nhật Xuất xứ nếu có
        if (model.getXuatXu() != null) {
            sanPhamChiTiet.setXuatXu(xuatXuRepository.findById(model.getXuatXu())
                    .orElseThrow(() -> new EntityNotFoundException("Xuất xứ không tồn tại")));
        }

        // Cập nhật Chất liệu nếu có
        if (model.getChatLieu() != null) {
            sanPhamChiTiet.setChatLieu(chatLieuRepository.findById(model.getChatLieu())
                    .orElseThrow(() -> new EntityNotFoundException("Chất liệu không tồn tại")));
        }

        // Cập nhật Cổ áo nếu có
        if (model.getCoAo() != null) {
            sanPhamChiTiet.setCoAo(coAoRepository.findById(model.getCoAo())
                    .orElseThrow(() -> new EntityNotFoundException("Cổ áo không tồn tại")));
        }

        // Cập nhật Tay áo nếu có
        if (model.getTayAo() != null) {
            sanPhamChiTiet.setTayAo(tayAoRepository.findById(model.getTayAo())
                    .orElseThrow(() -> new EntityNotFoundException("Tay áo không tồn tại")));
        }

        // Cập nhật Màu sắc nếu có
        if (model.getMauSac() != null) {
            sanPhamChiTiet.setMauSac(mauSacRepository.findById(model.getMauSac())
                    .orElseThrow(() -> new EntityNotFoundException("Màu sắc không tồn tại")));
        }

        // Cập nhật Kích thước nếu có
        if (model.getKichThuoc() != null) {
            sanPhamChiTiet.setKichThuoc(kichThuocRepository.findById(model.getKichThuoc())
                    .orElseThrow(() -> new EntityNotFoundException("Kích thước không tồn tại")));
        }

        // Cập nhật Số lượng nếu có
        if (model.getSoLuong() != null) {
            sanPhamChiTiet.setSoLuong(model.getSoLuong());
        }

        // Cập nhật Đơn giá nếu có
        if (model.getDonGia() != null) {
            sanPhamChiTiet.setDonGia(model.getDonGia());
        }

        // Cập nhật Hình ảnh nếu có
        if (model.getHinhAnh() != null) {
            sanPhamChiTiet.setHinhAnh(model.getHinhAnh());
        }

        // Lưu lại thông tin đã cập nhật vào database
        sanPhamChiTietRepository.save(sanPhamChiTiet);

        // Trả về DTO sau khi cập nhật
        return SanPhamChiTietMapper.toDTO(sanPhamChiTiet);
    }

    public SanPhamChiTietDto toggleTrangThai(Integer id) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SanPhamChiTiet không tồn tại"));

        sanPhamChiTiet.setTrangThai(!sanPhamChiTiet.getTrangThai());
        sanPhamChiTietRepository.save(sanPhamChiTiet);

        return SanPhamChiTietMapper.toDTO(sanPhamChiTiet);
    }

    @Transactional
    public void deleteSanPhamChiTiet(Integer id) {
        SanPhamChiTiet sanPhamChiTiet = sanPhamChiTietRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SanPhamChiTiet không tồn tại"));

        sanPhamChiTietRepository.delete(sanPhamChiTiet);
    }

    private void validateExistence(SanPhamChiTietModel model) {
        // Kiểm tra sự tồn tại của từng khóa ngoại (tương tự validateExistence của bạn)
        Optional.ofNullable(model.getSanPham())
                .ifPresent(sanPham -> sanPhamRepository.findById(sanPham)
                        .orElseThrow(() -> new EntityNotFoundException("Sản phẩm không tồn tại")));

        Optional.ofNullable(model.getThuongHieu())
                .ifPresent(thuongHieu -> thuongHieuRepository.findById(thuongHieu)
                        .orElseThrow(() -> new EntityNotFoundException("Thương hiệu không tồn tại")));

        // Tương tự cho các trường khác
        Optional.ofNullable(model.getXuatXu())
                .ifPresent(xuatXu -> xuatXuRepository.findById(xuatXu)
                        .orElseThrow(() -> new EntityNotFoundException("Xuất xứ không tồn tại")));

        Optional.ofNullable(model.getChatLieu())
                .ifPresent(chatLieu -> chatLieuRepository.findById(chatLieu)
                        .orElseThrow(() -> new EntityNotFoundException("Chất liệu không tồn tại")));

        Optional.ofNullable(model.getCoAo())
                .ifPresent(coAo -> coAoRepository.findById(coAo)
                        .orElseThrow(() -> new EntityNotFoundException("Cỡ áo không tồn tại")));

        Optional.ofNullable(model.getTayAo())
                .ifPresent(tayAo -> tayAoRepository.findById(tayAo)
                        .orElseThrow(() -> new EntityNotFoundException("Tay áo không tồn tại")));

        Optional.ofNullable(model.getMauSac())
                .ifPresent(mauSac -> mauSacRepository.findById(mauSac)
                        .orElseThrow(() -> new EntityNotFoundException("Màu sắc không tồn tại")));

        Optional.ofNullable(model.getKichThuoc())
                .ifPresent(kichThuoc -> kichThuocRepository.findById(kichThuoc)
                        .orElseThrow(() -> new EntityNotFoundException("Kích thước không tồn tại")));
    }

    private void checkUniqueSanPhamChiTiet(SanPhamChiTietModel model) {
        boolean exists = sanPhamChiTietRepository.existsBySanPhamAndThuongHieuAndXuatXuAndChatLieuAndCoAoAndTayAoAndMauSacAndKichThuoc(
                model.getSanPham(),
                model.getThuongHieu(),
                model.getXuatXu(),
                model.getChatLieu(),
                model.getCoAo(),
                model.getTayAo(),
                model.getMauSac(),
                model.getKichThuoc()
        );

        if (exists) {
            throw new EntityAlreadyExistsException("Chi tiết sản phẩm đã tồn tại trong cơ sở dữ liệu");
        }
    }

    private void mapToEntity(SanPhamChiTietModel model, SanPhamChiTiet entity) {
        entity.setSanPham(sanPhamRepository.findById(model.getSanPham()).orElse(null));
        entity.setThuongHieu(thuongHieuRepository.findById(model.getThuongHieu()).orElse(null));
        entity.setXuatXu(xuatXuRepository.findById(model.getXuatXu()).orElse(null));
        entity.setChatLieu(chatLieuRepository.findById(model.getChatLieu()).orElse(null));
        entity.setCoAo(coAoRepository.findById(model.getCoAo()).orElse(null));
        entity.setTayAo(tayAoRepository.findById(model.getTayAo()).orElse(null));
        entity.setMauSac(mauSacRepository.findById(model.getMauSac()).orElse(null));
        entity.setKichThuoc(kichThuocRepository.findById(model.getKichThuoc()).orElse(null));
        entity.setSoLuong(model.getSoLuong());
        entity.setDonGia(model.getDonGia());
        entity.setHinhAnh(model.getHinhAnh() != null ? model.getHinhAnh() : "default_image.jpg");
        entity.setTrangThai(true);
        entity.setNgayTao(Instant.now());
    }

    @Transactional
    public List<SanPhamChiTietPhanLoaiDTO> generateSanPhamChiTietGroupedByMauSac(SanPhamChiTietGenerateModel generateRequest) {
        // Bước 1: Generate danh sách tất cả các kết hợp sản phẩm chi tiết
        List<SanPhamChiTietGenerateDTO> allCombinations = generateSanPhamChiTietList(generateRequest);

        // Bước 2: Lọc ra các sản phẩm chi tiết chưa tồn tại
        List<SanPhamChiTietGenerateDTO> filteredCombinations = allCombinations.stream()
                .filter(this::isUniqueProductDetail)
                .collect(Collectors.toList());

        // Bước 3: Nhóm các sản phẩm đã lọc theo màu sắc
        Map<Integer, List<SanPhamChiTietGenerateDTO>> groupedByMauSac = filteredCombinations.stream()
                .collect(Collectors.groupingBy(SanPhamChiTietGenerateDTO::getMauSac));

        // Bước 4: Chuyển đổi thành danh sách DTO để trả về
        return groupedByMauSac.entrySet().stream()
                .map(this::convertEntryToPhanLoaiDTO)
                .collect(Collectors.toList());
    }

    private boolean isUniqueProductDetail(SanPhamChiTietGenerateDTO dto) {
        return !sanPhamChiTietRepository.existsBySanPhamAndThuongHieuAndXuatXuAndChatLieuAndCoAoAndTayAoAndMauSacAndKichThuoc(
                dto.getSanPham(),
                dto.getThuongHieu(),
                dto.getXuatXu(),
                dto.getChatLieu(),
                dto.getCoAo(),
                dto.getTayAo(),
                dto.getMauSac(),
                dto.getKichThuoc()
        );
    }

    private List<SanPhamChiTietGenerateDTO> generateSanPhamChiTietList(SanPhamChiTietGenerateModel generateRequest) {
        List<SanPhamChiTietGenerateDTO> result = new ArrayList<>();

        for (Integer thuongHieuId : generateRequest.getThuongHieu()) {
            for (Integer xuatXuId : generateRequest.getXuatXu()) {
                for (Integer chatLieuId : generateRequest.getChatLieu()) {
                    for (Integer coAoId : generateRequest.getCoAo()) {
                        for (Integer tayAoId : generateRequest.getTayAo()) {
                            for (Integer mauSacId : generateRequest.getMauSac()) {
                                for (Integer kichThuocId : generateRequest.getKichThuoc()) {
                                    SanPhamChiTietGenerateDTO dto = SanPhamChiTietGenerateDTO.builder()
                                            .sanPham(generateRequest.getSanPham())
                                            .tenSanPham(sanPhamRepository.findById(generateRequest.getSanPham()).orElseThrow(() -> new EntityNotFoundException("Không tìm thấy sản phẩm với ID: " + generateRequest.getSanPham()))
                                                    .getTenSanPham())
                                            .thuongHieu(thuongHieuId)
                                            .tenThuongHieu(thuongHieuRepository.findById(thuongHieuId)
                                                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy thương hiệu với ID: " + thuongHieuId))
                                                    .getTenThuongHieu())
                                            .xuatXu(xuatXuId)
                                            .tenXuatXu(xuatXuRepository.findById(xuatXuId)
                                                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy xuất xứ với ID: " + xuatXuId))
                                                    .getTenXuatXu())
                                            .chatLieu(chatLieuId)
                                            .tenChatLieu(chatLieuRepository.findById(chatLieuId)
                                                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy chất liệu với ID: " + chatLieuId))
                                                    .getTenChatLieu())
                                            .coAo(coAoId)
                                            .tenCoAo(coAoRepository.findById(coAoId)
                                                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy cổ áo với ID: " + coAoId))
                                                    .getTenCoAo())
                                            .tayAo(tayAoId)
                                            .tenTayAo(tayAoRepository.findById(tayAoId)
                                                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tay áo với ID: " + tayAoId))
                                                    .getTenTayAo())
                                            .mauSac(mauSacId)
                                            .tenMauSac(mauSacRepository.findById(mauSacId)
                                                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy màu sắc với ID: " + mauSacId))
                                                    .getTenMauSac())
                                            .kichThuoc(kichThuocId)
                                            .tenKichThuoc(kichThuocRepository.findById(kichThuocId)
                                                    .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy kích thước với ID: " + kichThuocId))
                                                    .getTenKichThuoc())
                                            .soLuong(generateRequest.getSoLuong())
                                            .donGia(generateRequest.getDonGia())
                                            .hinhAnh(null)
                                            .build();

                                    result.add(dto);
                                }
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    private SanPhamChiTietPhanLoaiDTO convertEntryToPhanLoaiDTO(Map.Entry<Integer, List<SanPhamChiTietGenerateDTO>> entry) {
        return SanPhamChiTietPhanLoaiDTO.builder()
                .maMauSac(entry.getKey())
                .tenMauSac(mauSacRepository.findById(entry.getKey())
                        .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy màu sắc với ID: " + entry.getKey()))
                        .getTenMauSac())
                .tenSanPham(entry.getValue().get(0).getTenSanPham())
                .sanPhamChiTiet(entry.getValue())
                .build();
    }

    public List<SanPhamChiTietResponse>getAll(){
        return sanPhamChiTietRepository.getAll();
    }
}
