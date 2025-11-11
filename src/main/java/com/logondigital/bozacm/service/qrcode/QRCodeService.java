package com.logondigital.bozacm.service.qrcode;

// ============================================
// IMPORTS NÉCESSAIRES
// ============================================

// Imports ZXing pour générer le QR Code
import com.google.zxing.BarcodeFormat;        // Type de code (QR_CODE, EAN13, etc.)
import com.google.zxing.WriterException;      // Exception si erreur de génération
import com.google.zxing.client.j2se.MatrixToImageWriter;  // Convertit matrice → image
import com.google.zxing.common.BitMatrix;     // Matrice de bits (pixels noir/blanc)
import com.google.zxing.qrcode.QRCodeWriter;  // Le générateur de QR Code

// Imports Spring pour configuration
import org.springframework.beans.factory.annotation.Value;  // Lire application.properties
import org.springframework.stereotype.Service;              // Marquer comme service Spring

// Imports Java pour manipulation de fichiers
import java.io.IOException;                    // Exception si erreur fichier
import java.nio.file.Files;                    // Utilitaires pour créer/supprimer fichiers
import java.nio.file.Path;                     // Représente un chemin de fichier
import java.nio.file.Paths;                    // Créer des chemins

/**
 * ============================================
 * SERVICE DE GÉNÉRATION DE QR CODE
 * ============================================

 * RÔLE : Générer automatiquement un QR Code pour chaque billet

 * PROCESSUS :
 * 1. Reçoit le numéro de billet (ex: BZC-abc123...)
 * 2. Génère une image QR Code (300x300 pixels)
 * 3. Sauvegarde l'image dans uploads/qrcodes/
 * 4. Retourne l'URL d'accès (http://localhost:8080/qrcodes/BZC-abc123.png)

 * RÈGLE MÉTIER :
 * - Chaque billet a un QR Code unique
 * - Le QR Code contient le numéro du billet
 * - L'agent scanne le QR Code à l'embarquement
 * - Le système valide le billet et change son statut
 */
@Service  // Spring va créer une instance automatiquement
public class QRCodeService {

    // ============================================
    // CONFIGURATION (depuis application.properties)
    // ============================================

    /**
     * Chemin où on stocke les QR codes sur le disque.
     * Par défaut : uploads/qrcodes

     * Exemple : Si le projet est dans C:/bozacm/
     * Les QR Codes seront dans C:/bozacm/uploads/qrcodes/
     */
    @Value("${app.qrcode.upload-dir:uploads/qrcodes}")
    private String uploadDir;

    /**
     * URL de base pour accéder aux images via HTTP.
     * Par défaut : http://localhost:8080/qrcodes

     * Exemple : Le fichier BZC-abc123.png sera accessible à :
     * http://localhost:8080/qrcodes/BZC-abc123.png
     */
    @Value("${app.qrcode.base-url:http://localhost:8080/qrcodes}")
    private String baseUrl;

    // ============================================
    // CONSTANTES DE DIMENSION
    // ============================================

    /** Largeur du QR Code en pixels */
    private static final int WIDTH = 300;

    /** Hauteur du QR Code en pixels */
    private static final int HEIGHT = 300;

    // ============================================
    // MÉTHODE PRINCIPALE : GÉNÉRER UN QR CODE
    // ============================================

