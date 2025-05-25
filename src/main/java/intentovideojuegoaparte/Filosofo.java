package intentovideojuegoaparte;

public class Filosofo extends Unidad {
    public Filosofo(String faccion) {
        super("Filósofo", "Letras", 105, 27, 28, 3, 2);
    }

    @Override
    public int habilidadEspecial(int danoBase) {
        // Recibe la mitad del daño, aquí se usa para atacar, así que devolvemos el daño normal
        return danoBase;
    }

    @Override
    public void recibirDanio(int dano) {
        int danoOriginal = dano;
        int danoReducido = danoOriginal / 2;
        int danoFinal = danoReducido - (int)(defensa * 0.1);
        if(danoFinal < 0) danoFinal = 0;
        this.hpActual -= danoFinal;
        if(hpActual < 0) hpActual = 0;
    }
}
