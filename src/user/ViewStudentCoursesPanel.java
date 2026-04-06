package user;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import studentcourseregistrationsystem.DatabaseConnection;

public class ViewStudentCoursesPanel extends JPanel {
        private String studentId;
        private DefaultTableModel model;
        private JTable table;

        public ViewStudentCoursesPanel(String studentId) {
                this.studentId = studentId;
                setLayout(new BorderLayout());
                setBackground(Color.WHITE);

                model = new DefaultTableModel(new String[] { "Course ID", "Course Name", "Department", "Credits" }, 0) {
                        @Override
                        public boolean isCellEditable(int r, int c) {
                                return false;
                        }
                };
                loadData();

                table = new JTable(model);
                table.setRowHeight(28);
                table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

                JScrollPane scroll = new JScrollPane(table);
                scroll.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

                JButton unenrollBtn = new JButton("Unenroll Selected Course");
                unenrollBtn.setBackground(new Color(220, 80, 80));
                unenrollBtn.setForeground(Color.WHITE);
                unenrollBtn.setFocusPainted(false);
                unenrollBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
                unenrollBtn.addActionListener(e -> unenroll());

                JPanel bottom = new JPanel();
                bottom.setBackground(Color.WHITE);
                bottom.add(unenrollBtn);

                add(scroll, BorderLayout.CENTER);
                add(bottom, BorderLayout.SOUTH);
        }

        public void loadData() {
                model.setRowCount(0);
                try (Connection conn = DatabaseConnection.getConnection();
                                PreparedStatement pst = conn.prepareStatement(
                                                "SELECT c.* FROM courses c JOIN enrollments e ON c.course_id = e.course_id WHERE e.student_id = ?")) {
                        pst.setString(1, studentId);
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                                model.addRow(new Object[] { rs.getString("course_id"), rs.getString("course_name"),
                                                rs.getString("department"), rs.getInt("credits") });
                        }
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                }
        }

        private void unenroll() {
                int row = table.getSelectedRow();
                if (row == -1) {
                        JOptionPane.showMessageDialog(this, "Select a course first.");
                        return;
                }

                String id = (String) model.getValueAt(row, 0);
                String name = (String) model.getValueAt(row, 1);

                if (JOptionPane.showConfirmDialog(this, "Unenroll from " + name + "?", "Confirm",
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        try (Connection conn = DatabaseConnection.getConnection();
                                        PreparedStatement pst = conn.prepareStatement(
                                                        "DELETE FROM enrollments WHERE student_id=? AND course_id=?")) {
                                pst.setString(1, studentId);
                                pst.setString(2, id);
                                if (pst.executeUpdate() > 0) {
                                        JOptionPane.showMessageDialog(this, "Unenrolled from " + name);
                                        loadData();
                                }
                        } catch (SQLException e) {
                                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                        }
                }
        }
}
