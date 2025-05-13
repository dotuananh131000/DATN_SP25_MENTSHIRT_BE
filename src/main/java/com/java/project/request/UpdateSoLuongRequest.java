package com.java.project.request;

import lombok.Data;

public class UpdateSoLuongRequest {
    private Integer soLuong;

    public Integer getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(Integer soLuong) {
        this.soLuong = soLuong;
    }
}
