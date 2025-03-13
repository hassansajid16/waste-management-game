package wastemanagement;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

/**
 * A diagnostic tool to test individual components of the Waste Management Game
 */
public class GameDiagnosticTool extends JFrame {
    
    public GameDiagnosticTool() {
        setTitle("Waste Management Game Diagnostics");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Title
        JLabel titleLabel = new JLabel("Waste Management Game - Diagnostic Tool");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Test buttons
        addTestButton(mainPanel, "Test Splash Screen", () -> new SplashScreen());
        addTestButton(mainPanel, "Test Login Screen", () -> new Login());
        addTestButton(mainPanel, "Test Main Game Board", () -> {
            // Create test players
            ArrayList<Player> testPlayers = new ArrayList<>();
            Player p1 = new Player("Test Player 1");
            p1.setRole("Business Owner");
            p1.applyRole("Business Owner");
            p1.setPlayerColor(Color.BLUE);
            
            Player p2 = new Player("Test Player 2");
            p2.setRole("Community Leader");
            p2.applyRole("Community Leader");
            p2.setPlayerColor(Color.RED);
            
            testPlayers.add(p1);
            testPlayers.add(p2);
            
            return new WasteManagementGame(testPlayers);
        });
        
        addTestButton(mainPanel, "Test PlayerStatisticsPanel", () -> {
            // Create a test frame
            JFrame testFrame = new JFrame("Player Statistics Test");
            testFrame.setSize(300, 400);
            testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            
            // Create a test player
            Player testPlayer = new Player("Test Player");
            testPlayer.setRole("Business Owner");
            testPlayer.applyRole("Business Owner");
            
            // Create panel to test
            JPanel testPanel = new JPanel();
            testPanel.setLayout(new BoxLayout(testPanel, BoxLayout.Y_AXIS));
            testPanel.setBackground(new Color(50, 70, 50));
            
            // Create the player statistics panel - we need a sample WasteManagementGame instance
            ArrayList<Player> players = new ArrayList<>();
            players.add(testPlayer);
            WasteManagementGame tempGame = new WasteManagementGame(players);
            
            // Now create the statistics panel with the game instance
            PlayerStatisticsPanel statsPanel = new PlayerStatisticsPanel(testPlayer, tempGame);
            testPanel.add(statsPanel);
            
            // Show the frame
            testFrame.add(testPanel);
            testFrame.setVisible(true);
            return testFrame;
        });
        
        addTestButton(mainPanel, "Test ObjectiveGameDialog", () -> {
            // Create a test player
            Player testPlayer = new Player("Test Player");
            testPlayer.setRole("Business Owner");
            testPlayer.applyRole("Business Owner");
            testPlayer.updateCash(6000); // Ensure enough cash
            
            // Create a test frame and game instance
            JFrame testFrame = new JFrame("Test Frame");
            testFrame.setSize(100, 100);
            testFrame.setVisible(true);
            
            // Create test log
            JTextArea testLog = new JTextArea();
            
            // Show dialog
            ObjectiveGameDialog dialog = new ObjectiveGameDialog(testFrame, testPlayer, testLog);
            dialog.setVisible(true);
            
            return dialog;
        });
        
        // Console output panel
        JTextArea consoleOutput = new JTextArea();
        consoleOutput.setEditable(false);
        consoleOutput.setFont(new Font("Monospaced", Font.PLAIN, 12));
        
        JScrollPane scrollPane = new JScrollPane(consoleOutput);
        scrollPane.setPreferredSize(new Dimension(750, 300));
        
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(new JLabel("Console Output:"));
        mainPanel.add(Box.createVerticalStrut(5));
        mainPanel.add(scrollPane);
        
        // Redirect console output to the text area
        System.setOut(new java.io.PrintStream(new java.io.OutputStream() {
            private StringBuilder buffer = new StringBuilder();
            
            @Override
            public void write(int b) {
                char c = (char) b;
                buffer.append(c);
                if (c == '\n') {
                    final String text = buffer.toString();
                    SwingUtilities.invokeLater(() -> {
                        consoleOutput.append(text);
                        consoleOutput.setCaretPosition(consoleOutput.getDocument().getLength());
                    });
                    buffer.setLength(0);
                }
            }
        }));
        
        // Set content pane
        setContentPane(mainPanel);
    }
    
    private <T> void addTestButton(JPanel panel, String text, ThrowingSupplier<T> action) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 40));
        button.addActionListener(e -> {
            try {
                System.out.println("\n--- Starting: " + text + " ---");
                T result = action.get();
                System.out.println("--- Completed: " + text + " ---");
            } catch (Exception ex) {
                System.err.println("Error in " + text + ": " + ex.getMessage());
                ex.printStackTrace();
            }
        });
        panel.add(button);
        panel.add(Box.createVerticalStrut(10));
    }
    
    @FunctionalInterface
    public interface ThrowingSupplier<T> {
        T get() throws Exception;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new GameDiagnosticTool().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}