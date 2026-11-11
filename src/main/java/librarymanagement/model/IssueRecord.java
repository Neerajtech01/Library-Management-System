package librarymanagement.model;

public class IssueRecord {
    public int id;
    public int bookId;
    public int studentId;
    public String issueDate;
    public String dueDate;
    public String returnDate;
    public String status;

    public IssueRecord(int id, int bookId, int studentId, String issueDate, String dueDate, String returnDate, String status) {
        this.id = id;
        this.bookId = bookId;
        this.studentId = studentId;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public String toFileLine() {
        return id + "|" + bookId + "|" + studentId + "|" + issueDate + "|" + dueDate + "|" + returnDate + "|" + status;
    }

    public static IssueRecord fromFileLine(String line) {
        String[] p = line.split("\\|", -1);
        return new IssueRecord(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]), p[3], p[4], p[5], p[6]);
    }
}
