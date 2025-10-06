# Justificación de Estructuras de Datos Seleccionadas
## Sistema de Bicicletas Públicas (SiBiP)

### Integrantes del Equipo
[Completar con nombres y números de estudiante]

---

## Índice de Contenidos

1. [Introducción](#1-introducción)
2. [Listas Simplemente Enlazadas: Fundamentos](#2-listas-simplemente-enlazadas-fundamentos)
3. [Aplicación al Dominio del Problema](#3-aplicación-al-dominio-del-problema)
4. [Justificación Específica por Estructura](#4-justificación-específica-por-estructura)
5. [Análisis de Complejidad Temporal](#5-análisis-de-complejidad-temporal)
6. [Consideraciones de Diseño](#6-consideraciones-de-diseño)
7. [Colas (Queue): Gestión de Listas de Espera](#7-colas-queue-gestión-de-listas-de-espera)
8. [Pila (Stack): Historial de Retiros](#8-pila-stack-historial-de-retiros)
9. [Estructuras de Conteo y Estadísticas](#9-estructuras-de-conteo-y-estadísticas)
10. [Alternativas Consideradas y Descartadas](#10-alternativas-consideradas-y-descartadas)
11. [Resumen de Estructuras Seleccionadas](#11-resumen-de-estructuras-seleccionadas)
12. [Conclusiones](#12-conclusiones)
13. [Reflexión Final](#13-reflexión-final)

---

## Resumen Ejecutivo

| 📊 Aspecto | 🎯 Decisión |
|-----------|------------|
| **Estructura Base** | Listas Simplemente Enlazadas |
| **Listas de Espera** | Colas (FIFO) |
| **Historial de Retiros** | Pila (LIFO) |
| **Estadísticas** | Contadores incrementales |
| **Justificación Principal** | Dinamismo, eficiencia y modelado fiel del dominio |

---

## 1. Introducción

En este proyecto se implementó el **Sistema de Bicicletas Públicas (SiBiP)** que permite gestionar estaciones, bicicletas y alquileres/devoluciones en diferentes áreas de Montevideo. El sistema debe manejar:

- **Estaciones** con anclajes limitados
- **Bicicletas** con diferentes estados y ubicaciones
- **Usuarios** que alquilan y devuelven bicicletas
- **Listas de espera** cuando no hay disponibilidad
- **Historial de operaciones** para deshacer retiros

Para la implementación se seleccionaron las siguientes estructuras de datos:
1. **Listas Simplemente Enlazadas** - Para colecciones dinámicas
2. **Colas (Queue)** - Para listas de espera FIFO
3. **Pilas (Stack)** - Para historial de operaciones LIFO

A continuación se justifica cada decisión técnica en el contexto del dominio del problema.

---

## 2. Listas Simplemente Enlazadas: Fundamentos

### 2.1 Definición y Características

Una lista simplemente enlazada es una estructura de datos lineal compuesta por nodos, donde cada nodo contiene:
- Un **dato** (elemento almacenado)
- Una **referencia** al siguiente nodo en la secuencia

### 2.2 Ventajas Principales

#### **2.2.1 Gestión Dinámica de Memoria**
La ventaja fundamental de las listas enlazadas frente a los **arrays/vectores** es que **no requieren espacios consecutivos de memoria**. Esto se debe a que cada nodo almacena una referencia al siguiente, permitiendo que los elementos estén dispersos en diferentes posiciones de la memoria RAM.

**Implicaciones prácticas:**
- **Crecimiento dinámico**: La lista se crea vacía y crece según sea necesario
- **Sin desperdicio de memoria**: No se reserva espacio que podría no utilizarse
- **Sin redimensionamiento costoso**: No es necesario copiar todos los elementos a un nuevo espacio cuando se alcanza la capacidad máxima

En contraste, un **vector/array** requiere:
1. Definir un tamaño fijo al momento de creación
2. Reservar ese espacio completo en memoria (esté o no siendo utilizado)
3. Si se supera la capacidad, crear un nuevo array más grande y copiar todos los elementos

#### **2.2.2 Eficiencia en Inserciones y Eliminaciones**
- **Inserción al inicio**: O(1) - Solo se modifica la referencia de cabeza
- **Inserción al final**: O(1) - Con referencia al nodo `fin`, se añade directamente
- **Eliminación**: O(n) - Requiere búsqueda, pero la eliminación en sí es O(1)

---

## 3. Aplicación al Dominio del Problema

### 3.1 Contexto del Sistema

El sistema de gestión de bicicletas compartidas presenta las siguientes características del dominio:

1. **Cantidad variable e impredecible** de entidades:
   - No se sabe cuántas bicicletas estarán en el Depósito en un momento dado
   - No se conoce cuántas bicicletas estarán En Uso simultáneamente
   - El número de bicicletas en cada Estación fluctúa constantemente
   - Los usuarios se registran de manera incremental
   - Las estaciones pueden agregarse o eliminarse según necesidades urbanas

2. **Alta frecuencia de cambios de estado**:
   - Bicicletas que se alquilan (salen de una estación)
   - Bicicletas que se devuelven (entran a una estación)
   - Bicicletas que van a mantenimiento (salen de circulación)
   - Bicicletas reparadas que vuelven a estar disponibles

3. **Operaciones principales del sistema**:
   - Agregar nuevas entidades (usuarios, estaciones, bicicletas)
   - Buscar entidades específicas
   - Mover bicicletas entre ubicaciones
   - Listar elementos

---

## 4. Justificación Específica por Estructura

### 4.1 Lista de Usuarios (`ListaSE<Usuario>`)

**Uso en el sistema:**
```java
private ListaSE<Usuario> usuarios;
```

**Justificación:**
- **Registro incremental**: Los usuarios se registran uno a uno a lo largo del tiempo. No hay forma de predecir cuántos usuarios totales tendrá el sistema.
- **Operaciones frecuentes**:
  - `registrarUsuario()`: Agrega un nuevo usuario al final → O(1)
  - `obtenerUsuario(cedula)`: Busca un usuario por cédula → O(n)
  - `listarUsuarios()`: Itera sobre todos los usuarios → O(n)

**Alternativa descartada - Array:**
- Requeriría estimar un tamaño máximo de usuarios (¿100? ¿1000? ¿10000?)
- Si el límite se supera, habría que redimensionar (operación costosa O(n))
- Desperdiciaría memoria si se sobreestima la capacidad

**Conclusión:** La lista enlazada se adapta perfectamente al crecimiento orgánico del número de usuarios.

---

### 4.2 Lista de Estaciones (`ListaSE<Estacion>`)

**Uso en el sistema:**
```java
private ListaSE<Estacion> estaciones;
```

**Justificación:**
- **Cantidad variable**: El número de estaciones de bicicletas puede cambiar según la planificación urbana (nuevas estaciones, eliminación de estaciones poco utilizadas).
- **Operaciones del sistema**:
  - `registrarEstacion()`: Agrega una nueva estación al final → O(1)
  - `eliminarEstacion()`: Elimina una estación específica → O(n)
  - Búsqueda de estación por nombre → O(n)

**Ventaja sobre Array:**
- La eliminación de una estación en un array requeriría desplazar todos los elementos posteriores para evitar huecos.
- En la lista enlazada, solo se modifican las referencias del nodo anterior → más eficiente.

**Conclusión:** La flexibilidad de agregar/eliminar estaciones justifica el uso de listas enlazadas.

---

### 4.3 Lista de Bicicletas (`ListaSE<Bicicleta>`)

**Uso en el sistema:**
```java
private ListaSE<Bicicleta> bicicletas;
```

**Justificación:**
- **Gestión del inventario completo**: Esta lista mantiene todas las bicicletas del sistema, independientemente de su ubicación o estado.
- **Estados dinámicos**: Las bicicletas cambian de estado constantemente:
  - `DISPONIBLE` (en depósito)
  - `ALQUILADA` (en uso por un usuario)
  - `MANTENIMIENTO` (fuera de servicio)

**Operaciones principales:**
- `registrarBicicleta()`: Agrega una nueva bicicleta al inventario → O(1)
- `marcarEnMantenimiento()`: Busca y modifica el estado → O(n)
- `repararBicicleta()`: Busca y cambia estado → O(n)
- `listarBicisEnDeposito()`: Filtra por ubicación → O(n)

**Importancia del crecimiento dinámico:**
- El sistema puede adquirir nuevas bicicletas en cualquier momento
- Bicicletas viejas pueden darse de baja
- No tiene sentido reservar espacio para un número fijo cuando la flota es cambiante

**Conclusión:** La lista enlazada permite gestionar eficientemente un inventario que crece, decrece y se modifica constantemente.

---

### 4.4 Lista de Bicicletas por Estación (`Estacion.bicicletas`)

**Uso en el sistema:**
```java
public class Estacion {
    public ListaSE<Bicicleta> bicicletas;
}
```

**Justificación - El caso más crítico:**

Este es posiblemente el **uso más importante** de listas enlazadas en el sistema, ya que representa el comportamiento más dinámico:

#### **Escenario real:**
Una estación de bicicletas tiene una capacidad máxima (ej: 20 espacios), pero el número de bicicletas ancladas varía constantemente:
- 8:00 AM: 15 bicicletas (hora pico - la gente las retira para ir al trabajo)
- 10:00 AM: 3 bicicletas (la mayoría están en uso)
- 12:00 PM: 8 bicicletas (algunas devoluciones)
- 6:00 PM: 18 bicicletas (hora pico - la gente las devuelve al volver a casa)

#### **Operaciones frecuentísimas:**
```java
// Cuando un usuario alquila una bicicleta:
estacion.getBicicletas().eliminar(bicicleta); // La bicicleta sale de la estación

// Cuando un usuario devuelve una bicicleta:
estacion.getBicicletas().agregarAlFinal(bicicleta); // La bicicleta entra a la estación
```

Estas operaciones ocurren **cientos de veces al día** en cada estación.

#### **Por qué NO usar un Array:**

Con un array, se presentarían dos problemas graves:

1. **Problema de capacidad:**
   - Si la estación tiene capacidad para 20 bicicletas, ¿crear un array de tamaño 20?
   - ¿Qué pasa cuando están todas en uso y el array está vacío? → Desperdicio de memoria
   
2. **Problema de fragmentación:**
   - Al eliminar bicicletas del medio del array quedan "huecos"
   - Requeriría compactar el array (desplazar elementos) → O(n) en cada retiro
   - O bien, mantener un contador de elementos válidos y buscar linealmente posiciones libres

#### **Ventaja de la Lista Enlazada:**
```java
// Agregar bicicleta cuando llega a la estación
bicicletas.agregarAlFinal(bicicleta); // O(1) - simplemente se añade un nodo

// Retirar bicicleta cuando un usuario la alquila  
bicicletas.eliminar(bicicleta); // O(n) búsqueda, pero sin fragmentación
```

**Conclusión:** Para una estructura que tiene **altas y bajas constantes** (bicicletas que entran y salen), la lista enlazada es la opción óptima. No hay desperdicio de memoria y las operaciones de inserción/eliminación son eficientes.

---

## 5. Análisis de Complejidad Temporal

### 5.1 Operaciones Implementadas en ListaSE

| Operación | Complejidad | Justificación |
|-----------|-------------|---------------|
| `agregarAlInicio()` | **O(1)** | Solo se modifica la referencia de `cabeza` |
| `agregarAlFinal()` | **O(1)** | Se tiene referencia a `fin`, inserción directa |
| `eliminar(dato)` | **O(n)** | Requiere recorrer la lista buscando el elemento |
| `existeElemento()` | **O(n)** | Búsqueda lineal |
| `obtenerElemento()` | **O(n)** | Búsqueda lineal |
| `obtenerElementoPorIndice()` | **O(n)** | Acceso secuencial desde cabeza |
| `adicionarOrdenado()` | **O(n)** | Debe encontrar la posición correcta |
| `esVacia()` | **O(1)** | Solo verifica si `cabeza == null` |
| `getCantidadElementos()` | **O(1)** | Retorna el contador almacenado |

### 5.2 Comparación con Arrays

| Operación | Lista Enlazada | Array |
|-----------|----------------|-------|
| Acceso por índice | O(n) | **O(1)** ✓ |
| Búsqueda | O(n) | O(n) |
| Inserción al inicio | **O(1)** ✓ | O(n) |
| Inserción al final | **O(1)** ✓ | O(1)* |
| Eliminación | O(n) | O(n) |
| Uso de memoria | Dinámico ✓ | Fijo |

*\* Siempre que no se supere la capacidad*

### 5.3 ¿Por qué O(n) en búsqueda es aceptable?

En este sistema:
1. **Los conjuntos de datos no son masivos**: Es razonable esperar cientos o pocos miles de usuarios/estaciones/bicicletas, no millones.
2. **La frecuencia de búsquedas es moderada**: Las operaciones de alquiler/devolución son frecuentes pero manejables.
3. **El beneficio del dinamismo supera el costo**: La flexibilidad de no desperdiciar memoria y crecer/decrecer libremente es más valiosa que el acceso O(1) de un array.

---

## 6. Consideraciones de Diseño

### 6.1 Uso de Genéricos (`<T extends Comparable<T>>`)

La implementación utiliza **genéricos** para hacer la lista reutilizable:
```java
public class ListaSE<T extends Comparable<T>> implements IListaSimple<T>
```

**Ventajas:**
- La misma implementación sirve para `Usuario`, `Estacion`, y `Bicicleta`
- Seguridad de tipos en tiempo de compilación
- Permite ordenamiento mediante `Comparable`

### 6.2 Mantenimiento de Referencia a `fin`

```java
private Nodo<T> cabeza;
private Nodo<T> fin; // ← Optimización importante
```

**Justificación:**
- Sin `fin`: agregar al final sería O(n) (recorrer toda la lista)
- Con `fin`: agregar al final es O(1)
- **Trade-off**: Usa un poco más de memoria (una referencia extra) a cambio de mejor rendimiento

### 6.3 Contador de Elementos (`cantidadElementos`)

```java
private int cantidadElementos;
```

**Justificación:**
- Permite conocer el tamaño en O(1) sin recorrer la lista
- Útil para validaciones (ej: verificar si una estación está llena)
- Se actualiza en cada inserción/eliminación

---

## 7. Colas (Queue): Gestión de Listas de Espera

### 7.1 Fundamentos y Necesidad

Una **Cola** es una estructura de datos que sigue el principio **FIFO (First In, First Out)**: el primer elemento que entra es el primero que sale, como una fila de personas esperando.

**Operaciones principales:**
- `encolar(elemento)` - Agregar al final de la cola → O(1)
- `desencolar()` - Remover del frente de la cola → O(1)
- `frente()` - Ver el primer elemento sin removerlo → O(1)
- `esVacia()` - Verificar si la cola está vacía → O(1)

### 7.2 Aplicación al Sistema SiBiP

#### **7.2.1 Cola de Espera para Alquilar Bicicletas**

**Requisito según consigna (punto 2.9):**
> "Si no hay bici disponible en la estación, el usuario queda en espera hasta que llegue una. Se priorizará el orden para asignar las bicicletas, entre aquellos que están esperando."

**Implementación en Estacion:**
```java
public class Estacion {
    private Cola<Usuario> colaEsperaAlquiler; // Usuarios esperando por una bici
}
```

**Escenario real:**
1. Estación "Pocitos Centro" tiene 0 bicicletas disponibles
2. Usuario A (cedula: 12345678) llega a las 8:00 y queda en espera
3. Usuario B (cedula: 87654321) llega a las 8:05 y queda en espera
4. A las 8:10 llega una bicicleta a la estación
5. **El sistema DEBE asignarla al Usuario A** (primero en llegar)

**¿Por qué una Cola y no una Lista?**
- ✅ **Semántica correcta**: "Primero en llegar, primero en ser atendido" es EXACTAMENTE FIFO
- ✅ **Operaciones O(1)**: Encolar y desencolar son constantes
- ✅ **Código claro**: `colaEsperaAlquiler.desencolar()` es más expresivo que manipular índices
- ❌ **Lista**: Requeriría eliminar del índice 0 y agregar al final, menos eficiente y menos semántico

#### **7.2.2 Cola de Espera para Anclar/Devolver Bicicletas**

**Requisito según consigna (punto 2.10):**
> "Si no hay anclaje libre, el usuario queda en espera por un anclaje (hasta que se libere)."

**Implementación en Estacion:**
```java
public class Estacion {
    private Cola<Usuario> colaEsperaAlquiler;  // Esperando por bicis
    private Cola<Usuario> colaEsperaAnclaje;   // Esperando por espacio para devolver
}
```

**Escenario real:**
1. Estación "Cordón" tiene capacidad 10, con 10 bicicletas ancladas (LLENA)
2. Usuario X llega a devolver su bicicleta a las 18:00 → entra en `colaEsperaAnclaje`
3. Usuario Y llega a devolver a las 18:05 → entra en `colaEsperaAnclaje`
4. A las 18:10 alguien retira una bici → se libera un anclaje
5. **El Usuario X (primero en espera) puede anclar su bicicleta**

**Importancia de dos colas separadas:**
- Una estación puede tener gente esperando para alquilar Y gente esperando para devolver simultáneamente
- Son procesos independientes que requieren estructuras separadas

### 7.3 Implementación Basada en Lista Enlazada

La Cola se puede implementar **utilizando la ListaSE ya creada**:

```java
public class Cola<T> {
    private ListaSE<T> elementos;
    
    public Cola() {
        this.elementos = new ListaSE<>();
    }
    
    public void encolar(T elemento) {
        elementos.agregarAlFinal(elemento);  // O(1) con referencia a 'fin'
    }
    
    public T desencolar() {
        if (esVacia()) return null;
        T primero = elementos.obtenerElementoPorIndice(0);
        elementos.eliminar(primero);
        return primero;
    }
    
    public boolean esVacia() {
        return elementos.esVacia();
    }
}
```

**Ventaja de reutilizar ListaSE:**
- No duplicar código
- Aprovechar la estructura ya implementada y probada
- Mantener consistencia en el proyecto

### 7.4 Justificación de Cola vs Alternativas

| Alternativa | Evaluación | Descartada por: |
|-------------|------------|-----------------|
| **Lista sin orden** | Requiere criterio de selección arbitrario | No cumple requisito "orden de llegada" |
| **Array circular** | Implementación más compleja | Innecesaria cuando ya tenemos ListaSE |
| **Priorizar por cédula** | Sería injusto | No refleja el mundo real (primero en llegar = primero atendido) |
| **Cola (FIFO)** | ✅ Cumple requisitos perfectamente | - |

---

## 8. Pila (Stack): Historial de Retiros

### 8.1 Fundamentos y Necesidad

Una **Pila** es una estructura de datos que sigue el principio **LIFO (Last In, First Out)**: el último elemento que entra es el primero que sale, como una pila de platos.

**Operaciones principales:**
- `apilar(elemento)` - Agregar al tope → O(1)
- `desapilar()` - Remover del tope → O(1)
- `tope()` - Ver el elemento superior sin removerlo → O(1)
- `esVacia()` - Verificar si la pila está vacía → O(1)

### 8.2 Aplicación al Sistema SiBiP

#### **8.2.1 Deshacer Últimos N Retiros**

**Requisito según consigna (punto 2.11):**
> "Deshacer las últimas n asignaciones de alquiler realizadas en el sistema. Efecto inverso: devuelve la bici a su estación de origen."

**Implementación en Sistema:**
```java
public class Sistema {
    private Pila<RegistroRetiro> historialRetiros;
}

class RegistroRetiro {
    String codigoBici;
    String cedulaUsuario;
    String estacionOrigen;
    // Constructor, getters...
}
```

**Escenario de uso:**
1. **10:00** - Usuario A alquila bici XYZ123 en estación "Pocitos"
2. **10:15** - Usuario B alquila bici ABC456 en estación "Centro"
3. **10:30** - Usuario C alquila bici DEF789 en estación "Cordón"
4. **10:45** - Se ejecuta `deshacerUltimosRetiros(2)`

**Orden de deshecho (LIFO):**
```
Primero deshace: Usuario C - bici DEF789 (último alquiler)
Segundo deshace: Usuario B - bici ABC456 (penúltimo alquiler)
```

**¿Por qué una Pila y no otra estructura?**
- ✅ **LIFO es exactamente lo que se necesita**: "Últimas N operaciones" = las más recientes
- ✅ **Operaciones O(1)**: Apilar cada retiro al momento de alquilar, desapilar al deshacer
- ✅ **Semántica de "deshacer"**: Como Ctrl+Z en un editor de texto (siempre deshace lo último)

#### **8.2.2 Almacenamiento de Información del Retiro**

Cada vez que se alquila una bicicleta, se debe registrar:
```java
// En el método alquilarBicicleta():
RegistroRetiro retiro = new RegistroRetiro(
    bicicleta.getCodigo(),
    usuario.getCedula(),
    estacion.getNombre()
);
historialRetiros.apilar(retiro);  // O(1)
```

Al deshacer:
```java
// En el método deshacerUltimosRetiros(int n):
for (int i = 0; i < n && !historialRetiros.esVacia(); i++) {
    RegistroRetiro retiro = historialRetiros.desapilar();  // O(1)
    // Devolver la bici a la estación de origen
    // Liberar al usuario
}
```

### 8.3 Implementación Basada en Lista Enlazada

Similar a la Cola, la Pila puede implementarse usando ListaSE:

```java
public class Pila<T> {
    private ListaSE<T> elementos;
    
    public Pila() {
        this.elementos = new ListaSE<>();
    }
    
    public void apilar(T elemento) {
        elementos.agregarAlInicio(elemento);  // O(1)
    }
    
    public T desapilar() {
        if (esVacia()) return null;
        T tope = elementos.obtenerElementoPorIndice(0);
        elementos.eliminar(tope);
        return tope;
    }
    
    public T tope() {
        if (esVacia()) return null;
        return elementos.obtenerElementoPorIndice(0);
    }
    
    public boolean esVacia() {
        return elementos.esVacia();
    }
}
```

**Nota:** Se usa `agregarAlInicio()` para que el último elemento agregado esté en la cabeza y sea el primero en salir (LIFO).

### 8.4 Justificación de Pila vs Alternativas

| Alternativa | Evaluación | Descartada por: |
|-------------|------------|-----------------|
| **Array con índice** | Requiere tamaño máximo | No sabemos cuántos retiros habrá |
| **Lista recorrida al final** | Ineficiente | Desapilar sería O(n) |
| **Cola** | Orden incorrecto | FIFO no sirve para "últimas N operaciones" |
| **Pila (LIFO)** | ✅ Estructura ideal | - |

---

## 9. Estructuras de Conteo y Estadísticas

### 9.1 Contadores en Clases de Dominio

Para los reportes y estadísticas del sistema, se requieren estructuras adicionales:

#### **9.1.1 Contador de Alquileres por Usuario**

```java
public class Usuario {
    private String cedula;
    private String nombre;
    private int cantidadAlquileres;  // Para reporte 3.10 (usuarioMayor)
    
    public void incrementarAlquileres() {
        this.cantidadAlquileres++;
    }
}
```

**Uso:** Identificar el usuario con mayor cantidad de alquileres (punto 3.10)

#### **9.1.2 Contador de Alquileres por Tipo de Bicicleta**

Opción 1 - En la clase Sistema:
```java
public class Sistema {
    private int alquileresUrbanas;
    private int alquileresMountain;
    private int alquileresElectricas;
}
```

Opción 2 - Usando un mapa/diccionario (más flexible):
```java
public class Sistema {
    private HashMap<String, Integer> alquileresPorTipo;
}
```

**Uso:** Ranking de tipos por uso (punto 3.8)

### 9.2 Justificación de Contadores vs Alternativas

**¿Por qué no recalcular cada vez?**
- ❌ Ineficiente: Recorrer todas las bicicletas y contar cada vez que se pide el reporte → O(n)
- ✅ Eficiente: Incrementar contador en cada alquiler → O(1), consultar reporte → O(1)

**Trade-off:**
- Usa un poco más de memoria (algunos enteros)
- Mejora drásticamente el tiempo de respuesta de reportes

---

## 10. Alternativas Consideradas y Descartadas

### 10.1 Arrays/Vectores (para listas dinámicas)
**Descartados por:**
- Tamaño fijo (no se ajusta al dominio dinámico)
- Desperdicio de memoria cuando hay baja ocupación
- Redimensionamiento costoso al superar capacidad
- No representan bien el comportamiento de altas/bajas frecuentes

### 10.2 ArrayList (Estructura de Java)
**Descartada por:**
- Requisitos académicos (implementar estructuras desde cero)
- Menos control sobre la implementación interna
- Objetivo pedagógico de comprender el funcionamiento de estructuras de datos
- No permite demostrar comprensión de conceptos fundamentales

### 10.3 Listas Doblemente Enlazadas
**No necesaria porque:**
- No se requiere recorrido bidireccional en este sistema
- Mayor complejidad de implementación sin beneficio claro en este contexto
- Mayor uso de memoria (dos referencias por nodo en lugar de una)
- Las listas simplemente enlazadas cumplen todos los requisitos

### 10.4 Tablas Hash / HashMap
**Considerada para búsquedas O(1), pero descartada por:**
- Complejidad de implementación mayor para un proyecto académico inicial
- Requiere función hash bien diseñada para cada entidad
- Posibles colisiones que degradarían rendimiento
- **Posible evolución futura** del sistema si escala significativamente
- El volumen de datos esperado no justifica la complejidad adicional

### 10.5 Árboles Binarios de Búsqueda (ABB/AVL)
**Considerados pero descartados por:**
- Complejidad de implementación mayor (balanceo, rotaciones)
- No son necesarios para el volumen de datos esperado
- Los reportes ordenados se pueden resolver con algoritmos de ordenamiento simples
- **Posible evolución futura** si se requieren búsquedas muy frecuentes

### 10.6 Arrays Circulares (para Colas)
**Descartados por:**
- Mayor complejidad de implementación (manejo de índices wrap-around)
- Requiere tamaño máximo predefinido
- La implementación basada en ListaSE es más simple y cumple los requisitos
- No aporta beneficios significativos en este contexto

---

## 11. Resumen de Estructuras Seleccionadas

### 11.1 Tabla Comparativa de Estructuras

| Estructura | Uso en el Sistema | Principio | Operaciones Clave | Complejidad |
|------------|-------------------|-----------|-------------------|-------------|
| **Lista Enlazada** | Usuarios, Estaciones, Bicicletas | Dinámica | Agregar, buscar, eliminar | O(1) inserción, O(n) búsqueda |
| **Cola (FIFO)** | Esperas de alquiler/anclaje | First In, First Out | Encolar, desencolar | O(1) ambas |
| **Pila (LIFO)** | Historial de retiros | Last In, First Out | Apilar, desapilar | O(1) ambas |
| **Contadores** | Estadísticas | Acumuladores | Incrementar, consultar | O(1) ambas |

### 11.2 Mapa de Estructuras en el Sistema

```java
public class Sistema {
    // LISTAS ENLAZADAS - Colecciones principales
    private ListaSE<Estacion> estaciones;
    private ListaSE<Usuario> usuarios;
    private ListaSE<Bicicleta> bicicletas;
    
    // PILA - Historial para deshacer
    private Pila<RegistroRetiro> historialRetiros;
    
    // CONTADORES - Estadísticas
    private int alquileresUrbanas;
    private int alquileresMountain;
    private int alquileresElectricas;
}

public class Estacion {
    // LISTA ENLAZADA - Bicis ancladas (muy dinámica)
    private ListaSE<Bicicleta> bicicletas;
    
    // COLAS - Listas de espera
    private Cola<Usuario> colaEsperaAlquiler;  // Esperando bicis
    private Cola<Usuario> colaEsperaAnclaje;   // Esperando espacio
}

public class Usuario {
    private String cedula;
    private String nombre;
    private int cantidadAlquileres;  // Contador para estadísticas
}
```

---

## 12. Conclusiones

### 12.1 Resumen de Justificación General

El conjunto de estructuras seleccionadas para el Sistema de Bicicletas Públicas (SiBiP) responde de manera óptima a los requisitos funcionales y no funcionales del proyecto:

#### **1. Listas Simplemente Enlazadas**
✅ **Adaptabilidad dinámica**: El sistema crece y decrece según las necesidades reales, sin desperdiciar memoria.

✅ **Operaciones eficientes**: Las inserciones al inicio y final son O(1), que son las operaciones más frecuentes.

✅ **Flexibilidad de modificación**: Agregar/eliminar estaciones, usuarios y bicicletas no requiere redimensionamiento costoso.

✅ **Modelado fiel del dominio**: El comportamiento dinámico de las bicicletas (alquileres, devoluciones, mantenimiento) se refleja perfectamente en una estructura dinámica.

✅ **Base reutilizable**: Sirve como fundamento para implementar Colas y Pilas.

#### **2. Colas (FIFO)**
✅ **Semántica correcta**: "Primero en llegar, primero en ser atendido" es inherente a FIFO.

✅ **Justicia en asignación**: Garantiza que los usuarios sean atendidos en orden de llegada.

✅ **Operaciones constantes**: Encolar y desencolar en O(1).

✅ **Claridad de código**: El código expresa claramente la intención del sistema.

#### **3. Pilas (LIFO)**
✅ **Deshacer operaciones**: LIFO es exactamente lo que se necesita para revertir las últimas N acciones.

✅ **Eficiencia**: Apilar y desapilar son O(1).

✅ **Historial automático**: Cada alquiler se registra sin costo adicional.

#### **4. Contadores**
✅ **Reportes instantáneos**: Consultar estadísticas en O(1) en lugar de recalcular cada vez.

✅ **Mínimo overhead**: Solo algunos enteros adicionales en memoria.

### 12.2 Casos de Uso que Demuestran las Elecciones

#### **Caso 1: Hora Pico - Estación sin Bicicletas**
**Situación:** Estación "Pocitos" a las 8:00 AM, 0 bicicletas disponibles, 5 usuarios llegan sucesivamente.

**Sin Cola (alternativa incorrecta):**
- ❌ ¿A quién asignar la primera bici que llegue? → Ambiguo
- ❌ Implementar orden manualmente → Propenso a errores

**Con Cola:**
- ✅ Los 5 usuarios se encolan en orden de llegada
- ✅ Cuando llega una bici, se asigna automáticamente al primero de la cola
- ✅ Justo, eficiente, y código simple

#### **Caso 2: Deshacer Errores Operativos**
**Situación:** Se detecta que las últimas 3 asignaciones fueron erróneas y deben revertirse.

**Sin Pila (alternativa incorrecta):**
- ❌ Buscar manualmente las últimas 3 operaciones → O(n)
- ❌ Mantener lista ordenada por timestamp → Complejidad adicional

**Con Pila:**
- ✅ `deshacerUltimosRetiros(3)` desapila las 3 últimas operaciones
- ✅ Automáticamente en orden inverso (LIFO)
- ✅ Operación O(1) por cada retiro deshecho

#### **Caso 3: Estación con Alta Rotación**
**Situación:** Estación "Centro" tiene capacidad 15, durante 1 hora: 45 retiros y 40 devoluciones.

**Con Array:**
- ❌ 45 eliminaciones dejan "huecos" → Fragmentación
- ❌ Compactar array después de cada operación → O(n) × 85 operaciones
- ❌ O bien desperdiciar memoria manteniendo elementos marcados como "inválidos"

**Con Lista Enlazada:**
- ✅ Agregar/eliminar bicicletas sin fragmentación
- ✅ 85 operaciones O(1) para inserción/eliminación
- ✅ Memoria se ajusta automáticamente (15 bicis → 10 bicis → 15 bicis)

#### **Caso 4: Reportes de Estadísticas**
**Situación:** Se solicita el ranking de tipos de bicicletas más usados.

**Sin contadores:**
- ❌ Recorrer todas las bicicletas y contar cada vez → O(n)
- ❌ Ineficiente si se consulta frecuentemente

**Con contadores:**
- ✅ Incrementar en cada alquiler → O(1)
- ✅ Consultar reporte → O(1)
- ✅ Respuesta instantánea

### 12.3 Coherencia del Diseño

El diseño presenta una **arquitectura coherente** donde:

1. **ListaSE es la base fundamental**:
   - Implementada una sola vez
   - Reutilizada para Colas y Pilas
   - Reduce duplicación de código

2. **Cada estructura tiene un propósito claro**:
   - Lista → Almacenamiento dinámico
   - Cola → Orden de llegada (FIFO)
   - Pila → Deshacer operaciones (LIFO)
   - Contadores → Estadísticas eficientes

3. **Cumple objetivos pedagógicos**:
   - Implementación desde cero (no usar Java Collections)
   - Comprensión profunda de estructuras de datos
   - Aplicación práctica en un dominio realista

### 12.4 Escalabilidad y Mantenibilidad

**Para el alcance actual:**
- ✅ Las estructuras seleccionadas son **suficientes y óptimas**
- ✅ Código claro, mantenible y fácil de depurar
- ✅ Cumple todos los requisitos funcionales de la consigna

**Para crecimiento futuro (si escala significativamente):**

Si el sistema llegara a manejar **decenas de miles** de usuarios/bicicletas:

1. **Índices secundarios con HashMap**:
   - Mantener listas como estructura primaria
   - Agregar `HashMap<String, Bicicleta>` para búsquedas O(1) por código
   - Mantener ambas estructuras sincronizadas

2. **Árboles de Búsqueda (ABB/AVL)**:
   - Para usuarios/estaciones si las búsquedas son muy frecuentes
   - Búsqueda O(log n) en lugar de O(n)

3. **Base de datos**:
   - Para persistencia y consultas complejas
   - Las estructuras en memoria como caché

Sin embargo, **para el contexto académico y el volumen esperado**, las estructuras actuales son la **solución correcta y completa**.

---

## 13. Reflexión Final

Este proyecto demuestra que la **selección de estructuras de datos** no es una decisión arbitraria, sino que debe estar **fundamentada en**:

1. **Análisis del dominio**: Comprender las operaciones reales del sistema
2. **Requisitos funcionales**: Qué debe hacer el sistema
3. **Eficiencia computacional**: Complejidad temporal y espacial
4. **Claridad de código**: Mantenibilidad y expresividad
5. **Objetivos pedagógicos**: Aprendizaje de conceptos fundamentales

La combinación de **Listas Enlazadas, Colas y Pilas** no solo cumple con todos los requisitos técnicos del Sistema de Bicicletas Públicas, sino que también representa un diseño **elegante, eficiente y conceptualmente correcto**.

---

**Fecha:** 6 de Octubre de 2025  
**Materia:** Algoritmos y Estructuras de Datos  
