package librarymanagement.ui;

import librarymanagement.model.*;
import librarymanagement.service.LibraryService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;

public class MainFrame extends JFrame {
    private final LibraryService service;
    private final CardLayout cards = new CardLayout();
    private final JPanel content = new JPanel(cards);
    private JLabel libraryTitle;

    public MainFrame(LibraryService service) {
        this.service = service;
        setTitle("Library Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        add(createSidebar(), BorderLayout.WEST);
        add(content, BorderLayout.CENTER);
        refreshAll();
    }

    private JPanel createSidebar() {
        JPanel side = new JPanel();
        side.setPreferredSize(new Dimension(220, 0));
        side.setBackground(new Color(30, 35, 45));
        side.setBorder(new EmptyBorder(20, 12, 20, 12));
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));

        libraryTitle = new JLabel(service.getLibraryName());
        libraryTitle.setForeground(Color.WHITE);
        libraryTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        libraryTitle.setBorder(new EmptyBorder(0, 10, 25, 5));
        side.add(libraryTitle);

        addNavButton(side, "1. Dashboard", "dashboard");
        addNavButton(side, "2. Book Management", "books");
        addNavButton(side, "3. Student Management", "students");
        addNavButton(side, "4. Book Issue & Return", "issues");
        addNavButton(side, "5. Payments & Fines", "payments");
        addNavButton(side, "6. Reports", "reports");
        addNavButton(side, "7. Settings", "settings");
        return side;
    }

    private void addNavButton(JPanel side, String text, String card) {
        JButton b = new JButton(text);
        b.setAlignmentX(Component.LEFT_ALIGNMENT);
        b.setMaximumSize(new Dimension(Integer.MAX_VALUE, 48));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setForeground(Color.WHITE);
        b.setBackground(new Color(30, 35, 45));
        b.setFocusPainted(false);
        b.setBorder(new EmptyBorder(8, 10, 8, 5));
        b.addActionListener(e -> cards.show(content, card));
        side.add(b);
        side.add(Box.createVerticalStrut(5));
    }

    private void refreshAll() {
        content.removeAll();
        content.add(createDashboard(), "dashboard");
        content.add(createBooksPanel(), "books");
        content.add(createStudentsPanel(), "students");
        content.add(createIssuesPanel(), "issues");
        content.add(createPaymentsPanel(), "payments");
        content.add(createReportsPanel(), "reports");
        content.add(createSettingsPanel(), "settings");
        content.revalidate();
        content.repaint();
        cards.show(content, "dashboard");
    }

    private JPanel basePanel(String title) {
        JPanel p = new JPanel(new BorderLayout(12, 12));
        p.setBorder(new EmptyBorder(22, 25, 22, 25));
        JLabel heading = new JLabel(title);
        heading.setFont(new Font("SansSerif", Font.BOLD, 26));
        p.add(heading, BorderLayout.NORTH);
        return p;
    }

    private JPanel createDashboard() {
        JPanel p = basePanel("Dashboard");
        JPanel stats = new JPanel(new GridLayout(1, 4, 15, 15));
        stats.add(statCard("Total Books", String.valueOf(service.getBooks().stream().mapToInt(b -> b.totalCopies).sum())));
        stats.add(statCard("Students", String.valueOf(service.getStudents().size())));
        stats.add(statCard("Issued Books", String.valueOf(service.countIssued())));
        stats.add(statCard("Available Books", String.valueOf(service.countAvailableBooks())));
        p.add(stats, BorderLayout.CENTER);
        JTextArea info = new JTextArea("Welcome to " + service.getLibraryName() + "!\n\nUse the menu to manage books, students, issues, virtual payments, reports and settings.\n\nAll records are stored in the data folder as simple text files.");
        info.setEditable(false); info.setFont(new Font("SansSerif", Font.PLAIN, 16)); info.setBorder(new EmptyBorder(15, 5, 5, 5));
        p.add(info, BorderLayout.SOUTH);
        return p;
    }

    private JPanel statCard(String label, String value) {
        JPanel card = new JPanel(new GridLayout(2, 1));
        card.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(new Color(210,210,210)), new EmptyBorder(15, 15, 15, 15)));
        JLabel v = new JLabel(value, SwingConstants.CENTER); v.setFont(new Font("SansSerif", Font.BOLD, 30));
        JLabel l = new JLabel(label, SwingConstants.CENTER); l.setFont(new Font("SansSerif", Font.PLAIN, 15));
        card.add(v); card.add(l); return card;
    }

    private JPanel createBooksPanel() {
        JPanel p = basePanel("Book Management");
        String[] cols = {"ID", "Title", "Author", "Category", "Total", "Available"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        JTable table = new JTable(model);
        fillBooks(model, "");
        JTextField search = new JTextField();
        search.setToolTipText("Search title, author or category");
        search.addActionListener(e -> fillBooks(model, search.getText()));
        JButton searchBtn = new JButton("Search"); searchBtn.addActionListener(e -> fillBooks(model, search.getText()));
        JPanel top = new JPanel(new BorderLayout(5, 5)); top.add(new JLabel("Search: "), BorderLayout.WEST); top.add(search, BorderLayout.CENTER); top.add(searchBtn, BorderLayout.EAST);
        JButton add = new JButton("Add Book");
        JButton edit = new JButton("Edit Selected");
        JButton delete = new JButton("Delete Selected");
        add.addActionListener(e -> addBookDialog(model));
        edit.addActionListener(e -> editBookDialog(table, model));
        delete.addActionListener(e -> deleteBook(table, model));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT)); buttons.add(add); buttons.add(edit); buttons.add(delete);
        JPanel center = new JPanel(new BorderLayout(8,8)); center.add(top, BorderLayout.NORTH); center.add(new JScrollPane(table), BorderLayout.CENTER); center.add(buttons, BorderLayout.SOUTH);
        p.add(center, BorderLayout.CENTER); return p;
    }

    private void fillBooks(DefaultTableModel model, String search) {
        model.setRowCount(0); String q = search.toLowerCase();
        for (Book b : service.getBooks()) if (q.isBlank() || b.title.toLowerCase().contains(q) || b.author.toLowerCase().contains(q) || b.category.toLowerCase().contains(q)) model.addRow(new Object[]{b.id,b.title,b.author,b.category,b.totalCopies,b.availableCopies});
    }

    private void addBookDialog(DefaultTableModel model) {
        JTextField title = new JTextField(), author = new JTextField(), category = new JTextField(), total = new JTextField("1");
        JPanel form = form("Title", title, "Author", author, "Category", category, "Total Copies", total);
        if (JOptionPane.showConfirmDialog(this, form, "Add Book", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try { int t = Integer.parseInt(total.getText().trim()); if (t < 1) throw new NumberFormatException(); service.addBook(new Book(service.nextBookId(), title.getText().trim(), author.getText().trim(), category.getText().trim(), t, t)); fillBooks(model, ""); } catch (NumberFormatException ex) { error("Total copies must be a positive number."); }
        }
    }

    private void editBookDialog(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow(); if (row < 0) { error("Select a book first."); return; }
        Book b = service.findBook((Integer) model.getValueAt(row, 0));
        JTextField title = new JTextField(b.title), author = new JTextField(b.author), category = new JTextField(b.category), total = new JTextField(String.valueOf(b.totalCopies));
        JPanel form = form("Title", title, "Author", author, "Category", category, "Total Copies", total);
        if (JOptionPane.showConfirmDialog(this, form, "Edit Book", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try { int newTotal = Integer.parseInt(total.getText().trim()); int issued = b.totalCopies - b.availableCopies; if (newTotal < issued) throw new IllegalArgumentException("Total cannot be less than currently issued copies (" + issued + ")."); b.totalCopies = newTotal; b.availableCopies = newTotal - issued; b.title = title.getText().trim(); b.author = author.getText().trim(); b.category = category.getText().trim(); service.saveBooks(); fillBooks(model, ""); } catch (Exception ex) { error(ex.getMessage()); }
        }
    }

    private void deleteBook(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow(); if (row < 0) { error("Select a book first."); return; }
        if (JOptionPane.showConfirmDialog(this, "Delete selected book?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try { service.deleteBook((Integer) model.getValueAt(row, 0)); fillBooks(model, ""); } catch (Exception ex) { error(ex.getMessage()); }
        }
    }

    private JPanel createStudentsPanel() {
        JPanel p = basePanel("Student Management");
        String[] cols = {"ID", "Name", "Roll No", "Phone", "Email"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r, int c) { return false; } };
        JTable table = new JTable(model); fillStudents(model);
        JButton add = new JButton("Add Student"), edit = new JButton("Edit Selected"), delete = new JButton("Delete Selected");
        add.addActionListener(e -> addStudentDialog(model)); edit.addActionListener(e -> editStudentDialog(table, model)); delete.addActionListener(e -> deleteStudent(table, model));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT)); buttons.add(add); buttons.add(edit); buttons.add(delete);
        p.add(new JScrollPane(table), BorderLayout.CENTER); p.add(buttons, BorderLayout.SOUTH); return p;
    }

    private void fillStudents(DefaultTableModel model) { model.setRowCount(0); for (Student s : service.getStudents()) model.addRow(new Object[]{s.id,s.name,s.rollNo,s.phone,s.email}); }

    private void addStudentDialog(DefaultTableModel model) {
        JTextField name = new JTextField(), roll = new JTextField(), phone = new JTextField(), email = new JTextField();
        JPanel form = form("Name", name, "Roll No", roll, "Phone", phone, "Email", email);
        if (JOptionPane.showConfirmDialog(this, form, "Add Student", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) { service.addStudent(new Student(service.nextStudentId(), name.getText().trim(), roll.getText().trim(), phone.getText().trim(), email.getText().trim())); fillStudents(model); }
    }

    private void editStudentDialog(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow(); if (row < 0) { error("Select a student first."); return; }
        Student s = service.findStudent((Integer) model.getValueAt(row,0));
        JTextField name = new JTextField(s.name), roll = new JTextField(s.rollNo), phone = new JTextField(s.phone), email = new JTextField(s.email);
        JPanel form = form("Name", name, "Roll No", roll, "Phone", phone, "Email", email);
        if (JOptionPane.showConfirmDialog(this, form, "Edit Student", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) { s.name=name.getText().trim(); s.rollNo=roll.getText().trim(); s.phone=phone.getText().trim(); s.email=email.getText().trim(); service.saveStudents(); fillStudents(model); }
    }

    private void deleteStudent(JTable table, DefaultTableModel model) {
        int row = table.getSelectedRow(); if (row < 0) { error("Select a student first."); return; }
        if (JOptionPane.showConfirmDialog(this,"Delete selected student?","Confirm",JOptionPane.YES_NO_OPTION)==JOptionPane.YES_OPTION) try { service.deleteStudent((Integer)model.getValueAt(row,0)); fillStudents(model); } catch(Exception ex){error(ex.getMessage());}
    }

    private JPanel createIssuesPanel() {
        JPanel p = basePanel("Book Issue & Return");
        String[] cols = {"Issue ID", "Book", "Student", "Issue Date", "Due Date", "Return Date", "Status", "Fine"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) { public boolean isCellEditable(int r,int c){return false;} };
        JTable table = new JTable(model); fillIssues(model);
        JButton issue = new JButton("Issue Book"), ret = new JButton("Return Selected");
        issue.addActionListener(e -> issueDialog(model)); ret.addActionListener(e -> returnIssue(table, model));
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT)); buttons.add(issue); buttons.add(ret);
        p.add(new JScrollPane(table), BorderLayout.CENTER); p.add(buttons, BorderLayout.SOUTH); return p;
    }

    private void fillIssues(DefaultTableModel model) {
        model.setRowCount(0);
        for (IssueRecord i : service.getIssues()) { Book b=service.findBook(i.bookId); Student s=service.findStudent(i.studentId); model.addRow(new Object[]{i.id,b==null?"Unknown":b.title,s==null?"Unknown":s.name,i.issueDate,i.dueDate,i.returnDate,i.status,String.format("₹%.2f",service.calculateFine(i))}); }
    }

    private void issueDialog(DefaultTableModel model) {
        if (service.getBooks().isEmpty() || service.getStudents().isEmpty()) { error("Add at least one book and one student first."); return; }
        JComboBox<String> books = new JComboBox<>(), students = new JComboBox<>();
        for(Book b:service.getBooks()) if(b.availableCopies>0) books.addItem(b.id+" - "+b.title+" (available: "+b.availableCopies+")");
        for(Student s:service.getStudents()) students.addItem(s.id+" - "+s.name+" ("+s.rollNo+")");
        if(books.getItemCount()==0){error("No book copies are currently available.");return;}
        JPanel form = form("Book",books,"Student",students);
        if(JOptionPane.showConfirmDialog(this,form,"Issue Book",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
            try { int bookId=Integer.parseInt(books.getSelectedItem().toString().split(" - ")[0]); int studentId=Integer.parseInt(students.getSelectedItem().toString().split(" - ")[0]); IssueRecord r=service.issueBook(bookId,studentId); fillIssues(model); JOptionPane.showMessageDialog(this,"Book issued successfully.\nDue date: "+r.dueDate); } catch(Exception ex){error(ex.getMessage());}
        }
    }

    private void returnIssue(JTable table, DefaultTableModel model) {
        int row=table.getSelectedRow(); if(row<0){error("Select an issued record first.");return;}
        int id=(Integer)model.getValueAt(row,0);
        try { double fine=service.returnBook(id); fillIssues(model); if(fine>0) JOptionPane.showMessageDialog(this,String.format("Book returned. Late fine: ₹%.2f\nUse Payments & Fines to record a virtual payment.",fine)); else JOptionPane.showMessageDialog(this,"Book returned successfully. No fine."); } catch(Exception ex){error(ex.getMessage());}
    }

    private JPanel createPaymentsPanel() {
        JPanel p=basePanel("Payments & Fines");
        String[] cols={"Payment ID","Issue ID","Student","Amount","Date","Method","Status"};
        DefaultTableModel model=new DefaultTableModel(cols,0){public boolean isCellEditable(int r,int c){return false;}}; JTable table=new JTable(model); fillPayments(model);
        JButton calculate=new JButton("Check Fine"), pay=new JButton("Record Virtual Payment");
        calculate.addActionListener(e->checkFineDialog()); pay.addActionListener(e->paymentDialog(model));
        JPanel buttons=new JPanel(new FlowLayout(FlowLayout.LEFT));buttons.add(calculate);buttons.add(pay);
        JLabel note=new JLabel("Payments are virtual records only — no real money or payment gateway is connected."); note.setBorder(new EmptyBorder(5,0,5,0));
        JPanel bottom=new JPanel(new BorderLayout());bottom.add(buttons,BorderLayout.WEST);bottom.add(note,BorderLayout.CENTER);
        p.add(new JScrollPane(table),BorderLayout.CENTER);p.add(bottom,BorderLayout.SOUTH);return p;
    }

    private void fillPayments(DefaultTableModel model){model.setRowCount(0);for(Payment pay:service.getPayments()){Student s=service.findStudent(pay.studentId);model.addRow(new Object[]{pay.id,pay.issueId,s==null?"Unknown":s.name,String.format("₹%.2f",pay.amount),pay.date,pay.method,pay.status});}}

    private void checkFineDialog(){
        if(service.getIssues().isEmpty()){error("No issue records available.");return;}
        String[] options=new String[service.getIssues().size()];int x=0;for(IssueRecord i:service.getIssues()){Book b=service.findBook(i.bookId);Student s=service.findStudent(i.studentId);options[x++]=i.id+" - "+(b==null?"Unknown":b.title)+" / "+(s==null?"Unknown":s.name);}
        JComboBox<String> box=new JComboBox<>(options);
        if(JOptionPane.showConfirmDialog(this,form("Issue",box),"Check Fine",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){int id=Integer.parseInt(box.getSelectedItem().toString().split(" - ")[0]);IssueRecord i=service.findIssue(id);JOptionPane.showMessageDialog(this,String.format("Current fine: ₹%.2f\nDue date: %s",service.calculateFine(i),i.dueDate));}
    }

    private void paymentDialog(DefaultTableModel model){
        if(service.getIssues().isEmpty()){error("No issue records available.");return;}
        String[] options=new String[service.getIssues().size()];int x=0;for(IssueRecord i:service.getIssues()){Student s=service.findStudent(i.studentId);options[x++]=i.id+" - "+(s==null?"Unknown":s.name)+" - Fine ₹"+String.format("%.2f",service.calculateFine(i));}
        JComboBox<String> issueBox=new JComboBox<>(options); JTextField amount=new JTextField(); JComboBox<String> method=new JComboBox<>(new String[]{"Cash (Virtual)","UPI (Virtual)","Card (Virtual)","Other (Virtual)"});
        JPanel form=form("Issue",issueBox,"Amount",amount,"Method",method);
        if(JOptionPane.showConfirmDialog(this,form,"Record Virtual Payment",JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){try{int id=Integer.parseInt(issueBox.getSelectedItem().toString().split(" - ")[0]);double a=Double.parseDouble(amount.getText().trim());if(a<=0)throw new NumberFormatException();service.recordVirtualPayment(id,a,method.getSelectedItem().toString());fillPayments(model);JOptionPane.showMessageDialog(this,"Virtual payment recorded successfully.");}catch(Exception ex){error("Enter a valid positive amount.");}}
    }

    private JPanel createReportsPanel(){
        JPanel p=basePanel("Reports");
        JTextArea area=new JTextArea();area.setEditable(false);area.setFont(new Font("Monospaced",Font.PLAIN,14));
        JButton refresh=new JButton("Refresh Reports");refresh.addActionListener(e->area.setText(buildReport()));
        area.setText(buildReport());p.add(new JScrollPane(area),BorderLayout.CENTER);p.add(refresh,BorderLayout.SOUTH);return p;
    }

    private String buildReport(){
        StringBuilder r=new StringBuilder();r.append("LIBRARY REPORT - ").append(service.getLibraryName()).append("\n");r.append("Generated: ").append(LocalDate.now()).append("\n");r.append("=".repeat(70)).append("\n\n");
        r.append("BOOK SUMMARY\nTotal titles: ").append(service.getBooks().size()).append("\nTotal copies: ").append(service.getBooks().stream().mapToInt(b->b.totalCopies).sum()).append("\nAvailable copies: ").append(service.countAvailableBooks()).append("\nIssued copies: ").append(service.countIssued()).append("\n\n");
        r.append("ISSUE RECORDS\n");for(IssueRecord i:service.getIssues()){Book b=service.findBook(i.bookId);Student s=service.findStudent(i.studentId);r.append("#").append(i.id).append(" | ").append(b==null?"Unknown":b.title).append(" | ").append(s==null?"Unknown":s.name).append(" | ").append(i.status).append(" | Fine ₹").append(String.format("%.2f",service.calculateFine(i))).append("\n");}
        r.append("\nPAYMENT RECORDS\n");double total=0;for(Payment pay:service.getPayments()){total+=pay.amount;r.append("#").append(pay.id).append(" | Issue ").append(pay.issueId).append(" | ₹").append(String.format("%.2f",pay.amount)).append(" | ").append(pay.method).append(" | ").append(pay.date).append("\n");}r.append("Total virtual payments recorded: ₹").append(String.format("%.2f",total)).append("\n");return r.toString();
    }

    private JPanel createSettingsPanel(){
        JPanel p=basePanel("Settings");
        JTextField name=new JTextField(service.getLibraryName()), fine=new JTextField(String.valueOf(service.getFinePerDay())), duration=new JTextField(String.valueOf(service.getIssueDuration()));
        JPanel form=form("Library Name",name,"Fine Per Day (₹)",fine,"Issue Duration (days)",duration);
        JButton save=new JButton("Save Settings");save.addActionListener(e->{try{double f=Double.parseDouble(fine.getText().trim());int d=Integer.parseInt(duration.getText().trim());if(f<0||d<1)throw new NumberFormatException();service.saveSettings(name.getText().trim(),f,d);libraryTitle.setText(service.getLibraryName());JOptionPane.showMessageDialog(this,"Settings saved.");}catch(Exception ex){error("Enter valid fine and duration values.");}});
        JPanel center=new JPanel(new BorderLayout());center.add(form,BorderLayout.NORTH);center.add(save,BorderLayout.SOUTH);p.add(center,BorderLayout.NORTH);return p;
    }

    private JPanel form(Object... fields){
        JPanel p=new JPanel(new GridLayout(fields.length/2,2,8,8));
        for(int i=0;i<fields.length;i+=2){p.add(new JLabel(String.valueOf(fields[i])+":"));p.add((Component)fields[i+1]);}p.setBorder(new EmptyBorder(10,10,10,10));return p;
    }

    private void error(String message){JOptionPane.showMessageDialog(this,message,"Error",JOptionPane.ERROR_MESSAGE);}
}
