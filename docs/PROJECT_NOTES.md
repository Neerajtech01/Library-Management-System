# Project Notes for Viva

## Why this design?
- Java Swing keeps the project completely Java-based.
- Text files are easier for a beginner to understand than setting up MySQL.
- No authentication keeps the scope small.
- Payments are virtual records, so there is no real financial transaction.

## Basic OOP used
- **Classes/Objects:** Book, Student, IssueRecord, Payment.
- **Encapsulation by organization:** service methods handle library operations.
- **Abstraction:** UI calls simple methods such as `issueBook()` and `returnBook()` instead of handling file operations itself.
- **Collections:** `ArrayList` stores records while the program is running.
- **File handling:** records are saved in the `data` folder.

## Main workflow
1. Add a book and student.
2. Issue a book to the student.
3. The available copy count decreases.
4. The system calculates a due date from Settings.
5. Return the book.
6. If it is late, the system calculates the fine.
7. Record a virtual payment if needed.
8. View the result in Reports.
