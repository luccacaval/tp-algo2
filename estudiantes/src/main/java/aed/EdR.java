package aed;
public class EdR {
    private MinHeap<Alumno> _notas_de_estudiantes;
    private MaxHeap<Alumno> _estudiantes_entregados;
    private MinHeap.HandleHeap[] _estudiantes_por_id;
    private int _lado_aula;
    private int[] _examen_canonico;
    private int idActual; 

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        double[] res = new double[_estudiantes_por_id.length];
        for(int i = 0;i<_estudiantes_por_id.length;i++){
            if (_estudiantes_por_id[i] != null){
                Alumno alumnoActual = (Alumno) _estudiantes_por_id[i].valor;
                res[i] = alumnoActual.getNota();
            } else {
                res[i] = 0;
            }
        }
        return res;
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------






//-----------------------------------------------RESOLVER----------------------------------------------------------------




    public void resolver(int estudiante, int NroEjercicio, int res) {
        if (_estudiantes_por_id[estudiante] == null){
            Alumno alumnoActual = new Alumno(this._examen_canonico.length,this.idActual);
            idActual++;
            alumnoActual.resolverEjercicio(NroEjercicio, res, _examen_canonico);
            this._estudiantes_por_id[estudiante] = this._notas_de_estudiantes.insertar(alumnoActual);
        } else{
            Alumno alumnoActual = (Alumno) _estudiantes_por_id[estudiante].valor;
            alumnoActual.resolverEjercicio(NroEjercicio, res, _examen_canonico);
            _estudiantes_por_id[estudiante].restaurarInv();
        }
    }



//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        throw new UnsupportedOperationException("Sin implementar");
    }
 

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        throw new UnsupportedOperationException("Sin implementar");
    }

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        throw new UnsupportedOperationException("Sin implementar");
    }

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        throw new UnsupportedOperationException("Sin implementar");
    }


    public EdR(int ladoAula, int cantidadEstudiantes, int[] examenCanonico) {
        //comprobar si entran los estudiantes en el aula de ladoAula;
        _estudiantes_por_id = new MinHeap.HandleHeap[cantidadEstudiantes];
        _notas_de_estudiantes = new MinHeap<>(cantidadEstudiantes);
        _estudiantes_entregados = new MaxHeap<>(cantidadEstudiantes);
        idActual = 0;
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
