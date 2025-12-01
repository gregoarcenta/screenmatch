package org.arcentales.screenmatch.principal;

import java.util.Arrays;
import java.util.List;

public class EjemploStreams {
    public void ejemplo() {
        List<String> nombres = Arrays.asList("Alex", "Juan", "Pedro");
        nombres.stream()
                .sorted()
                .limit(2)
                .filter(n -> n.startsWith("A"))
                .map(String::toUpperCase)
                .forEach(System.out::println);
    }
}
