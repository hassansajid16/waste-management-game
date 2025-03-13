package wastemanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Panel for displaying player statistics with objective game button
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
    
    public PlayerStatisticsPanel(Player player, WasteManagementGame gameInstance) {
        this.player = player;
        this.gameInstance = gameInstance;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(50, 70, 50));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Player name with role
        JLabel nameLabel = new JLabel(player.getName() + " (" + player.getRole() + ")");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);
        nameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // Create statistics labels
        positionLabel = new JLabel("Position: " + player.getPosition());
        cashLabel = new JLabel("Cash: R" + player.getCash());
        propertiesLabel = new JLabel("Properties: " + player.getPropertyCount());
        
        // Role-specific label
        if (player.getRole().equalsIgnoreCase("Business Owner")) {
            ecoScoreLabel = new JLabel("Eco Score: " + player.getEcoScore());
        } else {
            ecoScoreLabel = new JLabel("Public Influence: " + player.getPublicInfluence());
        }
        
        // Optional labels for compatibility
        recyclablesLabel = new JLabel("Recyclables: " + player.getRecyclables());
        foodWasteLabel = new JLabel("Food Waste: " + player.getFoodWaste());
        
        // Style all labels
        JLabel[] labels = {positionLabel, cashLabel, propertiesLabel, ecoScoreLabel, recyclablesLabel, foodWasteLabel};
        for (JLabel label : labels) {
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.PLAIN, 12));
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
        }
        
        // Create start objective button
        startObjectiveButton = new JButton("Start Objective");
        startObjectiveButton.setFont(new Font("Arial", Font.BOLD, 12));
        startObjectiveButton.setBackground(new Color(100, 180, 100));
        startObjectiveButton.setForeground(Color.WHITE);
        startObjectiveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        startObjectiveButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, startObjectiveButton.getPreferredSize().height));
        
        // Add button action
        startObjectiveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openObjectiveGameDialog();
            }
        });
        
        // Add all components
        add(nameLabel);
        add(Box.createVerticalStrut(5));
        add(positionLabel);
        add(cashLabel);
        add(propertiesLabel);
        add(ecoScoreLabel);
        add(recyclablesLabel);
        add(foodWasteLabel);
        add(Box.createVerticalStrut(10));
        add(startObjectiveButton);
    }
    
    private void openObjectiveGameDialog() {
        ObjectiveGameDialog dialog = new ObjectiveGameDialog(
            gameInstance, player, gameInstance.getGameLog());
        dialog.setVisible(true);
        
        // Update UI after dialog closes
        if (dialog.wasGameSelected()) {
            gameInstance.updatePlayerStats();
        }
    }
    
    public void update() {
        // Update all labels with current player data
        positionLabel.setText("Position: " + player.getPosition());
        cashLabel.setText("Cash: R" + player.getCash());
        propertiesLabel.setText("Properties: " + player.getPropertyCount());
        
        if (player.getRole().equalsIgnoreCase("Business Owner")) {
            ecoScoreLabel.setText("Eco Score: " + player.getEcoScore());
        } else {
            ecoScoreLabel.setText("Public Influence: " + player.getPublicInfluence());
        }
        
        recyclablesLabel.setText("Recyclables: " + player.getRecyclables());
        foodWasteLabel.setText("Food Waste: " + player.getFoodWaste());
    }
}