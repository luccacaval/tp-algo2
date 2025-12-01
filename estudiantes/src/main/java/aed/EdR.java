package aed;

import java.util.ArrayList;

public class EdR {
    private MinHeap<NotaFinal> notasDeEstudiantes;
    private Alumno[] alumnosPorId;
    private int cantidadEstudiantes;
    private MaxHeap<AlumnoEntregado> estudiantesEntregados;
    private int _lado_aula;
    private int[] examenCanonico;
    private int[][] respuestasPorEjercicio; 
    private ArrayList<Integer> copiadoresId;

//-------------------------------------------------NOTAS--------------------------------------------------------------------------

    public double[] notas(){ 
        double[] res = new double[cantidadEstudiantes];
        for(int i = 0;i<cantidadEstudiantes;i++){ // O(E)
            Alumno alumnoActual = this.alumnosPorId[i]; // O(1)
            res[i] = alumnoActual.getNota(); // O(1)
        }
        return res;
    }

//------------------------------------------------COPIARSE------------------------------------------------------------------------

    public void copiarse(int estudiante) { 
        Alumno alumnoActual = this.alumnosPorId[estudiante]; // O(1)
        if(alumnoActual.entrego){ // O(1) 
            return;
        }
        int[] vecinos = hallarVecinos(estudiante); // O (1) porq son todas operaciones acotadas
        if( vecinos != null){ // Si es distinto a null osea hay gente para copiarse , llamo a una funcion para ver cual es el que tiene mas rtas
            int pos_vecino_a_copiar = chequeoRtasVecinos(estudiante, vecinos); // O(R)
            if (pos_vecino_a_copiar != -1){ 
            //Busco el inciso que voy a copiar
            int[] examen_vecino = this.alumnosPorId[pos_vecino_a_copiar].getExamen(); // O(R)
            int[] examen_copiador = this.alumnosPorId[estudiante].getExamen(); // O(R)
            int inciso_a_copiar = -1;
            int j = 0;
            while (inciso_a_copiar == -1 && j < examen_copiador.length){ // O(R) PEOR CASO
                if(examen_copiador[j] == -1 && examen_vecino[j] != -1){
                    inciso_a_copiar = j;
                }
                j++;
            }
            if (inciso_a_copiar != -1){
            resolver(estudiante, inciso_a_copiar, examen_vecino[inciso_a_copiar]);} //O(log(E))
            }
        }
    }


