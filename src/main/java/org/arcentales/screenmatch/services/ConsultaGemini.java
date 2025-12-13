package org.arcentales.screenmatch.services;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;

import java.util.Objects;

public class ConsultaGemini {

    static String API_KEY="abc...";

    public static String obtenerTraduccion(String texto) {
        String modelo = "gemini-2.5-flash-lite"; // Puedes cambiar la versión si lo deseas
        String prompt = "Traduce el siguiente texto al español: " + texto;

        Client cliente = new Client.Builder().apiKey(API_KEY).build();
        try {
            GenerateContentResponse respuesta = cliente.models.generateContent(
                    modelo,
                    prompt,
                    null
            );

            if (!Objects.requireNonNull(respuesta.text()).isEmpty()) {
                return respuesta.text();
            }
        } catch (Exception e) {
            System.err.println("Error al llamar a la API de Gemini para traducción: " + e.getMessage());
        }

        return null;
    }
}
