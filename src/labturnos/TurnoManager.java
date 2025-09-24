package labturnos;


import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Orquesta la cola de turnos (PriorityQueue), las 4 cabinas, a la list de atendidos y el ranking de cabinas libres.
 */
public class TurnoManager {

    // Cabinas
    private final List<Cabina> cabinas = new ArrayList<>();
    // Lista de atendidos
    private final List<Turno> atendidos = new ArrayList<>();
    // Ranking de cabinas libres (TreeSet) — TODO(6)
    private final TreeSet<Cabina> rankingLibres = new TreeSet<>(new Comparator<Cabina>() {
        @Override
        public int compare(Cabina c1, Cabina c2) {
            // (1) menor cantidad de atendidos
            if (c1.getAtendidos() != c2.getAtendidos())
                return Integer.compare(c1.getAtendidos(), c2.getAtendidos());
            // (2) la que liberó ANTES su última asignación (lastFinishedAt más antiguo)
            // null (nunca liberó) se considera "más nuevo" que cualquier fecha, así no gana sobre quien sí descansó
            if (!Objects.equals(c1.getLastFinishedAt(), c2.getLastFinishedAt())) {
                if (c1.getLastFinishedAt() == null) return 1; // c1 peor.
                if (c2.getLastFinishedAt() == null) return -1; // c1 mejor.
                int cmp = c1.getLastFinishedAt().compareTo(c2.getLastFinishedAt());
                if (cmp != 0) return cmp; // más antiguo primero
            }
            // (3) menor id de cabina
            return Integer.compare(c1.getId(), c2.getId());
        }
    });
    // Cola de prioridad de turnos
    private final PriorityQueue<Turno> cola = new PriorityQueue<>(COLA_COMPARATOR);

    public TurnoManager() {
        // Crear 4 cabinas y agregarlas al ranking de libres
        for (int i = 1; i <= 4; i++) {
            Cabina c = new Cabina(i);
            cabinas.add(c);
            rankingLibres.add(c);
        }
        // Estado inicial: 2 turnos en espera
        nuevoTurno(Turno.Prioridad.ALTA);
        nuevoTurno(Turno.Prioridad.MEDIA);
    }

    // TODO(1): comparador prioridad + FIFO (ALTA > MEDIA > BAJA y, en empate, menor seq primero)
    private static final Comparator<Turno> COLA_COMPARATOR = (a, b) -> {
        int pa = prioridadScore(a.getPrioridad());
        int pb = prioridadScore(b.getPrioridad());
        if (pa != pb) return Integer.compare(pb, pa); // mayor score = más prioridad
        return Long.compare(a.getSeq(), b.getSeq()); // FIFO
    };

    private static int prioridadScore(Turno.Prioridad p) {
        switch (p) {
            case ALTA: return 3;
            case MEDIA: return 2;
            case BAJA: return 1;
        }
        return 0;
    }

    // Generación de IDs secuenciales por prioridad
    private int nextA = 1, nextM = 1, nextB = 1;
    private final AtomicLong seqGen = new AtomicLong(1); // para FIFO

    // TODO(2): generación de IDs A-### / M-### / B-###
    public Turno nuevoTurno(Turno.Prioridad p) {
        String id;
        switch (p) {
            case ALTA: id = String.format("A-%03d", nextA++); break;
            case MEDIA: id = String.format("M-%03d", nextM++); break;
            case BAJA: id = String.format("B-%03d", nextB++); break;
            default: throw new IllegalArgumentException("Prioridad desconocida");
        }
        Turno t = new Turno(id, p, seqGen.getAndIncrement());
        cola.offer(t);
        return t;
    }

    public boolean hayTurnosEnCola() { return !cola.isEmpty(); }
    public boolean hayCabinaLibre() { return rankingLibres.stream().anyMatch(Cabina::isLibre); }
    public List<Turno> getAtendidosSnapshot() { return Collections.unmodifiableList(new ArrayList<>(atendidos)); }
    public List<Cabina> getCabinas() { return Collections.unmodifiableList(cabinas); }

    // TODO(3): snapshot ordenado para UI de la cola
    public List<Turno> getColaSnapshot() {
        // PriorityQueue no garantiza iteración ordenada; hacemos copia y ordenamos con el comparador
        List<Turno> list = new ArrayList<>(cola);
        list.sort(COLA_COMPARATOR);
        return list;
    }

    /**
     * TODO(4): termina el turno de cierta cabina → mueve a la lista del final, incrementa contadores, registra lastFinishedAt y libera.
     * Luego reingresa al ranking de libres.
     */
    public Turno terminarCabina(int cabinaId) {
        Cabina c = cabinas.stream().filter(k -> k.getId() == cabinaId).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cabina inválida: " + cabinaId));
        if (c.isLibre()) throw new IllegalStateException("La cabina ya estaba libre");
        Turno t = c.terminar();
        atendidos.add(t);
        // vuelve al ranking de libres
        rankingLibres.add(c);
        return t;
    }

    /**
     * TODO(5): Siguiente global asigna el mejor turno a la mejor cabina libre según ranking.
     * Devuelve true si se asignó; false si no había turno o no había cabina libre.
     */
    public boolean siguienteGlobal() {
        if (cola.isEmpty()) return false;
        // tomar la mejor cabina libre
        Cabina destino = null;
        for (Cabina c : rankingLibres) {
            if (c.isLibre()) { destino = c; break; }
        }
        if (destino == null) return false;

        // retiramos la cabina del ranking temporalmente (para evitar inconsistencias al asignar)
        rankingLibres.remove(destino);
        Turno t = cola.poll();
        destino.asignar(t);
        // Al estar ocupada, NO se reingresa al ranking; volverá cuando libere
        return true;
    }

    // TODO(6): snapshot de cabinas libres ordenadas
    public List<Cabina> getRankingLibresSnapshot() {
        List<Cabina> libres = new ArrayList<>();
        for (Cabina c : rankingLibres) {
            if (c.isLibre()) libres.add(c);
        }
        return libres;
    }


}