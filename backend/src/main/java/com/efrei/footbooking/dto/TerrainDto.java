package com.efrei.footbooking.dto;

import com.efrei.footbooking.model.Terrain;
import com.efrei.footbooking.model.TypeTerrain;

public record TerrainDto(Long id, String nom, String adresse, TypeTerrain type) {

    public static TerrainDto from(Terrain terrain) {
        return new TerrainDto(
                terrain.getId(),
                terrain.getNom(),
                terrain.getAdresse(),
                terrain.getType()
        );
    }
}