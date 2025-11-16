package aed;

public class Alumno implements Comparable<Alumno>{
    private int[] examen;
    private int nota;
    private int id;
    private boolean entrego;

    public Alumno(int cantidadEjercicios, int id){
        this.examen = new int[cantidadEjercicios];
        for (int i = 0; i < cantidadEjercicios; i++) {
            examen[i] = -1;
        }
        int nota = 0;
        this.id = id;
        this.entrego = false;
    }

    public Alumno(int id,int[] examen,int nota,boolean entrego){
        this.examen = examen;
        this.id = id;
        this.nota = nota;
        this.entrego = entrego;
    }

    public void resolverEjercicio(int ejercicio,int respuesta, int[] examenCanonico){
        this.examen[ejercicio] = respuesta;
        if (examenCanonico[ejercicio] == respuesta){
            this.nota += 100/examenCanonico.length;
        }
    }

    public void entregar(){
        this.entrego = true;
    }

    public void reemplazarExamen(int[] nuevoExamen){
        if (this.examen.length != nuevoExamen.length){
            return;
        }
        for (int i = 0;i<this.examen.length;i++){
            this.examen[i] = nuevoExamen[i];
        }
    }

    public int getId(){
        return this.id;
    }

    public int[] getExamen() {
        return this.examen;
    }

    public int getNota() {
        return this.nota;
    }

    public boolean getEntrego(){
        return this.entrego;
    }

    public void actualizarNota(int nuevaNota) {
        this.nota = nuevaNota;
    }

@Override
public int compareTo(Alumno alumno2) {
    // Si uno entregó y el otro no, el que entregó tiene menor prioridad (va al final)
    if (this.entrego && !alumno2.entrego) return 1;
    if (!this.entrego && alumno2.entrego) return -1;
    
    // Si ambos entregaron o ambos no entregaron, comparar por nota
    if (this.nota < alumno2.nota) return -1;
    else if (this.nota > alumno2.nota) return 1;
    else {
        // Si las notas son iguales, desempatar por id
        if (this.id < alumno2.id) {
            return -1;
        } else if (this.id > alumno2.id) {
            return 1;
        } else {
            return 0;
        }
    }
}
}

