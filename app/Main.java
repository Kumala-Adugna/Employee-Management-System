/*
 * PROJECT: EMPLOYEE MANAGEMENT SYSTEM (EMS)
 * UNIVERSITY: ADAMA SCIENCE AND TECHNOLOGY UNIVERSITY
 * 
 * GROUP MEMBERS:                         ID
 * 1. Shimellis Regassa              Ugr/37774/17
 * 2. Kumala Adugna                  Ugr/37298/17
 * 3. Merga Adam                     Ugr/37391/17
 * 4. Fedawak Hailu                  Ugr/36856/17
 * 5. Midaga Buzuna                  Ugr/37416/17
 * 
 * INSTRUCTOR: Mr. Megersa Daraje
 * DATE: May 2026
 */
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