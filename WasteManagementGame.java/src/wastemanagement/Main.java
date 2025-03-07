package wastemanagement;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for better integration
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                // Continue with default look and feel if this fails
                System.out.println("Could not set system look and feel: " + e.getMessage());
            }
            
            // Start with the login screen
            new Login();
        });
    }
}