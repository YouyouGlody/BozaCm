package com.logondigital.bozacm.exceptions;

import com.logondigital.bozacm.DTO.common.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire global unique des exceptions pour l'application.
 * Centralise toute la gestion des exceptions et retourne des ErrorMessage standardisés.
 *
 * Il existait auparavant deux classes @RestControllerAdvice distinctes
 * (GlobalExceptionHandler et CustomGlobalExceptionHandler) qui géraient
 * toutes les deux RessourceNotFoundException et MethodArgumentNotValidException.
 * Cette ambiguïté faisait échouer la résolution du handler à l'exécution,
 * et la requête retombait alors sur la page d'erreur 500 par défaut de
 * Spring Boot au lieu d'une réponse JSON propre. Les deux classes ont été
 * fusionnées ici en une seule pour éliminer le conflit.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ════════════════════════════════════════════════════════════════════════
    // EXCEPTIONS MÉTIER
    // ════════════════════════════════════════════════════════════════════════

    @ExceptionHandler(RessourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleRessourceNotFoundException(RessourceNotFoundException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND, exception.getMessage(), "Élément non trouvé", request);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleEmailAlreadyExists(EmailAlreadyExistsException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT,
                "Cet email est déjà utilisé par un autre compte. Veuillez utiliser un email différent.",
                "Email déjà utilisé", request);
    }

    @ExceptionHandler(PhoneAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handlePhoneAlreadyExists(PhoneAlreadyExistsException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.CONFLICT,
                "Ce numéro de téléphone est déjà utilisé par un autre compte. Veuillez utiliser un numéro différent.",
                "Téléphone déjà utilisé", request);
    }

    @ExceptionHandler(InvalidReservationException.class)
    public ResponseEntity<ErrorMessage> handleBusinessLogicExceptions(InvalidReservationException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), "Erreur de validation métier", request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgument(IllegalArgumentException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), "Requête invalide", request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorMessage> handleIllegalState(IllegalStateException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), "Opération impossible", request);
    }

    // ════════════════════════════════════════════════════════════════════════
    // VALIDATION
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Gère les erreurs de validation Bean Validation (400).
     * Retourne un format spécial avec détails des erreurs par champ.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        Map<String, Object> response = new HashMap<>();
        response.put("statusCode", HttpStatus.BAD_REQUEST.value());
        response.put("timestamp", LocalDateTime.now());
        response.put("message", "Erreurs de validation");
        response.put("error", "Validation échouée");
        response.put("path", request.getRequestURI());
        response.put("errors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    // ════════════════════════════════════════════════════════════════════════
    // EXCEPTIONS SYSTÈME
    // ════════════════════════════════════════════════════════════════════════

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorMessage> handleNullPointerException(NullPointerException exception, HttpServletRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST,
                "Une information importante est manquante. Veuillez compléter toutes les informations requises.",
                "Informations incomplètes", request);
    }

    /**
     * Filet de sécurité : toute exception non prévue explicitement ci-dessus
     * retourne un JSON structuré (ErrorMessage) au lieu de la page d'erreur
     * HTML/JSON par défaut de Spring Boot. Reste un vrai bug à corriger côté
     * service si ce handler se déclenche souvent — mais le frontend recevra
     * toujours une réponse exploitable.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGlobalException(Exception exception, HttpServletRequest request) {
        log.error("Erreur non gérée sur {} :", request.getRequestURI(), exception);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                "Une erreur interne s'est produite. Veuillez réessayer plus tard.",
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), request);
    }

    // ════════════════════════════════════════════════════════════════════════
    // HELPER
    // ════════════════════════════════════════════════════════════════════════

    private ResponseEntity<ErrorMessage> buildResponse(HttpStatus status, String message, String error, HttpServletRequest request) {
        ErrorMessage body = new ErrorMessage(status.value(), LocalDateTime.now(), message, error);
        body.setPath(request.getRequestURI());
        return ResponseEntity.status(status).body(body);
    }
}
