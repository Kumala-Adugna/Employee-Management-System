package dao;

import employee.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLiteEmployeeDAO implements EmployeeDAO {
    private final String URL = "jdbc:sqlite:/home/kumala-adugna/Employee Management System/db/ems.db";

    public SQLiteEmployeeDAO() {
        try {
            Class.forName("org.sqlite.JDBC");
            new java.io.File("/home/kumala-adugna/Employee Management System/db").mkdirs();
            
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

    // Helper method to avoid duplicating logic in getAll and findById
    private Employee mapResultSetToEmployee(ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        Department d = new Department(rs.getString("dept"));
        String id = rs.getString("id");
        String name = rs.getString("name");
        double salary = rs.getDouble("salary");

        if ("Part-Time".equalsIgnoreCase(type)) {
            return new PartTimeEmployee(id, name, null, d, salary, 0);
        } else if ("Intern".equalsIgnoreCase(type)) {
            return new Intern(id, name, null, d, salary);
        } else {
            return new FullTimeEmployee(id, name, null, d, salary);
        }
    }
}