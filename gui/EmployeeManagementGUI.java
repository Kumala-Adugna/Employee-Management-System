package gui;

import dao.*;
import employee.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * EmployeeManagementGUI: The main user interface class.
 * It connects the DAO (Data) with the Swing components (UI).
 */
public class EmployeeManagementGUI extends JFrame {
    // DAO instance: Connects the GUI to the SQLite database logic.
    private final EmployeeDAO dao = new SQLiteEmployeeDAO();
    
    // Add this line at the top with your other private final variables
    private final JButton btnShow = new JButton("Refresh Table");
    // UI Components for data entry and display.
    private final JTextField txtId = new JTextField();
    private final JTextField txtName = new JTextField();
    private final JTextField txtDept = new JTextField();
    private final JTextField txtSalary = new JTextField();
    private final JTextField txtHours = new JTextField();
    private final JComboBox<String> cmbType = new JComboBox<>(new String[]{"Full-Time", "Part-Time", "Intern"});
    
    private DefaultTableModel model;
    private JTable table;
    private final JPanel cards = new JPanel(new CardLayout());
    private final JLabel lblStatus = new JLabel("No Employees Found", SwingConstants.CENTER);

    // Visual Styling constants.
    private final Color primaryColor = new Color(70, 130, 180); 
    private final Color backgroundColor = new Color(245, 247, 250);
    private final Color dangerColor = new Color(220, 53, 69); 

    public EmployeeManagementGUI() {
        super("Employee Management System");
        
        // Window Layout configuration.
        setLayout(new BorderLayout(0, 0));
        setPreferredSize(new Dimension(850, 750));
        setResizable(false);

        // Assemble the three main UI regions.
        add(buildForm(), BorderLayout.NORTH);
        add(buildTableArea(), BorderLayout.CENTER);
        add(buildActions(), BorderLayout.SOUTH);

        // Conditional UI Logic: Only enable Hours field if 'Part-Time' is selected.
        cmbType.addActionListener(e -> {
            boolean isPT = cmbType.getSelectedItem().equals("Part-Time");
            txtHours.setEnabled(isPT);
            txtHours.setBackground(isPT ? Color.WHITE : new Color(230, 230, 230));
        });
        txtHours.setEnabled(false);
        txtHours.setBackground(new Color(230, 230, 230));

        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        // Load stored data into the table upon startup.
        refreshTable();
    }

