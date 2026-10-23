package com.efrei.footbooking.service;

import com.efrei.footbooking.dto.CreerReservationRequest;
import com.efrei.footbooking.dto.ReservationDto;
import com.efrei.footbooking.exception.CreneauDejaReserveException;
import com.efrei.footbooking.exception.CreneauInvalideException;
import com.efrei.footbooking.exception.RessourceIntrouvableException;
import com.efrei.footbooking.model.Reservation;
import com.efrei.footbooking.model.Terrain;
import com.efrei.footbooking.repository.ReservationRepository;
import com.efrei.footbooking.repository.TerrainRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TerrainRepository terrainRepository;
    private final Clock clock;

    public ReservationService(ReservationRepository reservationRepository,
                              TerrainRepository terrainRepository,
                              Clock clock) {
        this.reservationRepository = reservationRepository;
        this.terrainRepository = terrainRepository;
        this.clock = clock;
    }

    @Transactional
    public ReservationDto reserver(CreerReservationRequest demande) {
        Terrain terrain = terrainRepository.findById(demande.terrainId())
                .orElseThrow(() -> new RessourceIntrouvableException(
                        "Le terrain " + demande.terrainId() + " n'existe pas"));

        verifierCreneau(demande.date(), demande.heureDebut());

        // 1re protection : vérification applicative, pour un message d'erreur clair
        if (reservationRepository.existsByTerrainIdAndDateReservationAndHeureDebut(
                terrain.getId(), demande.date(), demande.heureDebut())) {
            throw creneauDejaReserve(demande);
        }

        Reservation reservation = new Reservation(
                terrain,
                demande.date(),
                demande.heureDebut(),
                demande.nomClient().trim(),
                LocalDateTime.now(clock));

        // 2e protection : la contrainte UNIQUE de la base, en cas de requêtes simultanées
        try {
            reservation = reservationRepository.saveAndFlush(reservation);
        } catch (DataIntegrityViolationException ex) {
            throw creneauDejaReserve(demande);
        }

        return ReservationDto.from(reservation);
    }

    @Transactional(readOnly = true)
    public ReservationDto consulter(Long id) {
        return reservationRepository.findById(id)
                .map(ReservationDto::from)
                .orElseThrow(() -> new RessourceIntrouvableException(
                        "La réservation " + id + " n'existe pas"));
    }

    @Transactional
    public void annuler(Long id) {
        if (!reservationRepository.existsById(id)) {
            throw new RessourceIntrouvableException("La réservation " + id + " n'existe pas");
        }
        reservationRepository.deleteById(id);
    }

    private void verifierCreneau(LocalDate date, LocalTime heureDebut) {
        if (!CreneauService.estUnDebutDeCreneau(heureDebut)) {
            throw new CreneauInvalideException("L'heure " + heureDebut
                    + " ne correspond à aucun créneau (créneaux d'1 h entre "
                    + CreneauService.HEURE_OUVERTURE + " et " + CreneauService.HEURE_FERMETURE + ")");
        }
        if (LocalDateTime.of(date, heureDebut).isBefore(LocalDateTime.now(clock))) {
            throw new CreneauInvalideException("Impossible de réserver un créneau déjà passé");
        }
    }

    private CreneauDejaReserveException creneauDejaReserve(CreerReservationRequest demande) {
        return new CreneauDejaReserveException("Le créneau du " + demande.date() + " à "
                + demande.heureDebut() + " est déjà réservé sur ce terrain");
    }
}
