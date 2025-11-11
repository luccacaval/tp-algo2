package aed;

public class Alumno implements Comparable<Alumno>{
    private int[] examen;
    private int nota;
    private int id;

    public Alumno(int cantidadEjercicios, int id){
        this.examen = new int[cantidadEjercicios];
        int nota = 0;
        this.id = id;
    }

    public void resolverEjercicio(int ejercicio,int respuesta,int[] examenCanonico){
        this.examen[ejercicio] = respuesta;
        if (respuesta == examenCanonico[ejercicio]){
            this.nota += 10;
        }
    }

    public void reemplazarExamen(int[] nuevoExamen){
        if (this.examen.length != nuevoExamen.length){
            return;
        }
        for (int i = 0;i<this.examen.length;i++){
            this.examen[i] = nuevoExamen[i];
        }
    }

    public int[] getExamen() {
        int[] res = new int[examen.length];
        for (int i = 0; i < examen.length; i++) {
            res[i] = examen[i];
        }
        return res;
    }

    public int getNota() {
        return this.nota;
    }

    public void actualizarNota(int nuevaNota) {
        this.nota = nuevaNota;
    }

@Override
    public int compareTo(Alumno alumno2) {
        if (this.nota > alumno2.nota) return 1;
        else if (this.nota < alumno2.nota) return -1;
        else {
            if (this.id > alumno2.id){
                return 1;
            } else {
                return -1;
            }
        }
}
}

