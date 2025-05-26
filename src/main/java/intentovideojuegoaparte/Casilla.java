package intentovideojuegoaparte;

import intentovideojuegoaparte.Unidades.Unidad;

public class Casilla {
    private int costoMovimiento;
    private int modificadorDefensa;
    private int modificadorMovimiento;
    private Unidad unidad;

    public Casilla(int costoMovimiento, int modDefensa, int modMovimiento) {
        this.costoMovimiento = costoMovimiento;
        this.modificadorDefensa = modDefensa;
        this.modificadorMovimiento = modMovimiento;
    }

    public int getCostoMovimiento() { return costoMovimiento; }
    public int getModificadorDefensa() { return modificadorDefensa; }
    public int getModificadorMovimiento() { return modificadorMovimiento; }

    public boolean estaOcupada() { return unidad != null; }

    public Unidad getUnidad() { return unidad; }
    public void setUnidad(Unidad unidad) { this.unidad = unidad; }
}



