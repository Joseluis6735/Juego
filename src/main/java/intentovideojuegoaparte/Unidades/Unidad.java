package intentovideojuegoaparte.Unidades;

public abstract class Unidad {
    protected String nombre;
    public int hp;
    protected int ataque;
    protected int defensa;
    protected int rangoMovimiento;
    protected int rangoAtaque;
    protected int x, y;
    protected String faccion;

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
        int factor = (int) (Math.random() * 3); // 0, 1, 2
        int danio = Math.max(0, (factor * ataque) - objetivo.defensa);
        objetivo.recibirDanio(danio);
    }
}