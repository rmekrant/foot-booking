package com.efrei.footbooking.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RessourceIntrouvableException.class)
    public ProblemDetail handleRessourceIntrouvable(RessourceIntrouvableException ex) {
        return probleme(HttpStatus.NOT_FOUND, "Ressource introuvable", ex.getMessage());
    }

    @ExceptionHandler(CreneauDejaReserveException.class)
    public ProblemDetail handleCreneauDejaReserve(CreneauDejaReserveException ex) {
        return probleme(HttpStatus.CONFLICT, "Créneau déjà réservé", ex.getMessage());
    }

    @ExceptionHandler(CreneauInvalideException.class)
    public ProblemDetail handleCreneauInvalide(CreneauInvalideException ex) {
        return probleme(HttpStatus.BAD_REQUEST, "Créneau invalide", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> erreurs = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        erreur -> erreur.getDefaultMessage() == null ? "Valeur invalide" : erreur.getDefaultMessage(),
                        (premier, second) -> premier));

        ProblemDetail probleme = probleme(HttpStatus.BAD_REQUEST, "Requête invalide",
                "Certains champs sont invalides");
        probleme.setProperty("erreurs", erreurs);
        return probleme;
    }

    private ProblemDetail probleme(HttpStatus statut, String titre, String detail) {
        ProblemDetail probleme = ProblemDetail.forStatusAndDetail(statut, detail);
        probleme.setTitle(titre);
        return probleme;
    }
}
