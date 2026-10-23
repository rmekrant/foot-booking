package com.efrei.footbooking.dto;

import com.efrei.footbooking.model.Reservation;
import com.efrei.footbooking.service.CreneauService;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record ReservationDto(
        Long id,
        Long terrainId,
        String terrainNom,
        LocalDate date,
        @JsonFormat(pattern = "HH:mm") LocalTime heureDebut,
        @JsonFormat(pattern = "HH:mm") LocalTime heureFin,
        String nomClient
) {

    public static ReservationDto from(Reservation reservation) {
        return new ReservationDto(
                reservation.getId(),
                reservation.getTerrain().getId(),
                reservation.getTerrain().getNom(),
                reservation.getDateReservation(),
                reservation.getHeureDebut(),
                reservation.getHeureDebut().plus(CreneauService.DUREE_CRENEAU),
                reservation.getNomClient()
        );
    }
}
