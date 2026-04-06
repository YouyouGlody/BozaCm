package com.logondigital.bozacm.exceptions;

import com.logondigital.bozacm.dto.common.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
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
 * Gestionnaire global des exceptions pour l'application.
 *  Cette classe centralise la gestion de toutes les exceptions et retourne des ErrorMessage standardisés.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // ════════════════════════════════════════════════════════════════════════
    // EXCEPTIONS MÉTIER
    // ════════════════════════════════════════════════════════════════════════

    /**
     * Gère les exceptions de ressource non trouvée (404).
     */
    @ExceptionHandler(RessourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleRessourceNotFoundException(RessourceNotFoundException exception, HttpServletRequest request) {

        ErrorMessage error = new ErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now(),
                exception.getMessage(),
                "Élément non trouvé"
        );
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Gère l'exception d'email déjà existant (409).
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleEmailAlreadyExists(EmailAlreadyExistsException exception, HttpServletRequest request) {

        ErrorMessage error = new ErrorMessage(
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now(),
                "Cet email est déjà utilisé par un autre compte. Veuillez utiliser un email différent.",
                "Email déjà utilisé"
        );
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Gère les exceptions de logique métier (400).
     */
    @ExceptionHandler(InvalidReservationException.class)
    public ResponseEntity<ErrorMessage> handleBusinessLogicExceptions(Exception exception, HttpServletRequest request) {

        ErrorMessage error = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                exception.getMessage(),
                "Erreur de validation métier"
        );
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
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

        // Map des erreurs par champ
        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        // Réponse complète
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

    /**
     * Gère les NullPointerException (400).
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorMessage> handleNullPointerException(NullPointerException exception, HttpServletRequest request) {

        ErrorMessage error = new ErrorMessage(
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now(),
                "Une information importante est manquante. Veuillez compléter toutes les informations requises.",
                "Informations incomplètes"
        );
        error.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Gère toutes les exceptions non prévues (500).
     */
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorMessage> handleGlobalException(Exception exception, HttpServletRequest request) {
//
//        ErrorMessage error = new ErrorMessage(
//                HttpStatus.INTERNAL_SERVER_ERROR.value(),
//                LocalDateTime.now(),
//                "Une erreur interne s'est produite. Veuillez réessayer plus tard.",
//                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
//        );
//        error.setPath(request.getRequestURI());
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
//    }
}

/*

   Changements apportés

   Ajout du paramètre HttpServletRequest pour récupérer le path
   Utilisation de error.setPath() pour traçabilité
   Suppression de @ResponseStatus (redondant avec ResponseEntity.status())
   Format de validation amélioré avec errors détaillés
   Organisation par sections pour clarté
 */