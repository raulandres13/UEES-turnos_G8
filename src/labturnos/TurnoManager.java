package labturnos;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * MODO BASE (runnable sin lógica): este manager NO implementa la lógica real.
 * Sirve solo para que la app abra y se puedan ver las pantallas.
 * Los métodos muestran datos de demostración y no alteran estados reales.
 *
 * TODOs para estudiantes en la versión completa:
 *  - Comparador prioridad+FIFO para la cola (PriorityQueue)
 *  - Generación de IDs A-### / M-### / B-###
 *  - Siguiente global con ranking de cabinas libres
 *  - Terminar que mueva a bucket y actualice métricas de cabina
 */
public class TurnoManager {
    private final AtomicLong seq = new AtomicLong(0);

    private final PriorityQueue<Turno> cola;
    private final List<Turno> atendidos = new ArrayList<>();
    private final Cabina[] cabinas;

    public TurnoManager(int numCabinas) {
        this.cabinas = new Cabina[numCabinas];
        for (int i = 0; i < numCabinas; i++) cabinas[i] = new Cabina(i+1);

        // Comparador "dummy" por secuencia (para que no falle).
        this.cola = new PriorityQueue<>(Comparator.comparingLong(Turno::getSeq));

        // Estado inicial demostrativo: 2 tickets en cola
        cola.offer(new Turno("DEMO-M-001", Turno.Prioridad.MEDIA, seq.getAndIncrement()));
        cola.offer(new Turno("DEMO-A-001", Turno.Prioridad.ALTA, seq.getAndIncrement()));
    }

    // ------ Métodos "mock" (no hacen lógica real) ------

    // TODO(2): usar nextId() real cuando implementen generación de IDs
public Turno tomarTurno(Turno.Prioridad prioridad) {
        // Genera un id demostrativo y lo agrega a la cola para que la UI muestre algo.
        String pref = prioridad == Turno.Prioridad.ALTA ? "A" : prioridad == Turno.Prioridad.MEDIA ? "M" : "B";
        String id = "DEMO-" + pref + "-" + String.format("%03d", (seq.intValue()%999)+1);
        Turno t = new Turno(id, prioridad, seq.getAndIncrement());
        cola.offer(t);
        return t;
    }

    // TODO(3): ordenar por el mismo criterio del comparador prioridad+FIFO
public List<Turno> snapshotColaOrdenada() {
        // Retorna una lista legible, ordenada por seq (mock).
        List<Turno> list = new ArrayList<>(cola);
        list.sort(Comparator.comparingLong(Turno::getSeq));
        return list;
    }

    public List<Turno> getAtendidos() { return Collections.unmodifiableList(atendidos); }
    public Cabina[] getCabinas() { return cabinas; }

    public boolean hayCabinaLibre() {
        for (Cabina c : cabinas) if (c.getTurnoActual() == null) return true;
        return false;
    }

    public boolean hayTurnosEnCola() { return !cola.isEmpty(); }

    // TODO(4): implementar mover a bucket, ++atendidos, set lastFinishedAt, dejar cabina libre
public void terminar(int cabinaIdx) {
        // Mock: no hace nada (los estudiantes implementarán la lógica).
    }

    // TODO(5): elegir cabina libre por ranking (atendidos, lastFinishedAt, id) y asignar mejor turno
public void siguiente() {
        // Mock: no hace nada (los estudiantes implementarán la lógica).
    }

    // TODO(6): devolver cabinas libres ordenadas por (atendidosCount, lastFinishedAt, id)
public List<Cabina> snapshotCabinasLibresOrdenadas() {
        // Mock: devuelve todas como libres, en orden por id, para mostrar en la UI.
        List<Cabina> libres = new ArrayList<>();
        for (Cabina c : cabinas) if (c.getTurnoActual() == null) libres.add(c);
        libres.sort(Comparator.comparingInt(Cabina::getId));
        return libres;
    }
}