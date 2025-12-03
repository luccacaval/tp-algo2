package aed;
public interface ClonableYComparable<T> extends Comparable<T>, Cloneable {
    public int compareTo(T otro);

    public T clone();
}
