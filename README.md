# Employee Management System (EMS) Documentation

## 1. Project Summary

**Project Title:** Employee Management System (EMS)

**University:** Adama Science and Technology University (ASTU)

**Group Members:**

- Shimellis Regassa — Ugr/37774/17
- Kumala Adugna — Ugr/37298/17
- Merga Adam — Ugr/37391/17
- Fedawak Hailu — Ugr/36856/17
- Midaga Buzuna — Ugr/37416/17

**Instructor:** Mr. Megersa Daraje

**Version:** 1.1.0 (High-Capacity Data Handling)

## 2. Objective

The EMS is a Java-based application designed to demonstrate mastery of robust software architecture and Object-Oriented Programming (OOP) principles. It efficiently manages multiple employee types — including Full-Time, Part-Time, and Interns — while performing critical business tasks such as salary calculation and record persistence. The system is engineered for stability and specifically optimized to maintain high performance when managing extensive datasets.

## 3. Technical Stack

- **Language:** Java 17
- **GUI Framework:** Java Swing
- **Database:** SQLite (Relational Persistence)
- **Logic Architecture:** Object-Oriented Programming (OOP)

## 4. Technical Features & Optimization

### 4.1 High-Performance Search & Indexing

To ensure that search results remain instantaneous even as the database grows to hold extensive entries, a database index was implemented on the `name` column. This offloads the heavy lifting of sorting and searching to the SQLite engine rather than the Java application memory.

```java
// Performance Index: Created during initialization to speed up name-based queries
String indexSql = "CREATE INDEX IF NOT EXISTS idx_name ON employees(name)";
conn.createStatement().execute(indexSql);

// Optimized Search Query with Database-Level Sorting
@Override
public List<Employee> search(String query) {
    String sql = "SELECT * FROM employees WHERE id = ? OR name LIKE ? ORDER BY name ASC";
    // Logic handles filtering and sorting within the database engine to maintain UI speed
}
```

### 4.2 Data Visibility Toggle & Resource Management

The system includes a toggle mechanism for the `Refresh Table` button. This is a deliberate memory management choice that allows users to clear the UI table, freeing up Java Virtual Machine (JVM) resources and reducing UI clutter during heavy data entry or retrieval tasks.

```java
private void toggleTable() {
    if (btnShow.getText().equals("Hide Records")) {
        model.setRowCount(0); // Explicitly clears the UI model to save memory
        btnShow.setText("Refresh Table");
        lblStatus.setText("Records hidden. Search to find an employee.");
    } else {
        refreshTable(); // Re-fetches data from the persistence layer
    }
}
```

## 5. Comprehensive OOP Design and Principles

### 5.1 Encapsulation

The system maintains strict data security by hiding the internal state of objects.

- **Data Hiding:** All fields in classes like `Employee`, `Address`, and `Department` are kept private.
- **Controlled Access:** Access is only provided through public constructors and standardized getters/setters, ensuring that data validation occurs before any field is modified.

```java
public class Employee {
    private String id;
    private String name;
    private double salary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        }
    }
}
```

### 5.2 Inheritance & Generalization

A hierarchical structure was implemented to maximize code reuse.

- **Base Class:** The `Employee` abstract class defines shared attributes such as `id`, `name`, and `department`.
- **Specialization:** Subclasses like `FullTimeEmployee` and `PartTimeEmployee` extend this base, inheriting core logic while adding specialized fields.

```java
public abstract class Employee implements Payable {
    protected String id;
    protected String name;
    // Common logic for all employees
}

public class PartTimeEmployee extends Employee {
    private double hourlyRate;
    private int hoursWorked;
    // Specific logic for part-time staff
}
```

### 5.3 Polymorphism (Dynamic Binding)

Polymorphism allows the system to process a variety of employee types through a single interface.

- **Method Overriding:** Each subclass provides a unique implementation of `calculateSalary()`.
- **Runtime Execution:** The application stores diverse objects in a `List<Employee>`. At runtime, the JVM automatically invokes the correct salary logic based on the actual object type.

```java
private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
    String type = rs.getString("type");
    // Polymorphic instantiation based on the database 'type' value
    if ("Part-Time".equalsIgnoreCase(type)) {
        return new PartTimeEmployee(rs.getString("id"), rs.getString("name"), null, d, rs.getDouble("salary"), 0);
    } else if ("Intern".equalsIgnoreCase(type)) {
        return new Intern(rs.getString("id"), rs.getString("name"), null, d, rs.getDouble("salary"));
    } else {
        return new FullTimeEmployee(rs.getString("id"), rs.getString("name"), null, d, rs.getDouble("salary"));
    }
}
```

### 5.4 Abstraction & Interfaces

- **Payable Interface:** Defines the contract for any entity that receives a salary, ensuring consistent behavior across the application.
- **Abstract Classes:** The `Employee` class is marked abstract to prevent the creation of a generic employee, enforcing the rule that every record must have a specific category.

```java
public interface Payable {
    double calculateSalary();
}
```

### 5.5 Aggregation

The design uses aggregation to represent “has-a” relationships. An `Employee` contains references to `Address` and `Department` objects. This modularity allows a `Department` to exist independently of an `Employee`, reflecting real-world business logic.

## 6. Advanced Java Implementation

### 6.1 Graphical User Interface (GUI)

The interface is built using Java Swing with a focus on Event-Driven Programming:

- **User Feedback:** `ActionListeners` capture clicks and provide real-time updates via status labels.
- **Layout Management:** The system uses layout managers rather than absolute positioning, ensuring the GUI remains consistent across different operating systems.

### 6.2 Robust Exception Handling

Defensive programming ensures the application does not crash during database or logic errors.

- **Try-With-Resources:** Used in the DAO layer to ensure database connections are automatically closed, preventing memory leaks.
- **Graceful Degradation:** When a database error occurs (like a duplicate ID), the system catches the `SQLException` and alerts the user via a dialog box instead of terminating the program.

```java
try (Connection conn = DriverManager.getConnection(URL);
     PreparedStatement pstmt = conn.prepareStatement(sql)) {
    // Database operations here
} catch (SQLException e) {
    lblStatus.setText("Database Error: " + e.getMessage());
    e.printStackTrace();
}
```

### 6.3 File and I/O (Input/Output)

The system manages its own environment setup.

- **Automated Setup:** Upon launch, `java.io.File` checks for the existence of the `/db` directory. If missing, the application automatically creates the directory and initializes the database file.
- **Relational Persistence:** While using SQL, the system manages the physical database file on disk, ensuring that all entries are preserved permanently.

## 7. Package Structure

The code is organized into modular packages to ensure a clean separation of concerns:

- `app`: Main entry point.
- `dao`: Persistence and database logic.
- `gui`: User interface and event handling.
- `employee`: Core business models.
- `interfaces`: Contractual definitions.

## 8. Database Design & Integrity

- **Schema:** `id` (PK), `name` (Indexed), `type`, `dept`, `salary`.
- **Security:** The use of prepared statements prevents SQL injection attacks and ensures that data types are handled correctly during extensive data entry.

## 9. Key Strengths

- **Scalability:** Optimized for high-capacity data management without performance loss.
- **Robustness:** Effective use of exception handling to manage runtime errors gracefully.
- **Modular Architecture:** High adherence to OOP principles, making the system easy to maintain and expand.
