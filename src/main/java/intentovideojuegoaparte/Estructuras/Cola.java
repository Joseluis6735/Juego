package intentovideojuegoaparte.Estructuras;

public class Cola<T> {
    private Nodo<T> frente;
    private Nodo<T> fin;

    private static class Nodo<T> {
        T valor;
        Nodo<T> siguiente;

        Nodo(T valor) {
            this.valor = valor;
        }
    }

    public void encolar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (fin == null) {
            frente = fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
    }

    public T desencolar() {
        if (frente == null) return null;
        T valor = frente.valor;
        frente = frente.siguiente;
        if (frente == null) fin = null;
        return valor;
    }

    public T frente() {
        return (frente != null) ? frente.valor : null;
    }

    public boolean estaVacia() {
        return frente == null;}
}