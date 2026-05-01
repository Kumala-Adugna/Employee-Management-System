package employee;

/**
 * PartTimeEmployee: A specialized subclass of Employee for staff paid by the hour.
 * This class demonstrates how child classes can have their own unique fields 
 * while still inheriting the common traits of the parent class.
 */
public class PartTimeEmployee extends Employee {
    // Unique fields: Specific to Part-Time employees to calculate flexible pay.
    private double hourlyRate;
    private int hoursWorked;

    /**
     * Constructor: Initializes the Part-Time employee. 
     * It passes 0 to the parent 'baseSalary' because pay is calculated dynamically 
     * based on hours rather than a fixed monthly base.
     */
    public PartTimeEmployee(String id, String name, Address address, Department department, double rate, int hours) {
        super(id, name, address, department, 0);
        this.hourlyRate = rate;
        this.hoursWorked = hours;
    }

    /**
     * calculateSalary(): Overrides the method to implement specific logic.
     * Demonstrates Polymorphism: The system can call this method on any Employee 
     * object, but for this class, it specifically multiplies rate by hours.
     */
    @Override
    public double calculateSalary() {
        return hourlyRate * hoursWorked;
    }

    /**
     * getType(): Implementation of the abstract method.
     * Returns "Part-Time" to distinguish this category in the system and database.
     */
    @Override
    public String getType() {
        return "Part-Time";
    }
}