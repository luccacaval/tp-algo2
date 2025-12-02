package aed;

import java.util.ArrayList;

public class MinHeap <T extends Comparable<T>>{
    private T[] arrayHeap;
    private ArrayList<HandleMinHeap> handleArray;
    int cantidadElementos;

    public class HandleMinHeap implements Handle<T>{
        public int posicion;
        public T valor;
        
        HandleMinHeap(int posicion,T valor){
            this.posicion = posicion;
            this.valor = valor;
        }

        public int getPosicion(){
            return posicion;
        }


        public void reemplazarValor(T nuevoValor){
            this.valor = nuevoValor;
        }
}

    @SuppressWarnings("unchecked")
    public MinHeap(int capacidad){
        arrayHeap =  (T[]) new Comparable[capacidad];
        handleArray = new ArrayList<HandleMinHeap>(capacidad);
        for(int i = 0;i<capacidad;i++){
            handleArray.add(null);
        }
        this.cantidadElementos = 0;
    }

    private int obtenerPadre(int pos){
        return (pos-1) / 2;
    }

    private void intercambiar(int padre, int hijo){
        T temp = this.arrayHeap[padre];
        HandleMinHeap tempHandle = this.handleArray.get(padre);
        
        this.arrayHeap[padre] = arrayHeap[hijo];
        this.handleArray.set(padre, handleArray.get(hijo));
        this.handleArray.get(padre).posicion = padre;
        
        this.arrayHeap[hijo] = temp;
        this.handleArray.set(hijo, tempHandle);
        this.handleArray.get(hijo).posicion = hijo;
    }

    private int siftUp(int nuevoElemento){
        int padre = obtenerPadre(nuevoElemento);
        while (padre >= 0 && arrayHeap[nuevoElemento].compareTo(arrayHeap[padre]) < 0) {
            intercambiar(nuevoElemento, padre);
            nuevoElemento = padre;
            padre = obtenerPadre(nuevoElemento);
        }
        return nuevoElemento;
    }

    public HandleMinHeap insertar(T valor){
        if (cantidadElementos >= arrayHeap.length) {
            throw new IllegalStateException("El heap esta lleno");
        }
        this.arrayHeap[cantidadElementos] = valor;
        int posicionActual = cantidadElementos;
        HandleMinHeap nuevoHandle = new HandleMinHeap(posicionActual, valor);
        handleArray.set(posicionActual, nuevoHandle);
        if(cantidadElementos != 0){
            siftUp(cantidadElementos);
        }
        cantidadElementos++; 
        return nuevoHandle;
    }

        public T desencolar() {
        if (cantidadElementos == 0){
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

    private int siftDown(int nuevoElemento){
        int indiceHijo = indiceHijoMayorPrioridad(nuevoElemento);
        while (indiceHijo >= 0 && arrayHeap[nuevoElemento].compareTo(arrayHeap[indiceHijo]) > 0) {
            intercambiar(indiceHijo, nuevoElemento);
            nuevoElemento = indiceHijo;
            indiceHijo = indiceHijoMayorPrioridad(nuevoElemento);
        }
        return nuevoElemento;
    }

    private int indiceHijoMayorPrioridad(int pos){
        boolean existeDerecho = (2 * pos + 2) < cantidadElementos;
        boolean existeIzquierdo = (2 * pos + 1) < cantidadElementos;
        
        if (existeIzquierdo && existeDerecho) {
            T hijoDerecho = arrayHeap[(2 * pos) + 2];
            T hijoIzquierdo = arrayHeap[(2 * pos) + 1];

            if (hijoIzquierdo.compareTo(hijoDerecho) < 0) {
                return ((2 * pos) + 1);
            } else {
                return ((2 * pos) + 2);
            }
        } else if  (existeIzquierdo && !existeDerecho) {
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
                if (arrayHeap[i].compareTo(arrayHeap[hijoIzquierdo]) > 0) {
                    return false;
                }
            }
            if (hijoDerecho < cantidadElementos) {
                if (arrayHeap[i].compareTo(arrayHeap[hijoDerecho]) > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public int restaurarInvariante(int posicion){
        int nuevaPosicion;
        nuevaPosicion = siftUp(posicion);
        nuevaPosicion = siftDown(posicion);
        return nuevaPosicion;
    }


    public T eliminarElemento(int posicion){
        
        T elementoEliminado = arrayHeap[posicion];
        
        // Si es el último elemento, simplemente lo eliminamos
        if (posicion == cantidadElementos - 1) {
            arrayHeap[posicion] = null;
            handleArray.set(posicion, null);
            cantidadElementos--;
            return elementoEliminado;
        }
        
        // Reemplazar con el último elemento
        T ultimoElemento = arrayHeap[cantidadElementos - 1];
        arrayHeap[posicion] = ultimoElemento;
        HandleMinHeap ultimoHandle = handleArray.get(cantidadElementos - 1);
        handleArray.set(posicion, ultimoHandle);
        ultimoHandle.posicion = posicion;
        
        arrayHeap[cantidadElementos - 1] = null;
        handleArray.set(cantidadElementos - 1, null);
        cantidadElementos--;
        
        // Restaurar el invariante: siftDown
        siftDown(posicion);
        
        return elementoEliminado;
    }
    
}
