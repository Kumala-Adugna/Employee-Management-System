package employee;

/**
 * Intern: A concrete subclass of Employee representing a student or trainee.
 * This class demonstrates Inheritance and Method Overriding.
 */
public class Intern extends Employee {

    /**
     * Constructor: Initializes an Intern by calling the parent Employee constructor.
     * In this context, 'baseSalary' is used to store the intern's fixed stipend.
     */
    public Intern(String id, String name, Address address, Department department, double stipend) {
        super(id, name, address, department, stipend);
    }

    /**
     * calculateSalary(): Overrides the Payable interface method.
     * For an Intern, it simply returns the fixed stipend (baseSalary).
     */
    @Override
    public double calculateSalary() {
        return baseSalary;
    }

    /**
     * getType(): Provides a specific implementation of the abstract method from the Employee class.
     * Identifies the object as an "Intern" type for the GUI and Database logic.
     */
    @Override
    public String getType() {
        return "Intern";
    }
}