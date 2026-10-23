package com.efrei.footbooking.model;

import jakarta.persistence.*;

@Entity
@Table(name = "terrain")
public class Terrain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nom;

    @Column(nullable = false)
    private String adresse;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TypeTerrain type;

    protected Terrain() {
        // requis par JPA
    }

    public Terrain(String nom, String adresse, TypeTerrain type) {
        this.nom = nom;
        this.adresse = adresse;
        this.type = type;
    }

    public Long getId() { return id; }
    public String getNom() { return nom; }
    public String getAdresse() { return adresse; }
    public TypeTerrain getType() { return type; }
}