package org.magadiflo.mockito.app.services.impl;

import org.magadiflo.mockito.app.models.Examen;
import org.magadiflo.mockito.app.repositories.IExamenRepository;
import org.magadiflo.mockito.app.services.IExamenService;

public class ExamenServiceImpl implements IExamenService {

    private final IExamenRepository examenRepository;

    public ExamenServiceImpl(IExamenRepository examenRepository) {
        this.examenRepository = examenRepository;
    }

    @Override
    public Examen findExamenByNombre(String nombre) {
        return examenRepository.findAll().stream().filter(examen -> examen.getNombre().equals(nombre)).findFirst().orElseThrow();
    }

}
