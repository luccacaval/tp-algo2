package aed;

public class MinHeapAlumno {
    private Alumno[] arrayHeap;
    int cantidadElementos;

    public class HandleHeap {
        private int posicion;
        private Alumno valor;
        
        private HandleHeap(int posicion,Alumno valor){
            this.posicion = posicion;
            this.valor = valor;
        }

        public int obtenerNota(){
            return valor.getNota();
        }

        public int[] obtenerExamen(){
            return this.valor.getExamen();
        }

        public void resolverEjercicio(int ejercicio, int respuesta,int[] _examen_canonico){
            int notaAnterior = obtenerNota();
            valor.resolverEjercicio(ejercicio, respuesta, _examen_canonico);
            int notaActual = obtenerNota();
            if (notaActual > notaAnterior){
                this.posicion = siftDown(posicion);
            }
        }

    }

    @SuppressWarnings("unchecked")
    public MinHeapAlumno(int capacidad,int cantidadEjercicios){
        arrayHeap =  new Alumno[capacidad];
        for (int i = 0;i<capacidad;i++){
            arrayHeap[i] = new Alumno(cantidadEjercicios, i);
        }
        this.cantidadElementos = capacidad;
    }

    public HandleHeap obtenerHandle(int pos){
        return new HandleHeap(pos, arrayHeap[pos]);
    }

    private int obtenerPadre(int pos){
        return (pos-1) / 2;
    }

    private void intercambiar(int padre, int hijo){
        Alumno temp = this.arrayHeap[padre];
        this.arrayHeap[padre] = arrayHeap[hijo];
        this.arrayHeap[hijo] = temp;
    }

    private int shiftUp(int nuevoElemento){
        int padre = obtenerPadre(nuevoElemento);
        while (padre >= 0 && arrayHeap[nuevoElemento].compareTo(arrayHeap[padre]) < 0) {
            intercambiar(nuevoElemento, padre);
            nuevoElemento = padre;
            padre = obtenerPadre(nuevoElemento);
        }
        return nuevoElemento;
    }

    public HandleHeap insertar(Alumno valor){
        if (cantidadElementos >= arrayHeap.length) {
            throw new IllegalStateException("Heap is full");
        }
        
        this.arrayHeap[cantidadElementos] = valor;
        int posicionActual = cantidadElementos;
        if(cantidadElementos != 0){
           posicionActual = shiftUp(cantidadElementos);
        }
        cantidadElementos++;
        return new HandleHeap(posicionActual, valor);
    }

        public Alumno desencolar() {
        if(cantidadElementos == 1){
            Alumno res = arrayHeap[0];
            arrayHeap[0] = null;
            cantidadElementos--;
            return res;
        }

        Alumno res = arrayHeap[0];
        Alumno ultimoInsertado = arrayHeap[cantidadElementos-1];
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
            Alumno hijoDerecho = arrayHeap[(2 * pos) + 2];
            Alumno hijoIzquierdo = arrayHeap[(2 * pos) + 1];

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
    
}
