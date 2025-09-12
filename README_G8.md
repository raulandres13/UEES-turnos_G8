# Turnos con Colas de Prioridad — BASE **runnable** (sin lógica)
Este proyecto abre la **pantalla única** y permite pulsar botones, pero **no implementa** la lógica real (muestra mensajes "Funcionalidad por completar"). Es ideal para que pruebes la interfaz sin errores y luego entregues a los estudiantes la versión con TODOs.

## Requisitos
- Java 17+

## Ejecutar (sin Gradle)
```bash
# Compilar y crear JAR
bash build.sh

# Ejecutar
java -jar build/libs/turnos-base-mock.jar
```

## Qué verás
- Sección de **Control** con "Nuevo ALTA/MEDIA/BAJA" y "Siguiente".
- **Cola** con 2 tickets DEMO pre-cargados y los que generes.
- **4 cabinas** en "Libre" con botón "Terminar" deshabilitado.
- **Ranking** de cabinas libres (lista por id).
- **Bucket** de atendidos vacío.
- Al pulsar **Siguiente** o **Terminar**: aparece un aviso "Funcionalidad por completar".