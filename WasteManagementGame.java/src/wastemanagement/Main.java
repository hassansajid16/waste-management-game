package wastemanagement;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

// Main class for launching the Waste Management Game

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                System.out.println("Could not set system look and feel: " + e.getMessage());
            }
            
            new SplashScreen();

        });
    }
}