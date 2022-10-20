package org.magadiflo.mockito.app.services;

import org.magadiflo.mockito.app.models.Examen;

import java.util.Optional;

public interface IExamenService {

    Optional<Examen> findExamenByNombre(String nombre);

    Examen findExamenByNombreWithPreguntas(String nombre);

}
