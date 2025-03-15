package com.java.project.services;

import com.java.project.dtos.VaiTroDto;
import com.java.project.entities.VaiTro;
import com.java.project.exceptions.EntityNotFoundException;
import com.java.project.exceptions.ResourceNotFoundException;
import com.java.project.mappers.VaiTroMapper;
import com.java.project.repositories.VaiTroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VaiTroService {
    @Autowired
    private VaiTroRepository vaiTroRepository;

    public VaiTroDto getVaiTroById(Integer id) {
        VaiTro vaiTro = vaiTroRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("VaiTro not found"));
        if (vaiTro == null) {
            return null;
        }
        return VaiTroMapper.toVaiTroDTO(vaiTro);
    }

    public List<VaiTroDto> getAllVaiTro() {
        List<VaiTro> vaiTros = vaiTroRepository.findAll();
        List<VaiTroDto> vaiTroDtos = new ArrayList<>();
        for (VaiTro vaiTro : vaiTros) {
            vaiTroDtos.add(VaiTroMapper.toVaiTroDTO(vaiTro));
        }
        return vaiTroDtos;
    }
}
