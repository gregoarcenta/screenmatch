package org.arcentales.screenmatch.controllers;

import org.arcentales.screenmatch.dto.SerieDto;
import org.arcentales.screenmatch.repository.SerieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class SerieController {

    private final SerieRepository repository;

    public SerieController(SerieRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/series")
    public List<SerieDto> getSeries() {

        return repository.findAll()
                .stream()
                .map(s -> new SerieDto(s.getTitulo(),
                                       s.getTotalTemporadas(),
                                       s.getEvaluacion(),
                                       s.getPoster(),
                                       s.getGenero(),
                                       s.getActores(),
                                       s.getSinopsis()
                ))
                .toList();
    }
}
