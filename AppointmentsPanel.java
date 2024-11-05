import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class AppointmentsPanel extends JPanel {
    private JComboBox<String> patientComboBox;
    private JComboBox<String> doctorComboBox;
    private JTextField dateField;

    public AppointmentsPanel() {
        setLayout(new BorderLayout());

        // Form for booking appointments
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        formPanel.add(new JLabel("Select Patient:"));
        patientComboBox = new JComboBox<>();
        loadPatients();
        formPanel.add(patientComboBox);

        formPanel.add(new JLabel("Select Doctor:"));
        doctorComboBox = new JComboBox<>();
        loadDoctors();
        formPanel.add(doctorComboBox);

        formPanel.add(new JLabel("Appointment Date (YYYY-MM-DD HH:MM:SS):"));
        dateField = new JTextField();
        formPanel.add(dateField);

        JButton bookAppointmentButton = new JButton("Book Appointment");
        bookAppointmentButton.addActionListener(e -> bookAppointment());

        add(formPanel, BorderLayout.CENTER);
        add(bookAppointmentButton, BorderLayout.SOUTH);
    }

    // Load patients into the combo box
    private void loadPatients() {
        try (Connection connection = getConnection()) {
            String query = "SELECT id, name FROM patients";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                patientComboBox.addItem(resultSet.getInt("id") + " - " + resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading patients", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Load doctors into the combo box
    private void loadDoctors() {
        try (Connection connection = getConnection()) {
            String query = "SELECT id, name FROM doctors";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                doctorComboBox.addItem(resultSet.getInt("id") + " - " + resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading doctors", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to book an appointment
    private void bookAppointment() {
        String selectedPatient = (String) patientComboBox.getSelectedItem();
        String selectedDoctor = (String) doctorComboBox.getSelectedItem();
        String appointmentDate = dateField.getText();

        if (selectedPatient == null || selectedDoctor == null || appointmentDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.", "Input Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int patientId = Integer.parseInt(selectedPatient.split(" - ")[0]);
        int doctorId = Integer.parseInt(selectedDoctor.split(" - ")[0]);

        try (Connection connection = getConnection()) {
            String query = "INSERT INTO appointments (patient_id, doctor_id, appointment_date) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, patientId);
            preparedStatement.setInt(2, doctorId);
            preparedStatement.setString(3, appointmentDate);
            preparedStatement.executeUpdate();
            JOptionPane.showMessageDialog(this, "Appointment booked successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error booking appointment", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to establish a database connection
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/hospital_management", "root", "root");
    }
}
