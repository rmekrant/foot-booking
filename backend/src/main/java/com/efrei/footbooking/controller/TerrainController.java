package com.efrei.footbooking.controller;

import com.efrei.footbooking.dto.CreneauDto;
import com.efrei.footbooking.dto.TerrainDto;
import com.efrei.footbooking.service.CreneauService;
import com.efrei.footbooking.service.TerrainService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/terrains")
public class TerrainController {

    private final TerrainService terrainService;
    private final CreneauService creneauService;

    public TerrainController(TerrainService terrainService, CreneauService creneauService) {
        this.terrainService = terrainService;
        this.creneauService = creneauService;
    }

    @GetMapping
    public List<TerrainDto> listerTerrains() {
        return terrainService.listerTerrains();
    }

    @GetMapping("/{id}/creneaux")
    public List<CreneauDto> listerCreneaux(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return creneauService.listerCreneaux(id, date);
    }
}