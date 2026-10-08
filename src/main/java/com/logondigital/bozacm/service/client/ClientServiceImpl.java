package com.logondigital.bozacm.service.client;

import com.logondigital.bozacm.DTO.client.ChangePasswordDTO;
import com.logondigital.bozacm.entities.Client;
import com.logondigital.bozacm.exceptions.EmailAlreadyExistsException;
import com.logondigital.bozacm.exceptions.PhoneAlreadyExistsException;
import com.logondigital.bozacm.exceptions.RessourceNotFoundException;
import com.logondigital.bozacm.repository.ClientRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

/**
 * Implémentation du service Client.
 * Contient toute la logique métier pour la gestion des clients.
 */

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepo clientRepo;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.avatar.upload-dir:uploads/avatars}")
    private String avatarUploadDir;

    @Value("${app.documents.upload-dir:uploads/documents}")
    private String documentsUploadDir;

    public ClientServiceImpl(ClientRepo clientRepo, PasswordEncoder passwordEncoder) {
        this.clientRepo = clientRepo;
        this.passwordEncoder = passwordEncoder;
    }


    // ===========================================================
    // ==========        CRUD DE BASE DE CLIENT       =====
    // ===========================================================

    @Override
    public Client createClient(Client client) {
        if (clientRepo.existsByEmail(client.getEmail())) {
            throw new EmailAlreadyExistsException(client.getEmail());
        }
        if (clientRepo.existsByNumeroTelephone(client.getNumeroTelephone())) {
            throw new PhoneAlreadyExistsException(client.getNumeroTelephone());
        }
        return this.clientRepo.save(client);
    }

    @Override
    public Client getClientById(Integer idClient) {
        return this.clientRepo.findById(idClient).
                orElseThrow(
                        () -> new RessourceNotFoundException("Client non trouvé avec l'ID: " + idClient)
                );
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepo.findAll();
    }

    @Override
    public Client updateClient(Integer idClient, Client client) {
        Client clientToUpdate = this.clientRepo.findById(idClient).orElseThrow(
                () -> new RessourceNotFoundException("Client non trouvé avec l'ID: " + idClient)
        );

        String currentEmail = clientToUpdate.getEmail();
        String newEmail = client.getEmail();

        boolean emailChanged = (currentEmail == null && newEmail != null) ||
                (currentEmail != null && !currentEmail.equals(newEmail));

        if (emailChanged && newEmail != null && clientRepo.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException(newEmail);
        }

        String currentPhone = clientToUpdate.getNumeroTelephone();
        String newPhone = client.getNumeroTelephone();

        boolean phoneChanged = (currentPhone == null && newPhone != null) ||
                (currentPhone != null && !currentPhone.equals(newPhone));

        if (phoneChanged && newPhone != null && clientRepo.existsByNumeroTelephone(newPhone)) {
            throw new PhoneAlreadyExistsException(newPhone);
        }

        clientToUpdate.setNom(client.getNom());
        clientToUpdate.setPrenom(client.getPrenom());
        clientToUpdate.setEmail(newEmail);
        clientToUpdate.setNumeroTelephone(newPhone);
        clientToUpdate.setAdresse(client.getAdresse());

        this.clientRepo.save(clientToUpdate);
        return clientToUpdate;
    }

    @Override
    public void deleteClient(Integer idClient) {
        if (!clientRepo.existsById(idClient)) {
            throw new RessourceNotFoundException("Client non trouvé avec l'ID: " + idClient);
        }
        clientRepo.deleteById(idClient);
    }


    // ========== Méthodes Métier ==========

    @Override
    public Client findByEmail(String email) {
        return clientRepo.findByEmail(email).orElseThrow(
                () -> new RessourceNotFoundException("Client non trouvé avec l'email: " + email)
        );
    }

    @Override
    public Client findByNumeroTelephone(String numeroTelephone) {
        return clientRepo.findByNumeroTelephone(numeroTelephone)
                .orElseThrow(() -> new RessourceNotFoundException(
                        "Client non trouvé avec le numéro: " + numeroTelephone
                ));
    }

    @Override
    public long countClients() {
        return clientRepo.count();
    }


    // ========== Profil du client connecté (mot de passe, photo, documents) ==========

    @Override
    public void changePassword(String email, ChangePasswordDTO dto) {
        Client client = findByEmail(email);
        if (!passwordEncoder.matches(dto.getAncienMotDePasse(), client.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe incorrect");
        }
        client.setPassword(passwordEncoder.encode(dto.getNouveauMotDePasse()));
        clientRepo.save(client);
    }

    @Override
    public String updatePhoto(String email, MultipartFile file) {
        Client client = findByEmail(email);
        String url = storePublicFile(avatarUploadDir, "/avatars/", file);
        client.setPhotoUrl(url);
        clientRepo.save(client);
        return url;
    }

    @Override
    public String uploadDocument(String email, String type, MultipartFile file) {
        Client client = findByEmail(email);
        String relativePath = storePrivateFile(client.getIdClient(), type, file);

        switch (type) {
            case "cni-recto" -> client.setCniRectoUrl(relativePath);
            case "cni-verso" -> client.setCniVersoUrl(relativePath);
            case "passeport" -> client.setPasseportUrl(relativePath);
            default -> throw new IllegalArgumentException("Type de document inconnu : " + type);
        }
        clientRepo.save(client);
        return relativePath;
    }

    @Override
    public Resource getDocument(String email, String type) {
        Client client = findByEmail(email);
        String relativePath = switch (type) {
            case "cni-recto" -> client.getCniRectoUrl();
            case "cni-verso" -> client.getCniVersoUrl();
            case "passeport" -> client.getPasseportUrl();
            default -> throw new IllegalArgumentException("Type de document inconnu : " + type);
        };
        if (relativePath == null) {
            throw new RessourceNotFoundException("Aucun document de ce type n'a été fourni");
        }
        return new FileSystemResource(Paths.get(documentsUploadDir, relativePath));
    }

    // ─── Utilitaires de stockage ────────────────────────────────────────────

    private String storePublicFile(String dir, String publicPrefix, MultipartFile file) {
        try {
            Path dirPath = Paths.get(dir);
            Files.createDirectories(dirPath);
            String filename = UUID.randomUUID() + extensionOf(file);
            Files.copy(file.getInputStream(), dirPath.resolve(filename));
            return publicPrefix + filename;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier", e);
        }
    }

    private String storePrivateFile(Integer clientId, String type, MultipartFile file) {
        try {
            Path dirPath = Paths.get(documentsUploadDir, String.valueOf(clientId));
            Files.createDirectories(dirPath);
            String filename = type + extensionOf(file);
            Files.copy(file.getInputStream(), dirPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
            return clientId + "/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du document", e);
        }
    }

    private String extensionOf(MultipartFile file) {
        String original = file.getOriginalFilename();
        return (original != null && original.contains(".")) ? original.substring(original.lastIndexOf('.')) : "";
    }
}