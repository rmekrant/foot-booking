package com.efrei.footbooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RessourceIntrouvableException.class)
    public ProblemDetail handleRessourceIntrouvable(RessourceIntrouvableException ex) {
        ProblemDetail probleme = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        probleme.setTitle("Ressource introuvable");
        return probleme;
    }
}