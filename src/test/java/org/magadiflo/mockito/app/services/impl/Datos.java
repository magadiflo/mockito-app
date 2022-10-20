package org.magadiflo.mockito.app.services.impl;

import org.magadiflo.mockito.app.models.Examen;

import java.util.Arrays;
import java.util.List;

public class Datos {

    public static final List<Examen> EXAMENES = Arrays.asList(
            new Examen(1L, "Matemáticas"),
            new Examen(2L, "Lenguaje"),
            new Examen(3L, "Historia"),
            new Examen(4L, "Personal Social"),
            new Examen(5L, "Ciencia y Ambiente"),
            new Examen(6L, "Religión")
    );

    public static final List<String> PREGUNTAS = Arrays.asList("aritmética", "integrales", "derivadas", "trigonometría", "geometría", "álgebra");

    public static final Examen EXAMEN = new Examen(null, "Física");

}
