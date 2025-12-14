package aed;

public interface CopiableYComparable extends Comparable<CopiableYComparable>{
    public CopiableYComparable copiar();
    public int compareTo(CopiableYComparable o);
}
