package org.magadiflo.mockito.app.repositories.mock;

import org.magadiflo.mockito.app.models.Examen;
import org.magadiflo.mockito.app.repositories.IExamenRepository;

import java.util.Arrays;
import java.util.List;

public class ExamenRepositoryImpl implements IExamenRepository {

    @Override
    public List<Examen> findAll() {
        return Arrays.asList(
                new Examen(1L, "Matemáticas"),
                new Examen(2L, "Lenguaje"),
                new Examen(3L, "Historia"),
                new Examen(4L, "Personal Social"),
                new Examen(5L, "Ciencia y Ambiente"),
                new Examen(6L, "Religión")
        );
    }

}
