package org.magadiflo.mockito.app.repositories.impl;

import org.magadiflo.mockito.app.Datos;
import org.magadiflo.mockito.app.repositories.IPreguntasRepository;

import java.util.List;

public class PreguntaRepositoryImpl implements IPreguntasRepository {

    @Override
    public List<String> findPreguntasByExamenId(Long id) {
        System.out.println("PreguntaRepositoryImpl.findPreguntasByExamenId");
        return Datos.PREGUNTAS;
    }

    @Override
    public void guardarVarias(List<String> preguntas) {
        System.out.println("PreguntaRepositoryImpl.guardarVarias");

    }

}
