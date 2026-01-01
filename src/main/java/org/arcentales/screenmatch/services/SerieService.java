package org.arcentales.screenmatch.services;

import org.arcentales.screenmatch.dto.EpisodioDto;
import org.arcentales.screenmatch.dto.SerieDto;
import org.arcentales.screenmatch.models.*;
import org.arcentales.screenmatch.repository.SerieRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SerieService {
    private final SerieRepository repository;
    private final ConsumoAPI consumoApi = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();

    public SerieService(SerieRepository repository) {
        this.repository = repository;
    }

    public String createSerie(String serieName) {
        String URL_BASE = "https://www.omdbapi.com/?t=";
        String API_KEY = "&apikey=f012c411";
        var formattedName = serieName.replace(" ", "+");
        var json = consumoApi.obtenerDatos(URL_BASE + formattedName + API_KEY);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);

        if (datos.titulo() != null) {
            Serie serie = new Serie(datos);

            // Buscar episodios por cada temporada
            List<DatosTemporadas> temporadas = new ArrayList<>();
            for (int i = 1; i <= serie.getTotalTemporadas(); i++) {
                var jsonTemporada = consumoApi.obtenerDatos(URL_BASE + formattedName + "&season=" + i + API_KEY);
                DatosTemporadas datosTemporada = conversor.obtenerDatos(jsonTemporada, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }

            // Convertir DatosTemporadas a Entidades Episodio
            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numero(), e)))
                    .toList();

            // Asignar los episodios a la serie (esto establece la relación bidireccional)
            serie.setEpisodios(episodios);

            repository.save(serie);
            return "Serie y episodios guardados con éxito: " + serie.getTitulo();
        }
        return "No se pudo encontrar la serie.";
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
        return serie.map(value -> new SerieDto(value.getId(),
                                               value.getTitulo(),
                                               value.getTotalTemporadas(),
                                               value.getEvaluacion(),
                                               value.getPoster(),
                                               value.getGenero(),
                                               value.getActores(),
                                               value.getSinopsis()
        ));
    }

    public List<EpisodioDto> getSeasonBySerie(Long serieId, String season) {
        if (!season.equals("all")) return this.episodesToEpisodeDto(this.repository.getSeasonBySerie(serieId, season));

        Optional<Serie> serie = repository.findById(serieId);

        if (serie.isPresent()) {
            var foundSerie = serie.get();
            return episodesToEpisodeDto(foundSerie.getEpisodios());
        }
        return null;
    }

    public List<SerieDto> getSeriesByCategory(String category) {
        var categoria = Categoria.fromEspanol(category);
        return seriesToSerieDto(this.repository.findByGenero(categoria));
    }

    public List<SerieDto> seriesToSerieDto(List<Serie> series) {
        return series.stream()
                .map(serie -> new SerieDto(serie.getId(),
                                           serie.getTitulo(),
                                           serie.getTotalTemporadas(),
                                           serie.getEvaluacion(),
                                           serie.getPoster(),
                                           serie.getGenero(),
                                           serie.getActores(),
                                           serie.getSinopsis()
                ))
                .toList();
    }

    public List<EpisodioDto> episodesToEpisodeDto(List<Episodio> episodes) {
        return episodes.stream()
                .map(episode -> new EpisodioDto(episode.getTemporada(),
                                                episode.getTitulo(),
                                                episode.getNumeroEpisodio()
                ))
                .toList();
    }

}
