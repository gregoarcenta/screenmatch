package org.arcentales.screenmatch.controllers;

import org.arcentales.screenmatch.dto.EpisodioDto;
import org.arcentales.screenmatch.dto.SerieDto;
import org.arcentales.screenmatch.services.SerieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController @RequestMapping("/series") public class SerieController {
    private final SerieService serieService;

    SerieController(SerieService service) {
        this.serieService = service;
    }

    @GetMapping()
    public List<SerieDto> getSeries() {
        return serieService.getSeries();
    }

    @GetMapping("/top5")
    public List<SerieDto> getTop5Series() {
        return serieService.getTop5Series();
    }

    @GetMapping("/lanzamientos")
    public List<SerieDto> getMostRecentReleases() {
        return serieService.getMostRecentReleases();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SerieDto> getSerieById(@PathVariable Long id) {
        return serieService.getSerieById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/seasons/all")
    public ResponseEntity<List<EpisodioDto>> getSeasonsBySerieId(@PathVariable Long id) {
        return this.serieService.getSeasonsBySerieId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.ok(Collections.emptyList()));
    }
}
