import javax.swing.*;
import java.awt.*;

public class HospitalManagementSystem extends JFrame {
    public HospitalManagementSystem() {
        if (!login()) {
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        setTitle("Hospital Management System");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Doctors", new DoctorsPanel());
        tabbedPane.addTab("Patients", new PatientsPanel());
        tabbedPane.addTab("Book Appointment", new AppointmentsPanel()); // Booking panel
        tabbedPane.addTab("Appointment Details", new AppointmentsDetailsPanel()); // Appointment details panel

        add(tabbedPane, BorderLayout.CENTER);
        setVisible(true);
    }

    // Include your login method here if it's not already defined
    private boolean login() {
        // Dummy login for example purposes, you can implement actual authentication
        String username = JOptionPane.showInputDialog(this, "Enter Username:");
        String password = JOptionPane.showInputDialog(this, "Enter Password:");
        return "root".equals(username) && "root".equals(password);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HospitalManagementSystem::new);
    }
}
