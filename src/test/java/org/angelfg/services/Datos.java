package org.angelfg.services;

import org.angelfg.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {

    public final static List<Examen> EXAMENES = Arrays.asList(
        new Examen(5L, "Matemáticas"),
        new Examen(6L, "Lenguaje"),
        new Examen(7L, "Historia")
    );

    public final static List<String> PREGUNTAS = Arrays.asList(
        "Aritmética", "Integrales", "Derivadas", "Trigonometría", "Geometría"
    );

}
