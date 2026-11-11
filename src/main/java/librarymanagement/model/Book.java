package librarymanagement.model;

public class Book {
    public int id;
    public String title;
    public String author;
    public String category;
    public int totalCopies;
    public int availableCopies;

    public Book(int id, String title, String author, String category, int totalCopies, int availableCopies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.category = category;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
    }

    public String toFileLine() {
        return id + "|" + clean(title) + "|" + clean(author) + "|" + clean(category) + "|" + totalCopies + "|" + availableCopies;
    }

    public static Book fromFileLine(String line) {
        String[] p = line.split("\\|", -1);
        return new Book(Integer.parseInt(p[0]), p[1], p[2], p[3], Integer.parseInt(p[4]), Integer.parseInt(p[5]));
    }

    private String clean(String value) { return value.replace("|", " "); }
}
