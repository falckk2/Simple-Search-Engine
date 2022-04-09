public class Document implements IDocument {

    private String content;
    private int id;

    public Document(String content, int id) {
        this.content = content;
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public int getId() {
        return id;
    }

    public String toString() {
        return content;
    }

}