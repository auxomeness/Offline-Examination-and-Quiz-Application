# Offline Examination and Quiz Application

A Java-based desktop application for conducting offline examinations and quizzes. The system supports multiple user roles including administrators, instructors, and students, allowing full quiz lifecycle management without requiring an internet connection.

---

## Overview

This application is designed to:

* Create and manage quiz questions
* Control user access to exams
* Allow students to take quizzes offline
* Automatically calculate and store results

It uses Java Swing for the graphical user interface and local data handling for persistence.

---

## Project Structure

```text
Offline Examination and Quiz Application/
└── src/main/
    ├── AdminFrame.java
    ├── InstructorFrame.java
    ├── StudentLoginFrame.java
    ├── StartFrame.java
    ├── QuizFrame.java
    ├── QuizApp.java
    ├── ManageQuestionsPanel.java
    ├── ManageAccessPanel.java
    ├── QuestionEditorDialog.java
    ├── DataStore.java
    ├── Question.java
    ├── Result.java
    └── (compiled .class files)
```

---

## Features

### Administrator

* Manage user access
* Control system-level configurations

### Instructor

* Create, edit, and delete questions
* Manage quiz content
* Control exam availability

### Student

* Secure login interface
* Take quizzes in a controlled environment
* Automatic scoring and result display

---

## Core Components

* **QuizApp.java** – Entry point of the application
* **StartFrame.java** – Main navigation screen
* **AdminFrame.java** – Admin dashboard
* **InstructorFrame.java** – Instructor dashboard
* **StudentLoginFrame.java** – Student authentication
* **QuizFrame.java** – Quiz-taking interface
* **ManageQuestionsPanel.java** – Question management
* **ManageAccessPanel.java** – Access control
* **QuestionEditorDialog.java** – UI for editing questions
* **DataStore.java** – Handles data storage
* **Question.java** – Question model
* **Result.java** – Stores quiz results

---

## Requirements

* Java Development Kit (JDK) 8 or higher
* Any Java-compatible IDE (IntelliJ IDEA, Eclipse, NetBeans) or terminal

---

## Compilation

Navigate to the `src/main` directory and run:

```bash
javac *.java
```

---

## Running the Application

After compilation, run:

```bash
java QuizApp
```

---

## How to Use

1. Launch the application
2. Select user role:

   * Administrator
   * Instructor
   * Student
3. Perform actions based on role:

   * Admin: Manage access
   * Instructor: Create and manage quizzes
   * Student: Log in and take quiz

---

## Data Handling

* Data is stored locally using the `DataStore` class
* No internet connection is required
* All results and questions persist within the application environment

---

## Limitations

* No database integration (uses local storage only)
* No encryption for stored data
* UI is based on Java Swing
* Not designed for distributed or online use

---

## Possible Improvements

* Add database support (e.g., MySQL or SQLite)
* Implement user authentication with encryption
* Improve UI/UX with modern frameworks (JavaFX)
* Add timer functionality for quizzes
* Export results to CSV or PDF
* Multi-user concurrency support

---

## License

This project is licensed under the **GNU General Public License v3.0 (GPL-3.0)**.

You are free to:

* Use
* Modify
* Distribute

Under the condition that:

* Any derivative work must also be licensed under GPL v3
* Source code must be made available