    /**
     * Génère un QR Code pour un billet.

     * ÉTAPES :
     * 1. Créer le dossier uploads/qrcodes/ s'il n'existe pas
     * 2. Encoder le numéro de billet en QR Code (matrice de bits)
     * 3. Convertir la matrice en image PNG
     * 4. Sauvegarder l'image sur le disque
     * 5. Retourner l'URL pour y accéder
     *
     * @param numeroBillet Le numéro unique du billet (ex: BZC-abc123-def456...)
     * @return L'URL complète du QR Code (ex: http://localhost:8080/qrcodes/BZC-abc123.png)
     *
     * @throws IOException Si impossible de créer le fichier (disque plein, permissions, etc.)
     * @throws WriterException Si impossible d'encoder le QR Code (texte trop long, etc.)
     */
    public String generateQRCode(String numeroBillet) throws IOException, WriterException {

        // ============================================
        // ÉTAPE 1 : CRÉER LE DOSSIER SI N'EXISTE PAS
        // ============================================

        // Convertir le chemin texte en objet Path (Java NIO)
        Path uploadPath = Paths.get(uploadDir);

        // Vérifier si le dossier existe
        if (!Files.exists(uploadPath)) {
            // Sinon, créer tous les dossiers parents nécessaires
            // createDirectories = mkdir -p sous Linux
            Files.createDirectories(uploadPath);
            System.out.println("✅ Dossier QR Code créé : " + uploadPath.toAbsolutePath());
        }

        // ============================================
        // ÉTAPE 2 : GÉNÉRER LE QR CODE EN MÉMOIRE
        // ============================================

        // Créer l'encodeur de QR Code
        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        // Contenu du QR Code = le numéro du billet
        // Quand l'agent scanne, il verra ce texte
        String qrContent = numeroBillet;

        // ALTERNATIVE : URL complète pour validation automatique
        // String qrContent = "https://bozacm.com/api/v1/billets/valider/" + numeroBillet;
        // Avantage : Le scan peut directement appeler l'API

        // Encoder le texte en QR Code (matrice de bits noir/blanc)
        BitMatrix bitMatrix = qrCodeWriter.encode(
                qrContent,              // Le texte à encoder
                BarcodeFormat.QR_CODE,  // Le type de code-barres (QR_CODE vs CODE128, EAN13...)
                WIDTH,                  // Largeur en pixels
                HEIGHT                  // Hauteur en pixels
        );

        // À ce stade : bitMatrix est une grille de 300x300 bits (0=blanc, 1=noir)
        System.out.println("✅ QR Code encodé pour : " + numeroBillet);

        // ============================================
        // ÉTAPE 3 : SAUVEGARDER L'IMAGE SUR LE DISQUE
        // ============================================

        // Nom du fichier = numéro du billet + .png
        // Ex: BZC-abc123-def456.png
        String fileName = numeroBillet + ".png";

        // Chemin complet du fichier
        // Ex: uploads/qrcodes/BZC-abc123.png
        Path filePath = uploadPath.resolve(fileName);

        // Convertir la matrice de bits en image PNG et sauvegarder
        // MatrixToImageWriter fait la magie :
        // - Crée une BufferedImage avec pixels noir/blanc
        // - Écrit l'image en format PNG sur le disque
        MatrixToImageWriter.writeToPath(
                bitMatrix,     // La matrice de bits
                "PNG",         // Format de l'image (PNG, JPG, GIF...)
                filePath       // Où sauvegarder
        );

        System.out.println("✅ Image sauvegardée : " + filePath.toAbsolutePath());

        // ============================================
        // ÉTAPE 4 : RETOURNER L'URL D'ACCÈS
        // ============================================

        // Construire l'URL complète
        // Ex: http://localhost:8080/qrcodes/BZC-abc123.png
        String qrcodeUrl = baseUrl + "/" + fileName;

        System.out.println("✅ URL du QR Code : " + qrcodeUrl);

        return qrcodeUrl;
    }

    // ============================================
    // MÉTHODE BONUS : SUPPRIMER UN QR CODE
    // ============================================

    /**
     * Supprime le fichier QR Code du disque.
     * Utilisé quand on supprime un billet.

     * POURQUOI ? Pour éviter d'accumuler des fichiers orphelins.
     *
     * @param qrcodeUrl L'URL du QR Code à supprimer
     * @return true si supprimé avec succès, false sinon
     */
    public boolean deleteQRCode(String qrcodeUrl) {
        // Si l'URL est vide, rien à supprimer
        if (qrcodeUrl == null || qrcodeUrl.isEmpty()) {
            return false;
        }

        try {
            // Extraire le nom du fichier de l'URL
            // Ex: http://localhost:8080/qrcodes/BZC-abc123.png → BZC-abc123.png
            String fileName = qrcodeUrl.substring(qrcodeUrl.lastIndexOf("/") + 1);

            // Construire le chemin complet
            Path filePath = Paths.get(uploadDir).resolve(fileName);

            // Supprimer le fichier (retourne true si supprimé, false si n'existait pas)
            boolean deleted = Files.deleteIfExists(filePath);

            if (deleted) {
                System.out.println("✅ QR Code supprimé : " + fileName);
            } else {
                System.out.println("⚠️ QR Code introuvable : " + fileName);
            }

            return deleted;

        } catch (IOException e) {
            System.err.println("❌ Erreur lors de la suppression du QR Code : " + e.getMessage());
            return false;
        }
    }

    // ============================================
    // MÉTHODE BONUS : RÉGÉNÉRER UN QR CODE
    // ============================================

    /**
     * Régénère un QR Code (utile si l'image est corrompue).

     * PROCESSUS :
     * 1. Supprimer l'ancien fichier
     * 2. Générer un nouveau
     *
     * @param numeroBillet Le numéro du billet
     * @return La nouvelle URL du QR Code
     */
    public String regenerateQRCode(String numeroBillet) throws IOException, WriterException {
        // Construire l'URL de l'ancien QR Code
        String oldUrl = baseUrl + "/" + numeroBillet + ".png";

        // Supprimer l'ancien (si existe)
        deleteQRCode(oldUrl);

        // Générer un nouveau
        return generateQRCode(numeroBillet);
    }
}