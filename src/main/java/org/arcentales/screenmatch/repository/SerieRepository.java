package org.arcentales.screenmatch.repository;

import org.arcentales.screenmatch.models.Categoria;
import org.arcentales.screenmatch.models.Episodio;
import org.arcentales.screenmatch.models.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainsIgnoreCase(String titulo);

    List<Serie> findTop5ByOrderByEvaluacionDesc();

    List<Serie> findByGenero(Categoria genero);

//    List<Serie> findAllByTotalTemporadasIsLessThanEqualAndEvaluacionIsGreaterThanEqual(Integer temporadas, Double evaluacion);

    @Query("SELECT s FROM Serie s where s.totalTemporadas <= :totalTemporadas and s.evaluacion >= :evaluacion")
    List<Serie> seriesPorTemporadasYEvaluacion(@Param("totalTemporadas") int totalTemporadas, double evaluacion);

    //    Nueva lista de episodios por serie
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:nombreEpisodio%")
    List<Episodio> episodiosPorNombre(@Param("nombreEpisodio") String nombreEpisodio);

    //    Top 5 episodios
    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.evaluacion DESC LIMIT 5")
    List<Episodio> top5EpisodiosPorSerie(Serie serie);

    @Query("SELECT s FROM Serie s JOIN s.episodios e GROUP BY s ORDER BY MAX(e.fechaDeLanzamiento) DESC LIMIT 5")
    List<Serie> getMostRecentReleases();


}
