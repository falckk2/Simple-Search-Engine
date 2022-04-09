public class Main {

    /* Testing class */
    public static void main(String[] args) {
        Document[] docs = new Document[3];
        docs[0] = new Document("the brown fox jumped over the brown dog", 0);
        docs[1] = new Document("the lazy brown dog sat in the corner", 1);
        docs[2] = new Document("the red fox bit the lazy dog", 2);

        SearchEngine engine = new SearchEngine(docs);
        IDocument[] result = engine.search("brown");
        System.out.println("Length of the return: " + result.length);
        for (int i = 0; i < result.length; i++) {
            System.out.println("ID: " + (result[i].getId() + 1));
            System.out.println("Content: " + result[i].getContent());
        }
    }
}
