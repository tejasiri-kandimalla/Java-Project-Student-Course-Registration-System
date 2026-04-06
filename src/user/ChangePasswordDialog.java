package user;

import studentcourseregistrationsystem.DatabaseConnection;
import studentcourseregistrationsystem.PasswordUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ChangePasswordDialog extends JDialog {

    private JPasswordField txtOldPassword;
    private JPasswordField txtNewPassword;
    private JPasswordField txtConfirmPassword;
    private String userId;

    public ChangePasswordDialog(JFrame parent, String userId) {
        super(parent, "Change Password", true);
        this.userId = userId;
        
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(15, 76, 117));
        JLabel titleLabel = new JLabel("Change Password");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titlePanel.add(titleLabel);
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        gbc.gridx = 0; gbc.gridy = 0;
        mainPanel.add(new JLabel("Old Password:"), gbc);
        
        txtOldPassword = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 0;
        mainPanel.add(txtOldPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        mainPanel.add(new JLabel("New Password:"), gbc);
        
        txtNewPassword = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 1;
        mainPanel.add(txtNewPassword, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        mainPanel.add(new JLabel("Confirm Password:"), gbc);
        
        txtConfirmPassword = new JPasswordField(15);
        gbc.gridx = 1; gbc.gridy = 2;
        mainPanel.add(txtConfirmPassword, gbc);
        
        JPanel btnPanel = new JPanel();
        btnPanel.setBackground(Color.WHITE);
        JButton btnSave = new JButton("Save");
        btnSave.setBackground(new Color(15, 76, 117));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFocusPainted(false);
        btnSave.addActionListener(e -> changePassword());
        
        JButton btnCancel = new JButton("Cancel");
        btnCancel.setFocusPainted(false);
        btnCancel.addActionListener(e -> dispose());
        
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        
        add(titlePanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(btnPanel, BorderLayout.SOUTH);
    }
    
    private void changePassword() {
        String oldPass = String.valueOf(txtOldPassword.getPassword());
        String newPass = String.valueOf(txtNewPassword.getPassword());
        String confirmPass = String.valueOf(txtConfirmPassword.getPassword());
        
        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required.");
            return;
        }
        
        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "New passwords do not match.");
            return;
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement("SELECT password FROM users WHERE username = ?")) {
             
            checkStmt.setString(1, userId);
            try (ResultSet rs = checkStmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    if (PasswordUtils.checkPassword(oldPass, storedHash)) {
                        // Fetch user email
                        String email = null;
                        try (PreparedStatement emailStmt = conn.prepareStatement("SELECT email FROM students WHERE student_id = ?")) {
                            emailStmt.setString(1, userId);
                            try (ResultSet rsEmail = emailStmt.executeQuery()) {
                                if (rsEmail.next()) {
                                    email = rsEmail.getString("email");
                                }
                            }
                        }

                        if (email == null || email.trim().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "No registered email found for OTP verification.");
                            return;
                        }

                        // Generate OTP and send email
                        String otp = studentcourseregistrationsystem.EmailUtils.generateOTP();
                        boolean sent = studentcourseregistrationsystem.EmailUtils.sendOTP(email, otp);
                        
                        if (!sent) {
                            JOptionPane.showMessageDialog(this, "Failed to send OTP to " + email + ".");
                            return;
                        }

                        String enteredOtpStr = JOptionPane.showInputDialog(this, "OTP sent to " + email + ".\nEnter OTP:");
                        if (enteredOtpStr == null || !enteredOtpStr.equals(otp)) {
                            JOptionPane.showMessageDialog(this, "Invalid or missing OTP. Password not changed.");
                            return;
                        }

                        String newHash = PasswordUtils.hashPassword(newPass);
                        try (PreparedStatement updateStmt = conn.prepareStatement("UPDATE users SET password = ? WHERE username = ?")) {
                            updateStmt.setString(1, newHash);
                            updateStmt.setString(2, userId);
                            updateStmt.executeUpdate();
                            JOptionPane.showMessageDialog(this, "Password changed successfully.");
                            dispose();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Incorrect old password.");
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database Error: " + ex.getMessage());
        }
    }
}
