package org.magadiflo.mockito.app.services.impl;

import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.magadiflo.mockito.app.Datos;
import org.magadiflo.mockito.app.models.Examen;
import org.magadiflo.mockito.app.repositories.IExamenRepository;
import org.magadiflo.mockito.app.repositories.impl.ExameRepositoryImpl;
import org.magadiflo.mockito.app.repositories.IPreguntasRepository;
import org.magadiflo.mockito.app.repositories.impl.PreguntaRepositoryImpl;
import org.magadiflo.mockito.app.services.IExamenService;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @ExtendWith(MockitoExtension.class)
 * ************************************
 * 2° Forma de habilitar el uso de anotaciones de mockito.
 *    Pero para esta segunda forma es importante tener en las dependencias del pom.xml,
 *    la dependencia de mockito-junit-jupiter
 */
@ExtendWith(MockitoExtension.class)
class ExamenServiceImplTest {

    /**
     * Dos formas de habilitar el uso de anotaciones de Mockito para el uso de Iny. de Dep.
     * ************************************************************************************
     * 1° En el método setUp() del @BeforeEach agregar: MockitoAnnotations.openMocks(this);
     * 2° En la clase principal anotar con: @ExtendWith(MockitoExtension.class).
     *    Pero para esta segunda forma es importante tener una dependencia en el pom.xml,
     *    la dependencia de mockito-junit-jupiter (es una dependencia que integra mockito con
     *    el platform de JUnit para poder ejecutar nuestras pruebas unitarias con este plugin,
     *    con esta extensión habilitando las anotaciones)
     */

    /**
     * @Mock y @InjectMocks
     * *********************
     * Usando estas anotaciones, ya no es necesario crear los mocks de forma manual, crear la clase
     * de prueba con new y luego inyectar los mocks, no... ahora se hace en automático
     * con el uso de esas anotaciones: @Mock y @InjectMocks, pero debemos habilitar el
     * uso de esas anotaciones para que se aplique la inyección de dependencia eligiendo
     * una de las dos formas descritas en la parte superior.
     */

    /**
     * @Mock
     * ******
     * Crea un mock para las interfaces o clases que han sido anotadas, en nuestro caso
     * serían las siguientes interfaces:
     *
     *      @Mock
     *      IExamenRepository examenRepository;
     *      @Mock
     *      IPreguntasRepository preguntasRepository;
     *
     * Ahora, como en el método de test testDoCallRealMethod() se usa
     * el Mockito.doCallRealMethod() quien es el que hará llamadas reales
     * a los métodos, es necesario que quienes estén anotados
     * con @Mock no sean interfaces, sino más bien sus clases concretas, es por eso
     * que a partir del video 50 cambiamos las anotaciones en interfaces
     * por anotaciones en clases concretas, tal como sigue:
     */
    @Mock
    ExameRepositoryImpl examenRepository;
    @Mock
    PreguntaRepositoryImpl preguntasRepository;

    /**
     * @InjectMocks
     * ************
     * - Crear la instancia del ExamenServiceImpl y además inyecta los dos mocks de arriba.
     * - Importante que el objeto anotado con @InjectMocks sea una clase de implementación,
     *   es decir, un tipo concreto y no una interfaz, sino no se podrá realizar la
     *   inyección de dependencias y mostrará errores.
     */
    @InjectMocks
    ExamenServiceImpl examenService;

    /*
    * MockitoAnnotations.openMocks(this);
    * ***********************************
    * 1° forma de habilitar el uso de anotaciones de mockito
    *
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
     */

    /*
    * Creación del objeto a probar: ExamenServiceImpl manualmente
    * ***********************************************************
    * Sín usar las anotaciones u otros elementos de mockito como:
    * @Mock, @InjectMocks, @ExtendWith(MockitoExtension.class) o
    * MockitoAnnotations.openMocks(this);
    * Podemos crear los mocks e inyectarla a la clase a probar usando
    * solamente la clase Mock y haciéndolo algo manual, tal como se ve
    * en el código del método setUp del @BeforeEach comentado.
    * Para eso seguimos los siguientes pasos:
    *
    * 1. Creamos los dos mocks que la clase ExamenServiceImpl requiere.
    * 2. Inyectamos vía constructor ambos mocks a la clase que será la que probaremos.
    *
    @BeforeEach
    void setUp() {
        examenRepository = Mockito.mock(IExamenRepository.class);
        preguntaRepository = Mockito.mock(IPreguntaRepository.class);
        service = new ExamenServiceImpl(examenRepository, preguntaRepository);
    }
    */

