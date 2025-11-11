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
    public int compareTo(Alumno o) {
        // TODO Auto-generated method stub
        return Integer.compare(this.nota, o.nota);
}
}

