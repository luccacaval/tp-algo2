package aed;

import java.util.ArrayList;

public class MinHeap <T extends ClonableYComparable<T>>{
    private T[] arrayHeap;
    private ArrayList<HandleMinHeap> handleArray;
    int cantidadElementos;

    public class HandleMinHeap implements Handle<T>{
        private int posicion;
        private MinHeap<T> minHeapMadre;
        //private T valor;
        
        //HandleMinHeap(int posicion, T valor) {
        HandleMinHeap(MinHeap<T> minHeap, int posicion) {
            this.posicion = posicion;
            this.minHeapMadre = minHeap;
        }

        public int getPosicion(){
            return posicion;
        }

        public void setPosicion(int nuevaPosicion) {
            this.posicion = nuevaPosicion;
        }

        public T getValor() {
            return minHeapMadre.getValorPorIndice(this.posicion);
        }
        public void reemplazarValor(T nuevoValor){
            //Actualiza posicion también, luego de cambiar el valor
            int nuevaPosicion = minHeapMadre.setAt(posicion, nuevoValor);
            this.posicion = nuevaPosicion;
        } 
}
    //Para evitar el Warning del casteo en la linea 33
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
        //intercambiamos los valores T del arrayHeap
        //junto con la posicion y valor de los handles de ambos 

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
        HandleMinHeap nuevoHandle = new HandleMinHeap(this, posicionActual);
        handleArray.set(posicionActual, nuevoHandle);

        if (cantidadElementos != 0){
            siftUp(cantidadElementos);
        }
        cantidadElementos++; 
        return nuevoHandle;
    }

    public T desencolar() {
        if (cantidadElementos == 0){
            return null;
        }
        if (cantidadElementos == 1){
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
        
        // Si es el último elemento, simplemente lo eliminamos.
        if (posicion == cantidadElementos - 1) {
            arrayHeap[posicion] = null;
            handleArray.set(posicion, null);
            cantidadElementos--;
            return elementoEliminado;
        }
        
        // Reemplazar con el último elemento.
        T ultimoElemento = arrayHeap[cantidadElementos - 1];
        arrayHeap[posicion] = ultimoElemento;

        HandleMinHeap ultimoHandle = handleArray.get(cantidadElementos - 1);
        handleArray.set(posicion, ultimoHandle);
        ultimoHandle.posicion = posicion;
        
        arrayHeap[cantidadElementos - 1] = null;
        handleArray.set(cantidadElementos - 1, null);
        cantidadElementos--;
        
        siftDown(posicion);
        
        return elementoEliminado;
    }
    
//-------------------------------------------Metodos privados para el Handle------------------------------------------------

private T getValorPorIndice(int posicion) {
        if (posicion < 0 || posicion > cantidadElementos) {
            return null;
        }
        //Nota: para no devolverlo por referencia,
        //deberíamos pedir que T sea también Objeto (tenga método .clone).
        return this.arrayHeap[posicion].clone();
    }

    private int setAt(int posicion, T nuevoValor) {
        // Requiere 0 >= posicion > cantidadElementos
        
        if (arrayHeap[posicion] == nuevoValor) {
            //Caso trivial
            return posicion;
        }
        arrayHeap[posicion] = nuevoValor;
        int nuevaPosicion = this.restaurarInvariante(posicion);

        return nuevaPosicion;
    }

}
