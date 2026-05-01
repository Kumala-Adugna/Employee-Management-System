package employee;

public class Intern extends Employee {
    public Intern(String id, String name, Address address, Department department, double stipend) {
        super(id, name, address, department, stipend);
    }

    @Override
    public double calculateSalary() {
        return baseSalary;
    }

    @Override
    public String getType() {
        return "Intern";
    }
}