package org.magadiflo.mockito.app.repositories;

import org.magadiflo.mockito.app.models.Examen;

import java.util.List;

public interface IExamenRepository {

    List<Examen> findAll();

    Examen guardar(Examen examen);

}
