# Sección 3: Mockito

## [Workshop Pruebas Unitarias en Microservicios JUnit 5](https://www.youtube.com/watch?v=Sf0237bn6lE)
Podemos ver el mini-tutorial en el canal de [Centripio](https://www.youtube.com/watch?v=Sf0237bn6lE)

## ¿Qué es un Mock? ¿y un Spy?
Tomado de [Mariano Torres](https://www.linkedin.com/pulse/mockito-diferencias-entre-mock-y-spy-mariano-j-aznar-psm-/?originalSubdomain=es)

Tanto Mock como Spy son usados en testing para aislar el sistema a testear o SUT (por sus siglas en
inglés), diviendo el código a ser testado de las posibles dependencias que puedan existir (otras clases, conexión con bases de datos, etc.)

### Mock
Un Mock es un "simulador" o doble de otro objeto. Contiene todos los métodos del objeto original, 
pero son falsos (no contienen implementaciones). 
**Cuando se usa un Mock es obligatorio definir el comportamiento de sus métodos.**

Ejemplo:

```
@Test
public void whenITryToCutACarrotThenTrue() {
   Knife knife = mock( Knife.class );
   doReturn( true ).when( knife ).cut( "carrot" );

   assertEquals( knife.cut("carrot"), true );
}

@Test
public void whenITryToCutWaterThenFalse() {
   Knife knife = mock( Knife.class );
   doReturn( false ).when( knife ).cut( "water" );

   assertEquals( knife.cut("water", false);
}
 
```
En este caso, tenemos una clase Knife con un método: cut(), pero no estamos 
interesados realmente en la funcionalidad que esté implementada en el método 
(si es que hay alguna), así que usamos un "doReturn(...)" para simular la 
funcionalidad y devolver el valor que deseemos.

### Spy
Un Spy es como un Mock. La diferencia es que, si queremos, puede funcionar 
como un objeto real y llamará a las implementaciones de los métodos reales. 
Tambien tenemos la opción podemos simular la funcionalidad de cualquier método, 
como con el Mock.

Por ejemplo, pongamos que la clase Knife es como sigue:

```
public class Knife{
    ...
    public boolean cut(String something) {
         return true;
    }
    ...
}
```

Entonces, los tests podrían ser:

```
@Test
public void whenITryToCutACarrotThenTrue() {
   Knife knife = spy( Knife.class );
   
   assertEquals( knife.cut("carrot"), true );
}

@Test
public void whenITryToCutWaterThenFalse() {
   Knife knife = spy( Knife.class );
   doReturn( false ).when( knife ).cut( "water" );

   assertEquals( knife.cut("water", false);
}
```

En este caso, en el primer tests el objeto "knife" funciona como un objeto 
normal y cuando llamamos al método "cut()", estamos llamando al método real de
la clase "Knife". Sin embargo, debido a que este método siempre devuelve "true", 
en el segundo test necesitamos simular su comportamiento para pasar el test, 
ya que necesitamos un "false", así que usamos el "doReturn(...)".

## Resumen
Básicamente, la diferencia es que con Mock es obligatorio simular los comportamientos de los
métodos. Los Mocks son muy usados cuando se programa haciendo TDD (Test Driven Development 
o Desarrollo basado en tests), ya que no requieren de que haya ninguna funcionalidad implementada. 
Sin embargo, Spy, aunque puede simular el comportamiento de los métodos al igual que Mock, 
también nos da la oportunidad de llamar a la implementación real de los métodos del objeto. 
Es usado cuando nos interesa mantener la consistencia de los métodos ya implementados con la
nueva parte que estemos desarrollando y realizando TDD.
---

## Primeras pruebas con Mockito
Crearemos el siguiente método test

```
@Test
void findExamenByNombre() {
    /**
     * Mockito.mock(...)
     * *****************
     * Creará una referencia, una instancia, una implementación al vuelo
     * de una clase concreta o una interfaz, pero simulada con los métodos y
     * con un comportamiento que le proporcionaremos.
     *
     * Con esto no necesitamos de cualquier implementación de real de IExamenRepository,
     * ya que Mockito asumirá ese papel.
     *
     * Entonces, cuando en nuestra clase bajo prueba (ExamenServiceImpl) se llame a su método
     * findExamenByNombre(...), vemos que éste internamente llamará al método
     * this.examenRepository.findAll() del IExamenRepository y cuando lo haga le diremos
     * a Mockito que lo simule devolviendo algún valor determinado,
     * en nuestro caso una lista de exámenes.
     *
     * No se pueden hacer Mock de cualquier método, solo de aquellos que son públicos o default,
     * pero no se puede hacer la simulación de un método privado, estático o final.
     */
    IExamenRepository repository = Mockito.mock(IExamenRepository.class);
    IExamenService service = new ExamenServiceImpl(repository);

    List<Examen> datos = Arrays.asList(
            new Examen(1L, "Matemáticas"),
            new Examen(2L, "Historia"),
            new Examen(3L, "Lenguaje"),
            new Examen(4L, "Personal Social"),
            new Examen(5L, "Ciencia y Ambiente")
    );

    /**
     * Mockito.when(...)
     * *****************
     * Así implementamos o damos un comportamiento al método findAll() del IExamenRepository,
     * pero realizada por Mockito, es decir, le decimos.. oye cuando al repository
     * llamen al método finAll(), detén esa invocación real y entonces retórname estos datos
     */
    Mockito.when(repository.findAll()).thenReturn(datos);

    Optional<Examen> examenOptional = service.findExamenByNombre("Matemáticas");

    Assertions.assertTrue(examenOptional.isPresent(), () -> "El examen Matemáticas debe existir");
    Assertions.assertEquals(1L, examenOptional.orElseThrow().getId());
    Assertions.assertEquals("Matemáticas", examenOptional.orElseThrow().getNombre());
}
```

Como vemos, parte del código se va a repetir en todos los test. Podemos usar 
el ciclo de vida de los test @BeforeEach y definir en él los Mocks, de 
manera que estén disponibles para todos los test. A continuación se muestra
un ejemplo donde además se le agrega una nueva dependencia (IPreguntaRepository).

NOTA: En este punto, no nos interesa la implementación tanto del IExamenRepository ni la de
IPreguntaRepository, ya que lo que queremos probar es este SERVICIO, pero
para hacerlo requerimos esos repositorios como dependencias, pero no 
su implementación real, entonces para tener esas dependencias simuladas
es que precisamente usamos MOCKITO.

```
class ExamenServiceImplTest {

    IExamenRepository examenRepository;
    IPreguntaRepository preguntaRepository;
    IExamenService service;

    @BeforeEach
    void setUp() {
        examenRepository = Mockito.mock(IExamenRepository.class);
        preguntaRepository = Mockito.mock(IPreguntaRepository.class);
        service = new ExamenServiceImpl(examenRepository, preguntaRepository);
    }
    ..........
    ..........
    ..........
```