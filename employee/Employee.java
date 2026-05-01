package employee;

import interfaces.Payable;
import java.io.Serializable;

public abstract class Employee implements Payable, Serializable {
    protected String id;
    protected String name;
    protected Address address;
    protected Department department;
    protected double baseSalary;

    public Employee(String id, String name, Address address, Department department, double baseSalary) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.department = department;
        this.baseSalary = baseSalary;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public Department getDepartment() { return department; }
    public void setName(String name) { this.name = name; }
    public void setDepartment(Department department) { this.department = department; }

    public abstract String getType();
}