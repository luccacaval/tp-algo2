package aed;

public class Alumno implements Comparable<Alumno>{
    private int[] examen;
    private int nota;

    public Alumno(int cantidadEjercicios){
        this.examen = new int[cantidadEjercicios];
        int nota = 0;
    }

    public void resolverEjercicio(int ejercicio,int respuesta){
        this.examen[ejercicio] = respuesta;
    }

    public void reemplazarExamen(int[] nuevoExamen){
        if (this.examen.length != nuevoExamen.length){
            return;
        }
        for (int i = 0;i<this.examen.length;i++){
            this.examen[i] = nuevoExamen[i];
        }
    }

    public void corregirEjercicio(int[] ExamenCanonico, int ejercicio){
        if (this.examen[ejercicio] == ExamenCanonico[ejercicio]){
            this.nota += 1;
        }
    }
@Override
    public int compareTo(Alumno o) {
        // TODO Auto-generated method stub
        return Integer.compare(this.nota, o.nota);
}
}

