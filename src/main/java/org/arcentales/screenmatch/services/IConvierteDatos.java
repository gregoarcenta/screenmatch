package org.arcentales.screenmatch.services;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
