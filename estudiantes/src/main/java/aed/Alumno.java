package aed;

public class Alumno {
    private int[] examen;
    private MinHeap<NotaFinal>.HandleMinHeap nota; // DOCENTE hay un getNot y un actualizarNota, este atributo es esencialmente publico
    private int id;
    public boolean entrego;

    public Alumno(int cantidadEjercicios, int id, MinHeap<NotaFinal>.HandleMinHeap nota){
        this.examen = new int[cantidadEjercicios];
        for (int i = 0; i < cantidadEjercicios; i++) {
            examen[i] = -1;
        }
        this.nota = nota;
        this.id = id;
        this.entrego = false;
    }

    public Alumno(int id, int[] examen, MinHeap<NotaFinal>.HandleMinHeap nota, boolean entrego){
        this.examen = examen;
        this.id = id;
        this.nota = nota;
        this.entrego = entrego;
    }

    public void resolverEjercicio(int ejercicio,int respuesta, int[] examenCanonico){
        if (examen[ejercicio] == respuesta){
            //Si son iguales no cambiamos el examen
            return;
        }
        NotaFinal nuevaNota = this.nota.getValor();
        if (this.examen[ejercicio] == -1) {
        //Si no habias respondido esta pregunta:
            if (respuesta == examenCanonico[ejercicio]) {
            //y la nueva respuesta es la correcta.
                nuevaNota._nota += 100/examenCanonico.length;
            } 
        } else {
            //Si ya había respondido algo:
            if (this.examen[ejercicio] == examenCanonico[ejercicio]){
                //y esa respuesta era la correcta. (la nueva es necesariamente distinta).
                nuevaNota._nota -= 100/examenCanonico.length;
            }
        }
        this.nota.reemplazarValor(nuevaNota);
        this.examen[ejercicio] = respuesta;
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

    public double getNota(){
        return nota.getValor()._nota;
    }

    public int getPosicionNota(){
        return nota.getPosicion();
    }

    public int[] getExamen() {
        int[] res = new int[this.examen.length];
        for(int i = 0;i<this.examen.length;i++){
            res[i] = this.examen[i];
        }
        return res;
    }

    public boolean getEntrego(){
        return this.entrego;
    }

    public void actualizarPosicionNota(int nuevaPosicion) {
        this.nota.setPosicion(nuevaPosicion);
    }

}

