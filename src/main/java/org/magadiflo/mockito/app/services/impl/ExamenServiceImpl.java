package org.magadiflo.mockito.app.services.impl;

import org.magadiflo.mockito.app.models.Examen;
import org.magadiflo.mockito.app.repositories.IExamenRepository;
import org.magadiflo.mockito.app.repositories.IPreguntasRepository;
import org.magadiflo.mockito.app.services.IExamenService;

import java.util.List;
import java.util.Optional;

public class ExamenServiceImpl implements IExamenService {

    private final IExamenRepository examenRepository;
    private final IPreguntasRepository preguntasRepository;

    public ExamenServiceImpl(IExamenRepository examenRepository, IPreguntasRepository preguntasRepository) {
        this.examenRepository = examenRepository;
        this.preguntasRepository = preguntasRepository;
    }

    @Override
    public Optional<Examen> findExamenByNombre(String nombre) {
        return examenRepository.findAll().stream().filter(examen -> examen.getNombre().equals(nombre)).findFirst();
    }

    @Override
    public Examen findExamenByNombreWithPreguntas(String nombre) {
        Optional<Examen> examenOptional = this.findExamenByNombre(nombre);
        Examen examen = null;
        if (examenOptional.isPresent()) {
            examen = examenOptional.orElseThrow();
            List<String> preguntas = this.preguntasRepository.findPreguntasByExamenId(examen.getId());

            this.preguntasRepository.findPreguntasByExamenId(examen.getId());

            examen.setPreguntas(preguntas);
        }
        return examen;
    }

    @Override
    public Examen guardar(Examen examen) {
        if(!examen.getPreguntas().isEmpty()) {
            this.preguntasRepository.guardarVarias(examen.getPreguntas());
        }
        return this.examenRepository.guardar(examen);
    }

}
