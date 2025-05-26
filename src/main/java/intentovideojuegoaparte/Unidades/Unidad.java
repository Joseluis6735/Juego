package intentovideojuegoaparte.Unidades;

import java.util.Random;

public abstract class Unidad {
    protected String nombre;
    public int hp;
    protected int ataque;
    protected int defensa;
    protected int rangoMovimiento;
    protected int rangoAtaque;
    protected int x, y;
    protected String faccion;
    private static final Random random = new Random();

    public Unidad(String nombre, int hp, int ataque, int defensa, int rangoMovimiento, int rangoAtaque, String faccion) {
        this.nombre = nombre;
        this.hp = hp;
        this.ataque = ataque;
        this.defensa = defensa;
        this.rangoMovimiento = rangoMovimiento;
        this.rangoAtaque = rangoAtaque;
        this.faccion = faccion;
    }

    public void setPosicion(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getRangoMovimiento() { return rangoMovimiento; }
    public int getRangoAtaque() { return rangoAtaque; }
    public String getFaccion() { return faccion; }
    public int getHP() { return hp; }
    public String getNombre() { return nombre; }

    public boolean estaViva() { return hp > 0; }

    public void recibirDanio(int danio) {
        hp -= danio;
        if (hp < 0) hp = 0;
    }

    public void atacar(Unidad objetivo) {
        int dado = random.nextInt(6) + 1; // 1 a 6
        int danioBase = Math.max(0, ataque - objetivo.defensa);
        int danioFinal;

        if (esCientifico()) {
            // Ciencias: si dado es 1-3 duplica daño, sino daño base
            if (dado >= 1 && dado <= 3) {
                danioFinal = danioBase * 2;
            } else {
                danioFinal = danioBase;
            }
        } else {
            // Letras: si dado es 4-6 duplica daño, sino daño base
            if (dado >= 4 && dado <= 6) {
                danioFinal = danioBase * 2;
            } else {
                danioFinal = danioBase;
            }
        }

        objetivo.recibirDanio(danioFinal);
    }

    private boolean esCientifico() {
        // Consideramos que Ciencias son estas facciones:
        // "Científicos" o las que definas para esa facción
        // O también puedes decidirlo por nombre de unidad
        // Mejor usar la facción:
        return faccion.equalsIgnoreCase("Científicos");
    }
}
