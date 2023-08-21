import java.util.Comparator;

public class Edge {

    private int index1;

    private int index2;
    private int value;

    public Edge(int index1, int index2, int value) {
        this.index1 = index1;
        this.index2 = index2;
        this.value = value;
    }

    public int getIndex1() {
        return index1;
    }

    public void setIndex1(int index1) {
        this.index1 = index1;
    }

    public int getIndex2() {
        return index2;
    }

    public void setIndex2(int index2) {
        this.index2 = index2;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

