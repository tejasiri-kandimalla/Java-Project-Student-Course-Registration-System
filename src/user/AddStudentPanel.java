package user;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import studentcourseregistrationsystem.DatabaseConnection;

public class AddStudentPanel extends JPanel {

    private JTextField txtId, txtName, txtEmail;
    private JComboBox<String> dept, year;

    public AddStudentPanel() {
        setLayout(null);
        setBackground(Color.WHITE);

        addLabel("Student ID", 60);
        txtId = addTextField(60);

        addLabel("Student Name", 110);
        txtName = addTextField(110);

        addLabel("Email", 160);
        txtEmail = addTextField(160);

        addLabel("Department", 210);
        dept = new JComboBox<>(
                new String[] { "CSE", "ECE", "EEE", "MECH", "CIVIL" });
        dept.setBounds(200, 210, 250, 30);
        add(dept);

        addLabel("Year", 260);
        year = new JComboBox<>(
                new String[] { "1st Year", "2nd Year", "3rd Year", "4th Year" });
        year.setBounds(200, 260, 250, 30);
        add(year);

        JButton save = new JButton("Save");
        save.setBounds(200, 320, 120, 35);
        save.setBackground(new Color(70, 130, 210));
        save.setForeground(Color.WHITE);
        save.setFocusPainted(false);
        save.addActionListener(e -> saveStudent());

        add(save);
    }

    private void saveStudent() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String email = txtEmail.getText().trim();
        String department = (String) dept.getSelectedItem();
        String yearVal = (String) year.getSelectedItem();

        if (id.isEmpty() || name.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false); // Transaction

            // 1. Insert into students table
            String sqlStudent = "INSERT INTO students (student_id, name, email, department, year) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pst1 = conn.prepareStatement(sqlStudent)) {
                pst1.setString(1, id);
                pst1.setString(2, name);
                pst1.setString(3, email);
                pst1.setString(4, department);
                pst1.setString(5, yearVal);
                pst1.executeUpdate();
            }

            // 2. Insert into users table (default password = 12345)
            String sqlUser = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            try (PreparedStatement pst2 = conn.prepareStatement(sqlUser)) {
                pst2.setString(1, id); // Username is Student ID
                pst2.setString(2, studentcourseregistrationsystem.PasswordUtils.hashPassword("12345")); // Hashed default password
                pst2.setString(3, "student");
                pst2.executeUpdate();
            }

            conn.commit();
            JOptionPane.showMessageDialog(this, "Student Added Successfully!");

            // Clear fields
            txtId.setText("");
            txtName.setText("");
            txtEmail.setText("");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding student: " + e.getMessage());
        }
    }

    void addLabel(String text, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setBounds(30, y, 150, 30);
        add(lbl);
    }

    JTextField addTextField(int y) {
        JTextField txt = new JTextField();
        txt.setBounds(200, y, 250, 30);
        add(txt);
        return txt;
    }
}
