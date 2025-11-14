package aed;

public class AlumnoEntregado implements Comparable<AlumnoEntregado>{
    private int nota;
    private int id;

    public AlumnoEntregado(int id, int nota){
        this.id = id;
        this.nota = nota;
    }

    public int getNota(){
        return nota;
    }

    public int getId(){
        return id;
    }

    

@Override
public int compareTo(AlumnoEntregado alumno2) {
    // Si uno entregó y el otro no, el que entregó tiene menor prioridad (va al final)
    
    // Si ambos entregaron o ambos no entregaron, comparar por nota
    if (this.nota < alumno2.nota) return -1;
    else if (this.nota > alumno2.nota) return 1;
    else {
        // Si las notas son iguales, desempatar por id
        if (this.id > alumno2.id) {
            return 1;
        } else if (this.id < alumno2.id) {
            return -1;
        } else {
            return 0;
        }
    }
}
}
