package user;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import studentcourseregistrationsystem.DatabaseConnection;

public class ViewCoursePanel extends JPanel {
        private DefaultTableModel model;
        private JTable table;
        private JTextField txtSearch;

        public ViewCoursePanel() {
                setLayout(new BorderLayout());
                setBackground(Color.WHITE);

                JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
                controls.setBackground(Color.WHITE);
                controls.setBorder(BorderFactory.createEmptyBorder(10, 30, 0, 30));

                controls.add(new JLabel("Search Course:"));
                txtSearch = new JTextField(15);
                txtSearch.setPreferredSize(new Dimension(180, 30));
                controls.add(txtSearch);

                JButton btnSearch = createBtn("Search", new Color(70, 130, 210));
                btnSearch.addActionListener(e -> loadData(txtSearch.getText().trim()));
                controls.add(btnSearch);

                JButton btnReset = createBtn("Reset", new Color(150, 150, 150));
                btnReset.addActionListener(e -> {
                        txtSearch.setText("");
                        loadData("");
                });
                controls.add(btnReset);

                controls.add(Box.createHorizontalStrut(20));

                JButton btnDelete = createBtn("Delete Selected", new Color(220, 80, 80));
                btnDelete.addActionListener(e -> delete());
                controls.add(btnDelete);

                add(controls, BorderLayout.NORTH);

                model = new DefaultTableModel(new String[] { "ID", "Name", "Department", "Credits", "Semester" }, 0) {
                        @Override
                        public boolean isCellEditable(int r, int c) {
                                return false;
                        }
                };
                table = new JTable(model);
                table.setRowHeight(28);
                table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
                table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                JScrollPane scroll = new JScrollPane(table);
                scroll.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));
                add(scroll, BorderLayout.CENTER);
                loadData("");
        }

        private JButton createBtn(String txt, Color bg) {
                JButton b = new JButton(txt);
                b.setBackground(bg);
                b.setForeground(Color.WHITE);
                b.setFocusPainted(false);
                return b;
        }

        public void loadData() {
                loadData("");
        }

        public void loadData(String filter) {
                model.setRowCount(0);
                String sql = "SELECT * FROM courses"
                                + (filter.isEmpty() ? "" : " WHERE course_id LIKE ? OR course_name LIKE ?");
                try (Connection conn = DatabaseConnection.getConnection();
                                PreparedStatement pst = conn.prepareStatement(sql)) {
                        if (!filter.isEmpty()) {
                                pst.setString(1, "%" + filter + "%");
                                pst.setString(2, "%" + filter + "%");
                        }
                        ResultSet rs = pst.executeQuery();
                        while (rs.next()) {
                                model.addRow(new Object[] { rs.getString("course_id"), rs.getString("course_name"),
                                                rs.getString("department"), rs.getInt("credits"),
                                                rs.getInt("semester") });
                        }
                } catch (SQLException e) {
                        JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                }
        }

        private void delete() {
                int row = table.getSelectedRow();
                if (row == -1) {
                        JOptionPane.showMessageDialog(this, "Select a course first!");
                        return;
                }

                String id = (String) model.getValueAt(row, 0);
                if (JOptionPane.showConfirmDialog(this, "Delete course " + id + "?", "Confirm",
                                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        try (Connection conn = DatabaseConnection.getConnection()) {
                                conn.setAutoCommit(false);
                                String[] queries = { "DELETE FROM enrollments WHERE course_id=?",
                                                "DELETE FROM courses WHERE course_id=?" };
                                for (String q : queries) {
                                        try (PreparedStatement p = conn.prepareStatement(q)) {
                                                p.setString(1, id);
                                                p.executeUpdate();
                                        }
                                }
                                conn.commit();
                                JOptionPane.showMessageDialog(this, "Course deleted!");
                                loadData(txtSearch.getText().trim());
                        } catch (SQLException e) {
                                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
                        }
                }
        }
}
