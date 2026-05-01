package app;

import gui.EmployeeManagementGUI;
import javax.swing.SwingUtilities;

/**
 * Main: The entry point of the application.
 * This class contains the main method which starts the execution of the program.
 */
public class Main {
    /**
     * main(): The standard method that the Java Virtual Machine (JVM) calls to run the app.
     */
    public static void main(String[] args) {
        /*
         * SwingUtilities.invokeLater(): Ensures that the GUI is created and updated 
         * on the Event Dispatch Thread (EDT). This is a thread-safety best practice 
         * in Swing to prevent potential interface freezing or crashes.
         */
        SwingUtilities.invokeLater(() -> {
            // Instantiates the GUI and makes the window visible to the user.
            new EmployeeManagementGUI().setVisible(true);
        });
    }
}