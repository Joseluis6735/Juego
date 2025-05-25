package intentovideojuegoaparte;

public class Ingeniero extends Unidad {
    public Ingeniero(String faccion) {
        super("Ingeniero", "Ciencias", 120, 20, 30, 3, 1);
    }

    @Override
    public int habilidadEspecial(int danoBase) {
        // Si enemigo HP < 50% hace daño base 20, sino hace 10% de la vida del enemigo como daño
        return 20; // Para simplificar, asumiremos daño base 20
    }
}

