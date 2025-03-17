package wastemanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel for displaying player statistics with objectives integrated
 */
public class PlayerStatisticsPanel extends JPanel {
    private Player player;
    private JLabel positionLabel;
    private JLabel cashLabel;
    private JLabel propertiesLabel;
    private JLabel ecoScoreLabel;
    private JLabel recyclablesLabel;
    private JLabel foodWasteLabel;
    private JButton startObjectiveButton;
    private WasteManagementGame gameInstance;
    
    // Game cost constant
    private static final int GAME_COST = 200;
    
    public PlayerStatisticsPanel(Player player, WasteManagementGame gameInstance) {
        this.player = player;
        this.gameInstance = gameInstance;
        
        // Use BoxLayout to stack components vertically
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(50, 70, 50));
        setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3)); // Reduced padding
        
        // Player name with role
        JLabel nameLabel = new JLabel(player.getName(), SwingConstants.LEFT);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 11)); // Smaller font
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create statistics labels
        positionLabel = new JLabel("Pos: " + player.getPosition());
        cashLabel = new JLabel("Cash: Rand " + player.getCash());
        propertiesLabel = new JLabel("Props: " + player.getPropertyCount());
        
        // Role-specific label
        if (player.getRole().equalsIgnoreCase("Business Owner")) {
            ecoScoreLabel = new JLabel("Eco: " + player.getEcoScore());
        } else {
            ecoScoreLabel = new JLabel("Infl: " + player.getPublicInfluence());
        }
        
        // Optional labels for compatibility
        recyclablesLabel = new JLabel("Recyc: " + player.getRecyclables());
        foodWasteLabel = new JLabel("Food: " + player.getFoodWaste());
        
        // Style all labels
        JLabel[] labels = {positionLabel, cashLabel, propertiesLabel, ecoScoreLabel, recyclablesLabel, foodWasteLabel};
        for (JLabel label : labels) {
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.PLAIN, 11)); // Smaller font
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        
        // Create stats grid for more compact display
        JPanel statsGrid = new JPanel(new GridLayout(3, 2, 5, 0)); // 3 rows, 2 columns
        statsGrid.setOpaque(false);
        statsGrid.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsGrid.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60)); // Control height
        
        // Add labels to grid
        statsGrid.add(positionLabel);
        statsGrid.add(cashLabel);
        statsGrid.add(propertiesLabel);
        statsGrid.add(ecoScoreLabel);
        statsGrid.add(recyclablesLabel);
        statsGrid.add(foodWasteLabel);
        
        // Create mini-game button
        startObjectiveButton = new JButton("Play Mini-Game (R" + GAME_COST + ")");
        startObjectiveButton.setFont(new Font("Arial", Font.BOLD, 12));
        startObjectiveButton.setBackground(new Color(100, 180, 100));
        startObjectiveButton.setForeground(Color.WHITE);
        startObjectiveButton.setOpaque(true);
        startObjectiveButton.setBorderPainted(true);
        startObjectiveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        startObjectiveButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20)); // Shorter button
        
        // Hide button initially - THIS IS THE CHANGE
        startObjectiveButton.setVisible(false);
        
        // Add button action with improved implementation
        startObjectiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player.getCash() >= GAME_COST) {
                    try {
                        // Deduct cost
                        player.updateCash(-GAME_COST);
                        
                        // Update cash display immediately
                        cashLabel.setText("Cash: R" + player.getCash());
                        
                        // Get game log from game instance
                        JTextArea gameLog = gameInstance.getGameLog();
                        if (gameLog != null) {
                            gameLog.append("💰 " + player.getName() + " spent R" + GAME_COST + " to play a mini-game!\n");
                        }
                        
                        // Launch mini-game with 50/50 chance for each type
                        if (Math.random() < 0.5) {
                            // Launch quiz
                            SpecialTask.askQuestion(player, gameLog);
                        } else {
                            // Launch waste sorting
                            WasteSortingLauncher.launchSortingGame(player, gameLog);
                        }
                        
                        // Award role-specific points
                        if (player.getRole().equalsIgnoreCase("Business Owner")) {
                            int points = 10;
                            player.increaseEcoScore(points);
                            if (gameLog != null) {
                                gameLog.append("♻️ " + player.getName() + " gained " + points + " Eco Score points!\n");
                            }
                            ecoScoreLabel.setText("Eco Score: " + player.getEcoScore());
                        } else {
                            int points = 10;
                            player.increasePublicInfluence(points);
                            if (gameLog != null) {
                                gameLog.append("👥 " + player.getName() + " gained " + points + " Public Influence points!\n");
                            }
                            ecoScoreLabel.setText("Public Influence: " + player.getPublicInfluence());
                        }
                        
                        // Update game stats - this should call our update method
                        if (gameInstance != null) {
                            gameInstance.updatePlayerStats();
                        }
                        
                        // Update button state
                        update();
                    } catch (Exception ex) {
                        System.err.println("Error launching mini-game: " + ex.getMessage());
                        ex.printStackTrace();
                    }
                } else {
                    // Player doesn't have enough cash - show message
                    JOptionPane.showMessageDialog(null, 
                        player.getName() + " needs at least R" + GAME_COST + " to play a mini-game.\n" +
                        "Current cash: R" + player.getCash(), 
                        "Insufficient Funds", 
                        JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        
        // Add all components
        add(nameLabel);
        add(Box.createVerticalStrut(2)); // Reduced spacing
        add(statsGrid);
        add(Box.createVerticalStrut(2)); // Reduced spacing
        // Removed: add(objectivesPanel); // This line is removed to fix the duplicate objectives issue
        add(startObjectiveButton);
        
        // Add a separator at the bottom
        add(Box.createVerticalStrut(3));
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        add(separator);
    }
    
    /**
     * Update the panel with the player's current information
     */
    public void update() {
        // Update all labels with current player data - Fixed to match constructor labels
        positionLabel.setText("Pos: " + player.getPosition());
        cashLabel.setText("Cash: Rand " + player.getCash());
        propertiesLabel.setText("Props: " + player.getPropertyCount());
        
        if (player.getRole().equalsIgnoreCase("Business Owner")) {
            ecoScoreLabel.setText("Eco: " + player.getEcoScore());
        } else {
            ecoScoreLabel.setText("Infl: " + player.getPublicInfluence());
        }
        
        recyclablesLabel.setText("Recyc: " + player.getRecyclables());
        foodWasteLabel.setText("Food: " + player.getFoodWaste());
        
        // IMPORTANT: Keep button hidden - THIS IS THE CHANGE
        startObjectiveButton.setVisible(false);
        startObjectiveButton.setEnabled(player.getCash() >= GAME_COST);
        
        // Update button text to show the cost and whether player can afford it
        if (player.getCash() >= GAME_COST) {
            startObjectiveButton.setText("Play Mini-Game (Rand " + GAME_COST + ")");
            startObjectiveButton.setBackground(new Color(100, 180, 100)); // Green
        } else {
            startObjectiveButton.setText("Need Rand " + GAME_COST + " to Play");
            startObjectiveButton.setBackground(new Color(150, 150, 150)); // Gray
        }
    }
}