    /**
     * buildForm(): Creates the top input section using a Grid layout.
     */
    private JPanel buildForm() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, primaryColor));

        JPanel grid = new JPanel(new GridLayout(3, 2, 30, 15));
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(25, 40, 25, 40));

        Font labelFont = new Font("Segoe UI", Font.BOLD, 13);

        grid.add(createInputGroup("Employee ID", txtId, labelFont));
        grid.add(createInputGroup("Full Name", txtName, labelFont));
        grid.add(createInputGroup("Employment Type", cmbType, labelFont));
        grid.add(createInputGroup("Department", txtDept, labelFont));
        grid.add(createInputGroup("Salary / Rate", txtSalary, labelFont));
        grid.add(createInputGroup("Worked Hours (PT Only)", txtHours, labelFont));

        JButton btnAdd = new JButton("REGISTER EMPLOYEE");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnAdd.setBackground(primaryColor);
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnAdd.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        btnAdd.addActionListener(e -> addEmp());

        header.add(grid, BorderLayout.CENTER);
        header.add(btnAdd, BorderLayout.SOUTH);
        return header;
    }

    /**
     * createInputGroup(): Helper to create a labeled input field.
     */
    private JPanel createInputGroup(String title, JComponent field, Font font) {
        JPanel p = new JPanel(new BorderLayout(0, 5));
        p.setOpaque(false);
        JLabel lbl = new JLabel(title.toUpperCase());
        lbl.setFont(font);
        lbl.setForeground(primaryColor);
        
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 210, 210)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        p.add(lbl, BorderLayout.NORTH);
        p.add(field, BorderLayout.CENTER);
        return p;
    }

    /**
     * buildTableArea(): Sets up the JTable and CardLayout for switching between data and empty views.
     */
    private JPanel buildTableArea() {
    // 1. Create a main container to hold both the Search Bar and the Table
    JPanel container = new JPanel(new BorderLayout(0, 10)); 
    container.setOpaque(false);

    // 2. BUILD THE SEARCH BAR SECTION
    JPanel searchPanel = new JPanel(new BorderLayout(10, 0));
    searchPanel.setOpaque(false);
    
    JTextField txtSearch = new JTextField();
    txtSearch.setToolTipText("Search by Name or ID...");
    
    // This listener makes the search "Real-Time" as you type

    txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
        @Override
        public void keyReleased(java.awt.event.KeyEvent evt) {
            String q = txtSearch.getText().trim();
            
            if (q.isEmpty()) {
                // OPTIMIZATION: Instead of calling refreshTable(), 
                // we clear the table and show the "Search your name" message.
                lblStatus.setText("Enter a name or ID above to find an employee.");
                ((CardLayout) cards.getLayout()).show(cards, "EMPTY");
                model.setRowCount(0); 
            } else {
                // Only query the database if there is actual text to search for.
                updateTableData(dao.search(q)); 
            }
        }
    });

    JLabel lblSearch = new JLabel("SEARCH:");
    lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
    lblSearch.setForeground(primaryColor);

    searchPanel.add(lblSearch, BorderLayout.WEST);
    searchPanel.add(txtSearch, BorderLayout.CENTER);

    // 3. SETUP THE TABLE (Existing Logic)
    String[] cols = {"ID", "NAME", "TYPE", "DEPT", "NET SALARY"};
    model = new DefaultTableModel(cols, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    table = new JTable(model);
    table.setRowHeight(35);
    // ... (Keep your existing table styling here) ...

    // 4. ASSEMBLE THE PIECES
    cards.add(lblStatus, "EMPTY");
    cards.add(new JScrollPane(table), "DATA");

    container.add(searchPanel, BorderLayout.NORTH); // Search bar at the very top
    container.add(cards, BorderLayout.CENTER);      // Table area below it
    
    container.setBorder(BorderFactory.createEmptyBorder(10, 40, 10, 40));
    return container;
}
    /**
     * buildActions(): Creates buttons for manual refresh and data deletion.
     */
    /**
     * buildActions(): Creates buttons for toggling data, deletion, and salary updates.
     */
    private JPanel buildActions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        p.setBackground(backgroundColor);

        // This button now toggles between "Refresh Table" and "Hide Records"
        btnShow.addActionListener(e -> toggleTable());
        
        JButton btnDelete = new JButton("DELETE SELECTED");
        btnDelete.setBackground(dangerColor);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteSelectedEmp());

        JButton btnUpdate = new JButton("UPDATE SALARY");
        btnUpdate.setBackground(new Color(40, 167, 69)); // Success Green
        btnUpdate.setForeground(Color.WHITE);
        btnUpdate.setFocusPainted(false);
        btnUpdate.addActionListener(e -> updateEmployeeSalary());
        
        p.add(btnShow);
        p.add(btnDelete);
        p.add(btnUpdate);
        return p;
    }
   
    /**
     * addEmp(): Logic to instantiate the correct Employee subclass based on UI selection.
     */
    private void addEmp() {
    try {
        String type = (String) cmbType.getSelectedItem();
        Employee e;
        Department d = new Department(txtDept.getText());
        
        // Factory-style instantiation logic
        if ("Part-Time".equals(type)) {
            e = new PartTimeEmployee(txtId.getText(), txtName.getText(), null, 
                d, Double.parseDouble(txtSalary.getText()), 
                Integer.parseInt(txtHours.getText()));
        } else if ("Intern".equals(type)) {
            e = new Intern(txtId.getText(), txtName.getText(), null, d, 
                Double.parseDouble(txtSalary.getText()));
        } else {
            e = new FullTimeEmployee(txtId.getText(), txtName.getText(), null, 
                d, Double.parseDouble(txtSalary.getText()));
        }
        
        // 1. Save to the hard disk database
        dao.add(e); 
        
        // 2. Clear the form fields for the next entry
        clearFields(); 

        // 3. OPTIMIZATION: Instead of loading everyone, show the success message
        lblStatus.setText("Successfully Registered! Search your name to check.");
        ((CardLayout) cards.getLayout()).show(cards, "EMPTY");
        
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Input Error", JOptionPane.ERROR_MESSAGE);
    }
}

    /**
     * deleteSelectedEmp(): Handles row selection validation and database removal.
     */
    private void deleteSelectedEmp() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee from the table first.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) model.getValueAt(selectedRow, 0);
        String name = (String) model.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete employee: " + name + " (ID: " + id + ")?", 
            "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            dao.delete(id);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Employee successfully removed from records.");
        }
    }
    
    /**
     * updateEmployeeSalary(): Logic to modify the salary of a selected employee.
     * This handles the UI prompts and calls the DAO for the database update.
     */
    private void updateEmployeeSalary() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee from the table first.", 
                "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String id = (String) model.getValueAt(selectedRow, 0);
        String name = (String) model.getValueAt(selectedRow, 1);

        String input = JOptionPane.showInputDialog(this, "Enter new salary for " + name + " (ID: " + id + "):");
        
        if (input != null && !input.isEmpty()) {
            try {
                double newSalary = Double.parseDouble(input);
                
                // Call the DAO to update the database
                dao.updateSalary(id, newSalary); 
                
                JOptionPane.showMessageDialog(this, "Salary updated successfully!");
                
                // Refresh logic for 10,000+ records
                // If search bar is empty, don't dump all data, just show status
                JTextField txtSearch = (JTextField)((JPanel)((JPanel)getContentPane().getComponent(1)).getComponent(0)).getComponent(1);
                String currentSearch = txtSearch.getText().trim();
                
                if (currentSearch.isEmpty()) {
                    lblStatus.setText("Salary updated! Search to verify.");
                    ((CardLayout) cards.getLayout()).show(cards, "EMPTY");
                    model.setRowCount(0);
                } else {
                    updateTableData(dao.search(currentSearch));
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Please enter a valid numeric amount.", 
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


   /**
     * updateTableData: The central UI workhorse.
     * It takes a list (either from search or refresh) and displays it.
     */
    private void updateTableData(List<Employee> list) {
        model.setRowCount(0); // Clear current rows

        if (list.isEmpty()) {
            ((CardLayout) cards.getLayout()).show(cards, "EMPTY");
            // ADD THIS LINE: Ensures button is ready to 'Refresh' if UI is empty
            btnShow.setText("Refresh Table"); 
        } else {
            for (Employee e : list) {
                model.addRow(new Object[]{
                    e.getId(), 
                    e.getName(), 
                    e.getType(), 
                    e.getDepartment().getName(), 
                    e.calculateSalary()
                });
            }
            ((CardLayout) cards.getLayout()).show(cards, "DATA");
        }
        
        cards.revalidate();
        cards.repaint();
    }


    private void toggleTable() {
        if (btnShow.getText().equals("Hide Records")) {
            model.setRowCount(0);
            lblStatus.setText("Records hidden. Search to find an employee.");
            ((CardLayout) cards.getLayout()).show(cards, "EMPTY");
            btnShow.setText("Refresh Table");
        } else {
            refreshTable();
            // refreshTable will automatically set text to "Hide Records" if data exists
        }
    } 

    /**
     * refreshTable(): Syncs the table and updates the toggle button text.
     * This is the "Switch" that allows you to hide the 10,000 records later.
     */
    private void refreshTable() {
        List<Employee> list = dao.getAll(); // Fetch the data
        updateTableData(list);
        
        // If the list isn't empty, we change the button text so the 
        // toggleTable() method knows the NEXT click should be to "Hide".
        if (!list.isEmpty()) {
            btnShow.setText("Hide Records");
        }
    }

    /**
     * clearFields(): Utility to reset the form after a successful operation.
     */
    private void clearFields() {
        txtId.setText(""); txtName.setText(""); txtDept.setText("");
        txtSalary.setText(""); txtHours.setText("");
    }
}