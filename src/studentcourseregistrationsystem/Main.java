package studentcourseregistrationsystem;



public class Main {
    public static void main(String[] args) {
        // Use the event dispatch thread for Swing components
        javax.swing.SwingUtilities.invokeLater(() -> {
            new user.StudentCourseLogin().setVisible(true);
        });
    }
}
