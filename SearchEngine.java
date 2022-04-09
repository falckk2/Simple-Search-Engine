import java.util.*;
import java.lang.Math;

public class SearchEngine {
    /*
     * the documents are wrapped as a separate class. Makes it easy to switch out
     * the type of document being used
     */
    private List<IDocument> docs;

    /*
     * values for indexing stored in a list of lists of tuples containing the term
     * and its tf_idf value
     */
    List<List<ITuple>> tfIdfList;

    public SearchEngine(IDocument[] docs) {
        this.docs = new ArrayList<IDocument>(Arrays.asList(docs));
        tfIdfList = index(docs);
    }

    public IDocument[] search(String term) {
        // Add all documents including the search term
        List<IDocument> documents = new ArrayList<IDocument>();
        List<Integer> indexList = new ArrayList<Integer>();
        // find docs with term
        for (int i = 0; i < tfIdfList.size(); i++) {
            int index = findWord(tfIdfList.get(i), term);
            if (index >= 0) {
                documents.add(docs.get(i));
                indexList.add(index);
            }
        }
        // sort it
        sort(documents, indexList, term);
        IDocument[] documentList = new IDocument[documents.size()];
        documents.toArray(documentList);
        return documentList;
    }

    /* Sorting in accordance with of TF-IDF */
    private void sort(List<IDocument> documents, List<Integer> indexList, String term) {
        // sort the documents in accordance with the term's TF-IDF
        int size = documents.size();
        double[] tfidf = new double[size];
        for (int i = 0; i < size; i++) {
            int id = documents.get(i).getId();
            int pointer = indexList.get(i);
            tfidf[i] = tfIdfList.get(id).get(pointer).getValue();
        }
        quicksort(tfidf, documents, 0, size - 1);
    }

    /* quicksorts the list by descending order */
    private void quicksort(double[] tfidf, List<IDocument> documents, int low, int high) {
        if (low < high) {
            int pivot = partition(tfidf, documents, low, high);

            quicksort(tfidf, documents, low, pivot - 1);
            quicksort(tfidf, documents, pivot + 1, high);
        }
    }

    /* partition part of the quicksort */
    private int partition(double[] tfidf, List<IDocument> documents, int low, int high) {
        double pivot = tfidf[high];
        int i = low - 1;

        for (int j = low; j <= high - 1; j++) {
            if (tfidf[j] > pivot) {
                i++;
                double temp = tfidf[j];
                IDocument tempDoc = documents.get(j);
                tfidf[j] = tfidf[i];
                tfidf[i] = temp;
                documents.set(j, documents.get(i));
                documents.set(i, tempDoc);
            }
        }
        i++;
        IDocument tempDoc = documents.get(high);
        tfidf[high] = tfidf[i];
        tfidf[i] = pivot;
        documents.set(high, documents.get(i));
        documents.set(i, tempDoc);

        return i;
    }

    /* creates the table of tf-idf for each document's words */
    private List<List<ITuple>> index(IDocument[] documents) {
        int size = documents.length;

        List<List<ITuple>> countList = new ArrayList<List<ITuple>>(size);

        for (int i = 0; i < size; i++) {
            countList.add(new ArrayList<ITuple>());
        }

        // add words to each list from their corresponding documents
        // keep them sorted in alphabetic order via bubble sort as the new words come
        for (int i = 0; i < size; i++) {
            String[] words = documents[i].getContent().toLowerCase().split("[ (),.;:-?+'=!@#&{}]");
            List<ITuple> tempList = countList.get(i);
            for (int j = 0; j < words.length; j++) {
                int arrayPointer = findWord(tempList, words[j]);
                if (arrayPointer < 0) {
                    tempList.add(new CountTuple(words[j], 1));
                    bubbleSort(tempList);
                } else {
                    CountTuple tuple = (CountTuple) tempList.get(arrayPointer);
                    tuple.increment();
                }
            }
        }
        // calculate the tf-idf using the word frequency count from before
        List<List<ITuple>> tfIdfList = new ArrayList<List<ITuple>>(size);
        for (int i = 0; i < size; i++) {
            tfIdfList.add(new ArrayList<ITuple>());
        }
        for (int i = 0; i < size; i++) {
            double documentSize = 0;
            for (int j = 0; j < countList.get(i).size(); j++) {
                documentSize += countList.get(i).get(j).getValue();
            }
            for (int j = 0; j < countList.get(i).size(); j++) {
                double documentsWithTerm = 0;
                for (int k = 0; k < size; k++) {
                    if (findWord(countList.get(k), countList.get(i).get(j).getWord()) >= 0) {
                        documentsWithTerm += 1.0;
                    }
                }
                double idf = Math.log(size / documentsWithTerm);
                double tf = (countList.get(i).get(j).getValue() / documentSize);
                double tfIdf = tf * idf;
                String word = countList.get(i).get(j).getWord();
                tfIdfList.get(i).add(new TFIDFTuple(word, tfIdf));
            }
        }
        return tfIdfList;
    }

    /* uses a binary search method to check */
    private int findWord(List<ITuple> list, String term) {

        if (list.size() < 1) {
            return -1;
        } else {

            int top = list.size() - 1;
            int bottom = 0;
            int currentPoint;
            while (bottom <= top) {
                currentPoint = bottom + (top - bottom) / 2;
                if (list.get(currentPoint).getWord().equals(term)) {
                    return currentPoint;
                }
                if (list.get(currentPoint).getWord().compareTo(term) < 0) {
                    bottom = currentPoint + 1;
                } else {
                    top = currentPoint - 1;
                }
            }
            // word doesn't exist in the list
            return -1;

        }
    }

    /* bubblesort a mostly sorted list is quick */
    private void bubbleSort(List<ITuple> list) {
        int currentPoint = list.size() - 1;
        while (currentPoint > 0) {
            String currentString = list.get(currentPoint).getWord();
            String belowString = list.get(currentPoint - 1).getWord();
            if (currentString.compareTo(belowString) < 0) {
                ITuple temp = list.get(currentPoint - 1);
                list.set(currentPoint - 1, list.get(currentPoint));
                list.set(currentPoint, temp);
                currentPoint--;
            } else {
                break;
            }

        }
    }

}