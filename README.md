# Employee Management System

A professional Java Swing application for managing employee data using an SQLite database, built with a focus on modular architecture and clean Object-Oriented Programming (OOP) principles.

## 🚀 Getting Started for the Team

Since the project libraries and local database are excluded from this repository for performance and collaboration safety, follow these steps to get the app running:

### 1. Prerequisites
*   Ensure you have **Java 17** (JDK) or higher installed.
*   Make sure the **Language Support for Java** extension is installed in VS Code.

### 2. Setup Libraries
1.  Obtain the `lib/` folder containing the 3 JAR files (JDBC and SLF4J) from the team lead.
2.  In VS Code, go to the **Java Projects** view (bottom of the sidebar).
3.  Click the **+** icon next to **Referenced Libraries**.
4.  Select all three JAR files inside the `lib/` folder:
    *   `sqlite-jdbc-3.45.3.0.jar`
    *   `slf4j-api-2.0.9.jar`
    *   `slf4j-simple-2.0.9.jar`

### 3. Database Initialization
The application is configured to use a relative path (`db/ems.db`). The first time you run the program, the SQLite driver will automatically create the database file if it doesn't exist.

---

## 👥 Team Information: G1
*   **Kumala Adugna**
*   **Shimellis Regassa**
*   **Merga Adam**
*   **Fedawak Hailu**
*   **Midaga Buzuna**

**Date:** May 2026  
**Version:** 1.0.0

---

## 🛠️ Project Structure & Architecture
The project is logically partitioned into **Packages** to ensure high cohesion and low coupling.

*   `app/`: Contains the `Main` entry point.
*   `dao/`: Data Access Object logic for SQLite (Separates SQL from UI).
*   `gui/`: Swing-based user interface (The View layer).
*   `employee/`: Logic for employee models, categories, and attributes.
*   `interfaces/`: Shared project contracts and standards.

---

## 🧩 Core OOP Concepts Applied

### 1. Encapsulation
All data fields (ID, Name, Salary) are marked `private`. Access is strictly controlled via public constructors and getter/setter methods to prevent unauthorized data corruption.

### 2. Inheritance
We implemented a hierarchical tree where `FullTimeEmployee`, `PartTimeEmployee`, and `Intern` inherit core attributes from the `Employee` base class, promoting code reuse.

### 3. Polymorphism
The system treats all staff as `Employee` objects but dynamically executes the specific `calculateSalary()` logic of the subclass at runtime (Dynamic Binding).

### 4. Abstraction
We used **Interfaces** (like `Payable`) and **Abstract Classes** (like `Employee`) to define "what" the system does while hiding the "how" (complex math or SQL queries) from the end-user.

### 5. Aggregation
The `Employee` class maintains a "Has-A" relationship with `Address` and `Department`. This modular design allows these components to exist independently.

---

## 📋 Class Directory

| Component | Category | Description |
| :--- | :--- | :--- |
| `Payable` | **Interface** | Standardizes payroll calculation across the entire system. |
| `Employee` | **Abstract Class** | The foundational blueprint for all staff types. |
| `FullTimeEmployee` | **Subclass** | Implements fixed-rate monthly payroll logic. |
| `PartTimeEmployee` | **Subclass** | Implements hourly-based payroll logic (Rate × Hours). |
| `Intern` | **Subclass** | Implements stipend-based logic for temporary staff. |
| `Address` | **Aggregation** | Encapsulates location data (Street, City). |
| `EmployeeDAO` | **Interface** | Abstraction layer for database operations. |
| `SQLiteEmployeeDAO` | **Implementation** | Handles JDBC communication and SQL execution for `ems.db`. |
| `EmployeeManagementGUI` | **GUI** | Swing-based window for user interaction and data display. |
| `Main` | **Entry Point** | Safely launches the GUI on the Event Dispatch Thread (EDT). |

---

## 💾 Technical Environment
*   **Database:** SQLite (Serverless, file-based persistence).
*   **Driver:** JDBC (Java Database Connectivity).
*   **Version Control:** `.gitignore` is configured to exclude compiled `.class` files and the local `ems.db` instance to ensure smooth collaboration.