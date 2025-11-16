package aed;

import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;



public class EdR {
    private MinHeapAlumno _notas_de_estudiantes;
    private int cantidadEstudiantes;
    private MaxHeapAlumno _estudiantes_entregados;
    private int _lado_aula;
    private int[] _examen_canonico;
    private int[][] respuestasPorEjercicio;
    private ArrayList<Integer> copiadoresId;

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){ 
        double[] res = new double[cantidadEstudiantes];
        for(int i = 0;i<cantidadEstudiantes;i++){ // O(E)
            MinHeapAlumno.HandleHeap alumnoActual = this._notas_de_estudiantes.obtenerHandle(i); // O(1)
            res[i] = alumnoActual.obtenerNota(); // O(1)
        }
        return res;
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------

    public void copiarse(int estudiante) { 
        MinHeapAlumno.HandleHeap alumnoActual = this._notas_de_estudiantes.obtenerHandle(estudiante); // O(1)
        if(alumnoActual.obtenerEntrego() == true){ // O(1)
            return;
        }
        int[] vecinos = hallarVecinos(estudiante); // O (1) porq son todas operaciones acotadas
        if( vecinos != null){ // Si es distinto a null osea hay gente para copiarse , llamo a una funcion para ver cual es el que tiene mas rtas
            int pos_vecino_a_copiar = chequeoRtasVecinos(estudiante, vecinos); // O(R)
            if (pos_vecino_a_copiar != -1){ 
            //Busco el inciso que voy a copiar
            int[] examen_vecino = this._notas_de_estudiantes.obtenerHandle(pos_vecino_a_copiar).obtenerExamen(); // O(1)
            int[] examen_copiador = this._notas_de_estudiantes.obtenerHandle(estudiante).obtenerExamen(); // O(1)
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

    private int chequeoRtasVecinos(int copiador, int[] vecinos){ // O (R) porq son todas operaciones acotadas
        int copiado = -1;
        int max_rtas = -1;
        int[] examen_copiador = _notas_de_estudiantes.obtenerHandle(copiador).obtenerExamen();
        for (int i = 0 ; i < vecinos.length;i++){ // O(1)
            boolean vecinoEntrego = _notas_de_estudiantes.obtenerHandle(vecinos[i]).obtenerEntrego();
            if (vecinoEntrego != true){
                int contador = 0;
                int[] examen_vecino = _notas_de_estudiantes.obtenerHandle(vecinos[i]).obtenerExamen();
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
            }
            return copiado;
    }

//-----------------------------------------------RESOLVER----------------------------------------------------------------

    public void resolver(int estudiante, int NroEjercicio, int res) {
        MinHeapAlumno.HandleHeap alumnoActual = _notas_de_estudiantes.obtenerHandle(estudiante); // O(1)
        if(alumnoActual.obtenerEntrego()==true){ // O(1)
            return;
        }
        else if (NroEjercicio < 0 || NroEjercicio >= this._examen_canonico.length){ // O(1)
            return;
        }
        else if (res < 0 || res > 9){ // O(1)
            return;
        }
        else if (alumnoActual.obtenerExamen()[NroEjercicio] != -1){ // O(1)
            return;
        }
        alumnoActual.resolverEjercicio(NroEjercicio, res, _examen_canonico); // O(log(E))
        respuestasPorEjercicio[NroEjercicio][res] += 1; // O(1)
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
            copiadoresDW[m] = this._notas_de_estudiantes.desencolar(); // O(1)
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
        if (entregador.obtenerEntrego() == true){
            return;
        }
        entregador.entregar(); // O(log(E))
        this._estudiantes_entregados.insertar(new AlumnoEntregado(entregador.obtenerId(), entregador.obtenerNota())); // O(log(E))
    }

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        if (this._estudiantes_entregados.getCantidadElementos()!=cantidadEstudiantes){
            return new NotaFinal[]{};
        }
        ArrayList<NotaFinal> notas = new ArrayList<>(cantidadEstudiantes);
        for (int i = 0;i<cantidadEstudiantes;i++){ // O(E)
            AlumnoEntregado alumnoActual = this._estudiantes_entregados.desencolar(); // O(1)
            if (!esCopiador(alumnoActual.getId())){
               notas.add(new NotaFinal(alumnoActual.getNota(), alumnoActual.getId())); // O(1)
            } 
        }
        NotaFinal[] res = new NotaFinal[notas.size()];
        for(int j = 0;j<notas.size();j++){ // O(E)
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
        if (this._estudiantes_entregados.getCantidadElementos()!=cantidadEstudiantes){
            return new int[]{};
        }

        ArrayList<Integer> copiones = new ArrayList<Integer>(cantidadEstudiantes);
        for(int i = 0;i<this.cantidadEstudiantes;i++){ // O(E)
            MinHeapAlumno.HandleHeap alumnoActual = this._notas_de_estudiantes.obtenerHandle(i);
            int[] examen = alumnoActual.obtenerExamen();
            int id = alumnoActual.obtenerId();
            int j = 0;
            boolean esCopion = true;
            boolean estaEnBlanco = true;
            while (esCopion && j<examen.length) { // O(R)
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
        for (int i = 0;i<copiones.size();i++){ // O(E)
            res[i] = copiones.get(i);
        }
        return res;
    }


    public EdR(int ladoAula, int cantidadEstudiantes, int[] examenCanonico) {
        if(ladoAula <= 0 ){
            throw new UnsupportedOperationException("El lado del aula debe ser mayor igual que 0");
        }

        else if (ladoAula % 2 == 0 && (ladoAula*ladoAula)/2 < cantidadEstudiantes){
            throw new UnsupportedOperationException("Los estudiante no entran en el aula");

        }
        else if (ladoAula % 2 != 0 && (ladoAula*(ladoAula + 1))/2 < cantidadEstudiantes){
            throw new UnsupportedOperationException("Los estudiante no entran en el aula");

        }
        _lado_aula = ladoAula/2;
        this.cantidadEstudiantes = cantidadEstudiantes;  
        _notas_de_estudiantes = new MinHeapAlumno(cantidadEstudiantes,examenCanonico.length); // O(E*R)
        _estudiantes_entregados = new MaxHeapAlumno(cantidadEstudiantes); // O(E)
        copiadoresId = new ArrayList<>(cantidadEstudiantes); // O(E)
        respuestasPorEjercicio = new int[examenCanonico.length][10]; // O(R)
        _examen_canonico = examenCanonico;
    }


    // FUNCIONES PARA TESTING 

    public int[] getExamen(int estudiante){
        return this._notas_de_estudiantes.obtenerHandle(estudiante).obtenerExamen().clone();
    }

    public boolean getEntrego (int estudiante){
        return this._notas_de_estudiantes.obtenerHandle(estudiante).obtenerEntrego();
    }

}
