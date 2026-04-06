package user;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import studentcourseregistrationsystem.DatabaseConnection;

public class ViewProfilePanel extends JPanel {

    public ViewProfilePanel(String studentId) {
        setLayout(null);
        setBackground(Color.WHITE);

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement(
                        "SELECT * FROM students WHERE student_id = ?")) {

            pst.setString(1, studentId);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                addTitle("Student Profile", 20);

                addField("Student ID:", rs.getString("student_id"), 80);
                addField("Name:", rs.getString("name"), 120);
                addField("Email:", rs.getString("email"), 160);
                addField("Department:", rs.getString("department"), 200);
                addField("Year:", rs.getString("year"), 240);

            } else {
                JLabel lbl = new JLabel("Profile not found.");
                lbl.setBounds(80, 80, 200, 30);
                lbl.setForeground(Color.RED);
                add(lbl);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JLabel lbl = new JLabel("Error loading profile: " + e.getMessage());
            lbl.setBounds(80, 80, 400, 30);
            lbl.setForeground(Color.RED);
            add(lbl);
        }
    }

    private void addTitle(String text, int y) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setForeground(new Color(15, 76, 117));
        lbl.setBounds(80, y, 300, 30);
        add(lbl);
    }

    private void addField(String label, String value, int y) {
        JLabel lblField = new JLabel(label);
        lblField.setBounds(80, y, 150, 30);
        lblField.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblField.setForeground(Color.DARK_GRAY);

        JLabel lblValue = new JLabel(value);
        lblValue.setBounds(230, y, 300, 30); // Increased width
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblValue.setForeground(Color.BLACK);

        add(lblField);
        add(lblValue);
    }
}
