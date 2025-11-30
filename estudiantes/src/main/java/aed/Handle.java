package aed;

public interface Handle <T extends Comparable<T>>{

        public int getPosicion();
    
        public void reemplazarValor(T nuevoValor);
        
}
