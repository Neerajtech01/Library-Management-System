# Beginner Library Management System (Java)

A simple **B.Tech AIML beginner-level Library Management System** made using only Java Swing and file storage.

## Features
1. Dashboard - total books, students, issued books and available books.
2. Book Management - add, edit, delete, search and view books.
3. Student Management - add, edit, delete and view student records.
4. Book Issue & Return - issue books, return books and track due dates.
5. Payments & Fines - calculate late fees and record **virtual payments**. No real money/payment gateway is used.
6. Reports - issued books, returned books and fine/payment records.
7. Settings - library name, fine per day and issue duration.

## Technology
- Java 17+
- Java Swing (GUI)
- File storage using `.txt`/CSV-style files
- No SQL database
- No login/authentication
- No external libraries

## Run in VS Code
1. Install JDK 17 or newer.
2. Install the **Extension Pack for Java** in VS Code.
3. Open the `LibraryManagementSystem` folder in VS Code.
4. Open `src/main/java/librarymanagement/Main.java`.
5. Click **Run** above `main()`.

### Terminal option
From the project folder:

```bash
javac -d out src/main/java/librarymanagement/*.java src/main/java/librarymanagement/model/*.java src/main/java/librarymanagement/service/*.java src/main/java/librarymanagement/ui/*.java src/main/java/librarymanagement/util/*.java
java -cp out librarymanagement.Main
```

## Data storage
All records are saved automatically in the `data` folder:
- `books.txt`
- `students.txt`
- `issues.txt`
- `payments.txt`
- `settings.txt`

You can open these files with a normal text editor. This makes the project easy to understand for a beginner and avoids database setup.

## Important project rule
Payments are **virtual records only**. The system records that a fine was paid; it does not connect to UPI, cards, banks, or any real payment service.
