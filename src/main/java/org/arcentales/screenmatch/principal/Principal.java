package org.arcentales.screenmatch.principal;

import org.arcentales.screenmatch.models.DatosSerie;
import org.arcentales.screenmatch.models.DatosTemporadas;
import org.arcentales.screenmatch.models.Episodio;
import org.arcentales.screenmatch.models.Serie;
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
    private SerieRepository serieRepository;

    public Principal(SerieRepository serieRepository) {
        this.serieRepository = serieRepository;
        String OMDB_API_KEY = "";
        this.API_KEY = "&apikey=" + OMDB_API_KEY;

    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series
                    2 - Buscar episodios
                    3 - Mostrar series buscadas
                    
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

    // Opción 1 del menu
    private void buscarSerieWeb() {
        DatosSerie datos = getDatosSerie();
        Serie serie = new Serie(datos);
        serieRepository.save(serie);
    }

    // Opción 2 del menu
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
//            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream().map(e -> new Episodio(e.numeroEpisodio(), e))).toList();

            serieEncontrada.setEpisodios(episodios);
            serieRepository.save(serieEncontrada);
        }


    }

    private void mostrarSeriesBuscadas() {
        this.series = serieRepository.findAll();

        this.series.stream().sorted(Comparator.comparing(Serie::getGenero)).forEach(System.out::println);
    }
}