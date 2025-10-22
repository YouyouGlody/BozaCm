package com.logondigital.bozacm.dto.common;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

/**
 *  RÔLE : Format standardisé pour TOUTES les réponses de succès de l'API.

 *  POURQUOI ?
 * - Le frontend sait toujours à quoi s'attendre
 * - Format unique pour tous les endpoints
 * - Facile à traiter côté frontend

 *  EXEMPLE JSON :
 * {
 *   "success": true,
 *   "message": "Client créé avec succès",
 *   "data": { ... },
 *   "timestamp": "15/10/2025 14:30:00"
 * }
 */

/**
 *  @JsonInclude(JsonInclude.Include.NON_NULL)

 *  RÔLE : Exclut les champs null du JSON généré.

 *  SANS cette annotation :
 * { "success": true, "message": "OK", "data": null, "timestamp": "..." }
 *                                             ↑ présent même si null

 *  AVEC cette annotation :
 * { "success": true, "message": "OK", "timestamp": "..." }
 *                                     ↑ data absent car null
 *
 *  AVANTAGE : JSON plus léger et propre.
 */

/**
 * ️ record (Java 14+)

 *  QU'EST-CE QU'UN RECORD ?
 * Un record est une classe Java compacte pour transporter des données.

 *  CE QUE LE RECORD GÉNÈRE AUTOMATIQUEMENT :
 * - Constructeur avec tous les paramètres
 * - Getters (success(), message(), data(), timestamp())
 * - equals(), hashCode(), toString()
 * - Tous les champs sont final (immuables)

 *  ÉQUIVALENT SANS RECORD :
 * public class ApiResponse {
 *     private final boolean success;
 *     private final String message;
 *     private final Object data;
 *     private final LocalDateTime timestamp;

 *     public ApiResponse(boolean success, String message, ...) { ... }
 *     public boolean isSuccess() { return success; }
 *     public String getMessage() { return message; }
 *     ... (50 lignes de code boilerplate)
 * }

 *  AVEC RECORD : 4 lignes au lieu de 50 !
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse(

        /**
         * Indique si l'opération a réussi.

         * - true : Succès (200, 201)
         * - false : Erreur (mais on utilise ErrorMessage pour les erreurs)

         * Note : Dans cette architecture, success est toujours true
         * car les erreurs utilisent ErrorMessage au lieu d'ApiResponse.
         */
        boolean success,

        /**
         * Message descriptif pour l'utilisateur.

         *  EXEMPLES :
         * - "Client créé avec succès"
         * - "5 client(s) récupéré(s)"
         * - "Client supprimé avec succès"
         */
        String message,

        /**
         * Les données retournées.

         * Type Object : Peut contenir n'importe quoi
         * - ClientResponseDTO (GET /clients/1)
         * - List<ClientResponseDTO> (GET /clients)
         * - Long (GET /clients/count)
         * - null (DELETE)

         *  EXEMPLES :
         * GET /clients/1 → data = ClientResponseDTO
         * GET /clients → data = List<ClientResponseDTO>
         * DELETE /clients/1 → data = null
         */
        Object data,

        /**
         * Date et heure de la réponse.
         *
         * @JsonFormat : Formate la date en JSON
         * Pattern "dd/MM/yyyy HH:mm:ss" → "15/10/2025 14:30:00"

         *  SANS @JsonFormat:
         * "timestamp": "2025-10-15T14:30:00.123456789"

         *  AVEC @JsonFormat:
         * "timestamp": "15/10/2025 14:30:00"
         */
        @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
        LocalDateTime timestamp) {



    //CONSTRUCTEUR COMPACT (Record Feature)

    /**
     * Constructeur compact du record.

     *  RÔLE : Valider/Modifier les paramètres avant l'assignation.

     *  PROCESSUS :
     * 1. Les paramètres arrivent (success, message, data, timestamp)
     * 2. Ce code s'exécute
     * 3. Les champs du record sont assignés

     * 💡 ICI : Si timestamp est null, on le génère automatiquement.
     */
    public ApiResponse {
        if (timestamp == null) {
            timestamp = LocalDateTime.now();
        }
    }

    // CONSTRUCTEURS UTILITAIRES
    /**
     * Constructeur pour succès avec données.
     * Appelle le constructeur principal avec success=true.

     *  USAGE :
     * new ApiResponse("Client créé", clientDTO);

     * ↓ Équivalent à :
     * new ApiResponse (true, "Client créé", clientDTO, LocalDateTime.now());
     */
    public ApiResponse(String message, Object data) {
        this(true, message, data, LocalDateTime.now());
    }

    /**
     * Constructeur pour succès sans données.

     *  USAGE :
     * new ApiResponse("Client supprimé");

     * ↓ Équivalent à :
     * new ApiResponse(true, "Client supprimé", null, LocalDateTime.now());
     */
    public ApiResponse(String message) {
        this(true, message, null, LocalDateTime.now());
    }




    //  MÉTHODES FACTORY (Pattern Factory). Utilisées dans les Controllers

    /**
     *  PATTERN FACTORY : Méthodes statiques pour créer des instances.

     *  POURQUOI ?
     * - Plus lisible que new ApiResponse(...)
     * - Intention claire (success, error, created, etc.)
     * - Facile à utiliser dans les controllers

     *  COMPARAISON :

     *  PEU LISIBLE :
     * return new ApiResponse(true, "Client créé", dto, LocalDateTime.now());

     *  LISIBLE :
     * return ApiResponse.success("Client créé", dto);
     */


    /**
     * Crée une réponse de succès avec données.

     *  USAGE dans Controller :
     * return ResponseEntity.ok(
     *     ApiResponse.success("Client créé avec succès", clientDTO)
     * );
     */
    public static ApiResponse success(String message, Object data) {
        return new ApiResponse(true, message, data, LocalDateTime.now());
    }


    /**
     * Crée une réponse de succès sans données.

     *  USAGE dans Controller :
     * return ResponseEntity.ok(
     *     ApiResponse.success("Opération réussie")
     * );
     */
    public static ApiResponse success(String message) {
        return new ApiResponse(true, message, null, LocalDateTime.now());
    }


    /**
     * Crée une réponse pour une création (201 Created).

     *  USAGE dans Controller :
     * return ResponseEntity.status(HttpStatus.CREATED).body(
     *     ApiResponse.created("Client créé avec succès", clientDTO)
     * );
     */
    public static ApiResponse created(String message, Object data) {
        return new ApiResponse(true, message, data, LocalDateTime.now());
    }


    /**
     * Crée une réponse pour une suppression (data = null).

     *  USAGE dans Controller :
     * return ResponseEntity.ok(
     *     ApiResponse.deleted("Client supprimé avec succès")
     * );
     */
    public static ApiResponse deleted(String message) {
        return new ApiResponse(true, message, null, LocalDateTime.now());
    }

}




