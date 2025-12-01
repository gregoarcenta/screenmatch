package org.arcentales.screenmatch.services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface IConvierteDatos {
    <T> T convertirDatos(String json, Class<T> clase) throws JsonProcessingException;
}
