import getdatos from "./getDatos.js";

const elementos = {
  top5: document.querySelector('[data-name="top5"]'),
  lanzamientos: document.querySelector('[data-name="lanzamientos"]'),
  series: document.querySelector('[data-name="series"]')
};

const homeContent = document.getElementById("home-content");
const categoriaSection = document.getElementById("categoria-filtrada");
const categoriaSelect = document.querySelector("[data-categorias]");

categoriaSelect.addEventListener("change", function () {
  const categoriaSeleccionada = categoriaSelect.value;

  if (categoriaSeleccionada === "todos") {
    homeContent.classList.remove("hidden");
    categoriaSection.classList.add("hidden");
  } else {
    homeContent.classList.add("hidden");
    categoriaSection.classList.remove("hidden");

    const titulo = categoriaSection.querySelector('h2');
    titulo.textContent = `Explorando: ${categoriaSeleccionada.toUpperCase()}`;

    getdatos(`/series/categoria/${categoriaSeleccionada}`)
      .then((data) => {
        crearListaPeliculas(categoriaSection, data);
      })
      .catch((error) => {
        tratarConErrores("Error al cargar la categoría seleccionada.");
      });
  }
});

function crearListaPeliculas(elemento, datos) {
    // Eliminamos cualquier lista previa para que no se acumulen
    const ulExistente = elemento.querySelector("ul");
    if (ulExistente) {
        ulExistente.remove();
    }

    const ul = document.createElement("ul");
    ul.className = "lista";
    const listaHTML = datos
        .map(
            (pelicula) => `
        <li>
            <a href="/detalles.html?id=${pelicula.id}">
                <img src="${pelicula.poster}" alt="${pelicula.titulo}" loading="lazy">
            </a>
        </li>
    `
        )
        .join("");

    ul.innerHTML = listaHTML;
    elemento.appendChild(ul);
}

function tratarConErrores(mensajeError) {
    console.error(mensajeError);
}

generaSeries();

function generaSeries() {
    const urls = ["/series/top5", "/series/lanzamientos", "/series"];

    // Hace todas las solicitudes en paralelo
    Promise.all(urls.map((url) => getdatos(url)))
        .then((data) => {
            crearListaPeliculas(elementos.top5, data[0]);
            crearListaPeliculas(elementos.lanzamientos, data[1]);
            crearListaPeliculas(elementos.series, data[2]);
        })
        .catch((error) => {
            tratarConErrores("Ocurrio un error al cargar los datos iniciales.");
        });
}
