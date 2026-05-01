package employee;

public class FullTimeEmployee extends Employee {
    public FullTimeEmployee(String id, String name, Address address, Department department, double salary) {
        super(id, name, address, department, salary);
    }

    @Override
    public double calculateSalary() {
        return baseSalary;
    }

    @Override
    public String getType() {
        return "Full-Time";
    }
}