// ════════════════════════════════════════════════════════════════════════════
// EXEMPLES D'UTILISATION DANS LES CONTROLLERS
// ════════════════════════════════════════════════════════════════════════════

/**
 *  EXEMPLE 1 : POST (Création)
 *
 * @PostMapping
 * public ResponseEntity<ApiResponse> create(@RequestBody ClientRequestDTO dto) {
 *     Client client = mapper.toEntity(dto);
 *     Client saved = service.create(client);
 *     ClientResponseDTO response = mapper.toResponseDTO(saved);

 *     return ResponseEntity.status(HttpStatus.CREATED)
 *         .body(ApiResponse.created("Client créé avec succès", response));
 * }

 * JSON généré :
 * {
 *   "success": true,
 *   "message": "Client créé avec succès",
 *   "data": { "idClient": 1, "nom": "Dupont", ... },
 *   "timestamp": "15/10/2025 14:30:00"
 * }
 */

/**
 *  EXEMPLE 2 : GET (Liste)
 *
 * @GetMapping
 * public ResponseEntity<ApiResponse> getAll() {
 *     List<ClientResponseDTO> clients = ...;
 *     String msg = String.format("%d client(s) récupéré(s)", clients.size());

 *     return ResponseEntity.ok(ApiResponse.success(msg, clients));
 * }

 * JSON généré :
 * {
 *   "success": true,
 *   "message": "5 client(s) récupéré(s)",
 *   "data": [ {...}, {...}, ... ],
 *   "timestamp": "15/10/2025 14:35:00"
 * }
 */

/**
 *  EXEMPLE 3 : DELETE (Suppression)
 *
 * @DeleteMapping("/{id}")
 * public ResponseEntity<ApiResponse> delete(@PathVariable Integer id) {
 *     service.delete(id);

 *     return ResponseEntity.ok(ApiResponse.deleted("Client supprimé avec succès"));
 * }

 * JSON généré :
 * {
 *   "success": true,
 *   "message": "Client supprimé avec succès",
 *   "timestamp": "15/10/2025 14:40:00"
 * }
 *
 * Note : "data" est absent car null et @JsonInclude(NON_NULL)
 */

/**
 *  EXEMPLE 4 : GET /count (Données simples)
 *
 * @GetMapping("/count")
 * public ResponseEntity<ApiResponse> count() {
 *     long count = service.count();
 *     String msg = String.format("Nombre total : %d", count);

 *     return ResponseEntity.ok(ApiResponse.success(msg, count));
 * }

 * JSON généré :
 * {
 *   "success": true,
 *   "message": "Nombre total : 42",
 *   "data": 42,
 *   "timestamp": "15/10/2025 14:45:00"
 * }
 */


// ════════════════════════════════════════════════════════════════════════════
// ALTERNATIVES
// ════════════════════════════════════════════════════════════════════════════

/**
 *  ALTERNATIVE 1 : Classe Lombok au lieu de Record
 *
 * @Data
 * @AllArgsConstructor
 * public class ApiResponse {
 *     private boolean success;
 *     private String message;
 *     private Object data;
 *     private LocalDateTime timestamp;
 * }

 * Avantages : Compatible Java 8+
 * Inconvénients : Mutable (setters générés), moins concis
 */

/**
 *  ALTERNATIVE 2 : Générique typé

 * public record ApiResponse<T>(
 *     boolean success,
 *     String message,
 *     T data,
 *     LocalDateTime timestamp
 * ) {}

 * Usage :
 * ResponseEntity<ApiResponse<ClientResponseDTO>> create(...)

 * Avantages : Type-safe
 * Inconvénients : Plus verbeux dans les signatures
 */

/**
 *  ALTERNATIVE 3 : Builder pattern

 * ApiResponse.builder()
 *     .success(true)
 *     .message("Client créé")
 *     .data(clientDTO)
 *     .build();

 * Avantages : Très flexible
 * Inconvénients : Plus verbeux que les méthodes factory
 */