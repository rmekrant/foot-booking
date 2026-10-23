package com.efrei.footbooking.controller;

import com.efrei.footbooking.dto.TerrainDto;
import com.efrei.footbooking.service.TerrainService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/terrains")
public class TerrainController {

    private final TerrainService terrainService;

    public TerrainController(TerrainService terrainService) {
        this.terrainService = terrainService;
    }

    @GetMapping
    public List<TerrainDto> listerTerrains() {
        return terrainService.listerTerrains();
    }
}