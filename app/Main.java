package app;

import gui.EmployeeManagementGUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EmployeeManagementGUI().setVisible(true));
    }
}