    /**
     * @Captor (2da Forma de usarlo)
     * *****************************
     * Es una anotación de Mockito con el que estamos definiendo un ArgumentCaptor
     * similar a lo que se podría definir dentro de un método, así:
     *
     *   (1ra Forma de usarlo - Ver el método testArgumentCaptor())
     *   ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
     *
     * Pero para no estar haciendo eso, es decir, por cada método que quisiéramos capturar
     * un tipo de argumento (en este caso Long) mejor lo definimos de manera global
     * usando esa anotación.
     */
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
        /**
         * Mockito.anyLong()
         * ******************
         * Será aplicado a cualquier valor del tipo long.
         * Con eso le decimos, Mockito, cuando se llame a preguntasRepository y a tu método
         * findPreguntasByExamenId(...) y como argumento se le pase CUALQUIER valor de tipo LONG,
         * entonces retorname Datos.PREGUNTAS. De esa manera evitamos ser muy específicos, ya que
         * si le pasamos como argumento un id específico, por ejemplo, de esta manera:
         *      this.preguntasRepository.findPreguntasByExamenId(5L);
         * estaríamos esperando que precisamente cuando se llame a ese método findPreguntasByExamenId(5L);
         * se le llame con ese id, caso contrario no aplicará el Mock.
         */
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

        /**
         * Mockito.verify(...)
         * *******************
         * Nos permite verificar si se invocó un método de la clase o interfaz que hemos mockeado.
         *
         * Por ejemplo, verificamos que del examenRepository se invoque el método findAll() que es el
         * que precisamente le dijimos a Mockito que simule. Ahora, estamos verificando que eso ocurra
         * porque precisamente eso es lo que debe pasar para poder probar este test.
         * En caso nunca se llame al examenRepositorio.finAll(), el test no pasará.
         *
         * Lo mismo ocurre con el preguntasRepository.findPreguntasByExamenId(...), debemos verificar
         * que se esté llamando.
         */
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
    @DisplayName(value = "prueba rápida para guardar examen sin preguntas")
    void testGuardarExamenSinPreguntas() {
        // Arrange
        Examen nuevoExamen = Datos.EXAMEN;
        nuevoExamen.setId(6L);
        Mockito.when(this.examenRepository.guardar(Mockito.any(Examen.class))).thenReturn(nuevoExamen);

        // Act
        Examen examen = this.examenService.guardar(nuevoExamen);

        // Assert
        Assertions.assertNotNull(examen.getId());
        Assertions.assertEquals(6L, examen.getId());
        Assertions.assertEquals("Física", examen.getNombre());

        Mockito.verify(this.examenRepository, Mockito.times(1)).guardar(Mockito.any(Examen.class));
        Mockito.verify(this.preguntasRepository, Mockito.never()).guardarVarias(Mockito.anyList());
    }

    @Test
    @DisplayName(value = "prueba rápida para guardar examen con una lista de preguntas")
    void testGuardarExamenConPreguntas() {
        // Arrange
        Examen nuevoExamen = Datos.EXAMEN;
        nuevoExamen.setId(6L);
        nuevoExamen.setPreguntas(Datos.PREGUNTAS);

        Mockito.when(this.examenRepository.guardar(Mockito.any(Examen.class))).thenReturn(nuevoExamen);

        // Act
        Examen examen = this.examenService.guardar(nuevoExamen);

        // Assert
        Assertions.assertNotNull(examen.getId());
        Assertions.assertEquals(6L, examen.getId());
        Assertions.assertEquals("Física", examen.getNombre());

        Mockito.verify(this.examenRepository, Mockito.times(1)).guardar(Mockito.any(Examen.class));
        Mockito.verify(this.preguntasRepository, Mockito.times(1)).guardarVarias(Mockito.anyList());
    }

