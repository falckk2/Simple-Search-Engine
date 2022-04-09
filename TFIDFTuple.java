
public class TFIDFTuple implements ITuple, Comparable<TFIDFTuple> {
    private String word;
    private double tf_idf;

    public TFIDFTuple(String term, double tfidf) {
        word = term;
        tf_idf = tfidf;
    }

    /*
     * run through the documents
     * sort each word and calculate corresponding tf_idf value
     * for their corresponding document they belong to
     * will give a list of lists
     * 1st will be the document,
     * 2nd will be the words with corresponding tf_idf
     */

    public String getWord() {
        return word;
    }

    public double getValue() {
        return tf_idf;
    }

    public void setValue(double value) {
        tf_idf = value;
    }

    public int compareTo(TFIDFTuple other) {
        double other_tdidf = other.getValue();
        if (tf_idf == other_tdidf) {
            return 0;
        } else if (tf_idf < other_tdidf) {
            return -1;
        } else {
            return 1;
        }
    }
}