package employee;

public class PartTimeEmployee extends Employee {
    private double hourlyRate;
    private int hoursWorked;

    public PartTimeEmployee(String id, String name, Address address, Department department, double rate, int hours) {
        super(id, name, address, department, 0);
        this.hourlyRate = rate;
        this.hoursWorked = hours;
    }

    @Override
    public double calculateSalary() {
        return hourlyRate * hoursWorked;
    }

    @Override
    public String getType() {
        return "Part-Time";
    }
}