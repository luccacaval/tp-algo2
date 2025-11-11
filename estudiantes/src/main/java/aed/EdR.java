package aed;
public class EdR {
    private MinHeap<Integer> _notas_de_estudiantes;
    private MaxHeap<Integer> _estudiantes_entregados;
    private Alumno[] _estudiantes_por_id;
    private Integer _lado_aula;
    private Integer[] _examen_canonico;

    public EdR nuevoEdR(int ladoAula, int cantidadEstudiantes, Integer[] examenCanonico) {
        return new EdR(ladoAula, cantidadEstudiantes, examenCanonico);
    }

    private EdR(int ladoAula, int cantidadEstudiantes, Integer[] examenCanonico) {
        //comprobar si entran los estudiantes en el aula de ladoAula;
        _estudiantes_por_id = new Alumno[cantidadEstudiantes];
        _notas_de_estudiantes = new MinHeap<>(cantidadEstudiantes);
        _estudiantes_entregados = new MaxHeap<>(cantidadEstudiantes);
        //esto no tarda O(E*R) ni en pedo
        //crear un heap con nodos.

        //hace mas facil los calculos tratar el aula como si no hubiera espacios:
        //se lleva mejor con el array.
        _lado_aula = ladoAula/2;
        //a fin de cuentas, sabiendo que entran todos, dsps solo sirve para el array
        _examen_canonico = examenCanonico;

    }

    private void corregirEjercicio(Integer[] ExamenCanonico, Alumno alumno, int nroEjercicio, int rtaEjercicio){
        if (ExamenCanonico[nroEjercicio] == rtaEjercicio){
            alumno.actualizarNota(alumno.getNota() + 1);
        }
    }

    public void copiarse(int estudiante) {
        //busco vecinos
        //elijo el vecino copiado
        //desempato por id mayor
        //estudiante incorpora la primera rta que no tenga del otro
    }

    private Integer[] hallarVecinos(int estudiante) {
        if (estudiante > _estudiantes_por_id.length || estudiante < 0) {
                return null;
        }

        Integer[] vecinos = new Integer[3];
                                    //java truca el cociente entre enteros
        int vecino_de_adelante = estudiante + _lado_aula/2;
        if (vecino_de_adelante < _estudiantes_por_id.length &&
            _estudiantes_por_id[vecino_de_adelante] != null) {
            vecinos[0] = vecino_de_adelante;
        }
        if (estudiante - 1 > 0 && _estudiantes_por_id[estudiante-1] != null) {
            vecinos[1] = (estudiante - 1);
        }
        if (estudiante + 1 < _estudiantes_por_id.length && _estudiantes_por_id[estudiante+1] != null) {
            vecinos[2] = (estudiante + 1);
        }

        //nice n sweet
        return vecinos;
    }



}
