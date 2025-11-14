package aed;

import java.util.ArrayList;


public class EdR {
    private MinHeapAlumno _notas_de_estudiantes;
    private MaxHeapAlumno _estudiantes_entregados;
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
        int[] vecinos = hallarVecinos(estudiante); // O (1) porq son todas operaciones acotadas
        if( vecinos != null){ // Si es distinto a null osea hay gente para copiarse , llamo a una funcion para ver cual es el que tiene mas rtas
            
            int pos_vecino_a_copiar = chequeoRtasVecinos(estudiante, vecinos); // O(R)
            if (pos_vecino_a_copiar != -1){ 
            //Busco el inciso que voy a copiar
            int[] examen_vecino = _estudiantes_por_id[pos_vecino_a_copiar].obtenerExamen();
            int[] examen_copiador = _estudiantes_por_id[estudiante].obtenerExamen();
            int inciso_a_copiar = -1;
            int j = 0;
            while (inciso_a_copiar == -1 && j < examen_copiador.length){ // O(R) PEOR CASO
                if(examen_copiador[j] == -1 && examen_vecino[j] != -1){
                    inciso_a_copiar = j;
                }
                j++;
            }
            if (inciso_a_copiar != -1){
            resolver(estudiante, inciso_a_copiar, examen_vecino[inciso_a_copiar]);} // LOG(E)
            }
        }
    }

    private int[] hallarVecinos(int estudiante) { // O (1) porq son todas operaciones acotadas
        //Preguntar valen si el array en java cuando se inicializa , como se inicializaria
        if (estudiante > _estudiantes_por_id.length || estudiante < 0) {
                return null;
        }
        int contador_vecinos_validos = 0;
        int[] vecinos_aux = {-1,-1,-1};
                                    //java truca el cociente entre enteros
        int vecino_de_adelante = estudiante + _lado_aula/2;

        if (vecino_de_adelante < _estudiantes_por_id.length &&
            _estudiantes_por_id[vecino_de_adelante] != null) {
            vecinos_aux[0] = vecino_de_adelante;
            contador_vecinos_validos++;
        }
        if (estudiante - 1 >= 0 && _estudiantes_por_id[estudiante-1] != null) {
            vecinos_aux[1] = (estudiante - 1);
            contador_vecinos_validos++;
        }
        if (estudiante + 1 < _estudiantes_por_id.length && _estudiantes_por_id[estudiante+1] != null) {
            vecinos_aux[2] = (estudiante + 1);
            contador_vecinos_validos++;
        }
        if (contador_vecinos_validos == 0){
            return null;}

        int[] vecinos = new int[contador_vecinos_validos];
        int indice_pasaje = 0;
        int i;
        for (i = 0; i < vecinos_aux.length; i++){
            if(vecinos_aux[i] != -1){
                    vecinos[indice_pasaje] = vecinos_aux[i];
                    indice_pasaje ++;
            }

            }
        return vecinos;
        

    }

    private int chequeoRtasVecinos(int copiador, int[] vecinos){ // O (R) porq son todas operaciones acotadas
        int copiado = -1;
        int max_rtas = -1;
        int[] examen_copiador = _estudiantes_por_id[copiador].obtenerExamen();
        for (int i = 0 ; i < vecinos.length;i++){ // O(1)
            int contador = 0;
            int[] examen_vecino = _estudiantes_por_id[vecinos[i]].obtenerExamen();
            for (int j = 0 ; j < examen_vecino.length ; j++){
                if(examen_vecino[j] != -1 && examen_copiador[j] == -1){
                    contador ++;
                }
            }
            if (contador > max_rtas){
                copiado = vecinos[i];
                max_rtas = contador;
            }
            else if(contador == max_rtas) {
                if (vecinos[i] > copiado){
                    copiado = vecinos[i];
                    max_rtas = contador;
                }
            }
        }
        return copiado;
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
            Alumno alumnoActual = this._notas_de_estudiantes.desencolar(); // O(1)
            int alumnoActualId = alumnoActual.getId(); //O(1)
            int[] examenAnterior = alumnoActual.getExamen(); //O(1)
            for (int k = 0;k<examenAnterior.length;k++){ //O(R)
                if(examenAnterior[k] != -1){
                    this.respuestasPorEjercicio[k][examenAnterior[k]]--;
                }
            }
            alumnoActual.reemplazarExamen(examenDW); // O(R)
            alumnoActual.actualizarNota(notaExamenDW); //O(1)
            this._estudiantes_por_id[alumnoActualId] = this._notas_de_estudiantes.insertar(alumnoActual); //O(log(K))
        }
    }
 

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        MinHeapAlumno.HandleHeap entregador = this._estudiantes_por_id[estudiante];
        entregador.entregar();
        this._estudiantes_entregados.insertar(new Alumno(entregador.obtenerId(),entregador.obtenerExamen(),entregador.obtenerNota(),true));
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
        _estudiantes_entregados = new MaxHeapAlumno(cantidadEstudiantes);
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
