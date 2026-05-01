package dao;

import employee.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLiteEmployeeDAO: Implements the EmployeeDAO interface using SQLite.
 * It handles all CRUD (Create, Read, Update, Delete) operations for the database.
 */
public class SQLiteEmployeeDAO implements EmployeeDAO {
    // Database connection string using a relative path for portability.
    private final String URL = "jdbc:sqlite:db/ems.db";

    /**
     * Constructor: Initializes the database, loads the driver, 
     * and creates the employees table if it doesn't already exist.
     */
    public SQLiteEmployeeDAO() {
        try {
            // Load the JDBC driver and ensure the database directory exists.
            Class.forName("org.sqlite.JDBC");
            new java.io.File("db").mkdirs();
            
            // Connection block to initialize the table structure.
            try (Connection conn = DriverManager.getConnection(URL)) {
                String sql = "CREATE TABLE IF NOT EXISTS employees (" +
                             "id TEXT PRIMARY KEY, " +
                             "name TEXT, " +
                             "type TEXT, " +
                             "dept TEXT, " +
                             "salary REAL)";
                conn.createStatement().execute(sql);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("CRITICAL: SQLite Driver not found!");
        } catch (SQLException e) {
            System.err.println("Database Init Error: " + e.getMessage());
        }
    }

    /**
     * add(): Persists an Employee object into the database.
     * Uses PreparedStatement to prevent SQL Injection attacks.
     */
    @Override
    public void add(Employee e) {
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement("INSERT INTO employees VALUES(?,?,?,?,?)")) {
            
            pstmt.setString(1, e.getId());
            pstmt.setString(2, e.getName());
            pstmt.setString(3, e.getType());
            pstmt.setString(4, e.getDepartment().getName());
            pstmt.setDouble(5, e.calculateSalary());
            
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Insert Error: " + ex.getMessage());
        }
    }

    /**
     * getAll(): Retrieves all records from the employees table 
     * and converts them back into a List of Employee objects.
     */
    @Override
    public List<Employee> getAll() {
        List<Employee> list = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {
            
            while (rs.next()) {
                list.add(mapResultSetToEmployee(rs));
            }
        } catch (SQLException ex) { 
            System.err.println("Select Error: " + ex.getMessage());
        }
        return list;
    }

    /**
     * delete(): Removes a specific employee record based on their unique ID.
     */
    @Override
    public void delete(String id) {
        String sql = "DELETE FROM employees WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            System.err.println("Delete Error: " + ex.getMessage());
        }
    }

    /**
     * findById(): Searches for a single employee record by ID.
     */
    @Override 
    public Employee findById(String id) {
        String sql = "SELECT * FROM employees WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToEmployee(rs);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Find Error: " + ex.getMessage());
        }
        return null;
    }

    /**
     * mapResultSetToEmployee(): A helper method that converts a Database Row (ResultSet)
     * back into the appropriate Java Object (Polymorphism).
     */
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        Department d = new Department(rs.getString("dept"));
        String id = rs.getString("id");
        String name = rs.getString("name");
        double salary = rs.getDouble("salary");

        // Logic to determine which specific subclass to instantiate.
        if ("Part-Time".equalsIgnoreCase(type)) {
            return new PartTimeEmployee(id, name, null, d, salary, 0);
        } else if ("Intern".equalsIgnoreCase(type)) {
            return new Intern(id, name, null, d, salary);
        } else {
            return new FullTimeEmployee(id, name, null, d, salary);
        }
    }
}