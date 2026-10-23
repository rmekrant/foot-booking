package com.efrei.footbooking.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "terrain_id", nullable = false)
    private Terrain terrain;

    @Column(name = "date_reservation", nullable = false)
    private LocalDate dateReservation;

    @Column(name = "heure_debut", nullable = false)
    private LocalTime heureDebut;

    @Column(name = "nom_client", nullable = false, length = 100)
    private String nomClient;

    @Column(name = "date_creation", nullable = false, updatable = false)
    private LocalDateTime dateCreation;

    protected Reservation() {
        // requis par JPA
    }

    public Reservation(Terrain terrain, LocalDate dateReservation, LocalTime heureDebut,
                       String nomClient, LocalDateTime dateCreation) {
        this.terrain = terrain;
        this.dateReservation = dateReservation;
        this.heureDebut = heureDebut;
        this.nomClient = nomClient;
        this.dateCreation = dateCreation;
    }

    public Long getId() { return id; }
    public Terrain getTerrain() { return terrain; }
    public LocalDate getDateReservation() { return dateReservation; }
    public LocalTime getHeureDebut() { return heureDebut; }
    public String getNomClient() { return nomClient; }
    public LocalDateTime getDateCreation() { return dateCreation; }
}