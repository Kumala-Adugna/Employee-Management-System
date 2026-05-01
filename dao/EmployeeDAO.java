package dao;

import employee.Employee;
import java.util.List;

public interface EmployeeDAO {
    void add(Employee e);
    List<Employee> getAll();
    void delete(String id);
    Employee findById(String id);
}