package aed;

public class MaxHeap <T extends Comparable<T>>{
    private T[] arrayHeap;
    int cantidadElementos;

    public class HandleHeap {
        int posicion;
        T valor;
        
        private HandleHeap(int posicion, T valor){
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
            intercambiar(nuevoElemento, padre);
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
        if(cantidadElementos != 0){
            restaurarInvariante(cantidadElementos);
        }
        cantidadElementos++;
        return new HandleHeap(posicionActual, valor);
    }

    public T desencolar() {
        if(cantidadElementos == 0){
            return null;
        }
        if(cantidadElementos == 1){
            T res = arrayHeap[0];
            arrayHeap[0] = null;
            cantidadElementos--;
            return res;
        }

        T res = arrayHeap[0];
        T ultimoInsertado = arrayHeap[cantidadElementos-1];
        arrayHeap[0] = ultimoInsertado;
        arrayHeap[cantidadElementos - 1] = null;
        cantidadElementos--;
        
        siftDown(0);
        return res;
    }

    private void siftDown(int nuevoElemento){
        int indiceHijo = indiceHijoMayorPrioridad(nuevoElemento);
        while (indiceHijo > 0 && arrayHeap[nuevoElemento].compareTo(arrayHeap[indiceHijo]) < 0) {
            intercambiar(indiceHijo, nuevoElemento);
            siftDown(indiceHijo);
        }
    }

    private int indiceHijoMayorPrioridad(int pos){
        boolean existeDerecho = (2 * pos + 2) < cantidadElementos;
        boolean existeIzquierdo = (2 * pos + 1) < cantidadElementos;
        
        if (existeIzquierdo && existeDerecho) {
            T hijoDerecho = arrayHeap[(2 * pos) + 2];
            T hijoIzquierdo = arrayHeap[(2 * pos) + 1];

            if (hijoIzquierdo.compareTo(hijoDerecho) > 0) {
                return ((2 * pos) + 1);
            } else {
                return ((2 * pos) + 2);
            }
        } else if (existeIzquierdo && !existeDerecho) {
            return ((2 * pos) + 1);
        } else if (!existeIzquierdo && existeDerecho) {
            return ((2 * pos) + 2);
        } else {
            return -1;
        }
    }

    public boolean esHeapValido(){
        for (int i = 0; i < cantidadElementos; i++) {
            int hijoIzquierdo = (2 * i) + 1;
            int hijoDerecho = (2 * i) + 2;
            
            if (hijoIzquierdo < cantidadElementos) {
                if (arrayHeap[i].compareTo(arrayHeap[hijoIzquierdo]) < 0) {
                    return false;
                }
            }
            if (hijoDerecho < cantidadElementos) {
                if (arrayHeap[i].compareTo(arrayHeap[hijoDerecho]) < 0) {
                    return false;
                }
            }
        }
        return true;
    }
}
