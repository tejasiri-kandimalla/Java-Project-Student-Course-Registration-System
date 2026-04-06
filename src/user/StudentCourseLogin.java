package user;

import javax.swing.*;
import java.awt.*;

public class StudentCourseLogin extends JFrame {

    JTextField txtUser;
    JPasswordField txtPass;
    JToggleButton eyeBtn;

    ImageIcon eyeHide;
    ImageIcon eyeShow;

    // 🎨 Theme Colors
    Color BG = new Color(236, 232, 225);
    Color CARD_GLASS = new Color(255, 255, 255, 210);
    Color PRIMARY = new Color(15, 76, 117); // ✅ CHANGED TO TEAL
    Color TEXT = new Color(60, 60, 60);
    Color BORDER = new Color(200, 200, 200);

    public StudentCourseLogin() {
        setTitle("Student Course Registration - Login");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 👁 Load eye icons
        eyeHide = scaleIcon("/icons/hide.png", 18, 18);
        eyeShow = scaleIcon("/icons/visible.png", 18, 18);

        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(BG);

        JPanel card = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(CARD_GLASS);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 22, 22);
            }
        };
        card.setPreferredSize(new Dimension(440, 450));
        card.setOpaque(false);

        JLabel title = new JLabel("Student Course Portal", JLabel.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(TEXT);
        title.setBounds(40, 40, 360, 35);
        card.add(title);

        JLabel sub = new JLabel("Login to manage your courses", JLabel.CENTER);
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        sub.setForeground(Color.DARK_GRAY);
        sub.setBounds(100, 75, 240, 20);
        card.add(sub);

        JLabel userLbl = new JLabel("Username / ID");
        userLbl.setForeground(TEXT);
        userLbl.setBounds(60, 130, 200, 20);
        card.add(userLbl);

        txtUser = new JTextField();
        txtUser.setBounds(60, 155, 320, 40);
        txtUser.setBorder(BorderFactory.createLineBorder(BORDER));
        card.add(txtUser);

        JLabel passLbl = new JLabel("Password");
        passLbl.setForeground(TEXT);
        passLbl.setBounds(60, 210, 120, 20);
        card.add(passLbl);

        txtPass = new JPasswordField();
        txtPass.setBounds(60, 235, 280, 40);
        txtPass.setBorder(BorderFactory.createLineBorder(BORDER));
        txtPass.setEchoChar('•');
        card.add(txtPass);

        // 👁 Eye icon button
        eyeBtn = new JToggleButton(eyeHide);
        eyeBtn.setBounds(340, 235, 40, 40);
        eyeBtn.setBorder(BorderFactory.createLineBorder(BORDER));
        eyeBtn.setContentAreaFilled(false);
        eyeBtn.setFocusPainted(false);
        eyeBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        eyeBtn.addActionListener(e -> togglePassword());
        card.add(eyeBtn);

        JButton btnLogin = new JButton("Login");
        btnLogin.setBounds(60, 300, 320, 45);
        btnLogin.setBackground(PRIMARY);
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorder(BorderFactory.createEmptyBorder());
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.addActionListener(e -> login());
        card.add(btnLogin);



        container.add(card);
        add(container);
    }

    // 👁 Toggle password visibility
    void togglePassword() {
        if (eyeBtn.isSelected()) {
            txtPass.setEchoChar((char) 0);
            eyeBtn.setIcon(eyeShow);
        } else {
            txtPass.setEchoChar('•');
            eyeBtn.setIcon(eyeHide);
        }
    }

    // 🔐 Login Logic
    void login() {
        String username = txtUser.getText().trim();
        String password = String.valueOf(txtPass.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please enter credentials");
            return;
        }

        try (java.sql.Connection conn = studentcourseregistrationsystem.DatabaseConnection.getConnection();
                java.sql.PreparedStatement pstmt = conn
                        .prepareStatement("SELECT password, role FROM users WHERE username = ?")) {

            pstmt.setString(1, username);

            try (java.sql.ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String role = rs.getString("role");

                    // Verify password using BCrypt
                    if (studentcourseregistrationsystem.PasswordUtils.checkPassword(password, storedHash)) {
                        
                        // Migrate plain-text password to BCrypt hash
                        if (!storedHash.startsWith("$2a$")) {
                            try (java.sql.PreparedStatement updatePstmt = conn.prepareStatement(
                                "UPDATE users SET password = ? WHERE username = ?")) {
                                updatePstmt.setString(1, studentcourseregistrationsystem.PasswordUtils.hashPassword(password));
                                updatePstmt.setString(2, username);
                                updatePstmt.executeUpdate();
                            } catch (java.sql.SQLException ex) {
                                ex.printStackTrace();
                            }
                        }

                        if ("admin".equalsIgnoreCase(role)) {
                            new AdminDashboard().setVisible(true);
                            dispose();
                        } else if ("student".equalsIgnoreCase(role)) {
                            new StudentDashboard(username).setVisible(true);
                            dispose();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Invalid ID or password");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid ID or password");
                }
            }
        } catch (java.sql.SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }

    // 🛠 Icon scaler
    ImageIcon scaleIcon(String path, int w, int h) {
        Image img = new ImageIcon(getClass().getResource(path))
                .getImage()
                .getScaledInstance(w, h, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    public static void main(String[] args) {
        new StudentCourseLogin().setVisible(true);
    }
}
