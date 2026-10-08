package com.logondigital.bozacm.DTO.mapper;

import com.logondigital.bozacm.DTO.admin.AdminResponseDTO;
import com.logondigital.bozacm.DTO.admin.AdminUpdateDTO;
import com.logondigital.bozacm.entities.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {

    public AdminResponseDTO toResponseDTO(Admin admin) {
        if (admin == null) {
            return null;
        }

        AdminResponseDTO dto = new AdminResponseDTO();
        dto.setId(admin.getId());
        dto.setNom(admin.getNom());
        dto.setEmail(admin.getEmail());
        dto.setPhotoUrl(admin.getPhotoUrl());
        dto.setRole(admin.getRole());

        return dto;
    }

    public void updateEntityFromDTO(Admin admin, AdminUpdateDTO dto) {
        if (admin == null || dto == null) {
            return;
        }

        if (dto.getNom() != null) {
            admin.setNom(dto.getNom());
        }
        if (dto.getEmail() != null) {
            admin.setEmail(dto.getEmail());
        }
    }
}
