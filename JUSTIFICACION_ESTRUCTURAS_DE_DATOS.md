# Justificación de Estructuras de Datos Seleccionadas
## Sistema de Gestión de Bicicletas Compartidas

### Integrantes del Equipo
[Completar con nombres y números de estudiante]

---

## 1. Introducción

En este proyecto se implementó un sistema de gestión de bicicletas compartidas que permite administrar estaciones, usuarios y bicicletas. Para la implementación se optó por utilizar **Listas Simplemente Enlazadas** como estructura de datos principal. A continuación se justifica esta decisión técnica en el contexto del dominio del problema.

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

## 7. Alternativas Consideradas y Descartadas

### 7.1 Arrays/Vectores
**Descartados por:**
- Tamaño fijo (no se ajusta al dominio dinámico)
- Desperdicio de memoria
- Redimensionamiento costoso

### 7.2 ArrayList (Estructura de Java)
**Descartada por:**
- Requisitos académicos (implementar la estructura desde cero)
- Menos control sobre la implementación interna
- Objetivo pedagógico de comprender el funcionamiento de listas enlazadas

### 7.3 Listas Doblemente Enlazadas
**No necesaria porque:**
- No se requiere recorrido bidireccional en este sistema
- Mayor complejidad de implementación sin beneficio claro
- Mayor uso de memoria (dos referencias por nodo en lugar de una)

### 7.4 Tablas Hash
**Considerada para búsquedas O(1), pero descartada por:**
- Complejidad de implementación mayor
- Requiere función hash bien diseñada para cada entidad
- Posibles colisiones que degradarían rendimiento
- **Posible evolución futura** del sistema si crece significativamente

---

## 8. Conclusiones

### 8.1 Resumen de Justificación

Las **Listas Simplemente Enlazadas** son la estructura de datos óptima para este sistema porque:

1. ✅ **Adaptabilidad dinámica**: El sistema crece y decrece según las necesidades reales, sin desperdiciar memoria.

2. ✅ **Operaciones eficientes**: Las inserciones al inicio y final son O(1), que son las operaciones más frecuentes.

3. ✅ **Flexibilidad de modificación**: Agregar/eliminar estaciones, usuarios y bicicletas no requiere redimensionamiento costoso.

4. ✅ **Modelado fiel del dominio**: El comportamiento dinámico de un sistema de bicicletas compartidas (alquileres, devoluciones, mantenimiento) se refleja perfectamente en una estructura dinámica.

5. ✅ **Simplicidad de implementación**: Código claro y mantenible, cumpliendo con los objetivos pedagógicos de la materia.

### 8.2 Casos de Uso que Demuestran la Elección

#### **Caso 1: Registro de Bicicletas**
Cuando el sistema adquiere 50 bicicletas nuevas:
- ✅ Con lista: Se agregan de una en una, O(1) cada una
- ❌ Con array: Si se supera capacidad, copiar todo el array anterior → O(n)

#### **Caso 2: Hora Pico en Estación**
Entre las 8:00 y 9:00 AM, 15 usuarios retiran bicicletas de una estación:
- ✅ Con lista: 15 eliminaciones, sin fragmentación
- ❌ Con array: 15 "huecos" en el array, requiere compactación o manejo de índices complejos

#### **Caso 3: Sistema en Crecimiento**
La ciudad decide duplicar el número de estaciones:
- ✅ Con lista: Agregar estaciones sin límite predefinido
- ❌ Con array: Redimensionar array de estaciones → operación costosa

---

## 9. Posibles Mejoras Futuras

Si el sistema escalara significativamente (cientos de miles de operaciones), se podrían considerar:

1. **Índices secundarios con Tablas Hash**:
   - Mantener la lista como estructura primaria
   - Agregar HashMap<String, Bicicleta> para búsquedas O(1) por código

2. **Árboles de Búsqueda (ABB o AVL)**:
   - Para usuarios/estaciones si las búsquedas se vuelven un cuello de botella
   - Complejidad O(log n) en lugar de O(n)

3. **Listas Doblemente Enlazadas**:
   - Si se requiere recorrido bidireccional o eliminación más eficiente

Sin embargo, para el alcance actual del proyecto, **las Listas Simplemente Enlazadas son la solución correcta y suficiente**.

---

**Fecha:** 6 de Octubre de 2025  
**Materia:** Algoritmos y Estructuras de Datos  
