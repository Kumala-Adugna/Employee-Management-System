package employee;

import interfaces.Payable;
import java.io.Serializable;

/**
 * Employee: The base abstract class representing the blueprint for all employees.
 * It implements Payable for salary logic and Serializable for object data handling.
 */
public abstract class Employee implements Payable, Serializable {
    // Protected fields: Accessible by subclasses (Inheritance) but hidden from the public.
    protected String id;
    protected String name;
    protected Address address;
    protected Department department;
    protected double baseSalary;

    /**
     * Constructor: Initializes the common attributes shared by all employee types.
     */
    public Employee(String id, String name, Address address, Department department, double baseSalary) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.department = department;
        this.baseSalary = baseSalary;
    }

    // Getters: Provide read access to encapsulated fields.
    public String getId() { return id; }
    public String getName() { return name; }
    public Department getDepartment() { return department; }
    
    // Setters: Provide controlled write access to encapsulated fields.
    public void setName(String name) { this.name = name; }
    public void setDepartment(Department department) { this.department = department; }

    /**
     * abstract String getType(): Forces all subclasses to define their specific 
     * employee type (e.g., Full-Time, Intern).
     */
    public abstract String getType();
}