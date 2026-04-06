package user;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import studentcourseregistrationsystem.DatabaseConnection;

public class ViewEnrollmentPanel extends JPanel {
        private JTable table;
        private JComboBox<String> courseCombo;
        private DefaultTableModel model;

        public ViewEnrollmentPanel() {
                setLayout(new BorderLayout());
                setBackground(Color.WHITE);

                JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 15));
                top.setBackground(Color.WHITE);
                top.add(new JLabel("Select Course:"));

                courseCombo = new JComboBox<>();
                loadCourses();
                top.add(courseCombo);

                JButton loadBtn = new JButton("View Enrollments");
                loadBtn.setBackground(new Color(70, 130, 210));
                loadBtn.setForeground(Color.WHITE);
                loadBtn.setFocusPainted(false);
                loadBtn.addActionListener(e -> loadEnrollments());
                top.add(loadBtn);

                model = new DefaultTableModel(new String[] { "Student ID", "Student Name", "Department", "Year" }, 0) {
                        @Override
                        public boolean isCellEditable(int r, int c) {
                                return false;
                        }
                };
                table = new JTable(model);
                table.setRowHeight(28);
                table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

                JScrollPane scroll = new JScrollPane(table);
                scroll.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

                add(top, BorderLayout.NORTH);
                add(scroll, BorderLayout.CENTER);
        }

        public void loadCourses() {
                courseCombo.removeAllItems();
                try (Connection conn = DatabaseConnection.getConnection();
                                PreparedStatement pst = conn
                                                .prepareStatement("SELECT course_id, course_name FROM courses")) {
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                                courseCombo.addItem(rs.getString("course_id") + " - " + rs.getString("course_name"));
                        }
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                }
        }

        public void loadEnrollments() {
                String selected = (String) courseCombo.getSelectedItem();
                if (selected == null)
                        return;

                String courseId = selected.split(" - ")[0];
                model.setRowCount(0);

                String sql = "SELECT s.* FROM students s JOIN enrollments e ON s.student_id = e.student_id WHERE e.course_id = ?";
                try (Connection conn = DatabaseConnection.getConnection();
                                PreparedStatement pst = conn.prepareStatement(sql)) {
                        pst.setString(1, courseId);
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                                model.addRow(new Object[] { rs.getString("student_id"), rs.getString("name"),
                                                rs.getString("department"), rs.getString("year") });
                        }
                        if (model.getRowCount() == 0)
                                JOptionPane.showMessageDialog(this, "No enrollments found.");
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                }
        }
}
