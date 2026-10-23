package com.efrei.footbooking.service;

import com.efrei.footbooking.dto.TerrainDto;
import com.efrei.footbooking.repository.TerrainRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TerrainService {

    private final TerrainRepository terrainRepository;

    public TerrainService(TerrainRepository terrainRepository) {
        this.terrainRepository = terrainRepository;
    }

    @Transactional(readOnly = true)
    public List<TerrainDto> listerTerrains() {
        return terrainRepository.findAll(Sort.by("nom"))
                .stream()
                .map(TerrainDto::from)
                .toList();
    }
}