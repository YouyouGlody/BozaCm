package com.logondigital.bozacm.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 *  RÔLE : Format standardisé pour TOUTES les erreurs de l'API.

 *  DIFFÉRENCE AVEC ApiResponse ?
 * - ApiResponse : Pour les SUCCÈS (200, 201)
 * - ErrorMessage : Pour les ERREURS (400, 404, 409, 500)

 *  EXEMPLE JSON :
 * {
 *   "statusCode": 404,
 *   "timestamp": "15/10/2025 14:30:00",
 *   "message": "Client non trouvé avec l'ID: 999",
 *   "description": "Ressource non trouvée",
 *   "path": "/api/v1/clients/999"
 * }
 */

@Setter
@Getter
public class ErrorMessage {

    /**
     * Code HTTP de l'erreur.

     *  CODES COURANTS :
     * - 400 : Bad Request (validation échouée)
     * - 404 : Not Found (ressource introuvable)
     * - 409 : Conflict (email déjà utilisé)
     * - 500 : Internal Server Error (bug serveur)
     */
    private Integer statusCode;

    /**
     * Date et heure de l'erreur.
     * Format : dd/MM/yyyy HH:mm:ss
     */
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    LocalDateTime timestamp;

    /**
     * Message d'erreur détaillé.

     * 📊 EXEMPLES :
     * - "Client non trouvé avec l'ID: 999"
     * - "Cet email est déjà utilisé..."
     * - "Le nom doit contenir entre 2 et 50 caractères"
     */
    private String message;

    /**
     * Chemin de la requête qui a causé l'erreur.

     *  EXEMPLE :
     * "/api/v1/clients/999"

     * 💡 UTILITÉ :
     * Aide le frontend à identifier quelle requête a échoué
     */
    String path;

    /**
     * L'erreur proprement dit
     */
    private String error;


    public ErrorMessage() {
    }

    /**
     * Constructeur pour rétrocompatibilité (sans path).

     *  POURQUOI ?
     * Tu as peut-être du code existant qui utilise ErrorMessage sans path.
     * Ce constructeur permet de ne pas casser ce code.
     */
    public ErrorMessage(Integer statusCode, LocalDateTime timestamp, String message, String error) {
        this.statusCode = statusCode;
        this.timestamp = timestamp;
        this.message = message;
        this.error = error;
    }

}


// ════════════════════════════════════════════════════════════════════════════
// UTILISATION DANS GlobalExceptionHandler
// ════════════════════════════════════════════════════════════════════════════

/**
 * 📊 EXEMPLE 1 : 404 Not Found
 *
 * @ExceptionHandler(RessourceNotFoundException.class)
 * public ResponseEntity<ErrorMessage> handleNotFound(
 *         RessourceNotFoundException ex,
 *         HttpServletRequest request) {

 *     ErrorMessage error = new ErrorMessage(
 *             404,
 *             LocalDateTime.now(),
 *             ex.getMessage(),  // "Client non trouvé avec l'ID: 999"
 *             "Ressource non trouvée",
 *             request.getRequestURI()  // "/api/v1/clients/999"
 *     );

 *     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
 * }

 * JSON généré :
 * {
 *   "statusCode": 404,
 *   "timestamp": "15/10/2025 14:30:00",
 *   "message": "Client non trouvé avec l'ID: 999",
 *   "description": "Ressource non trouvée",
 *   "path": "/api/v1/clients/999"
 * }
 */

/**
 * 📊 EXEMPLE 2 : 409 Conflict (Email déjà utilisé)
 *
 * @ExceptionHandler(EmailAlreadyExistsException.class)
 * public ResponseEntity<ErrorMessage> handleEmailExists(
 *         EmailAlreadyExistsException ex,
 *         HttpServletRequest request) {

 *     ErrorMessage error = new ErrorMessage(
 *             409,
 *             LocalDateTime.now(),
 *             "Cet email est déjà utilisé par un autre compte.",
 *             "Email déjà utilisé",
 *             request.getRequestURI()
 *     );

 *     return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
 * }
 */