package librarymanagement.model;

public class Student {
    public int id;
    public String name;
    public String rollNo;
    public String phone;
    public String email;

    public Student(int id, String name, String rollNo, String phone, String email) {
        this.id = id;
        this.name = name;
        this.rollNo = rollNo;
        this.phone = phone;
        this.email = email;
    }

    public String toFileLine() {
        return id + "|" + clean(name) + "|" + clean(rollNo) + "|" + clean(phone) + "|" + clean(email);
    }

    public static Student fromFileLine(String line) {
        String[] p = line.split("\\|", -1);
        return new Student(Integer.parseInt(p[0]), p[1], p[2], p[3], p[4]);
    }

    private String clean(String value) { return value.replace("|", " "); }
}
