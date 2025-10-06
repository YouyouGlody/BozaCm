package com.logondigital.bozacm.exceptions;

import com.logondigital.bozacm.dto.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Gestionnaire global des exceptions pour l'application
 * Cette classe centralise la gestion de toutes les exceptions et fournit des réponses HTTP appropriées
 */
@RestControllerAdvice
public class GlobalExceptionHandler {


    /**
     * Gestionnaire pour toutes les exceptions non spécifiquement gérées ailleurs
     * Ce gestionnaire sert de "fallback" pour les exceptions imprévues
     * @param exception L'exception non gérée
     * @return Une réponse 500 (Internal Server Error) avec un message d'erreur
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleGlobalException(Exception exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorMessage(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        LocalDateTime.now(),
                        "Une erreur inattendue s'est produite: " + exception.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
                )
        );
    }


    /**
     * Gestionnaire spécifique pour les erreurs de pointeur nul
     * @param exception L'exception NullPointerException
     * @return Une réponse avec un message d'erreur clair
     */
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorMessage> handleNullPointerException(NullPointerException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorMessage(
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now(),
                        "Une information importante est manquante dans votre profil. Veuillez compléter toutes les informations requises.",
                        "Informations incomplètes"
                )
        );
    }




    /**
     * Gestionnaire pour les exceptions de type "ressource non trouvée"
     * Utilisé lorsqu'une entité demandée n'existe pas dans le système
     * @param exception L'exception RessourceNotFoundException
     * @return Une réponse 404 (Not Found) avec les détails de la ressource non trouvée
     */
    @ExceptionHandler(RessourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleRessourceNotFoundException(Exception exception) {
        return ResponseEntity.status(404).body(
                new ErrorMessage(
                        404,
                        LocalDateTime.now(),
                        exception.getMessage(),
                        "Élément non trouvé"


                )
        );

    }


    /**
     * Gestionnaire pour les erreurs de validation des données d'entrée
     * Utilisé lorsque les contraintes de validation Bean Validation ne sont pas respectées
     * @param ex L'exception de validation des arguments de méthode
     * @return Une carte des erreurs de validation, où la clé est le nom du champ et la valeur est le message d'erreur.
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }



    // Gérer les exceptions de contraintes métier
    // Invalid reservation / client / billet
    /**
     * Gestionnaire pour les exceptions liées à la logique métier
     * Gère plusieurs types d'exceptions relatives aux règles métier de l'application
     *
     * @param ex Une exception de type métier (InvalidReservationException ou EmailAlreadyExistsException)
     * @return Une réponse 400 (Bad Request) avec les détails de l'erreur métier
     */
    @ExceptionHandler({
            InvalidReservationException.class,
    })
    public ResponseEntity<ErrorMessage> handleBusinessLogicExceptions(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorMessage(
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now(),
                        ex.getMessage(),
                        "Erreur de validation"

                )
        );
    }


    /**
     * Gestionnaire pour l'exception d'email déjà existant
     * @param ex L'exception EmailAlreadyExistsException
     * @return Une réponse avec un message d'erreur clair
     */
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorMessage> handleEmailAlreadyExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorMessage(
                        HttpStatus.CONFLICT.value(),
                        LocalDateTime.now(),
                        "Cet email est déjà utilisé par un autre compte. Veuillez utiliser un email différent.",
                        "Email déjà utilisé"
                )
        );
    }



}