    @Test
    @DisplayName(value = "debería guardar un examen con id = null y al retornar el examen guardado el id debería tener un valor")
    void testGuardarExamenRetornandoElIdGenerado() {
        //BDD (Bihavior, Driven, Development)

        // Given (dado un entorno de prueba)
        Examen newExamen = Datos.EXAMEN;
        newExamen.setPreguntas(Datos.PREGUNTAS);

        // Mockeamos una respuesta cuando se llame a this.examenRepository.guardar(...)
        // para que retorne un examen con un id ya asignado
        Mockito.when(this.examenRepository.guardar(Mockito.any(Examen.class))).then(new Answer<Examen>() {
            private Long secuencia = 7L;

            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                /**
                 * invocationOnMock.getArgument(0); es el argumento que se le pasa al
                 * this.examenRepository.guardar(...) porque es el que estamos
                 * mockeando aquí. La invocación de ese this.examenRepository.guardar(...)
                 * se realiza internamente cuando se llama al this.examenService.guardar(...)
                 * y se le envía el newExamen con id = null, precisamente ese examen es
                 * el que se recibe como argumento en nuestro mock. Ahora, lo que aquí se
                 * hará es que al examen con id = null, se le asignará un valor incremental
                 * a su id y ese examen es el que será retornado.
                 */
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(this.secuencia++);
                return examen;
            }
        });

        // When (Cuando ejecutamos el método que queremos probar)
        Examen examen = this.examenService.guardar(newExamen);

        // Then (entonces validamos)
        assertNotNull(examen.getId());
        assertEquals(7L, examen.getId());
        assertEquals("Física", examen.getNombre());

        Mockito.verify(this.examenRepository, Mockito.times(1)).guardar(Mockito.any(Examen.class));
        Mockito.verify(this.preguntasRepository, Mockito.times(1)).guardarVarias(Mockito.anyList());
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
     * ArgumentMatchers: Mockito.any(), Mockito.anyLong(), Mockito.argThat(), etc...
     * *****************************************************************************
     * ArgumentMatchers, permite saber si coincide el valor real que se pasa como argumento en un método,
     * y lo comparamos con los definidos en el Mock (when o verify), si coinciden pasa, sino falla.
     * En otras palabras, se usa para asegurarse de que ciertos argumentos se pasen a los mock's.
     *
     * Mockito.argThat(arg -> arg != null && arg >= 1L), con esto verificamos de forma más específica
     * que los argumentos que se pasan al método invocado cumplan determinada condición.
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

        // Internamente este método llama al this.preguntaRepository.findPreguntasPorExamenId(...)
        this.examenService.findExamenByNombreWithPreguntas("Matemáticas");

        /***
         * ArgumentCaptor (1ra Forma de usarlo)
         * ************************************
         *
         *      ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
         *
         * Definimos el tipo de argumento que capturaremos en la variable captor.
         * Otra forma de hacerlo es usando la anotación @Captor en una variable global de esta clase.
         */

        // Verificamos que se llame a this.preguntaRepository.findPreguntasPorExamenId(...) y
        // capturamos el argumento que se le pasa
        Mockito.verify(this.preguntasRepository).findPreguntasByExamenId(this.captor.capture());

