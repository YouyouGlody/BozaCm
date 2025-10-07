package com.logondigital.bozacm.service.utilisateur;

import com.logondigital.bozacm.entities.Utilisateur;

import java.util.List;

public interface UtilisateurService {
   void createUtilisateur(Utilisateur utilisateur);
    List<Utilisateur> getUtilisateurs();
    Utilisateur getUtilisateurById(Integer idUtilisateur);
    void updateUtilisateur(Integer idUtilisateur, Utilisateur utilisateur);
    void deleteUtilisateur(Integer idUtilisateur);
}
