package org.magadiflo.mockito.app.repositories.impl;

import org.magadiflo.mockito.app.Datos;
import org.magadiflo.mockito.app.models.Examen;
import org.magadiflo.mockito.app.repositories.IExamenRepository;

import java.util.List;

public class ExameRepositoryImpl implements IExamenRepository {

    @Override
    public List<Examen> findAll() {
        System.out.println("ExameRepositoryImpl.findAll");
        return Datos.EXAMENES;
    }

    @Override
    public Examen guardar(Examen examen) {
        System.out.println("ExameRepositoryImpl.guardar");
        return Datos.EXAMEN;
    }

}
