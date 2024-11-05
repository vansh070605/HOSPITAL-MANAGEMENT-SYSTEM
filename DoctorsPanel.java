import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DoctorsPanel extends JPanel {
    private JTable doctorTable;
    private DefaultTableModel doctorModel;

    public DoctorsPanel() {
        setLayout(new BorderLayout());

        // Table to display doctors
        doctorModel = new DefaultTableModel(new String[]{"ID", "Name", "Specialization"}, 0);
        doctorTable = new JTable(doctorModel);
        loadDoctorData();

        // Buttons for doctor actions
        JPanel buttonPanel = new JPanel();
        JButton addDoctorButton = new JButton("Add Doctor");
        JButton refreshDoctorButton = new JButton("Refresh");

        addDoctorButton.addActionListener(e -> addDoctor());
        refreshDoctorButton.addActionListener(e -> loadDoctorData());

        buttonPanel.add(addDoctorButton);
        buttonPanel.add(refreshDoctorButton);

        add(new JScrollPane(doctorTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    // Method to load doctor data from the database
    private void loadDoctorData() {
        try (Connection connection = getConnection()) {
            String query = "SELECT * FROM doctors";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            doctorModel.setRowCount(0); // Clear existing data
            while (resultSet.next()) {
                doctorModel.addRow(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("specialization")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading doctor data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to add a new doctor
    private void addDoctor() {
        String name = JOptionPane.showInputDialog(this, "Enter Doctor's Name:");
        String specialization = JOptionPane.showInputDialog(this, "Enter Specialization:");

        if (name != null && specialization != null) {
            try (Connection connection = getConnection()) {
                String query = "INSERT INTO doctors (name, specialization) VALUES (?, ?)";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, specialization);
                preparedStatement.executeUpdate();
                loadDoctorData();
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error adding doctor", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Method to establish a database connection
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_management", "root", "root");
    }
}
