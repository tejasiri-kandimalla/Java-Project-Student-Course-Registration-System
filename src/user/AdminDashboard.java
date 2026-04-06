package user;

import javax.swing.*;
import java.awt.*;

public class AdminDashboard extends JFrame {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JLabel titleLabel;

    private final Color SIDEBAR = new Color(15, 76, 117); // Teal
    private final Color SIDEBAR_HEADER = new Color(27, 38, 44); // Navy

    private ViewStudentPanel viewStudentPanel;
    private ViewCoursePanel viewCoursePanel;
    private ViewEnrollmentPanel viewEnrollmentPanel;

    public AdminDashboard() {

        setTitle("Admin Dashboard");
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ================= LEFT SIDEBAR =================
        JPanel sideBar = new JPanel(new BorderLayout());
        sideBar.setPreferredSize(new Dimension(260, 0));
        sideBar.setBackground(SIDEBAR);

        JLabel menuLabel = new JLabel("MENU", JLabel.CENTER);
        menuLabel.setOpaque(true);
        menuLabel.setBackground(SIDEBAR_HEADER);
        menuLabel.setForeground(Color.WHITE);
        menuLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        menuLabel.setPreferredSize(new Dimension(260, 55));

        sideBar.add(menuLabel, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(SIDEBAR);

        JButton btnAddStudent = menuBtn("Add Student");
        JButton btnAddCourse = menuBtn("Add Course");
        JButton btnViewStudents = menuBtn("View Students");
        JButton btnViewCourses = menuBtn("View Courses");
        JButton btnViewEnrollments = menuBtn("View Enrollments");

        menuPanel.add(btnAddStudent);
        menuPanel.add(btnAddCourse);
        menuPanel.add(btnViewStudents);
        menuPanel.add(btnViewCourses);
        menuPanel.add(btnViewEnrollments);

        sideBar.add(menuPanel, BorderLayout.CENTER);
        add(sideBar, BorderLayout.WEST);

        // ================= RIGHT PANEL =================
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);

        // ---------- TOP BAR ----------
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        titleLabel = new JLabel("ADMIN DASHBOARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setBackground(new Color(220, 80, 80));
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);

        logoutBtn.addActionListener(e -> {
            new StudentCourseLogin().setVisible(true);
            dispose();
        });

        topBar.add(titleLabel, BorderLayout.WEST);
        topBar.add(logoutBtn, BorderLayout.EAST);

        // ================= CARD LAYOUT =================
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // ✅ DEFAULT PLACEHOLDER PANEL (NO EXTRA CLASS)
        JPanel defaultPanel = new JPanel(new GridBagLayout());
        defaultPanel.setBackground(Color.WHITE);

        JLabel msg = new JLabel("Select an option from the left menu");
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        msg.setForeground(Color.GRAY);

        defaultPanel.add(msg);

        // Initialize panels
        viewStudentPanel = new ViewStudentPanel();
        viewCoursePanel = new ViewCoursePanel();
        viewEnrollmentPanel = new ViewEnrollmentPanel();

        // ADD PANELS
        cardPanel.add(defaultPanel, "HOME");
        cardPanel.add(new AddStudentPanel(), "ADD_STUDENT");
        cardPanel.add(new AddCoursePanel(), "ADD_COURSE");
        cardPanel.add(viewStudentPanel, "VIEW_STUDENTS");
        cardPanel.add(viewCoursePanel, "VIEW_COURSES");
        cardPanel.add(viewEnrollmentPanel, "VIEW_ENROLLMENTS");

        rightPanel.add(topBar, BorderLayout.NORTH);
        rightPanel.add(cardPanel, BorderLayout.CENTER);

        add(rightPanel, BorderLayout.CENTER);

        // 🔹 SHOW PLACEHOLDER BY DEFAULT
        cardLayout.show(cardPanel, "HOME");

        // ================= BUTTON ACTIONS =================
        btnAddStudent.addActionListener(e -> show("ADD STUDENT", "ADD_STUDENT"));
        btnAddCourse.addActionListener(e -> show("ADD COURSE", "ADD_COURSE"));
        btnViewStudents.addActionListener(e -> {
            viewStudentPanel.loadData();
            show("VIEW STUDENTS", "VIEW_STUDENTS");
        });
        btnViewCourses.addActionListener(e -> {
            viewCoursePanel.loadData();
            show("VIEW COURSES", "VIEW_COURSES");
        });
        btnViewEnrollments.addActionListener(e -> {
            viewEnrollmentPanel.loadCourses();
            show("VIEW ENROLLMENTS", "VIEW_ENROLLMENTS");
        });
    }

    private JButton menuBtn(String text) {

        JButton btn = new JButton(text);

        btn.setPreferredSize(new Dimension(260, 45));
        btn.setMaximumSize(new Dimension(260, 45));

        btn.setBackground(SIDEBAR);
        btn.setForeground(Color.WHITE);

        btn.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.setBorder(BorderFactory.createMatteBorder(
                0, 0, 1, 0, new Color(20, 100, 150)));

        // Hover Effect (Recommended)
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(20, 100, 150));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(SIDEBAR);
            }
        });

        return btn;
    }

    private void show(String title, String card) {
        titleLabel.setText(title);
        cardLayout.show(cardPanel, card);
    }

    public static void main(String[] args) {
        new AdminDashboard().setVisible(true);
    }
}
