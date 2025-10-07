package com.logondigital.bozacm.service.utilisateur;

import com.logondigital.bozacm.entities.Utilisateur;
import com.logondigital.bozacm.repository.UtilisateurRepo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepo utilisateurRepo;
    // Injection du repository via le constructeur
    public UtilisateurServiceImpl(UtilisateurRepo  utilisateurRepo) {
        this.utilisateurRepo  = utilisateurRepo;
    }

    @Override
    public void createUtilisateur(Utilisateur utilisateur) {
        utilisateur.setCreatedAt(new Date());
        this.utilisateurRepo.save(utilisateur);
    }



    @Override
    public List<Utilisateur> getUtilisateurs() {
        return this.utilisateurRepo.findAll();
    }

    @Override
    public Utilisateur getUtilisateurById(Integer idUtilisateur) {
        return this.utilisateurRepo.findById(idUtilisateur).orElse(null);
    }

    @Override
    public void updateUtilisateur(Integer idUtilisateur, Utilisateur utilisateur) {
        Utilisateur utilisateurToUpdate = this.utilisateurRepo.findById(idUtilisateur).orElse(null);

        if (utilisateurToUpdate != null) {
            utilisateurToUpdate.setNom(utilisateur.getNom());
            utilisateurToUpdate.setPrenom(utilisateur.getPrenom());
            utilisateurToUpdate.setEmail(utilisateur.getEmail());
            utilisateurToUpdate.setUpdatedAt(new Date());

            this.utilisateurRepo.saveAndFlush(utilisateurToUpdate);
        }
    }

    @Override
    public void deleteUtilisateur(Integer idUtilisateur) {
        this.utilisateurRepo.deleteById(idUtilisateur);
    }
}

