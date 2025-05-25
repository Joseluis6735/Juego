package intentovideojuegoaparte;


public class Casilla {
    private int x, y;
    private int costoMovimiento;
    private int modDefensa; // puede ser positivo o negativo
    private int modMovimiento; // puede ser positivo o negativo
    private Unidad unidad; // null si está vacía

    public Casilla(int x, int y, int costoMovimiento, int modDefensa, int modMovimiento) {
        this.x = x;
        this.y = y;
        this.costoMovimiento = costoMovimiento;
        this.modDefensa = modDefensa;
        this.modMovimiento = modMovimiento;
        this.unidad = null;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getCostoMovimiento() { return costoMovimiento; }
    public int getModDefensa() { return modDefensa; }
    public int getModMovimiento() { return modMovimiento; }
    public Unidad getUnidad() { return unidad; }
    public void setUnidad(Unidad u) { this.unidad = u; }

    public boolean estaLibre() { return unidad == null; }
    public boolean estaOcupada() { return unidad != null; }

}


