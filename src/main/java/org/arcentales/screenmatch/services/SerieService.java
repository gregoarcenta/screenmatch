package org.arcentales.screenmatch.services;

import org.arcentales.screenmatch.dto.EpisodioDto;
import org.arcentales.screenmatch.dto.SerieDto;
import org.arcentales.screenmatch.models.Serie;
import org.arcentales.screenmatch.repository.SerieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SerieService {
    private final SerieRepository repository;

    public SerieService(SerieRepository repository) {
        this.repository = repository;
    }

    public List<SerieDto> getSeries() {
        return seriesToSerieDto(this.repository.findAll());
    }

    public List<SerieDto> getTop5Series() {
        var series = this.repository.findTop5ByOrderByEvaluacionDesc();
        return seriesToSerieDto(series);
    }


    public List<SerieDto> getMostRecentReleases() {
        var series = this.repository.getMostRecentReleases();
        return seriesToSerieDto(series);
    }

    public Optional<SerieDto> getSerieById(Long id) {
        Optional<Serie> serie = repository.findById(id);
        return serie.map(value -> new SerieDto(
                value.getId(),
                value.getTitulo(),
                value.getTotalTemporadas(),
                value.getEvaluacion(),
                value.getPoster(),
                value.getGenero(),
                value.getActores(),
                value.getSinopsis()
        ));
    }

    public Optional<List<EpisodioDto>> getSeasonsBySerieId(Long id) {
        Optional<Serie> serie = repository.findById(id);
        return serie.map(value -> {
            System.out.println(value.getEpisodios());
            return value.getEpisodios().stream().map(episodio -> (new EpisodioDto(
                    episodio.getTemporada(),
                    episodio.getTitulo(),
                    episodio.getNumeroEpisodio()
            )
            )).toList();
        });
    }

    public List<SerieDto> seriesToSerieDto(List<Serie> series) {
        return series.stream().map(serie -> new SerieDto(
                serie.getId(),
                serie.getTitulo(),
                serie.getTotalTemporadas(),
                serie.getEvaluacion(),
                serie.getPoster(),
                serie.getGenero(),
                serie.getActores(),
                serie.getSinopsis()
        )).toList();
    }

}
