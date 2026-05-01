package dao;

import employee.Employee;
import java.util.List;

/**
 * EmployeeDAO Interface: Defines the standard contract for data operations.
 * This follows the DAO Design Pattern, which separates the business logic 
 * from the data persistence logic (Abstraction).
 */
public interface EmployeeDAO {
    
    /**
     * add: Contract for saving a new employee record.
     */
    void add(Employee e);
    
    /**
     * getAll: Contract for retrieving all employee records as a List.
     */
    List<Employee> getAll();
    
    /**
     * delete: Contract for removing a record based on its unique identifier.
     */
    void delete(String id);
    
    /**
     * findById: Contract for searching for a specific record by its ID.
     */
    Employee findById(String id);
}