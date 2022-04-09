
public class CountTuple implements ITuple {
    private String word;
    private double count;

    public CountTuple(String term, double count) {
        word = term;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public double getValue() {
        return count;
    }

    public void increment() {
        count += 1;
    }

    public void setValue(double value) {
        count = value;
    }
}
