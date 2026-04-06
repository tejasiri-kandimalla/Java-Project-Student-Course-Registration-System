package user;

import javax.swing.*;
import java.awt.*;

public class StudentDashboard extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JLabel titleLabel;
    private final Color SIDEBAR = new Color(15, 76, 117);
    private final Color HEADER = new Color(27, 38, 44);

    private ViewProfilePanel profilePanel;
    private CourseRegistrationPanel regPanel;
    private ViewStudentCoursesPanel coursesPanel;

    public StudentDashboard(String userId) {
        setTitle("Student Dashboard - " + userId);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Sidebar
        JPanel sideBar = new JPanel(new BorderLayout());
        sideBar.setPreferredSize(new Dimension(260, 0));
        sideBar.setBackground(SIDEBAR);

        JLabel menu = new JLabel("MENU", JLabel.CENTER);
        menu.setOpaque(true);
        menu.setBackground(HEADER);
        menu.setForeground(Color.WHITE);
        menu.setFont(new Font("Segoe UI", Font.BOLD, 16));
        menu.setPreferredSize(new Dimension(260, 55));
        sideBar.add(menu, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(SIDEBAR);

        JButton btnProfile = menuBtn("View Profile");
        JButton btnRegister = menuBtn("Course Registration");
        JButton btnCourses = menuBtn("View Courses");
        menuPanel.add(btnProfile);
        menuPanel.add(btnRegister);
        menuPanel.add(btnCourses);

        sideBar.add(menuPanel, BorderLayout.CENTER);
        add(sideBar, BorderLayout.WEST);

        // Right Panel
        JPanel right = new JPanel(new BorderLayout());
        right.setBackground(Color.WHITE);

        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));

        titleLabel = new JLabel("STUDENT DASHBOARD");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton logout = new JButton("Logout");
        logout.setBackground(new Color(220, 80, 80));
        logout.setForeground(Color.WHITE);
        logout.setFocusPainted(false);
        logout.addActionListener(e -> {
            new StudentCourseLogin().setVisible(true);
            dispose();
        });

        JButton changePass = new JButton("Change Password");
        changePass.setBackground(new Color(15, 76, 117));
        changePass.setForeground(Color.WHITE);
        changePass.setFocusPainted(false);
        changePass.addActionListener(e -> {
            new ChangePasswordDialog(this, userId).setVisible(true);
        });

        JPanel rightBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightBtns.setBackground(Color.WHITE);
        rightBtns.add(changePass);
        rightBtns.add(logout);

        top.add(titleLabel, BorderLayout.WEST);
        top.add(rightBtns, BorderLayout.EAST);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        JPanel home = new JPanel(new GridBagLayout());
        home.setBackground(Color.WHITE);
        JLabel msg = new JLabel("Select an option from the left menu");
        msg.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        msg.setForeground(Color.GRAY);
        home.add(msg);

        profilePanel = new ViewProfilePanel(userId);
        regPanel = new CourseRegistrationPanel(userId);
        coursesPanel = new ViewStudentCoursesPanel(userId);

        cardPanel.add(home, "HOME");
        cardPanel.add(profilePanel, "PROFILE");
        cardPanel.add(regPanel, "REGISTER");
        cardPanel.add(coursesPanel, "COURSES");

        right.add(top, BorderLayout.NORTH);
        right.add(cardPanel, BorderLayout.CENTER);
        add(right, BorderLayout.CENTER);

        btnProfile.addActionListener(e -> show("VIEW PROFILE", "PROFILE"));
        btnRegister.addActionListener(e -> {
            regPanel.loadCourses();
            show("COURSE REGISTRATION", "REGISTER");
        });
        btnCourses.addActionListener(e -> {
            coursesPanel.loadData();
            show("VIEW COURSES", "COURSES");
        });

        cardLayout.show(cardPanel, "HOME");
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
        btn.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(20, 100, 150)));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) {
                btn.setBackground(new Color(20, 100, 150));
            }

            public void mouseExited(java.awt.event.MouseEvent e) {
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
        new StudentDashboard("test_student").setVisible(true);
    }
}
