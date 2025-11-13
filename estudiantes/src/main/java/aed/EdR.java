package aed;

import java.util.ArrayList;

public class EdR {
    private MinHeapAlumno _notas_de_estudiantes;
    private MaxHeap<Alumno> _estudiantes_entregados;
    private MinHeapAlumno.HandleHeap[] _estudiantes_por_id;
    private int _lado_aula;
    private int[] _examen_canonico;
    private int idActual; 
    private int[][] respuestasPorEjercicio;

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        double[] res = new double[_estudiantes_por_id.length];
        for(int i = 0;i<_estudiantes_por_id.length;i++){
            if (_estudiantes_por_id[i] != null){
                res[i] = _estudiantes_por_id[i].obtenerNota();
            } else {
                res[i] = 0;
            }
        }
        return res;
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------

    public void copiarse(int estudiante) {
        //busco vecinos
        //elijo el vecino copiado
        //desempato por id mayor
        //estudiante incorpora la primera rta que no tenga del otro
        int[] vecinos = hallarVecinos(estudiante);
        if( vecinos != null){ // Si es distinto a null osea hay gente para copiarse , llamo a una funcion para ver cual es el que tiene mas rtas
            //chequeoRtasVecinos(Copiador,ArrayVECINOS)

        }



    }

    private int[] hallarVecinos(int estudiante) {
        if (estudiante > _estudiantes_por_id.length || estudiante < 0) {
                return null;
        }

        int[] vecinos = new int[3];
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

    private int[] chequeoRtasVecinos(int copiador, int[] vecinos){
        int copiado = -1;
        int max_rtas = -1;
        int[] examen_copiador = _estudiantes_por_id[copiador].obtenerExamen();
        for (int i = 0 ; i < vecinos.length;i++){ // O(1)
            int contador = 0;
            int[] examen_vecino = _estudiantes_por_id[vecinos[i]].obtenerExamen(); // como accedo al examen del vecino (?)
            for (int j = 0 ; j < examen_vecino.length ; j++){
                if(examen_vecino[j] != -1 && examen_copiador[j] == -1){
                    contador ++;
                }
            if (contador > max_rtas){
                copiado = vecinos[i];
                max_rtas = contador;
            }

            }



        }
        return new int[0];
    }



//-----------------------------------------------RESOLVER----------------------------------------------------------------




    public void resolver(int estudiante, int NroEjercicio, int res) {
        if (_estudiantes_por_id[estudiante] == null){
            Alumno alumnoActual = new Alumno(this._examen_canonico.length, this.idActual);
            idActual++;
            alumnoActual.resolverEjercicio(NroEjercicio, res, _examen_canonico);
            this._estudiantes_por_id[estudiante] = this._notas_de_estudiantes.insertar(alumnoActual);
        } else{
            MinHeapAlumno.HandleHeap alumnoActual = _estudiantes_por_id[estudiante];
            alumnoActual.resolverEjercicio(NroEjercicio, res, _examen_canonico);
        }
        respuestasPorEjercicio[NroEjercicio][res] += 1;
    }



//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        int notaExamenDW = 0;
        for (int i = 0;i<examenDW.length;i++){
            if(examenDW[i] == this._examen_canonico[i]){
                notaExamenDW+= 10;
            }
        }
        for (int j = 0; j < n;j++){
            Alumno alumnoActual = this._notas_de_estudiantes.desencolar();
            int alumnoActualId = alumnoActual.getId();
            int[] examenAnterior = alumnoActual.getExamen();
            for (int k = 0;k<examenAnterior.length;k++){
                if(examenAnterior[k] != -1){
                    this.respuestasPorEjercicio[k][examenAnterior[k]]--;
                }
            }
            alumnoActual.reemplazarExamen(examenDW);
            alumnoActual.actualizarNota(notaExamenDW);
            this._estudiantes_por_id[alumnoActualId] = this._notas_de_estudiantes.insertar(alumnoActual);
        }
    }
 

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        
    }

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        throw new UnsupportedOperationException("Sin implementar");
    }

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    public int[] chequearCopias() {
        ArrayList<Integer> copiones = new ArrayList<Integer>(_estudiantes_por_id.length);
        for(int i = 0;i<this._estudiantes_por_id.length;i++){
            int[] examen = this._estudiantes_por_id[i].obtenerExamen();
            int j = 0;
            boolean esCopion = true;
            while (esCopion && j<examen.length) {
                if(respuestasPorEjercicio[j][examen[j]] / _estudiantes_por_id.length < 0.25){
                    esCopion = false;
                }
                j++;
            }
            if (esCopion){
                copiones.add(i);
            }
        }
        int[] res = new int[copiones.size()];
        for (int i = 0;i<copiones.size();i++){
            res[i] = copiones.get(i);
        }
        return res;
    }


    public EdR(int ladoAula, int cantidadEstudiantes, int[] examenCanonico) {
        //comprobar si entran los estudiantes en el aula de ladoAula;
        _estudiantes_por_id = new MinHeapAlumno.HandleHeap[cantidadEstudiantes];
        _notas_de_estudiantes = new MinHeapAlumno(cantidadEstudiantes,examenCanonico.length);
        for (int i = 0;i<cantidadEstudiantes;i++){
            _estudiantes_por_id[i] = _notas_de_estudiantes.obtenerHandle(i);
        }
        _estudiantes_entregados = new MaxHeap<>(cantidadEstudiantes);
        //esto no tarda O(E*R) ni en pedo
        //crear un heap con nodos.
        respuestasPorEjercicio = new int[examenCanonico.length][10];
        //hace mas facil los calculos tratar el aula como si no hubiera espacios:
        //se lleva mejor con el array.
        _lado_aula = ladoAula/2;
        //a fin de cuentas, sabiendo que entran todos, dsps solo sirve para el array
        _examen_canonico = examenCanonico;

    }

    private void corregirEjercicio(int[] ExamenCanonico, Alumno alumno, int nroEjercicio, int rtaEjercicio){
        if (ExamenCanonico[nroEjercicio] == rtaEjercicio){
            alumno.actualizarNota(alumno.getNota() + 1);
        }
    }




}
