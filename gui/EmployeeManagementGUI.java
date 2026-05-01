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
        String[] cols = {"ID", "NAME", "TYPE", "DEPT", "NET SALARY"};
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        table = new JTable(model);
        table.setRowHeight(35);
        table.getTableHeader().setBackground(primaryColor);
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 

        lblStatus.setFont(new Font("Segoe UI Light", Font.ITALIC, 24));
        lblStatus.setForeground(Color.GRAY);

        cards.add(lblStatus, "EMPTY");
        cards.add(new JScrollPane(table), "DATA");
        cards.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        return cards;
    }

    /**
     * buildActions(): Creates buttons for manual refresh and data deletion.
     */
    private JPanel buildActions() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        p.setBackground(backgroundColor);

        JButton btnShow = new JButton("Refresh Table");
        btnShow.addActionListener(e -> refreshTable());
        
        JButton btnDelete = new JButton("DELETE SELECTED");
        btnDelete.setBackground(dangerColor);
        btnDelete.setForeground(Color.WHITE);
        btnDelete.setFocusPainted(false);
        btnDelete.addActionListener(e -> deleteSelectedEmp());
        
        p.add(btnShow);
        p.add(btnDelete);
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
            
            // Polymorphism in action: instantiate different types based on user choice.
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
            
            dao.add(e); // Save to database.
            JOptionPane.showMessageDialog(this, "Employee Added Successfully!");
            refreshTable();
            clearFields();
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
     * refreshTable(): Syncs the JTable display with the current state of the database.
     */
    private void refreshTable() {
        List<Employee> list = dao.getAll();
        model.setRowCount(0); 

        if (list.isEmpty()) {
            ((CardLayout) cards.getLayout()).show(cards, "EMPTY");
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
            CardLayout cl = (CardLayout) cards.getLayout();
            cl.show(cards, "DATA");
        }
        
        cards.revalidate();
        cards.repaint();
    }

    /**
     * clearFields(): Utility to reset the form after a successful operation.
     */
    private void clearFields() {
        txtId.setText(""); txtName.setText(""); txtDept.setText("");
        txtSalary.setText(""); txtHours.setText("");
    }
}