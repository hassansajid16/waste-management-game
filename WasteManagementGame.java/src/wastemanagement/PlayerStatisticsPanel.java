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
    private ObjectivesPanel objectivesPanel; // This is defined but was never initialized
    
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
        cashLabel = new JLabel("Cash: R" + player.getCash());
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
        
        // Create integrated objectives panel - THIS IS THE KEY FIX
        objectivesPanel = new ObjectivesPanel(player);
        objectivesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        objectivesPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80)); // Limit height
        
        // Create start objective button
        startObjectiveButton = new JButton("Start Objective");
        startObjectiveButton.setFont(new Font("Arial", Font.BOLD, 11)); // Smaller font
        startObjectiveButton.setBackground(new Color(100, 180, 100));
        startObjectiveButton.setForeground(Color.WHITE);
        startObjectiveButton.setOpaque(true);
        startObjectiveButton.setBorderPainted(true);
        startObjectiveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        startObjectiveButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20)); // Shorter button
        
        // Border to make it stand out but not too thick
        startObjectiveButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 100, 50), 1),
            BorderFactory.createEmptyBorder(2, 5, 2, 5) // Reduced padding
        ));
        
        // Add button action
        startObjectiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openObjectiveGameDialog();
            }
        });
        
        // Add components in more compact layout
        add(nameLabel);
        add(Box.createVerticalStrut(2)); // Reduced spacing
        add(statsGrid);
        add(Box.createVerticalStrut(2)); // Reduced spacing
        add(objectivesPanel); // Add the objectives panel - THIS IS IMPORTANT
        add(Box.createVerticalStrut(2)); // Reduced spacing
        add(startObjectiveButton);
        
        // Add a separator at the bottom
        add(Box.createVerticalStrut(3));
        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        add(separator);
    }
    
    private void openObjectiveGameDialog() {
        System.out.println("Opening Objective Game Dialog for player: " + player.getName());
        try {
            ObjectiveGameDialog dialog = new ObjectiveGameDialog(
                gameInstance, player, gameInstance.getGameLog());
            System.out.println("Dialog created, setting visible...");
            dialog.setVisible(true);
            System.out.println("Dialog shown");
            
            // Update UI after dialog closes
            if (dialog.wasGameSelected()) {
                System.out.println("Game was selected, updating player stats");
                gameInstance.updatePlayerStats();
                
                // Specifically update the integrated objectives panel
                objectivesPanel.updateProgress();
            } else {
                System.out.println("No game was selected");
            }
        } catch (Exception e) {
            System.err.println("Error opening objective game dialog: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void update() {
        // Update all labels with current player data - Fixed to match constructor labels
        positionLabel.setText("Pos: " + player.getPosition());
        cashLabel.setText("Cash: R" + player.getCash());
        propertiesLabel.setText("Props: " + player.getPropertyCount());
        
        if (player.getRole().equalsIgnoreCase("Business Owner")) {
            ecoScoreLabel.setText("Eco: " + player.getEcoScore());
        } else {
            ecoScoreLabel.setText("Infl: " + player.getPublicInfluence());
        }
        
        recyclablesLabel.setText("Recyc: " + player.getRecyclables());
        foodWasteLabel.setText("Food: " + player.getFoodWaste());
        
        // Update objectives panel
        objectivesPanel.updateProgress();
    }
}