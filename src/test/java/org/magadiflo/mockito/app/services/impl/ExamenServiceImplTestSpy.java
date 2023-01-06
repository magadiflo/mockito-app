package org.magadiflo.mockito.app.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.magadiflo.mockito.app.Datos;
import org.magadiflo.mockito.app.models.Examen;
import org.magadiflo.mockito.app.repositories.IExamenRepository;
import org.magadiflo.mockito.app.repositories.IPreguntasRepository;
import org.magadiflo.mockito.app.repositories.impl.ExameRepositoryImpl;
import org.magadiflo.mockito.app.repositories.impl.PreguntaRepositoryImpl;
import org.magadiflo.mockito.app.services.IExamenService;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTestSpy {

    /**
     * @Spy
     * ******
     * Es otra forma de usar los Spy con anotaciones. La primera forma
     * como hemos venido trabajando en la clase ExamenServiceImplTest
     * fue así: Mockito.spy(ExameRepositoryImpl.class);
     */
    @Spy
    ExameRepositoryImpl examenRepository; // Importante que siempre sea la implementación concreta
    @Spy
    PreguntaRepositoryImpl preguntasRepository;

    @InjectMocks // Se usa tanto para Spy como para Mocks
    ExamenServiceImpl examenService;

    @Test
    void testSpyLlamadasReales() {
        Examen examen = this.examenService.findExamenByNombreWithPreguntas("Matemáticas");

        assertEquals(1L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
        assertEquals(6, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));
    }

    @Test
    void testSpySimulandoLlamadas() {
        Mockito.doReturn(Datos.EXAMENES).when(examenRepository).findAll();
        Mockito.doReturn(Datos.PREGUNTAS).when(preguntasRepository).findPreguntasByExamenId(Mockito.anyLong());

        Examen examen = this.examenService.findExamenByNombreWithPreguntas("Matemáticas");

        assertEquals(1L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
        assertEquals(6, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));
    }

}