package com.efrei.footbooking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record CreerReservationRequest(
        @NotNull(message = "Le terrain est obligatoire")
        Long terrainId,

        @NotNull(message = "La date est obligatoire")
        LocalDate date,

        @NotNull(message = "L'heure de début est obligatoire")
        @JsonFormat(pattern = "HH:mm")
        LocalTime heureDebut,

        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
        String nomClient
) {
}
