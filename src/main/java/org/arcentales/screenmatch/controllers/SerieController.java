package org.arcentales.screenmatch.controllers;

import org.arcentales.screenmatch.dto.EpisodioDto;
import org.arcentales.screenmatch.dto.SerieDto;
import org.arcentales.screenmatch.dto.SerieRequestDTO;
import org.arcentales.screenmatch.repository.SerieRepository;
import org.arcentales.screenmatch.services.SerieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController @RequestMapping("/series") public class SerieController {
    private final SerieService serieService;

    SerieController(SerieService service, SerieRepository serieRepository) {
        this.serieService = service;
    }

    @PostMapping
    public String crearSeriePorTitulo(@RequestBody SerieRequestDTO data) {
        return serieService.createSerie(data.nombreSerie());
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

    @GetMapping("/{serieId}/seasons/{season}")
    public List<EpisodioDto> getSeasonBySerie(
            @PathVariable Long serieId,
            @PathVariable String season
    ) {
        return this.serieService.getSeasonBySerie(serieId, season);
    }

    @GetMapping("/categoria/{categoria}")
    public List<SerieDto> getSeriesByCategory(@PathVariable String categoria) {
        return this.serieService.getSeriesByCategory(categoria);
    }
}
