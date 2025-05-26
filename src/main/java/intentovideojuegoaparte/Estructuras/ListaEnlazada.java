package intentovideojuegoaparte.Estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListaEnlazada<T> implements Iterable<T> {
    private Nodo<T> cabeza;

    public  static class Nodo<T> {
        public T valor;
        public Nodo<T> siguiente;

        Nodo(T valor) {
            this.valor = valor;
        }
    }

    public void agregar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            Nodo<T> actual = cabeza;
            while (actual.siguiente != null) {
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
    }

    public void eliminar(T elemento) {
        if (cabeza == null) return;

        if (cabeza.valor.equals(elemento)) {
            cabeza = cabeza.siguiente;
            return;
        }

        Nodo<T> actual = cabeza;
        while (actual.siguiente != null) {
            if (actual.siguiente.valor.equals(elemento)) {
                actual.siguiente = actual.siguiente.siguiente;
                return;
            }
            actual = actual.siguiente;
        }
    }

    public boolean contiene(T elemento) {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            if (actual.valor.equals(elemento)) return true;
            actual = actual.siguiente;
        }
        return false;
    }

    public boolean estaVacia() {
        return cabeza == null;
    }

    public void imprimir() {
        Nodo<T> actual = cabeza;
        while (actual != null) {
            System.out.println(actual.valor);
            actual = actual.siguiente;
        }
    }

    public Nodo<T> getCabeza() {
        return cabeza;
    }

    @Override
    public Iterator<T> iterator() {
        return new IteradorLista();
    }

    private class IteradorLista implements Iterator<T> {
        private Nodo<T> actual = cabeza;

        @Override
        public boolean hasNext() {
            return actual != null;
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException();
            T valor = actual.valor;
            actual = actual.siguiente;
            return valor;
        }
    }
}