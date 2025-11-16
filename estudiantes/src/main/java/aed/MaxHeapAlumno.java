package aed;

public class MaxHeapAlumno{
    private AlumnoEntregado[] arrayHeap;
    int cantidadElementos;


    @SuppressWarnings("unchecked")
    public MaxHeapAlumno(int capacidad){
        //lo inicializamos vacio
        arrayHeap =  new AlumnoEntregado[capacidad];

        cantidadElementos = 0;
    }


    private int obtenerPadre(int pos){
        return (pos-1) / 2;
    }

    private void intercambiar(int padre, int hijo){
        AlumnoEntregado temp = this.arrayHeap[padre];
        this.arrayHeap[padre] = arrayHeap[hijo];
        this.arrayHeap[hijo] = temp;
    }

    public int getCantidadElementos(){
        return this.cantidadElementos;
    }

    private int shiftUp(int nuevoElemento){
        int padre = obtenerPadre(nuevoElemento);
        while (padre >= 0 && arrayHeap[nuevoElemento].compareTo(arrayHeap[padre]) > 0) {
            intercambiar(nuevoElemento, padre);
            nuevoElemento = padre;
            padre = obtenerPadre(nuevoElemento);
        }
        return nuevoElemento;
    }

    public void insertar(AlumnoEntregado valor){
        if (cantidadElementos >= arrayHeap.length) {
            throw new IllegalStateException("el MaxHeap ya llego a su limite de alumnos");
        }
        
        this.arrayHeap[cantidadElementos] = valor;
        int posicionActual = cantidadElementos;
        if(cantidadElementos != 0){
           posicionActual = shiftUp(cantidadElementos);
        }
        cantidadElementos++;
    }

        public AlumnoEntregado desencolar() {
            if(cantidadElementos == 1){
                AlumnoEntregado res = arrayHeap[0];
                arrayHeap[0] = null;
                cantidadElementos--;
                return res;
            }

            AlumnoEntregado res = arrayHeap[0];
            AlumnoEntregado ultimoInsertado = arrayHeap[cantidadElementos-1];
            arrayHeap[0] = ultimoInsertado;
            arrayHeap[cantidadElementos - 1] = null;
            cantidadElementos--;
            
            siftDown(0);
            return res;
        }

    private int siftDown(int nuevoElemento){
        int indiceHijo = indiceHijoMayorPrioridad(nuevoElemento);
        while (indiceHijo >= 0 && arrayHeap[nuevoElemento].compareTo(arrayHeap[indiceHijo]) < 0) {
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
            AlumnoEntregado hijoDerecho = arrayHeap[(2 * pos) + 2];
            AlumnoEntregado hijoIzquierdo = arrayHeap[(2 * pos) + 1];

            if (hijoIzquierdo.compareTo(hijoDerecho) > 0) {
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
