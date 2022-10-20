package org.magadiflo.mockito.app.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.magadiflo.mockito.app.models.Examen;
import org.magadiflo.mockito.app.repositories.IExamenRepository;
import org.magadiflo.mockito.app.repositories.IPreguntasRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) //FORMA 02 - Habilitar el uso de anotaciones de Mockito, para eso es necesario la dependencia mockito-junit-jupiter en el pom.xml
class ExamenServiceImplTest {

    @Mock
    IExamenRepository examenRepository;

    @Mock
    IPreguntasRepository preguntasRepository;

    // Necesariamente necesitamos una clase concreta (ExamenServiceImpl) para inyectar los repositorios,
    // ya que si establecemos aquí la interfaz genérica (IExamenService) mostrará error
    @InjectMocks
    ExamenServiceImpl examenService;

    //@BeforeEach
    //void setUp() {
        // FORMA 01 - Habilitar uso de anotaciones de Mockito para esta clase
        // Esto nos permitirá trabajar con Inyección de Dependencias
        //MockitoAnnotations.openMocks(this);
    //}

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