package intentovideojuegoaparte;

public class Matematico extends Unidad {
    public Matematico() {
        super("Matemático", "Ciencias", 100, 30, 20, 3, 2);
    }

    @Override
    public int habilidadEspecial(int danoBase) {
        int multiplicador = 1 + (int)(Math.random() * 5); // 1-5
        return danoBase * multiplicador;
    }
}






