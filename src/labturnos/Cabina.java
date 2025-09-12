package labturnos;

public class Cabina {
    private final int id;
    private Turno turnoActual;
    private int atendidosCount = 0;
    private long lastFinishedAt = 0L;

    public Cabina(int id) { this.id = id; }

    public int getId() { return id; }
    public Turno getTurnoActual() { return turnoActual; }
    public void setTurnoActual(Turno t) { this.turnoActual = t; }

    public int getAtendidosCount() { return atendidosCount; }
    public void incAtendidos() { this.atendidosCount++; }
    public long getLastFinishedAt() { return lastFinishedAt; }
    public void setLastFinishedAt(long ts) { this.lastFinishedAt = ts; }
}