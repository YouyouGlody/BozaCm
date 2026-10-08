package com.logondigital.bozacm.service.admin;

import com.logondigital.bozacm.DTO.admin.AdminUpdateDTO;
import com.logondigital.bozacm.DTO.mapper.AdminMapper;
import com.logondigital.bozacm.entities.Admin;
import com.logondigital.bozacm.exceptions.EmailAlreadyExistsException;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.AdminRepo;
import org.springframework.stereotype.Service;

@Service
public class AdminServiceImpl implements AdminService {

    private final AdminRepo adminRepo;
    private final AdminMapper adminMapper;

    public AdminServiceImpl(AdminRepo adminRepo, AdminMapper adminMapper) {
        this.adminRepo = adminRepo;
        this.adminMapper = adminMapper;
    }

    @Override
    public Admin findByEmail(String email) {
        return adminRepo.findByEmail(email)
                .orElseThrow(() -> new RessourceNotFoundException("Admin non trouvé avec l'email : " + email));
    }

    @Override
    public Admin updateProfile(String email, AdminUpdateDTO dto) {
        Admin admin = findByEmail(email);

        String newEmail = dto.getEmail();
        boolean emailChanged = newEmail != null && !newEmail.equals(admin.getEmail());

        if (emailChanged && adminRepo.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException(newEmail);
        }

        adminMapper.updateEntityFromDTO(admin, dto);

        return adminRepo.save(admin);
    }

    @Override
    public Admin updatePhoto(String email, String photoUrl) {
        Admin admin = findByEmail(email);
        admin.setPhotoUrl(photoUrl);
        return adminRepo.save(admin);
    }
}
