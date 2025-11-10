package aed;

public class MaxHeap <T extends Comparable<T>>{
    private T[] arrayHeap;
    int cantidadElementos;

    private class HandleHeap {
        int posicion;
        T valor;
        
        public HandleHeap(int posicion,T valor){
            this.posicion = posicion;
            this.valor = valor;
        }
    }

    @SuppressWarnings("unchecked")
    public MaxHeap(int capacidad){
        arrayHeap = (T[]) new Comparable[capacidad];
        this.cantidadElementos = 0;
    }

    private int obtenerPadre(int pos){
        return (pos-1) / 2;
    }

    private void intercambiar(int padre, int hijo){
        T temp = this.arrayHeap[padre];
        this.arrayHeap[padre] = arrayHeap[hijo];
        this.arrayHeap[hijo] = temp;
    }

    private void restaurarInvariante(int nuevoElemento){
        int padre = obtenerPadre(nuevoElemento);
        while (padre >= 0 && arrayHeap[nuevoElemento].compareTo(arrayHeap[padre]) < 0) {
            intercambiar(nuevoElemento, padre);
            nuevoElemento = padre;
            padre = obtenerPadre(nuevoElemento);
        }
    }

    public HandleHeap incertar(T valor){
        if (cantidadElementos >= arrayHeap.length) {
            throw new IllegalStateException("Heap is full");
        }
        
        this.arrayHeap[cantidadElementos] = valor;
        int posicionActual = cantidadElementos;
        restaurarInvariante(cantidadElementos);
        cantidadElementos++;
        return new HandleHeap(posicionActual, valor);
}
}
