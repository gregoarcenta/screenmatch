package org.arcentales.screenmatch.models;

import jakarta.persistence.*;

import java.util.List;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String titulo;

    private Integer totalTemporadas;

    private Double evaluacion;

    private String poster;

    @Enumerated(EnumType.STRING)
    private Categoria genero;

    private String actores;

    private String sinopsis;

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios;

    public Serie() {}

    public Serie(DatosSerie datos) {
        this.titulo = datos.titulo();
        this.totalTemporadas = datos.totalTemporadas();
        this.evaluacion = OptionalDouble.of(Double.parseDouble(datos.evaluacion())).orElse(0);
        this.poster = datos.poster();
        this.genero = Categoria.fromString(datos.genero().split(",")[0].trim());
        this.actores = datos.actores();
        this.sinopsis = datos.sinopsis();
    }


    @Override
    public String toString() {
        return "titulo='" +
               titulo +
               '\'' +
               ", genero=" +
               genero +
               ", totalTemporadas=" +
               totalTemporadas +
               ", evaluacion=" +
               evaluacion +
               ", poster='" +
               poster +
               '\'' +
               ", actores='" +
               actores +
               '\'' +
               ", sinopsis='" +
               sinopsis +
               ", episodios='" +
               episodios +
               '\'';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(Double evaluacion) {
        this.evaluacion = evaluacion;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getActores() {
        return actores;
    }

    public void setActores(String actores) {
        this.actores = actores;
    }

    public String getSinopsis() {
        return sinopsis;
    }

    public void setSinopsis(String sinopsis) {
        this.sinopsis = sinopsis;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));
        this.episodios = episodios;
    }
}
