package com.efrei.footbooking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalTime;

public record CreneauDto(
        @JsonFormat(pattern = "HH:mm") LocalTime heureDebut,
        @JsonFormat(pattern = "HH:mm") LocalTime heureFin,
        StatutCreneau statut
) {
}