package intentovideojuegoaparte;

import java.util.Random;

public abstract class Unidad {
    protected String nombre;
    protected String faccion; // "Ciencias" o "Letras"
    protected int hpMax, hpActual;
    protected int ataque;
    protected int defensa;
    protected int rangoMovimiento;
    protected int rangoAtaque;
    protected int posX, posY;
    protected boolean haActuado;
    protected Random random = new Random();

    public Unidad(String nombre, String faccion, int hp, int ataque, int defensa, int rangoMovimiento, int rangoAtaque) {
        this.nombre = nombre;
        this.faccion = faccion;
        this.hpMax = hp;
        this.hpActual = hp;
        this.ataque = ataque;
        this.defensa = defensa;
        this.rangoMovimiento = rangoMovimiento;
        this.rangoAtaque = rangoAtaque;
        this.haActuado = false;
        this.posX = -1; // posición no asignada
        this.posY = -1;
    }

    // Getters y setters para edición en ventana
    public String getNombre() { return nombre; }
    public String getFaccion() { return faccion; }
    public int getHpMax() { return hpMax; }
    public void setHpMax(int hpMax) { this.hpMax = hpMax; }
    public int getHpActual() { return hpActual; }
    public void setHpActual(int hpActual) { this.hpActual = Math.min(hpActual, hpMax); }
    public int getAtaque() { return ataque; }
    public void setAtaque(int ataque) { this.ataque = ataque; }
    public int getDefensa() { return defensa; }
    public void setDefensa(int defensa) { this.defensa = defensa; }
    public int getRangoMovimiento() { return rangoMovimiento; }
    public void setRangoMovimiento(int rangoMovimiento) { this.rangoMovimiento = rangoMovimiento; }
    public int getRangoAtaque() { return rangoAtaque; }
    public void setRangoAtaque(int rangoAtaque) { this.rangoAtaque = rangoAtaque; }
    public int getPosX() { return posX; }
    public int getPosY() { return posY; }
    public void setPosicion(int x, int y) { this.posX = x; this.posY = y; }
    public boolean haActuado() { return haActuado; }
    public void setHaActuado(boolean val) { haActuado = val; }


    // Método abstracto para habilidad especial en ataque
    public abstract int habilidadEspecial(int danoBase);

    // Método para calcular daño considerando defensa del oponente
    public int calcularDanioRecibido(int dano) {
        int reduccion = (int)(this.defensa * 0.1); // defensa reduce 10% su valor al daño
        int danoFinal = dano - reduccion;
        return danoFinal > 0 ? danoFinal : 0;
    }

    // Recibir daño (reduce HP)
    public void recibirDanio(int dano) {
        int danoFinal = calcularDanioRecibido(dano);
        hpActual -= danoFinal;
        if(hpActual < 0) hpActual = 0;
    }

    // Distancia Manhattan
    public int distanciaA(int x, int y) {
        return Math.abs(posX - x) + Math.abs(posY - y);
    }

    // Atacar a una unidad enemiga (aplica habilidad especial)
    public void atacar(Unidad enemigo) {
        int danoBase = this.ataque;
        int danoEspecial = habilidadEspecial(danoBase);
        enemigo.recibirDanio(danoEspecial);
    }

    // Verifica si enemigo está en rango de ataque
    public boolean enemigoEnRango(Unidad enemigo) {
        return this.distanciaA(enemigo.posX, enemigo.posY) <= this.rangoAtaque;
    }

    @Override
    public String toString() {
        return nombre + " (" + faccion + ") HP:" + hpActual + "/" + hpMax + " Atq:" + ataque + " Def:" + defensa;
    }
}
