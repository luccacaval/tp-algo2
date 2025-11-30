package aed;

import java.util.ArrayList;



public class EdR {
    private MinHeap _notas_de_estudiantes;
    private int cantidadEstudiantes;
    private MaxHeapAlumno _estudiantes_entregados;
    private int _lado_aula;
    private int[] _examen_canonico;
    private int[][] respuestasPorEjercicio;
    private ArrayList<Integer> copiadoresId;

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){
        double[] res = new double[cantidadEstudiantes];
        for(int i = 0;i<cantidadEstudiantes;i++){
            MinHeap.HandleHeap alumnoActualHandle = this._notas_de_estudiantes.obtenerHandle(i);
            Alumno alumnoActual = (Alumno) alumnoActualHandle.obtenerValor();
            res[i] = alumnoActual.getNota();
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
            int[] examen_vecino = this._notas_de_estudiantes.obtenerHandle(pos_vecino_a_copiar).obtenerExamen();
            int[] examen_copiador = this._notas_de_estudiantes.obtenerHandle(estudiante).obtenerExamen();
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
        if (estudiante > cantidadEstudiantes || estudiante < 0) {
                return null;
        }
        int contador_vecinos_validos = 0;
        int[] vecinos_aux = {-1,-1,-1};
                                    //java truca el cociente entre enteros
        int vecino_de_adelante = estudiante + _lado_aula/2;

        if (vecino_de_adelante < cantidadEstudiantes) {
            vecinos_aux[0] = vecino_de_adelante;
            contador_vecinos_validos++;
        }
        if (estudiante - 1 >= 0) {
            vecinos_aux[1] = (estudiante - 1);
            contador_vecinos_validos++;
        }
        if (estudiante + 1 < cantidadEstudiantes) {
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

    private int chequeoRtasVecinos(int copiadorId, int[] vecinos){ // O (R) porq son todas operaciones acotadas
        int copiado = -1;
        int max_rtas = -1;
        MinHeap.HandleHeap copiadorHandle = _notas_de_estudiantes.obtenerHandle(copiadorId);
        Alumno copiador = (Alumno) copiadorHandle.obtenerValor(); 
        int[] examen_copiador = copiador.getExamen();
        for (int i = 0 ; i < vecinos.length;i++){ // O(1)
            int contador = 0;
            MinHeap.HandleHeap vecinoHandle = _notas_de_estudiantes.obtenerHandle(vecinos[i])
            Alumno vecino = (Alumno) vecinoHandle.obtenerValor();
            int[] examen_vecino = vecino.getExamen();
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
        MinHeap.HandleHeap alumnoActualHandle = _notas_de_estudiantes.obtenerHandle(estudiante);
        Alumno alumnoActual = (Alumno) alumnoActualHandle.obtenerValor();
        alumnoActual.resolverEjercicio(NroEjercicio, res, _examen_canonico);
        alumnoActualHandle.reemplazarValor(alumnoActual);
        respuestasPorEjercicio[NroEjercicio][res] += 1;
    }



//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        int notaExamenDW = 0;
        for (int i = 0;i<examenDW.length;i++){
            if(examenDW[i] == this._examen_canonico[i]){
                notaExamenDW += 100/this._examen_canonico.length;
            }
        }
        Alumno[] copiadoresDW = new Alumno[n];
        for (int m = 0;m<n;m++){
            copiadoresDW[m] = (Alumno) this._notas_de_estudiantes.desencolar();
        }
        for (int j = 0; j < n;j++){
            Alumno alumnoActual = copiadoresDW[j]; // O(1) 
            int[] examenAnterior = alumnoActual.getExamen(); //O(1)
            for (int k = 0;k<examenAnterior.length;k++){ //O(R)
                if(examenAnterior[k] != -1){
                    this.respuestasPorEjercicio[k][examenAnterior[k]]--;
                }
            }
            for(int l = 0;l<examenDW.length;l++){
                respuestasPorEjercicio[l][examenDW[l]]++;
            }
            alumnoActual.reemplazarExamen(examenDW); // O(R)
            alumnoActual.actualizarNota(notaExamenDW); //O(1)
            this._notas_de_estudiantes.insertar(alumnoActual); //O(log(K))
        }
    }
 

//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        MinHeapAlumno.HandleHeap entregador = this._notas_de_estudiantes.obtenerHandle(estudiante);
        entregador.entregar();
        this._estudiantes_entregados.insertar(new AlumnoEntregado(entregador.obtenerId(), entregador.obtenerNota()));
    }

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        ArrayList<NotaFinal> notas = new ArrayList<>(cantidadEstudiantes);
        for (int i = 0;i<cantidadEstudiantes;i++){
            AlumnoEntregado alumnoActual = this._estudiantes_entregados.desencolar();
            if (!esCopiador(alumnoActual.getId())){
               notas.add(new NotaFinal(alumnoActual.getNota(), alumnoActual.getId()));
            } 
        }
        NotaFinal[] res = new NotaFinal[notas.size()];
        for(int j = 0;j<notas.size();j++){
            res[j] = notas.get(j);
        }
        return res;
    }

//-------------------------------------------------------CHEQUEAR COPIAS-------------------------------------------------

    private boolean esCopiador(int id) {
        int i = 0;
        while (i<copiadoresId.size()) {
            int idActual = copiadoresId.get(i);
            if(id == idActual){
                return true;
            }
            i++;
        }
        return false;
    }

    public int[] chequearCopias() {
        ArrayList<Integer> copiones = new ArrayList<Integer>(cantidadEstudiantes);
        for(int i = 0;i<this.cantidadEstudiantes;i++){
            MinHeapAlumno.HandleHeap alumnoActual = this._notas_de_estudiantes.obtenerHandle(i);
            int[] examen = alumnoActual.obtenerExamen();
            int id = alumnoActual.obtenerId();
            int j = 0;
            boolean esCopion = true;
            boolean estaEnBlanco = true;
            while (esCopion && j<examen.length) {
                if(examen[j] != -1){
                    estaEnBlanco = false;
                    if (respuestasPorEjercicio[j][examen[j]] / (new Float(cantidadEstudiantes)) <= 0.25){
                        esCopion = false;
                    }
                }
                j++;
            }
            if (esCopion && !estaEnBlanco){
                copiadoresId.add(id);
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
        this.cantidadEstudiantes = cantidadEstudiantes;  
        _notas_de_estudiantes = new MinHeap<Alumno>(cantidadEstudiantes);
        for(int i = 0; i<cantidadEstudiantes;i++){
            _notas_de_estudiantes.insertar(new Alumno(examenCanonico.length,i));
        }
        _estudiantes_entregados = new MaxHeapAlumno(cantidadEstudiantes);
        copiadoresId = new ArrayList<>(cantidadEstudiantes);
        respuestasPorEjercicio = new int[examenCanonico.length][10];
        _lado_aula = ladoAula/2;
        _examen_canonico = examenCanonico;

    }

}
