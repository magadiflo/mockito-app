package org.magadiflo.mockito.app.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.magadiflo.mockito.app.Datos;
import org.magadiflo.mockito.app.models.Examen;
import org.magadiflo.mockito.app.repositories.impl.ExameRepositoryImpl;
import org.magadiflo.mockito.app.repositories.IPreguntasRepository;
import org.magadiflo.mockito.app.repositories.impl.PreguntaRepositoryImpl;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) //FORMA 02 - Habilitar el uso de anotaciones de Mockito, para eso es necesario la dependencia mockito-junit-jupiter en el pom.xml
class ExamenServiceImplTest {

    @Mock
    ExameRepositoryImpl examenRepository;

    @Mock
    PreguntaRepositoryImpl preguntasRepository;

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

    @Captor
    ArgumentCaptor<Long> captor;

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
        //Given
        Mockito.when(this.examenRepository.findAll()).thenReturn(Collections.emptyList());
        Mockito.when(this.preguntasRepository.findPreguntasByExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);

        //When
        Examen examen = this.examenService.findExamenByNombreWithPreguntas("Matemáticas");

        //Then
        assertNull(examen);
        Mockito.verify(this.examenRepository).findAll();
        Mockito.verify(this.preguntasRepository).findPreguntasByExamenId(Mockito.anyLong());
    }

    @Test
    void testGuardarExamen() {
        //BDD (Bihavior, Driven, Development)

        // Given (dado un entorno de prueba)
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        //Simularemos, cuando se guarde cualquier examen, retorne una instancia de nuestra clase Datos
        Mockito.when(this.examenRepository.guardar(Mockito.any(Examen.class))).then(new Answer<Examen>() {
            private Long secuencia = 7L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0); //Es el argumento que se le pasa en el método guardar(...)
                examen.setId(this.secuencia++);
                return examen;
            }
        });

        // When (Cuando ejecutamos el método que queremos probar)
        Examen examen = this.examenService.guardar(newExamen);

        // Then (entonces validamos)
        assertNotNull(examen.getId());
        assertEquals(7, examen.getId());
        assertEquals("Física", examen.getNombre());

        Mockito.verify(this.examenRepository).guardar(Mockito.any(Examen.class));
        Mockito.verify(this.preguntasRepository).guardarVarias(Mockito.anyList());
    }

    @Test
    void testManejoException() {
        Mockito.when(this.examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NULL);
        Mockito.when(this.preguntasRepository.findPreguntasByExamenId(Mockito.isNull())).thenThrow(IllegalArgumentException.class);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            this.examenService.findExamenByNombreWithPreguntas("Matemáticas");
        });

        assertEquals(IllegalArgumentException.class, exception.getClass());

        Mockito.verify(this.examenRepository).findAll();
        Mockito.verify(this.preguntasRepository).findPreguntasByExamenId(Mockito.isNull());
    }

    /***
     * ArgumentMatchers, permite saber si coincide el valor real que se pasa como argumento en un método,
     * y lo comparamos con los definidos en el Mock (when o verify), si coinciden pasa, sino falla.
     * En otras palabras, se usa para asegurarse de que ciertos argumentos se pasen a los mock's
     */
    @Test
    void testArgumentMatchers() {
        Mockito.when(this.examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        Mockito.when(this.preguntasRepository.findPreguntasByExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);

        this.examenService.findExamenByNombreWithPreguntas("Matemáticas");

        Mockito.verify(this.examenRepository).findAll();
        Mockito.verify(this.preguntasRepository).findPreguntasByExamenId(Mockito.argThat(arg -> arg != null && arg >= 1L));
    }

    @Test
    void testArgumentMatchers2() {
        Mockito.when(this.examenRepository.findAll()).thenReturn(Datos.EXAMENES_ID_NEGATIVOS);
        Mockito.when(this.preguntasRepository.findPreguntasByExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);

        this.examenService.findExamenByNombreWithPreguntas("Matemáticas");

        Mockito.verify(this.examenRepository).findAll();
        Mockito.verify(this.preguntasRepository).findPreguntasByExamenId(Mockito.argThat(new MiArgsMatchers()));
    }

    @Test
    void testArgumentCaptor() {
        Mockito.when(this.examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        this.examenService.findExamenByNombreWithPreguntas("Matemáticas");

        //ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class); //Es lo mismo que la anotación realizada al inicio de esta clase
        Mockito.verify(this.preguntasRepository).findPreguntasByExamenId(this.captor.capture());

        assertEquals(1L, captor.getValue());
    }

    @Test
    void testDoThrow() {
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);
        //Usamos el doThrow al inicio cuando un método retorna void.
        //Aquí le indicamos que lance la excepción cuando se invoque al método guardarVarias
        Mockito.doThrow(IllegalArgumentException.class).when(this.preguntasRepository).guardarVarias(Mockito.anyList());

        assertThrows(IllegalArgumentException.class, () -> {
            this.examenService.guardar(examen);
        });
    }

    @Test
    void testDoAnswer() {
        Mockito.when(this.examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        //Mockito.when(this.preguntasRepository.findPreguntasByExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);
        Mockito.doAnswer(invocation -> {
           Long id = invocation.getArgument(0);
           return id == 1L ? Datos.PREGUNTAS : Collections.emptyList();
        }).when(this.preguntasRepository).findPreguntasByExamenId(Mockito.anyLong());

        Examen examen = this.examenService.findExamenByNombreWithPreguntas("Matemáticas");

        assertEquals(6, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("trigonometría"));
        assertEquals(1L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());

        Mockito.verify(this.preguntasRepository).findPreguntasByExamenId(Mockito.anyLong());
    }

    @Test
    void testDoCallRealMethod() {
        Mockito.when(this.examenRepository.findAll()).thenReturn(Datos.EXAMENES);
//        Mockito.when(this.preguntasRepository.findPreguntasByExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);

        //Invoca al método real
        Mockito.doCallRealMethod().when(this.preguntasRepository).findPreguntasByExamenId(Mockito.anyLong());

        Examen examen = this.examenService.findExamenByNombreWithPreguntas("Matemáticas");

        assertEquals(1L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
    }

    public static class MiArgsMatchers implements ArgumentMatcher<Long> {
        private Long argument;

        @Override
        public boolean matches(Long aLong) {
            this.argument = aLong;
            return aLong != null && aLong > 0;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("MiArgsMatchers{");
            sb.append("es para un mensaje personalizado de error que imprime mockito en caso de que falle el test. ");
            sb.append(String.format("%d debe ser un entero positivo", this.argument));
            sb.append('}');
            return sb.toString();
        }
    }
}