    // DOCENTE: Encapsular este comportamiento proceduralmente en una funcion esta bueno. 
    // Lo que es poco convencional es que este metodo privado lo dejen en el medio de la 
    // interfaz publica. Normalmente va primero la interfaz publica y despues los auxiliares.
    private int[] hallarVecinos(int estudiante) { // O (1) porq son todas operaciones acotadas
        //Preguntar valen si el array en java cuando se inicializa , como se inicializaria // DOCENTE: y esto?
        if (estudiante > cantidadEstudiantes || estudiante < 0) {
                return null;
        }
        int contador_vecinos_validos = 0;
        int[] vecinos_aux = {-1,-1,-1};
                                    //java truca el cociente entre enteros
                                    
        int vecino_de_adelante = estudiante + _lado_aula; 

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

    // DOCENTE: Encapsular este comportamiento proceduralmente en una funcion esta bueno. 
    // Lo que es poco convencional es que este metodo privado lo dejen en el medio de la 
    // interfaz publica. Normalmente va primero la interfaz publica y despues los auxiliares.
    private int chequeoRtasVecinos(int copiador, int[] vecinos){ // O (R) porq son todas operaciones acotadas
        int copiado = -1;
        int max_rtas = -1;
        int[] examen_copiador = this.alumnosPorId[copiador].getExamen();
        for (int i = 0 ; i < vecinos.length;i++){ // O(1)
            Alumno vecinoActual = this.alumnosPorId[vecinos[i]];
            boolean vecinoEntrego = vecinoActual.entrego;
            if (vecinoEntrego != true){
                int contador = 0;
                int[] examen_vecino = vecinoActual.getExamen();
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
        Alumno alumnoActual = this.alumnosPorId[estudiante]; // O(1)
        if(alumnoActual.entrego==true){ // O(1)
            return;
        }
        else if (NroEjercicio < 0 || NroEjercicio >= this.examenCanonico.length){ // O(1)
            return;
        }
        else if (res < 0 || res > 9){ // O(1)
            return;
        }
        else if (alumnoActual.getExamen()[NroEjercicio] != -1){ // O(1)
            return;
        }
        alumnoActual.resolverEjercicio(NroEjercicio, res, examenCanonico); // O(1)
        int nuevaPosicion = notasDeEstudiantes.restaurarInvariante(alumnoActual.getPosicionNota());
        alumnoActual.actualizarPosicionNota(nuevaPosicion);
        respuestasPorEjercicio[NroEjercicio][res] += 1; // O(1)
    }
    

//------------------------------------------------CONSULTAR DARK WEB-------------------------------------------------------

    public void consultarDarkWeb(int n, int[] examenDW) {
        int notaExamenDW = 0;
        for (int i = 0;i<examenDW.length;i++){
            if(examenDW[i] == this.examenCanonico[i]){
                notaExamenDW += 100/this.examenCanonico.length;
            }
        }
        NotaFinal[] copiadoresDW = new NotaFinal[n];
        for (int m = 0;m<n;m++){
            NotaFinal posibleCopiador = this.notasDeEstudiantes.desencolar();
            while(this.alumnosPorId[posibleCopiador._id].entrego){
                posibleCopiador = this.notasDeEstudiantes.desencolar();
            }
            copiadoresDW[m] = posibleCopiador; // O(1)
        }
        for (int j = 0; j < n;j++){
            Alumno alumnoActual = this.alumnosPorId[copiadoresDW[j]._id]; // O(1) 
            int[] examenAnterior = alumnoActual.getExamen(); //O(1)
            for (int k = 0;k<examenAnterior.length;k++){ //O(R)
                if(examenAnterior[k] != -1){
                    this.respuestasPorEjercicio[k][examenAnterior[k]]--;
                }
            }
            for(int l = 0;l<examenDW.length;l++){
                respuestasPorEjercicio[l][examenDW[l]]++;
            }
            MinHeap<NotaFinal>.HandleMinHeap handleNota = this.notasDeEstudiantes.insertar(new NotaFinal(notaExamenDW, alumnoActual.getId()));
            alumnosPorId[copiadoresDW[j]._id] = new Alumno(copiadoresDW[j]._id, examenDW, handleNota, false);
        }
    }
 
//-------------------------------------------------ENTREGAR-------------------------------------------------------------

    public void entregar(int estudiante) {
        Alumno entregador = this.alumnosPorId[estudiante];
        if (entregador.entrego == true){
            return;
        }
        entregador.entregar(); // O(log(E))
        this.estudiantesEntregados.insertar(new AlumnoEntregado(entregador.getId(), entregador.getNota())); // O(log(E))
    }

//-----------------------------------------------------CORREGIR---------------------------------------------------------

    public NotaFinal[] corregir() {
        if (this.estudiantesEntregados.getCantidadElementos()!=cantidadEstudiantes){
            return new NotaFinal[]{};
        }
        ArrayList<NotaFinal> notas = new ArrayList<>(cantidadEstudiantes);
        for (int i = 0;i<cantidadEstudiantes;i++){ // O(E)
            AlumnoEntregado alumnoActual = this.estudiantesEntregados.desencolar(); // O(1)
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
        if (this.estudiantesEntregados.getCantidadElementos()!=cantidadEstudiantes){
            return new int[]{};
        }

        ArrayList<Integer> copiones = new ArrayList<Integer>(cantidadEstudiantes);
        for(int i = 0;i<this.cantidadEstudiantes;i++){ // O(E)
            Alumno alumnoActual = this.alumnosPorId[i];
            int[] examen = alumnoActual.getExamen();
            int id = alumnoActual.getId();
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


    // DOCENTE: no esta mal, pero es raro poner los constructores al final. Normalmente estan al principio de la interfaz publica
    public EdR(int ladoAula, int cantidadEstudiantes, int[] examenCanonico) {
        _lado_aula = ladoAula;
        this.cantidadEstudiantes = cantidadEstudiantes;
        this.alumnosPorId = new Alumno[cantidadEstudiantes]; // O(E)
        this.notasDeEstudiantes = new MinHeap<NotaFinal>(cantidadEstudiantes); // O(E)
        for(int i = 0; i < cantidadEstudiantes;i++){ // O(E)
            MinHeap<NotaFinal>.HandleMinHeap handleNota = notasDeEstudiantes.insertar(new NotaFinal(0, i)); // O(1) ya que no hay que hacer ningun shift
            alumnosPorId[i] = new Alumno(examenCanonico.length, i, handleNota);
        }
        this.estudiantesEntregados = new MaxHeap<AlumnoEntregado>(cantidadEstudiantes); // O(E)
        this.copiadoresId = new ArrayList<>(cantidadEstudiantes); // O(E)
        this.respuestasPorEjercicio = new int[examenCanonico.length][10]; // O(R)
        this.examenCanonico = examenCanonico;
    }

    public int[] getExamen(int i) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getExamen'");
    }

    public boolean getEntrego(int i) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEntrego'");
    }

}
