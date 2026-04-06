package user;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import studentcourseregistrationsystem.DatabaseConnection;

public class CourseRegistrationPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;
    private String studentId;

    public CourseRegistrationPanel(String studentId) {
        this.studentId = studentId;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        model = new DefaultTableModel(new String[] { "Select", "Course ID", "Course Name", "Department", "Credits" },
                0) {
            @Override
            public Class<?> getColumnClass(int c) {
                return c == 0 ? Boolean.class : String.class;
            }

            @Override
            public boolean isCellEditable(int r, int c) {
                return c == 0;
            }
        };

        table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        JButton regBtn = new JButton("Register Selected Courses");
        regBtn.setBackground(new Color(70, 130, 210));
        regBtn.setForeground(Color.WHITE);
        regBtn.setFocusPainted(false);
        regBtn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        regBtn.addActionListener(e -> register());

        JPanel bottom = new JPanel();
        bottom.setBackground(Color.WHITE);
        bottom.add(regBtn);

        add(scroll, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
        loadCourses();
    }

    public void loadCourses() {
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pst = conn.prepareStatement("SELECT * FROM courses")) {
            ResultSet rs = pst.executeQuery();
            model.setRowCount(0);
            while (rs.next()) {
                model.addRow(new Object[] { false, rs.getString("course_id"), rs.getString("course_name"),
                        rs.getString("department"), rs.getInt("credits") });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private void register() {
        List<String> selected = new ArrayList<>();
        for (int i = 0; i < table.getRowCount(); i++) {
            if ((Boolean) table.getValueAt(i, 0))
                selected.add((String) table.getValueAt(i, 1));
        }

        if (selected.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select at least one course.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            PreparedStatement check = conn
                    .prepareStatement("SELECT COUNT(*) FROM enrollments WHERE student_id=? AND course_id=?");
            PreparedStatement insert = conn
                    .prepareStatement("INSERT INTO enrollments (student_id, course_id) VALUES (?, ?)");

            int count = 0;
            for (String id : selected) {
                check.setString(1, studentId);
                check.setString(2, id);
                ResultSet rs = check.executeQuery();
                if (rs.next() && rs.getInt(1) == 0) {
                    insert.setString(1, studentId);
                    insert.setString(2, id);
                    insert.executeUpdate();
                    count++;
                }
            }
            conn.commit();
            JOptionPane.showMessageDialog(this,
                    count > 0 ? "Registered for " + count + " courses!" : "Already registered.");
            if (count > 0)
                loadCourses();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
}
