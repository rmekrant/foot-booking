package com.efrei.footbooking.service;

import com.efrei.footbooking.dto.CreneauDto;
import com.efrei.footbooking.dto.StatutCreneau;
import com.efrei.footbooking.exception.RessourceIntrouvableException;
import com.efrei.footbooking.repository.ReservationRepository;
import com.efrei.footbooking.repository.TerrainRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CreneauService {

    public static final LocalTime HEURE_OUVERTURE = LocalTime.of(9, 0);
    public static final LocalTime HEURE_FERMETURE = LocalTime.of(23, 0);
    public static final Duration DUREE_CRENEAU = Duration.ofHours(1);

    private final TerrainRepository terrainRepository;
    private final ReservationRepository reservationRepository;
    private final Clock clock;

    public CreneauService(TerrainRepository terrainRepository,
                          ReservationRepository reservationRepository,
                          Clock clock) {
        this.terrainRepository = terrainRepository;
        this.reservationRepository = reservationRepository;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public List<CreneauDto> listerCreneaux(Long terrainId, LocalDate date) {
        if (!terrainRepository.existsById(terrainId)) {
            throw new RessourceIntrouvableException("Le terrain " + terrainId + " n'existe pas");
        }

        Set<LocalTime> heuresReservees = new HashSet<>(
                reservationRepository.findHeuresReservees(terrainId, date));
        LocalDateTime maintenant = LocalDateTime.now(clock);

        List<CreneauDto> creneaux = new ArrayList<>();
        for (LocalTime debut = HEURE_OUVERTURE; debut.isBefore(HEURE_FERMETURE); debut = debut.plus(DUREE_CRENEAU)) {
            StatutCreneau statut;
            if (LocalDateTime.of(date, debut).isBefore(maintenant)) {
                statut = StatutCreneau.PASSE;
            } else if (heuresReservees.contains(debut)) {
                statut = StatutCreneau.RESERVE;
            } else {
                statut = StatutCreneau.DISPONIBLE;
            }
            creneaux.add(new CreneauDto(debut, debut.plus(DUREE_CRENEAU), statut));
        }
        return creneaux;
    }
}