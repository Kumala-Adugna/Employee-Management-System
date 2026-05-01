package employee;

import java.io.Serializable;

/**
 * Address: A helper class used to represent the physical location of an employee.
 * It demonstrates the OOP concept of Aggregation, where an Employee "has an" Address.
 */
public class Address implements Serializable {
    // Encapsulated fields: Private access to protect the integrity of the data.
    private String street, city, state;

    /**
     * Constructor: Initializes a new Address object with street, city, and state details.
     */
    public Address(String street, String city, String state) {
        this.street = street;
        this.city = city;
        this.state = state;
    }

    // Getters: Public methods that allow other classes to safely read the address components.
    
    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }
}