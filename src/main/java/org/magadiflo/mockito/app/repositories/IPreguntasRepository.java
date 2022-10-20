package org.magadiflo.mockito.app.repositories;

import java.util.List;

public interface IPreguntasRepository {

    List<String> findPreguntasByExamenId(Long id);

    void guardarVarias(List<String> preguntas);

}
