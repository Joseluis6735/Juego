package intentovideojuegoaparte;

public class Filologo extends Unidad {
    public Filologo(String faccion) {
        super("Filólogo", "Letras", 100, 28, 22, 3, 2);
    }

    @Override
    public int habilidadEspecial(int danoBase) {
        int rand = 1 + (int)(Math.random() * 2); // 1 o 2
        if(rand == 2) {
            return danoBase * 2;
        }
        return danoBase;
    }
}