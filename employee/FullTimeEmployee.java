package employee;

/**
 * FullTimeEmployee: A concrete subclass of Employee.
 * This class demonstrates Inheritance by extending the Employee base class.
 */
public class FullTimeEmployee extends Employee {
    /**
     * Constructor: Passes the employee details to the parent (super) class.
     * Demonstrates the use of 'super' to reuse logic from the Employee constructor.
     */
    public FullTimeEmployee(String id, String name, Address address, Department department, double salary) {
        super(id, name, address, department, salary);
    }
    /**
     * calculateSalary(): Implements the contract from the Payable interface.
     * For a Full-Time employee, the logic returns the baseSalary without modifications.
     */
    @Override
    public double calculateSalary() {
        return baseSalary;
    }
    /**
     * getType(): Implements the abstract method defined in the Employee class.
     * Returns a specific string to identify this category of employee.
     */
    @Override
    public String getType() {
        return "Full-Time";
    }
}