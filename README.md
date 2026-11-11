# Library-Management-System
A beginner-friendly Library Management System built with Java and Java Swing. It manages books, students, book issue/return, due dates, fines, virtual payments, reports, and settings using simple file-based storage without SQL or authentication.

# 📚 Library Management System

A beginner-friendly **Library Management System** developed using **Java and Java Swing**. This project provides a simple graphical interface for managing books, students, book issue/return records, fines, virtual payments, reports, and library settings.

## ✨ Features

* 📊 **Dashboard** – View books, students, issued books, and available books.
* 📚 **Book Management** – Add, edit, delete, search, and view books.
* 👨‍🎓 **Student Management** – Add, edit, delete, and view student records.
* 🔄 **Issue & Return** – Issue books, return books, and track due dates.
* 💰 **Fines & Payments** – Calculate late fines and record virtual payments.
* 📋 **Reports** – View issue, return, fine, and payment records.
* ⚙️ **Settings** – Configure library name, fine per day, and issue duration.

## 🛠️ Technologies Used

* **Java**
* **Java Swing** – Graphical User Interface
* **File Handling** – Data storage
* **VS Code** – Development Environment

## 💾 Data Storage

This project uses **simple text files for data storage** instead of a SQL database.

All records are stored inside the `data` folder:

```text
data/
├── books.txt
├── students.txt
├── issues.txt
├── payments.txt
└── settings.txt
```

This approach keeps the project simple and easy to understand for beginners.

## 📁 Project Structure

```text
LibraryManagementSystem/
│
├── src/
│   └── main/
│       └── java/
│           └── librarymanagement/
│               ├── model/
│               ├── service/
│               ├── ui/
│               ├── util/
│               └── Main.java
│
├── data/
├── docs/
├── .vscode/
├── run.bat
├── run.sh
└── README.md
```

## ▶️ How to Run

### Requirements

* **JDK 17 or later**
* **VS Code** (recommended)

### Steps

1. Clone or download this repository.
2. Open the `LibraryManagementSystem` folder in VS Code.
3. Open the terminal in the project root folder.
4. Run:

```powershell
.\run.bat
```

The script automatically compiles the project and launches the application.

## 🔐 Authentication

This project intentionally **does not include login or authentication**, keeping it suitable for a beginner-level academic project.

## 💳 Payment System

Payments are **virtual records only**. No real money or online payment gateway is involved.

## 🎯 Project Objective

The objective of this project is to understand and apply fundamental concepts of:

* Object-Oriented Programming
* Java GUI development
* File handling
* CRUD operations
* Basic software architecture
* Data management

## 🚀 Future Improvements

* MySQL/database integration
* User authentication
* Book reservation
* Email notifications
* Advanced reports and analytics
* Online/cloud storage

## 👨‍💻 Project Type

**Academic / Beginner-Level B.Tech Project**

---

⭐ If you find this project useful, consider giving it a star!
