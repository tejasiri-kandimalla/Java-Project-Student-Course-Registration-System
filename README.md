# 🎓 Student Course Registration System (SCRS)

A robust, Java Swing-based desktop application designed to streamline the academic course enrollment process. SCRS provides a unified platform for students to register for courses and for administrators to manage the academic catalog efficiently.

---

## ✨ Features

### 👨‍💼 Administrator Portal
*   **Student Management**: Add new students (automatic account creation), view profiles, and manage records.
*   **Course Management**: Define new courses, set credit limits, and assign semesters.
*   **Enrollment Tracking**: Monitor all student-course registrations in real-time.
*   **Data Oversight**: Search, filter, and delete student or course records.

### 🎓 Student Portal
*   **Secure Authentication**: Role-based login system for students and admins.
*   **Profile View**: Access personal academic and contact details.
*   **Live Registration**: Browse available courses and enroll with a single click.
*   **Schedule Management**: View registered courses for the current semester.

---

## 🛠️ Tech Stack
*   **Frontend**: Java Swing & AWT (Desktop UI)
*   **Backend**: Java 8 or higher
*   **Database**: MySQL (Relational Schema)
*   **Driver**: MySQL Connector/J 9.6.0

---

## 🖥️ System Requirements
Before setting up the project, ensure you have the following installed:
1.  **Java Development Kit (JDK)**: Version 8 or later.
2.  **MySQL Server**: Version 5.7 or higher (MySQL 8 recommended).
3.  **Command Line**: PowerShell or CMD (for running scripts).

---

## 🚀 Setup Instructions (New System)

Follow these steps to get the project running on a fresh environment:

### 1. Database Configuration
1.  Open your MySQL terminal or a GUI tool like MySQL Workbench.
2.  Locate the `database.sql` file in the project root.
3.  Run the script to create the database and tables:
    ```sql
    SOURCE m:/4 sem/StudentCourseRegistration/database.sql;
    ```
    *Note: The default credentials created are `admin`/`12345` for Admin and `S1001`/`12345` for Student.*

### 2. Update Connection Credentials
1.  Navigate to `src/studentcourseregistrationsystem/DatabaseConnection.java`.
2.  Modify the `USER` and `PASS` constants to match your MySQL setup:
    ```java
    private static final String USER = "your_username";
    private static final String PASS = "your_password";
    ```

### 3. Verify Libraries
Ensure that the MySQL JDBC driver is present in the `lib` folder:
*   `lib/mysql-connector-j-9.6.0.jar`

### 4. Compiling & Running
The project includes a `run.bat` file for automated execution on Windows.
1.  Open a terminal in the project directory.
2.  Execute the batch file:
    ```cmd
    .\run.bat
    ```
    *This script handles compilation, resource copying (icons), and running the application.*

---

## 📂 Project Structure
```text
StudentCourseRegistration/
├── bin/                       # Compiled class files
├── lib/                       # Third-party JARs (MySQL Connector)
├── src/
│   ├── icons/                 # UI assets (png/jpg)
│   ├── studentcourseregistrationsystem/
│   │   ├── Main.java          # Entry Point
│   │   └── DatabaseConnection.java
│   └── user/                  # Dashboards and Panels
│       ├── AdminDashboard.java
│       ├── StudentDashboard.java
│       └── ...
├── database.sql               # Database schema & sample data
└── run.bat                    # Automation script
```

---

## 🔑 Default Login Credentials
| Role | Username | Password |
| :--- | :--- | :--- |
| **Admin** | `admin` | `12345` |
| **Student** | `S1001` | `12345` |

---
*Developed as a project for the 4th Semester Student Course Registration System.*
