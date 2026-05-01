# Employee Management System

A Java Swing application for managing employee data using an SQLite database.

## 🚀 Getting Started for the Team

Since the project libraries and local database are excluded from this repository for performance, follow these steps to get the app running:

### 1. Prerequisites
*   Ensure you have **Java 17** (JDK) installed.
*   Make sure the **Language Support for Java** extension is installed in VS Code.

### 2. Setup Libraries
1.  Obtain the `lib/` folder containing the 3 JAR files (JDBC and SLF4J) from the team lead.
2.  In VS Code, go to the **Java Projects** view (bottom of the sidebar).
3.  Click the **+** icon next to **Referenced Libraries**.
4.  Select all three JAR files inside the `lib/` folder.

### 3. Database Initialization
The application is configured to use a relative path (`db/ems.db`). The first time you run the program, the SQLite driver will automatically create the database file if it doesn't exist.

## 🛠️ Project Structure
*   `app/`: Contains the `Main` entry point.
*   `dao/`: Data Access Object logic for SQLite.
*   `gui/`: Swing-based user interface.
*   `employee/`: Logic for employee models/categories.
*   `interfaces/`: Shared project interfaces.