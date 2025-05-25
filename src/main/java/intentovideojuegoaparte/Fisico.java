package intentovideojuegoaparte;


public class Fisico extends Unidad {
    public Fisico() {
        super("Físico", "Ciencias", 110, 28, 25, 3, 2);
    }

    @Override
    public int habilidadEspecial(int danoBase) {
        int rand = 1 + (int)(Math.random() * 4); // 1-4
        if(rand % 2 == 0) {
            return danoBase * 2; // daño duplicado si par
        } else {
            return danoBase / 2; // daño reducido a la mitad si impar
        }
    }
}