        assertEquals(1L, captor.getValue());
    }

    @Test
    void testDoThrow() {
        Examen examen = Datos.EXAMEN;
        examen.setPreguntas(Datos.PREGUNTAS);
        /**
         * Mockito.when(...)...
         * ********************
         * Lo usamos siempre que el método a ser invocado retorne un objeto.
         * ¿Pero qué pasa si queremos mockear un método que retorne un void,
         * o sea, que no retorne nada?, con Mockito.when(...) ya no se podrá.
         *
         *
         * Mockito.do...
         * *************
         * Con la familia do... de mockito sí podríamos hacer algo.
         *
         * En el ejemplo siguiente mockearemos el método guardarVarias(...) quien por
         * definición de su método, nos devuelve un void.
         *
         * El mock que crearemos lanzará un IllegalArgumentException (por simular que ocurrió un error),
         * cada vez que se llame al método this.preguntaRepository.guardarVarias(...)
         */
        Mockito.doThrow(IllegalArgumentException.class).when(this.preguntasRepository).guardarVarias(Mockito.anyList());

        // Afirmamos que se lance la excepción cuando de nuestro servicio bajo prueba guardemos un examen
        assertThrows(IllegalArgumentException.class, () -> {
            this.examenService.guardar(examen);
        });

        Mockito.verify(this.preguntasRepository).guardarVarias(Mockito.anyList());
    }

    @Test
    void testDoAnswer() {
        Mockito.when(this.examenRepository.findAll()).thenReturn(Datos.EXAMENES);
        /**
         * Mockito.doAnswer(...)
         * *********************
         * Normalmente cuando queremos mockear algún método usamos el Mock.when(...), para decirle
         * que nos retorne cierta respuesta tal y como se ve en el siguiente ejemplo:
         *          Mockito.when(this.preguntasRepository.findPreguntasByExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);
         *
         * Ahora, ¿qué pasa si quisiéramos capturar el argumento que se le pasa al método que
         * estamos mockeando para hacer alguna operación y devolver una respuesta en función
         * a esa operación? Podríamos usar lo mismo que se hizo en el método test testGuardarExamenRetornandoElIdGenerado(),
         * donde usamos en el then, un answer, tal como se ve en el siguiente ejemplo:
         *
         *          Mockito.when(this.examenRepository.guardar(Mockito.any(Examen.class))).then(new Answer<Examen>() {...
         *
         * Existe otra posibilidad, usando el Mockito.doAnswer(....)..
         * Cuando se llame al método this.preguntaRepository.findPreguntasPorExamenId(...),
         * en el flujo real se le pasará un argumento, ese argumento es lo que con este
         * Mockito.doAnswer(...) capturamos, para hacer algún tratamiento y retornar
         * una respuesta como parte de la respuesta que queremos simular
         */
        Mockito.doAnswer(invocation -> {
           Long id = invocation.getArgument(0); //0, es por que solo pasamos un argumento
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
    @DisplayName(value = "debería guardar un examen con id = null y el método debería retornar el examen con id =! null")
    void testDoAnswerGuardarExamenRetornandoElIdGenerado() {
        Examen nuevoExamen = Datos.EXAMEN;
        nuevoExamen.setPreguntas(Datos.PREGUNTAS);
        Mockito.doAnswer(new Answer<Examen>() {
            private Long secuencia = 7L;
            @Override
            public Examen answer(InvocationOnMock invocationOnMock) throws Throwable {
                Examen examen = invocationOnMock.getArgument(0);
                examen.setId(secuencia++);
                return examen;
            }
        }).when(this.examenRepository).guardar(Mockito.any(Examen.class));

        Examen examen = this.examenService.guardar(nuevoExamen);

        Assertions.assertNotNull(examen.getId());
        Assertions.assertEquals(7L, examen.getId());
        Assertions.assertEquals("Física", examen.getNombre());

        Mockito.verify(this.examenRepository, Mockito.times(1)).guardar(Mockito.any(Examen.class));
        Mockito.verify(this.preguntasRepository, Mockito.times(1)).guardarVarias(Mockito.anyList());
    }

    @Test
    void testDoCallRealMethod() {
        // Aquí sí estamos mockeando el método findAll().
        Mockito.when(this.examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        // Aquí, en vez de generar un método simulado usaremos el Mockito.doCallRealMethod() para llamar al método real.
        //Mockito.when(this.preguntaRepository.findPreguntasPorExamenId(Mockito.anyLong())).thenReturn(Datos.PREGUNTAS);

        /**
         * Mockito.doCallRealMethod()
         * **************************
         * Invoca al método real para obtener datos reales y no simulados.
         * Para esto es importante que, la interfaz IPreguntaRepository tenga una clase concreta que la implemente.
         * Además, es necesario que las anotaciones con @Mock al inicio de la clase sean concretas y no interfaces.
         */
        Mockito.doCallRealMethod().when(this.preguntasRepository).findPreguntasByExamenId(Mockito.anyLong());

        Examen examen = this.examenService.findExamenByNombreWithPreguntas("Matemáticas");

        assertNotNull(examen.getId());
        assertEquals(1L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
        assertEquals(6, examen.getPreguntas().size());
    }


    /**
     * Mockito.spy()
     * **************
     * Un Spy es como un Mock. La diferencia es que, si queremos, puede funcionar como un objeto real
     * y llamar a las implementaciones de los métodos reales. También tenemos la opción de poder simular
     * la funcionalidad de cualquier método, como con el Mock.
     *
     * El Spy lo podemos usar con Mockito.spy(...) o con anotaciones @Spy, esta última similar
     * a cómo se trabajó con @Mock. Ver la clase ExamenServiceImplTestSpy.
     *
     * @Spy, define un mock parcial, ya que podemos utilizar los métodos reales o
     * simular el comportamiento de los métodos al igual que con el Mock
     * (para más información ver el README.md)
     *
     * Los Spy, para hacer llamadas reales no debería usarse mucho, porque nosotros no tenemos
     * control sobre la respuesta que podamos obtener, o en su defecto usarse solo para hacer
     * llamadas a servicios de terceros.
     *
     * Si usamos Spy, sería recomendable usarlo como un mock, es decir, usarlo para simular las
     * llamadas y trabajar siempre con datos simulados.
     *
     * Cuando trabajamos con un Mock, los métodos son 100% simulados.
     *
     * Cuando trabajamos con un Spy para simular llamadas, únicamente simulará aquellos
     * métodos que le definamos, el resto, si es que se utiliza y no está definido, hará
     * sus llamadas reales.
     *
     * Cuando trabajamos con un Spy para simular llamadas, es recomendable usar la familia del do...
     * doReturn(...), etc.. ya que si usamos el Mockito.when(...), se experimenta un comportamiento
     * extraño.
     */
    @Test
    void testSpyLlamadasReales() {
        /**
         * En este ejemplo, como en ninguna parte se está simulando la llamada, es decir
         * no se está usando el Mockito.when(...), el Spy hará la llamada real a los métodos
         */
        IExamenRepository examenRepository = Mockito.spy(ExameRepositoryImpl.class);
        IPreguntasRepository preguntasRepository = Mockito.spy(PreguntaRepositoryImpl.class);

        IExamenService examenService = new ExamenServiceImpl(examenRepository, preguntasRepository);

        Examen examen = examenService.findExamenByNombreWithPreguntas("Matemáticas");

        assertEquals(1L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
        assertEquals(6, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));
    }

    @Test
    void testSpySimulandoLlamadas() {
        /**
         * En este ejemplo, como estamos usando spy para simular la llamada a los métodos, es decir
         * estamos usando el Mockito.doReturn(...), el Spy ya no hará la llamada a los métodos reales,
         * sino que simulará la respuesta con datos falsos.
         *
         * Importante, usar el Mockito.doReturn(...) para simular las llamadas con Spy y no usar el
         * Mockito.when(...) ya que este último con los spy muestra un comportamiento extraño, como
         * si se hiciera la llamada real, aunque en realidad no lo hace
         */
        IExamenRepository examenRepository = Mockito.spy(ExameRepositoryImpl.class);
        IPreguntasRepository preguntasRepository = Mockito.spy(PreguntaRepositoryImpl.class);
        IExamenService examenService = new ExamenServiceImpl(examenRepository, preguntasRepository);

        Mockito.doReturn(Datos.EXAMENES).when(examenRepository).findAll();
        Mockito.doReturn(Datos.PREGUNTAS).when(preguntasRepository).findPreguntasByExamenId(Mockito.anyLong());

        Examen examen = examenService.findExamenByNombreWithPreguntas("Matemáticas");

        assertEquals(1L, examen.getId());
        assertEquals("Matemáticas", examen.getNombre());
        assertEquals(6, examen.getPreguntas().size());
        assertTrue(examen.getPreguntas().contains("aritmética"));
    }

    @Test
    @DisplayName(value = "verificando el orden en el que se ejecutan los métodos de los mocks")
    void testOrdenDeInvocaciones2() {
        Mockito.when(this.examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        this.examenService.findExamenByNombreWithPreguntas("Matemáticas");
        this.examenService.findExamenByNombreWithPreguntas("Lenguaje");

        InOrder inOrder = Mockito.inOrder(this.examenRepository, this.preguntasRepository);

        //Verificamos el orden en la que se deberían invocar
        inOrder.verify(this.examenRepository).findAll();
        inOrder.verify(this.preguntasRepository).findPreguntasByExamenId(1L);//1° se invocará Matemáticas

        inOrder.verify(this.examenRepository).findAll();
        inOrder.verify(this.preguntasRepository).findPreguntasByExamenId(2L);//2° se invocará Lenguaje
    }

    @Test
    void testOrdenDeInvocaciones() {
        Mockito.when(this.examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        this.examenService.findExamenByNombreWithPreguntas("Matemáticas");
        this.examenService.findExamenByNombreWithPreguntas("Lenguaje");

        InOrder inOrder = Mockito.inOrder(this.preguntasRepository);

        inOrder.verify(this.preguntasRepository).findPreguntasByExamenId(1L);//1° se invocará Matemáticas
        inOrder.verify(this.preguntasRepository).findPreguntasByExamenId(2L);//2° se invocará Lenguaje
    }

    @Test
    @DisplayName(value = "verificando el número de invocaciones que se hacen a los métodos mocks")
    void testNumeroInvocaciones() {
        Mockito.when(this.examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        this.examenService.findExamenByNombreWithPreguntas("Matemáticas");

        // Por defecto el número de invocaciones en un verify es 1 vez, es decir, siempre se espera que se invoque una vez.
        // Como no le ponemos el segundo argumento entonces será igual a Mockito.times(1)
        Mockito.verify(this.preguntasRepository).findPreguntasByExamenId(1L);

        Mockito.verify(this.preguntasRepository, Mockito.times(2)).findPreguntasByExamenId(1L);
        Mockito.verify(this.preguntasRepository, Mockito.atLeast(1)).findPreguntasByExamenId(1L);
        Mockito.verify(this.preguntasRepository, Mockito.atLeastOnce()).findPreguntasByExamenId(1L);
        Mockito.verify(this.preguntasRepository, Mockito.atMost(1)).findPreguntasByExamenId(1L);
        Mockito.verify(this.preguntasRepository, Mockito.atMostOnce()).findPreguntasByExamenId(1L);
    }

    @Test
    void testNumeroInvocaciones2() {
        Mockito.when(this.examenRepository.findAll()).thenReturn(Datos.EXAMENES);

        this.examenService.findExamenByNombreWithPreguntas("Matemáticas");

//        Mockito.verify(this.preguntasRepository).findPreguntasByExamenId(1L);
        Mockito.verify(this.preguntasRepository, Mockito.times(2)).findPreguntasByExamenId(1L);
        Mockito.verify(this.preguntasRepository, Mockito.atLeast(2)).findPreguntasByExamenId(1L);
        Mockito.verify(this.preguntasRepository, Mockito.atLeastOnce()).findPreguntasByExamenId(1L);
        Mockito.verify(this.preguntasRepository, Mockito.atMost(2)).findPreguntasByExamenId(1L);
//        Mockito.verify(this.preguntasRepository, Mockito.atMostOnce()).findPreguntasByExamenId(1L);
    }

    @Test
    void testNumeroInvocaciones3() {
        Mockito.when(this.examenRepository.findAll()).thenReturn(Collections.emptyList());

        this.examenService.findExamenByNombreWithPreguntas("Matemáticas");

        Mockito.verify(this.preguntasRepository, Mockito.never()).findPreguntasByExamenId(1L);
        Mockito.verifyNoInteractions(this.preguntasRepository);
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