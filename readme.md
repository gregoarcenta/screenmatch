# Screenmatch - Catálogo de Series y Películas

Este es un proyecto inicial desarrollado en Java con Spring Boot para practicar el consumo de APIs externas y la persistencia de datos.

### 🚀 Tecnologías Principales

*   **Lenguaje:** [Java 21](https://www.oracle.com/java/technologies/downloads/) (LTS)
*   **Framework:** [Spring Boot 4](https://spring.io/projects/spring-boot)
*   **Persistencia:** [Spring Data JPA](https://spring.io/projects/spring-data-jpa) / Jakarta EE
*   **Base de Datos:** [PostgreSQL](https://www.postgresql.org/)
*   **Gestión de Dependencias:** [Maven](https://maven.apache.org/)
*   **API Externa:** [OMDb API](http://www.omdbapi.com/)

### 🛠️ Configuración Local

1.  **Clonar el repositorio:**
    ```bash
    git clone https://github.com/tu-usuario/screenmatch.git
    cd screenmatch
    ```

2.  **Variables de entorno:**
    Debes configurar una clave de API de OMDb. Crea una variable de sistema o configúrala en el IDE:
    ```env
    API_KEY_OMDB=tu_clave
    ```

3.  **Base de Datos:**
    Asegúrate de tener PostgreSQL corriendo y configurar las credenciales en el archivo `src/main/resources/application.properties`.

4.  **Ejecutar en desarrollo:**
    Puedes usar el Maven Wrapper incluido en el proyecto:
    ```bash
    ./mvnw spring-boot:run
    ```
---
Proyecto desarrollado como parte de mi formación en desarrollo Backend con Java.