package user;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import studentcourseregistrationsystem.DatabaseConnection;

public class AddCoursePanel extends JPanel {

    private JTextField txtId, txtName;
    private JComboBox<String> dept, cmbCredits, cmbSem;

    public AddCoursePanel() {
        setLayout(null);
        setBackground(Color.WHITE);

        addLabel("Course ID", 60);
        txtId = addTextField(60);

        addLabel("Course Name", 110);
        txtName = addTextField(110);

        addLabel("Department", 160);
        dept = new JComboBox<>(
                new String[] { "CSE", "ECE", "EEE", "MECH", "CIVIL" });
        dept.setBounds(200, 160, 250, 30);
        add(dept);

        addLabel("Credits", 210);
        cmbCredits = new JComboBox<>(
                new String[] { "1", "2", "3", "4", "5" });
        cmbCredits.setBounds(200, 210, 250, 30);
        add(cmbCredits);

        addLabel("Semester", 260);
        cmbSem = new JComboBox<>(
                new String[] { "1", "2", "3", "4", "5", "6", "7", "8" });
        cmbSem.setBounds(200, 260, 250, 30);
        add(cmbSem);

        JButton save = new JButton("Save");
        save.setBounds(200, 320, 120, 35);
        save.setBackground(new Color(70, 130, 210));
        save.setForeground(Color.WHITE);
        save.setFocusPainted(false);
        save.addActionListener(e -> saveCourse());

        add(save);
    }

    private void saveCourse() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String department = (String) dept.getSelectedItem();
        int credits = Integer.parseInt((String) cmbCredits.getSelectedItem());
        int semester = Integer.parseInt((String) cmbSem.getSelectedItem());

        if (id.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO courses (course_id, course_name, department, credits, semester) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement pst = conn.prepareStatement(sql)) {
                pst.setString(1, id);
                pst.setString(2, name);
                pst.setString(3, department);
                pst.setInt(4, credits);
                pst.setInt(5, semester);
                pst.executeUpdate();
                JOptionPane.showMessageDialog(this, "Course Added Successfully!");

                txtId.setText("");
                txtName.setText("");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding course: " + e.getMessage());
        }
    }

    // ===== Helper methods =====
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
