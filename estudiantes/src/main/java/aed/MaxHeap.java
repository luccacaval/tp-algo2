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
        while (padre >= 0 && arrayHeap[nuevoElemento].compareTo(arrayHeap[padre]) > 0) {
            intercambiar(padre, nuevoElemento);
            nuevoElemento = padre;
            padre = obtenerPadre(nuevoElemento);
        }
    }

    public HandleHeap insertar(T valor){
        if (cantidadElementos >= arrayHeap.length) {
            throw new IllegalStateException("Heap is full");
        }
        
        this.arrayHeap[cantidadElementos] = valor;
        int posicionActual = cantidadElementos;
        restaurarInvariante(cantidadElementos);
        cantidadElementos++;
        return new HandleHeap(posicionActual, valor);
    }

    public static void main(String[] args) {
        MaxHeap<Integer> heap = new MaxHeap<>(3);
        heap.insertar(3);
        heap.insertar(0);
        heap.insertar(9);
    }
}
