package intentovideojuegoaparte;

public class Biologo extends Unidad {
    public Biologo() {
        super("Biólogo", "Ciencias", 90, 22, 18, 3, 1);
    }

    @Override
    public int habilidadEspecial(int danoBase) {
        // Si HP <= 0 resucita con 1 HP
        if(this.hpActual <= 0) {
            this.hpActual = 1;
        }
        return danoBase;
    }
}
