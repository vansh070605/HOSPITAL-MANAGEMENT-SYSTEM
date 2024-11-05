import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PatientsPanel extends JPanel {
    private JTable patientTable;
    private DefaultTableModel patientModel;

    public PatientsPanel() {
        setLayout(new BorderLayout());

        // Table to display patients
        patientModel = new DefaultTableModel(new String[]{"ID", "Name", "Age", "Ailment"}, 0);
        patientTable = new JTable(patientModel);
        loadPatientData();

        // Buttons for patient actions
        JPanel buttonPanel = new JPanel();
        JButton addPatientButton = new JButton("Add Patient");
        JButton refreshPatientButton = new JButton("Refresh");

        addPatientButton.addActionListener(e -> addPatient());
        refreshPatientButton.addActionListener(e -> loadPatientData());

        buttonPanel.add(addPatientButton);
        buttonPanel.add(refreshPatientButton);

        add(new JScrollPane(patientTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Method to load patient data from the database
    private void loadPatientData() {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM patients";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            patientModel.setRowCount(0); // Clear existing data
            while (resultSet.next()) {
                patientModel.addRow(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"),
                        resultSet.getString("ailment")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patient data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to add a new patient
    private void addPatient() {
        String name = JOptionPane.showInputDialog(this, "Enter Patient's Name:");
        String ageStr = JOptionPane.showInputDialog(this, "Enter Age:");
        String ailment = JOptionPane.showInputDialog(this, "Enter Ailment:");

        // Check if any field is empty
        if (name == null || ageStr == null || ailment == null || name.isEmpty() || ageStr.isEmpty() || ailment.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            // Parse age to ensure it's a valid integer
            int age = Integer.parseInt(ageStr);

            // Insert patient data into the database
            try (Connection connection = getConnection()) {
                String query = "INSERT INTO patients (name, age, ailment) VALUES (?, ?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, name);
                preparedStatement.setInt(2, age);
                preparedStatement.setString(3, ailment);

                // Execute the update and check if it was successful
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Patient added successfully!");
                    loadPatientData(); // Refresh the table to show the new data
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add patient.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for age.", "Input Error", JOptionPane.WARNING_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding patient to the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to establish a database connection
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_management", "root", "root");
    }
}
