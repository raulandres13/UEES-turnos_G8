package labturnos;


import java.time.Instant;


public class Cabina {
    private final int id; // 1..4
    private Turno actual; // null si Libre
    private int atendidos; // contador total
    private Instant lastFinishedAt; // instante de liberación (null al inicio)


    public Cabina(int id) {
        this.id = id;
        this.actual = null;
        this.atendidos = 0;
        this.lastFinishedAt = null;
    }


    public int getId() { return id; }
    public Turno getActual() { return actual; }
    public int getAtendidos() { return atendidos; }
    public Instant getLastFinishedAt() { return lastFinishedAt; }
    public boolean isLibre() { return actual == null; }


    /** Asigna un turno a la cabina (debe estar libre). */
    public void asignar(Turno t) {
        if (!isLibre()) throw new IllegalStateException("La cabina " + id + " no está libre");
        this.actual = t;
    }


    /**
     * Termina el turno actual, incrementa contador, registra lastFinishedAt y libera la cabina.
     * Devuelve el turno atendido para moverlo a la lista de atendidos.
     */
    public Turno terminar() {
        if (isLibre()) throw new IllegalStateException("La cabina " + id + " ya está libre");
        Turno t = this.actual;
        this.actual = null;
        this.atendidos++;
        this.lastFinishedAt = Instant.now();
        return t;
    }


    @Override
    public String toString() {
        String estado = isLibre() ? "Libre" : ("Atendiendo: " + actual.getId());
        return "Cabina " + id + " — " + estado + " | atendidos=" + atendidos;
    }
}