package employee;

import java.io.Serializable;

/**
 * Department: Represents the organizational unit an employee belongs to.
 * This class is another example of Aggregation, allowing the Employee class 
 * to associate with a specific department entity.
 */
public class Department implements Serializable {
    // Encapsulated field: Stores the name of the department privately.
    private String name;

    /**
     * Constructor: Initializes the Department with a specific name.
     */
    public Department(String name) {
        this.name = name;
    }

    /**
     * Getter: Provides read access to the department name.
     */
    public String getName() {
        return name;
    }
}