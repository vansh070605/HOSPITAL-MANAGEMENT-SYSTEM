import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class AppointmentsDetailsPanel extends JPanel {
    private JTable appointmentsTable;
    private DefaultTableModel appointmentsModel;

    public AppointmentsDetailsPanel() {
        setLayout(new BorderLayout());

        // Create the table to display appointments
        appointmentsModel = new DefaultTableModel(new String[]{"Appointment ID", "Patient Name", "Doctor Name", "Appointment Date"}, 0);
        appointmentsTable = new JTable(appointmentsModel);
        loadAppointmentsData();

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(appointmentsTable);
        add(scrollPane, BorderLayout.CENTER);

        // Refresh button to reload data
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> loadAppointmentsData());
        add(refreshButton, BorderLayout.SOUTH);
    }

    // Load appointments data from the database
    private void loadAppointmentsData() {
        try (Connection connection = getConnection()) {
            String query = "SELECT a.id, p.name AS patient_name, d.name AS doctor_name, a.appointment_date " +
                    "FROM appointments a " +
                    "JOIN patients p ON a.patient_id = p.id " +
                    "JOIN doctors d ON a.doctor_id = d.id";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            appointmentsModel.setRowCount(0); // Clear existing data
            while (resultSet.next()) {
                appointmentsModel.addRow(new Object[]{
                        resultSet.getInt("id"),
                        resultSet.getString("patient_name"),
                        resultSet.getString("doctor_name"),
                        resultSet.getString("appointment_date")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading appointment data", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to establish a database connection
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_management", "root", "root");
    }
}
