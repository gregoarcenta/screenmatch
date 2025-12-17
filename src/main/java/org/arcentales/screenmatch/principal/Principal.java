package org.arcentales.screenmatch.principal;

import org.arcentales.screenmatch.models.*;
import org.arcentales.screenmatch.repository.SerieRepository;
import org.arcentales.screenmatch.services.ConsumoAPI;
import org.arcentales.screenmatch.services.ConvierteDatos;

import java.util.*;

public class Principal {
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    List<Serie> series;
    private String API_KEY;
    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private ConvierteDatos conversor = new ConvierteDatos();
    private List<DatosSerie> datosSeries = new ArrayList<>();
    private Optional<Serie> serieBuscada = Optional.empty();
    private SerieRepository serieRepository;

    public Principal(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
        String OMDB_API_KEY = "f012c411";
        this.API_KEY = "&apikey=" + OMDB_API_KEY;

    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    4 - Buscar series por titulo
                    5 - Top 5 mejores series
                    6 - Buscar series por categorías
                    7 - Filtrar series por temporadas y evaluación
                    8 - Buscar episodio por titulo
                    9 - Top 5 episodios por serie
                    
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    mostrarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriesPorTitulo();
                    break;
                case 5:
                    buscarTop5Series();
                    break;
                case 6:
                    buscarSeriesPorCategoria();
                    break;
                case 7:
                    buscarSeriesPorTemporadasYEvaluacion();
                    break;
                case 8:
                    buscarEpisodioPorTitulo();
                    break;
                case 9:
                    top5EpisodiosPorSerie();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private DatosSerie getDatosSerie() {
        System.out.println("Escribe el nombre de la serie que deseas buscar");
        var nombreSerie = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE + nombreSerie.replace(" ", "+") + API_KEY);
        System.out.println(json);
        DatosSerie datos = conversor.obtenerDatos(json, DatosSerie.class);
        return datos;
    }

    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        serieRepository.save(serie);
    }

    private void buscarEpisodioPorSerie() {
        mostrarSeriesBuscadas();
        System.out.println("Escribe el nombre de la serie que deseas ver los episodios:");
        var nombreSerie = teclado.nextLine();

        Optional<Serie> serie = this.series.stream()
                .filter(s -> s.getTitulo().toLowerCase().contains(nombreSerie.toLowerCase()))
                .findFirst();

        if (serie.isPresent()) {
            var serieEncontrada = serie.get();
            List<DatosTemporadas> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumoApi.obtenerDatos(URL_BASE +
                                                   serieEncontrada.getTitulo().replace(" ", "+") +
                                                   "&season=" +
                                                   i +
                                                   API_KEY);

                DatosTemporadas datosTemporada = conversor.obtenerDatos(json, DatosTemporadas.class);
                temporadas.add(datosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream().map(e -> new Episodio(d.numero(), e))).toList();

            serieEncontrada.setEpisodios(episodios);
            serieRepository.save(serieEncontrada);
        }


    }

    private void mostrarSeriesBuscadas() {
        this.series = serieRepository.findAll();

        this.series.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);
    }

    private void buscarSeriesPorTitulo() {
        System.out.println("Escribe el nombre de la serie que deseas buscar:");
        var nombreSerie = teclado.nextLine();

         this.serieBuscada = this.serieRepository.findByTituloContainsIgnoreCase(nombreSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("La serie buscada es: " + serieBuscada.get());
        } else {
            System.out.println("Serie no encontrada");
        }
    }

    private void buscarTop5Series() {
        List<Serie> topSeries = this.serieRepository.findTop5ByOrderByEvaluacionDesc();

        topSeries.forEach(s -> System.out.println("Serie: " + s.getTitulo() + " - " + s.getEvaluacion()));
        System.out.println("----------------------------------------");
    }

    private void buscarSeriesPorCategoria() {
        System.out.println("Escribe la genero/categoria de la serie que deseas buscar:");
        var serie = teclado.nextLine();
        var categoria = Categoria.fromEspanol(serie);
        List<Serie> seriesPorCategoria = serieRepository.findByGenero(categoria);
        System.out.println("Series de la categoria: " + categoria);
        seriesPorCategoria.forEach(System.out::println);
    }

    private void buscarSeriesPorTemporadasYEvaluacion() {
        System.out.println("Filtrar series con cuantas temporadas?");
        var temporadas = teclado.nextInt();
        teclado.nextLine();
        System.out.println("Con evaluation a partir de cual valor?");
        var evaluacion = teclado.nextDouble();
        teclado.nextLine();
        List<Serie> seriesPorTemporadasYEvaluacion = serieRepository.seriesPorTemporadasYEvaluacion(temporadas,
                                                                                                    evaluacion
        );
        System.out.println("*** Series filtradas ***");
        seriesPorTemporadasYEvaluacion.forEach(s -> System.out.println(">" +
                                                                       s.getTitulo() +
                                                                       " - temporadas " +
                                                                       s.getTotalTemporadas() +
                                                                       " - evaluation " +
                                                                       s.getEvaluacion()));


    }

    private void buscarEpisodioPorTitulo() {
        System.out.println("Escribe el nombre del episodio que deseas buscar:");
        var nombreEpisodio = teclado.nextLine();
        List<Episodio> episodiosEncontrados = this.serieRepository.episodiosPorNombre(nombreEpisodio);
        episodiosEncontrados.forEach(e -> System.out.printf("Serie: %s Temporada %s Episodio %s Evaluación %s\n",
                                                            e.getSerie().getTitulo(),
                                                            e.getTemporada(),
                                                            e.getNumeroEpisodio(),
                                                            e.getEvaluacion()
        ));
    }

    private void top5EpisodiosPorSerie() {
        buscarSeriesPorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serieEncontrada = serieBuscada.get();
            List<Episodio> episodios = this.serieRepository.top5EpisodiosPorSerie(serieEncontrada);

            episodios.forEach(e -> System.out.printf("Serie: %s - Temporada %s - Titulo: %s Episodio %s - Evaluación %s\n",
                                                                e.getSerie().getTitulo(),
                                                                e.getTemporada(),
                                                                e.getTitulo(),
                                                                e.getNumeroEpisodio(),
                                                                e.getEvaluacion()
            ));
        }
    }

}