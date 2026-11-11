package librarymanagement.service;

import librarymanagement.model.*;
import librarymanagement.util.FileUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LibraryService {
    private final List<Book> books = new ArrayList<>();
    private final List<Student> students = new ArrayList<>();
    private final List<IssueRecord> issues = new ArrayList<>();
    private final List<Payment> payments = new ArrayList<>();

    private String libraryName = "My College Library";
    private double finePerDay = 5;
    private int issueDuration = 14;

    public LibraryService() {
        loadAll();
    }

    public void loadAll() {
        books.clear(); students.clear(); issues.clear(); payments.clear();
        for (String line : FileUtil.read("books.txt")) if (!line.isBlank()) books.add(Book.fromFileLine(line));
        for (String line : FileUtil.read("students.txt")) if (!line.isBlank()) students.add(Student.fromFileLine(line));
        for (String line : FileUtil.read("issues.txt")) if (!line.isBlank()) issues.add(IssueRecord.fromFileLine(line));
        for (String line : FileUtil.read("payments.txt")) if (!line.isBlank()) payments.add(Payment.fromFileLine(line));
        for (String line : FileUtil.read("settings.txt")) {
            if (line.startsWith("libraryName|")) libraryName = line.substring("libraryName|".length());
            if (line.startsWith("finePerDay|")) finePerDay = Double.parseDouble(line.substring("finePerDay|".length()));
            if (line.startsWith("issueDuration|")) issueDuration = Integer.parseInt(line.substring("issueDuration|".length()));
        }
    }

    public List<Book> getBooks() { return books; }
    public List<Student> getStudents() { return students; }
    public List<IssueRecord> getIssues() { return issues; }
    public List<Payment> getPayments() { return payments; }
    public String getLibraryName() { return libraryName; }
    public double getFinePerDay() { return finePerDay; }
    public int getIssueDuration() { return issueDuration; }

    public int nextBookId() { return nextId(books.stream().map(b -> b.id).toList()); }
    public int nextStudentId() { return nextId(students.stream().map(s -> s.id).toList()); }
    public int nextIssueId() { return nextId(issues.stream().map(i -> i.id).toList()); }
    public int nextPaymentId() { return nextId(payments.stream().map(p -> p.id).toList()); }

    private int nextId(List<Integer> ids) {
        int max = 0;
        for (int id : ids) max = Math.max(max, id);
        return max + 1;
    }

    public void saveBooks() { FileUtil.write("books.txt", books.stream().map(Book::toFileLine).toList()); }
    public void saveStudents() { FileUtil.write("students.txt", students.stream().map(Student::toFileLine).toList()); }
    public void saveIssues() { FileUtil.write("issues.txt", issues.stream().map(IssueRecord::toFileLine).toList()); }
    public void savePayments() { FileUtil.write("payments.txt", payments.stream().map(Payment::toFileLine).toList()); }

    public void saveSettings(String name, double fine, int duration) {
        libraryName = name;
        finePerDay = fine;
        issueDuration = duration;
        List<String> lines = List.of("libraryName|" + name.replace("|", " "), "finePerDay|" + fine, "issueDuration|" + duration);
        FileUtil.write("settings.txt", lines);
    }

    public Book findBook(int id) { return books.stream().filter(b -> b.id == id).findFirst().orElse(null); }
    public Student findStudent(int id) { return students.stream().filter(s -> s.id == id).findFirst().orElse(null); }
    public IssueRecord findIssue(int id) { return issues.stream().filter(i -> i.id == id).findFirst().orElse(null); }

    public void addBook(Book book) { books.add(book); saveBooks(); }
    public void updateBook(Book updated) {
        Book old = findBook(updated.id);
        if (old != null) {
            old.title = updated.title; old.author = updated.author; old.category = updated.category;
            old.totalCopies = updated.totalCopies; old.availableCopies = Math.min(updated.totalCopies, updated.availableCopies);
            saveBooks();
        }
    }
    public void deleteBook(int id) {
        if (issues.stream().anyMatch(i -> i.bookId == id && "ISSUED".equals(i.status))) throw new IllegalStateException("This book is currently issued.");
        books.removeIf(b -> b.id == id); saveBooks();
    }

    public void addStudent(Student student) { students.add(student); saveStudents(); }
    public void updateStudent(Student updated) {
        Student old = findStudent(updated.id);
        if (old != null) {
            old.name = updated.name; old.rollNo = updated.rollNo; old.phone = updated.phone; old.email = updated.email;
            saveStudents();
        }
    }
    public void deleteStudent(int id) {
        if (issues.stream().anyMatch(i -> i.studentId == id && "ISSUED".equals(i.status))) throw new IllegalStateException("This student has an issued book.");
        students.removeIf(s -> s.id == id); saveStudents();
    }

    public IssueRecord issueBook(int bookId, int studentId) {
        Book book = findBook(bookId);
        if (book == null) throw new IllegalArgumentException("Book not found.");
        if (findStudent(studentId) == null) throw new IllegalArgumentException("Student not found.");
        if (book.availableCopies <= 0) throw new IllegalStateException("No available copy of this book.");
        boolean alreadyIssued = issues.stream().anyMatch(i -> i.bookId == bookId && i.studentId == studentId && "ISSUED".equals(i.status));
        if (alreadyIssued) throw new IllegalStateException("This student already has this book.");

        LocalDate today = LocalDate.now();
        IssueRecord record = new IssueRecord(nextIssueId(), bookId, studentId, today.toString(), today.plusDays(issueDuration).toString(), "-", "ISSUED");
        issues.add(record);
        book.availableCopies--;
        saveIssues(); saveBooks();
        return record;
    }

    public double calculateFine(IssueRecord issue) {
        if (issue == null) return 0;
        LocalDate endDate = "-".equals(issue.returnDate) ? LocalDate.now() : LocalDate.parse(issue.returnDate);
        LocalDate due = LocalDate.parse(issue.dueDate);
        long lateDays = ChronoUnit.DAYS.between(due, endDate);
        return Math.max(0, lateDays) * finePerDay;
    }

    public double returnBook(int issueId) {
        IssueRecord issue = findIssue(issueId);
        if (issue == null) throw new IllegalArgumentException("Issue record not found.");
        if (!"ISSUED".equals(issue.status)) throw new IllegalStateException("This book is already returned.");

        issue.returnDate = LocalDate.now().toString();
        issue.status = "RETURNED";
        Book book = findBook(issue.bookId);
        if (book != null) book.availableCopies = Math.min(book.totalCopies, book.availableCopies + 1);
        double fine = calculateFine(issue);
        saveIssues(); saveBooks();
        return fine;
    }

    public Payment recordVirtualPayment(int issueId, double amount, String method) {
        IssueRecord issue = findIssue(issueId);
        if (issue == null) throw new IllegalArgumentException("Issue record not found.");
        Payment payment = new Payment(nextPaymentId(), issueId, issue.studentId, amount, LocalDate.now().toString(), method, "PAID (VIRTUAL)");
        payments.add(payment); savePayments();
        return payment;
    }

    public long countIssued() { return issues.stream().filter(i -> "ISSUED".equals(i.status)).count(); }
    public int countAvailableBooks() { return books.stream().mapToInt(b -> b.availableCopies).sum(); }
}
