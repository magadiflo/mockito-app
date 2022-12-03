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