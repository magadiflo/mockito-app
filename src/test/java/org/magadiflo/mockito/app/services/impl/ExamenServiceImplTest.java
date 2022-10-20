package org.magadiflo.mockito.app.services.impl;

import org.junit.jupiter.api.Test;
import org.magadiflo.mockito.app.models.Examen;
import org.magadiflo.mockito.app.repositories.IExamenRepository;
import org.magadiflo.mockito.app.services.IExamenService;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExamenServiceImplTest {

    @Test
    void findExamenByNombre() {
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
        IExamenRepository repository = Mockito.mock(IExamenRepository.class);
        IExamenService service = new ExamenServiceImpl(repository);

        List<Examen> datos = Arrays.asList(
                new Examen(1L, "Matemáticas"),
                new Examen(2L, "Lenguaje"),
                new Examen(3L, "Historia"),
                new Examen(4L, "Personal Social"),
                new Examen(5L, "Ciencia y Ambiente"),
                new Examen(6L, "Religión")
        );

        // Cuando se llame al método findAll() de la interfaz IExamenRepository entonces que retorne esa lista de datos
        Mockito.when(repository.findAll()).thenReturn(datos);

        Optional<Examen> examenOptional = service.findExamenByNombre("Matemáticas");

        assertTrue(examenOptional.isPresent());
        assertEquals(1L, examenOptional.orElseThrow().getId());
        assertEquals("Matemáticas", examenOptional.orElseThrow().getNombre());
    }

    @Test
    void findExamenByNombreListaVacia() {
        IExamenRepository repository = Mockito.mock(IExamenRepository.class);
        IExamenService service = new ExamenServiceImpl(repository);

        List<Examen> datos = Collections.emptyList();
        Mockito.when(repository.findAll()).thenReturn(datos);

        Optional<Examen> examenOptional = service.findExamenByNombre("Matemáticas");

        assertTrue(examenOptional.isPresent());
        assertEquals(1L, examenOptional.orElseThrow().getId());
        assertEquals("Matemáticas", examenOptional.orElseThrow().getNombre());
    }
}