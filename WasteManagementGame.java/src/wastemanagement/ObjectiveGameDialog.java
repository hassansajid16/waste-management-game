package wastemanagement;

import java.awt.*;
import javax.swing.*;

/**
 * Dialog for selecting which objective mini-game to play
 */
public class ObjectiveGameDialog extends JDialog {
    
    private static final int MINIMUM_CASH = 5000;
    private boolean gameSelected = false;
    private String selectedGame = "";
    
    public ObjectiveGameDialog(JFrame parent, Player player, JTextArea gameLog) {
        super(parent, "Start Objective Game", true);
        setSize(400, 350);
        setLocationRelativeTo(parent);
        
        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(45, 65, 45));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Check if player has enough cash
        if (player.getCash() < MINIMUM_CASH) {
            setupInsufficientFundsPanel(mainPanel, player);
        } else {
            setupGameSelectionPanel(mainPanel, player, gameLog);
        }
        
        // Set as content pane
        setContentPane(mainPanel);
    }
    
    private void setupInsufficientFundsPanel(JPanel panel, Player player) {
        // Title label
        JLabel titleLabel = new JLabel("Insufficient Funds", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        // Info message
        JLabel infoLabel = new JLabel("<html><center>" +
                "You need R" + MINIMUM_CASH + " to start an objective game.<br><br>" +
                "Your current cash: R" + player.getCash() + "<br><br>" +
                "You need R" + (MINIMUM_CASH - player.getCash()) + " more.</center></html>", 
                SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoLabel.setForeground(Color.WHITE);
        
        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Arial", Font.BOLD, 14));
        closeButton.addActionListener(e -> dispose());
        
        // Add components
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(infoLabel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(closeButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupGameSelectionPanel(JPanel panel, Player player, JTextArea gameLog) {
        // Title label
        JLabel titleLabel = new JLabel("Select Objective Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        // Info message
        JLabel infoLabel = new JLabel("<html><center>" +
                "Choose a game to play and earn points toward your objectives.<br>" +
                "Each game costs R" + MINIMUM_CASH + " to play.<br><br>" +
                "Your current cash: R" + player.getCash() + "</center></html>", 
                SwingConstants.CENTER);
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        infoLabel.setForeground(Color.WHITE);
        
        // Game option panel
        JPanel gamePanel = new JPanel(new GridLayout(2, 1, 0, 20));
        gamePanel.setOpaque(false);
        
        // Quiz button
        JButton quizButton = createGameButton("Quiz Challenge", 
                "Answer questions about sustainability and waste management", 
                "/resources/quiz_icon.png");
        
        // Waste sorting button
        JButton wasteSortingButton = createGameButton("Waste Sorting Challenge",
                "Sort different waste items into the correct bins",
                "/resources/waste_icon.png");
        
        // Add buttons to game panel
        gamePanel.add(quizButton);
        gamePanel.add(wasteSortingButton);
        
        // Cancel button
        JButton cancelButton = new JButton("Cancel");
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.addActionListener(e -> dispose());
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.add(cancelButton);
        
        // Add action listeners
        quizButton.addActionListener(e -> {
            gameSelected = true;
            selectedGame = "quiz";
            player.updateCash(-MINIMUM_CASH);
            gameLog.append("💰 " + player.getName() + " spent R" + MINIMUM_CASH + " to play the Quiz Challenge!\n");
            dispose();
            
            // Launch the quiz
            SwingUtilities.invokeLater(() -> {
                SpecialTask.askQuestion(player, gameLog);
            });
        });
        
        wasteSortingButton.addActionListener(e -> {
            gameSelected = true;
            selectedGame = "waste";
            player.updateCash(-MINIMUM_CASH);
            gameLog.append("💰 " + player.getName() + " spent R" + MINIMUM_CASH + " to play the Waste Sorting Challenge!\n");
            dispose();
            
            // Launch waste sorting game
            SwingUtilities.invokeLater(() -> {
                WasteSortingLauncher.launchSortingGame(player, gameLog);
            });
        });
        
        // Add components
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(infoLabel, BorderLayout.NORTH, 1);
        panel.add(gamePanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private JButton createGameButton(String title, String description, String iconPath) {
        JPanel buttonPanel = new JPanel(new BorderLayout(10, 5));
        buttonPanel.setBackground(new Color(60, 80, 60));
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 100), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Game title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        
        // Game description
        JLabel descLabel = new JLabel("<html>" + description + "</html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(Color.WHITE);
        
        // Try to load icon
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage().getScaledInstance(48, 48, Image.SCALE_SMOOTH);
                titleLabel.setIcon(new ImageIcon(img));
                titleLabel.setIconTextGap(10);
            }
        } catch (Exception e) {
            // No icon available, continue without it
        }
        
        // Add components to panel
        buttonPanel.add(titleLabel, BorderLayout.NORTH);
        buttonPanel.add(descLabel, BorderLayout.CENTER);
        
        // Create button with this panel
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.add(buttonPanel, BorderLayout.CENTER);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        
        return button;
    }
    
    public boolean wasGameSelected() {
        return gameSelected;
    }
    
    public String getSelectedGame() {
        return selectedGame;
    }
}