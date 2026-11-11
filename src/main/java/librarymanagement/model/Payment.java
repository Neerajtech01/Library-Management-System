package librarymanagement.model;

public class Payment {
    public int id;
    public int issueId;
    public int studentId;
    public double amount;
    public String date;
    public String method;
    public String status;

    public Payment(int id, int issueId, int studentId, double amount, String date, String method, String status) {
        this.id = id;
        this.issueId = issueId;
        this.studentId = studentId;
        this.amount = amount;
        this.date = date;
        this.method = method;
        this.status = status;
    }

    public String toFileLine() {
        return id + "|" + issueId + "|" + studentId + "|" + amount + "|" + date + "|" + method + "|" + status;
    }

    public static Payment fromFileLine(String line) {
        String[] p = line.split("\\|", -1);
        return new Payment(Integer.parseInt(p[0]), Integer.parseInt(p[1]), Integer.parseInt(p[2]), Double.parseDouble(p[3]), p[4], p[5], p[6]);
    }
}
