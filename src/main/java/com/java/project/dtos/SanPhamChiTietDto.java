package com.java.project.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for {@link com.java.project.entities.SanPhamChiTiet}
 */
@Data
@Builder
public class SanPhamChiTietDto implements Serializable {
    Integer id;
    ChatLieuDto chatLieu;
    CoAoDto coAo;
    KichThuocDto kichThuoc;
    MauSacDto mauSac;
    SanPhamDto sanPham;
    TayAoDto tayAo;
    ThuongHieuDto thuongHieu;
    XuatXuDto xuatXu;
    Integer soLuong;
    BigDecimal donGia;
    String hinhAnh;
    Instant ngayTao;
    Boolean trangThai;
}