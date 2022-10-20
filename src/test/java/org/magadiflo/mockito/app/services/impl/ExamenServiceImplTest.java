package org.magadiflo.mockito.app.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.magadiflo.mockito.app.models.Examen;
import org.magadiflo.mockito.app.repositories.IExamenRepository;
import org.magadiflo.mockito.app.repositories.IPreguntasRepository;
import org.magadiflo.mockito.app.services.IExamenService;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExamenServiceImplTest {

    IExamenRepository examenRepository;
    IPreguntasRepository preguntasRepository;
    IExamenService examenService;

    @BeforeEach
    void setUp() {
        /**
         * mock(...), dentro del método mock, agregamos la clase o interfaz que queremos simular.
         * En este caso, estamos colocando una interfaz, pero si colocamos una implementación real
         * de dicha interfaz, es decir, queremos simular dicha implementación real, jamás
         * la implementación real será ejecutada, si no, por el contrario, el mock tomará su lugar
         * y hará la simulación. Por ejemplo, si ejecutamos con una implementación concreta, el método
         * de dicha implementación concreta (findAll()) no será ejecutado, sino que será reemplazado
         * por el que se defina con mockito, tal como se ve en esta línea de código:
         * Mockito.when(repository.findAll()).thenReturn(datos);
         */
        this.examenRepository = Mockito.mock(IExamenRepository.class);
        this.preguntasRepository = Mockito.mock(IPreguntasRepository.class);

        this.examenService = new ExamenServiceImpl(examenRepository, preguntasRepository);
    }

    @Test
    void findExamenByNombre() {
        // Cuando se llame al método findAll() de la interfaz IExamenRepository entonces que retorne esa lista de datos
        Mockito.when(this.examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        Optional<Examen> examenOptional = this.examenService.findExamenByNombre("Matemáticas");

        assertTrue(examenOptional.isPresent());
        assertEquals(1L, examenOptional.orElseThrow().getId());
        assertEquals("Matemáticas", examenOptional.orElseThrow().getNombre());
    }

    @Test
    void findExamenByNombreListaVacia() {
        List<Examen> datos = Collections.emptyList();
        Mockito.when(this.examenRepository.findAll()).thenReturn(datos);

        Optional<Examen> examenOptional = this.examenService.findExamenByNombre("Matemáticas");

        assertFalse(examenOptional.isPresent());
    }

    @Test
    void testPreguntasExamen() {
        Mockito.when(this.examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        //Mockito.anyLong(), será aplicado a cualquier valor del tipo long
        Mockito.when(this.preguntasRepository.findPreguntasByExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = this.examenService.findExamenByNombreWithPreguntas("Matemáticas");
        assertEquals(6, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));
    }

    @Test
    void testPreguntasExamenVerify() {
        Mockito.when(this.examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        Mockito.when(this.preguntasRepository.findPreguntasByExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = this.examenService.findExamenByNombreWithPreguntas("Matemáticas");
        assertEquals(6, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));

        //Verificamos que del examenRepository se invoque el método findAll() que es el que precisamente Mockito simulará,
        //ya que en esta prueba test, se necesita que eso pase. En caso nunca se llame al examenRepositorio.finAll(), el test no pasará.
        //Lo mismo ocurre con el preguntasRepository.findPreguntasByExamenId(...), debemos verificar que se esté llamando
        Mockito.verify(this.examenRepository).findAll();
        Mockito.verify(this.preguntasRepository).findPreguntasByExamenId(Mockito.anyLong());
    }

    @Test
    void testNoExisteExamenVerify() {
        Mockito.when(this.examenRepository.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(this.preguntasRepository.findPreguntasByExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);

        Examen examen = this.examenService.findExamenByNombreWithPreguntas("Matemáticas");

        assertNull(examen);
        Mockito.verify(this.examenRepository).findAll();
        Mockito.verify(this.preguntasRepository).findPreguntasByExamenId(Mockito.anyLong());
    }
}