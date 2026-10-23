package com.efrei.footbooking.controller;

import com.efrei.footbooking.dto.CreerReservationRequest;
import com.efrei.footbooking.dto.ReservationDto;
import com.efrei.footbooking.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping
    public ResponseEntity<ReservationDto> reserver(@Valid @RequestBody CreerReservationRequest demande) {
        ReservationDto reservation = reservationService.reserver(demande);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservation.id())
                .toUri();
        return ResponseEntity.created(location).body(reservation);
    }

    @GetMapping("/{id}")
    public ReservationDto consulter(@PathVariable Long id) {
        return reservationService.consulter(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void annuler(@PathVariable Long id) {
        reservationService.annuler(id);
    }
}
