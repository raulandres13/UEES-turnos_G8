package labturnos;


import java.time.Instant;
import java.util.Objects;


public class Turno {
    public enum Prioridad { ALTA, MEDIA, BAJA }


    private final String id; // A-### / M-### / B-###
    private final Prioridad prioridad; // ALTA > MEDIA > BAJA
    private final long seq; // Para mantener FIFO dentro de la misma prioridad
    private final Instant createdAt;


    public Turno(String id, Prioridad prioridad, long seq) {
        this.id = id;
        this.prioridad = prioridad;
        this.seq = seq;
        this.createdAt = Instant.now();
    }


    public String getId() { return id; }
    public Prioridad getPrioridad() { return prioridad; }
    public long getSeq() { return seq; }
    public Instant getCreatedAt() { return createdAt; }


    @Override
    public String toString() {
        return id + " (" + prioridad + ")";
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Turno)) return false;
        Turno turno = (Turno) o;
        return seq == turno.seq && Objects.equals(id, turno.id) && prioridad == turno.prioridad;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, prioridad, seq);
    }
}