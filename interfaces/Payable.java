package interfaces;

/**
 * Payable Interface: Defines a contract for any object that can be paid.
 * This is a functional interface that ensures a standardized way to 
 * calculate compensation across different employee types.
 */
public interface Payable {
    
    /**
     * calculateSalary(): A method that must be implemented by any class 
     * using this interface to provide its specific payroll logic.
     */
    double calculateSalary();
}