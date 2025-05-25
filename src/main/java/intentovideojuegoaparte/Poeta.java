package intentovideojuegoaparte;

public class Poeta extends Unidad {
    public Poeta() {
        super("Poeta", "Letras", 95, 25, 20, 3, 2);
    }

    @Override
    public int habilidadEspecial(int danoBase) {
        // Se queda a 1 HP como el biólogo
        if(this.hpActual <= 0) {
            this.hpActual = 1;
        }
        return danoBase;
    }
}