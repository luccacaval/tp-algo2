package aed;

public class NotaFinal implements Comparable<NotaFinal> {
    public double _nota;
    public int _id;

    public NotaFinal(double nota, int id){
        _nota = nota;
        _id = id;
    }

    @Override
    public boolean equals(Object otraNota) {
        NotaFinal nota2 = null;
        if (otraNota != null){
            nota2 = (NotaFinal) otraNota;
        } else{
            nota2 = null;
            return false;
        }
        if(this._nota == nota2._nota && this._id == nota2._id){
            return true;
        } else{
            return false;
        }
    }
    
    @Override
    public int compareTo(NotaFinal otra){
        if (Double.compare(this._nota, otra._nota) == 0){
            return this._id - otra._id;
        } else{
            return Double.compare(this._nota, otra._nota);
        }
    }
}
