package com.logondigital.bozacm.config;

// ============================================
// IMPORTS NÉCESSAIRES
// ============================================

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * ============================================
 * CONFIGURATION DU SERVEUR WEB
 * ============================================

 * PROBLÈME À RÉSOUDRE :
 * Par défaut, Spring Boot ne sert QUE les fichiers dans src/main/resources/static/
 * Nos QR Codes sont dans uploads/qrcodes/ (en dehors du JAR)
 * → Comment y accéder via HTTP ?

 * SOLUTION :
 * Créer un "mapping" URL → Dossier
 * Comme un lien symbolique sous Linux

 * EXEMPLE CONCRET :
 * Fichier physique : D:/bozacm/uploads/qrcodes/BZC-abc123.png
 * URL accessible :   http://localhost:8080/qrcodes/BZC-abc123.png
 *                    └─────────┬─────────┘ └────────┬────────┘
 *                          baseUrl            fileName

 * ANALOGIE :
 * C'est comme dire à Apache/Nginx : "Quand on demande /qrcodes/*,
 * va chercher dans le dossier uploads/qrcodes/"
 */
@Configuration  // Spring va appliquer cette config au démarrage
public class WebConfig implements WebMvcConfigurer {

    // ============================================
    // CONFIGURATION (depuis application.properties)
    // ============================================

    /**
     * Chemin du dossier contenant les QR Codes.
     * DOIT être le même que dans QRCodeService !

     * Valeur par défaut : uploads/qrcodes
     */
    @Value("${app.qrcode.upload-dir:uploads/qrcodes}")
    private String uploadDir;

    // ============================================
    // MÉTHODE DE CONFIGURATION
    // ============================================

    /**
     * Configure le serveur pour servir les fichiers statiques.

     * EXPLICATION LIGNE PAR LIGNE :

     * addResourceHandler("/qrcodes/**")
     * │
     * └─> Définit le pattern d'URL à intercepter
     *     - /qrcodes/** = toutes les URLs qui commencent par /qrcodes/
     *     - ** = n'importe quel sous-chemin

     *     Exemples d'URLs matchées :
     *     ✅ /qrcodes/BZC-abc123.png
     *     ✅ /qrcodes/subfolder/image.png
     *     ❌ /images/logo.png (ne commence pas par /qrcodes/)

     * addResourceLocations("file:" + uploadDir + "/")
     * │                     │       │           │
     * │                     │       │           └─> Slash final important !
     * │                     │       └─> Variable (ex: uploads/qrcodes)
     * │                     └─> Préfixe obligatoire pour chemin filesystem
     * └─> Indique OÙ aller chercher les fichiers

     * PROTOCOLES POSSIBLES :
     * - file:   → Filesystem local (notre cas)
     * - classpath: → Dans resources/ du JAR
     * - http://    → Serveur distant

     * EXEMPLE COMPLET :
     * URL demandée : http://localhost:8080/qrcodes/BZC-abc123.png

     * 1. Spring intercepte : "/qrcodes/BZC-abc123.png" match le pattern "/qrcodes/**" ✅
     * 2. Spring extrait : "BZC-abc123.png" (après /qrcodes/)
     * 3. Spring construit : "file:uploads/qrcodes/" + "BZC-abc123.png"
     *                    = "file:uploads/qrcodes/BZC-abc123.png"
     * 4. Spring lit le fichier et le retourne au navigateur

     * DÉBOGAGE :
     * Si ça ne marche pas, vérifier :
     * - Le dossier uploads/qrcodes/ existe ?
     * - Les permissions de lecture ?
     * - Le chemin est relatif au répertoire d'exécution ?
     *
     * @param registry Le registre de handlers (fourni par Spring)
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // Log pour debug (optionnel mais utile)
        System.out.println("🌐 Configuration du serveur de fichiers statiques");
        System.out.println("   Pattern URL : /qrcodes/**");
        System.out.println("   Dossier     : " + uploadDir);
        System.out.println("   Chemin absolu : " +
                java.nio.file.Paths.get(uploadDir).toAbsolutePath());

        // Enregistrer le mapping
        registry
                .addResourceHandler("/qrcodes/**")              // Pattern d'URL
                .addResourceLocations("file:" + uploadDir + "/"); // Dossier source

        System.out.println(" Serveur configuré avec succès !");

        // NOTES IMPORTANTES :
        //
        // 1. SLASH FINAL : Le "/" après uploadDir est CRUCIAL !
        //    Avec    : file:uploads/qrcodes/ → OK
        //    Sans    : file:uploads/qrcodes  → ERREUR
        //
        // 2. CHEMIN RELATIF vs ABSOLU :
        //    Relatif : uploads/qrcodes → Relatif au répertoire d'exécution
        //    Absolu  : C:/bozacm/uploads/qrcodes → Chemin complet
        //
        //    En développement : relatif est OK
        //    En production : préférer absolu (plus prévisible)
        //
        // 3. SÉCURITÉ :
        //    Ce mapping rend TOUS les fichiers du dossier accessibles !
        //    Ne JAMAIS mettre de fichiers sensibles dans uploads/qrcodes/
        //    (mots de passe, clés privées, etc.)
        //
        // 4. PERFORMANCE :
        //    Pour la production, considérer :
        //    - Nginx/Apache pour servir les fichiers statiques
        //    - CDN (Cloudflare, AWS CloudFront)
        //    - Cache HTTP (ETag, Last-Modified)
    }

    // ============================================
    // CONFIGURATION BONUS (OPTIONNEL)
    // ============================================

    /**
     * OPTIONNEL : Ajouter des headers de cache.
     * Améliore les performances en évitant de retélécharger les mêmes images.

     * Décommenter pour activer :
     */
    /*
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/qrcodes/**")
            .addResourceLocations("file:" + uploadDir + "/")
            .setCachePeriod(3600)  // Cache pendant 1 heure (3600 secondes)
            .resourceChain(true);   // Activer la chaîne de traitement
    }
    */
}