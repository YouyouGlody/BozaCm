package com.logondigital.bozacm.exceptions;

import com.logondigital.bozacm.dto.ErrorMessage;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {


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



    @ExceptionHandler(RessourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleRessourceNotFoundException(Exception exception) {
        return ResponseEntity.status(404).body(
                new ErrorMessage(
                        404,
                        LocalDateTime.now(),
                        exception.getMessage(),
                        HttpStatus.NOT_FOUND.getReasonPhrase()
                )
        );

    }

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

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(InvalidReservationException.class)
//    public Map<String, String> handleInvalidReservationException(
//            InvalidReservationException ex) {
//        Map<String, String> errors = new HashMap<>();
//        errors.put("message", ex.getMessage());
//            }

    // Invalid reservation / client / billet
    @ExceptionHandler({InvalidReservationException.class})
    public ResponseEntity<ErrorMessage> handleInvalidReservation(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ErrorMessage(
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now(),
                        ex.getMessage(),
                        HttpStatus.BAD_REQUEST.getReasonPhrase()
                )
        );
    }


}
