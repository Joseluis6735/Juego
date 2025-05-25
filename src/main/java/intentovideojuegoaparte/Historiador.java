package intentovideojuegoaparte;


public class Historiador extends Unidad {
    public Historiador() {
        super("Historiador", "Letras", 100, 29, 20, 3, 2);
    }

    @Override
    public int habilidadEspecial(int danoBase) {
        int rand = 1 + (int)(Math.random() * 6); // 1-6
        if(rand % 2 == 0) {
            return danoBase * 2;
        }
        return danoBase;
    }
}
