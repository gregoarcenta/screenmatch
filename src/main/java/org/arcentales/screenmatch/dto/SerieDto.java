package org.arcentales.screenmatch.dto;

import org.arcentales.screenmatch.models.Categoria;

public record SerieDto(
        Long id,
        String titulo,
        Integer totalTemporadas,
        Double evaluacion,
        String poster,
        Categoria genero,
        String actores,
        String sinopsis
) {}
