package wastemanagement;

import java.awt.*;
import javax.swing.*;

/**
 * Panel for displaying game objectives and player progress
 */
public class ObjectivesPanel extends JPanel {
    private Player player;
    private JProgressBar ecoScoreBar;
    private JProgressBar publicInfluenceBar;
    private JProgressBar propertiesBar;
    private JLabel objective1Label;
    private JLabel objective2Label;
    
    private static final int ECO_SCORE_THRESHOLD = 50;
    private static final int PUBLIC_INFLUENCE_THRESHOLD = 50;
    private static final int PROPERTY_THRESHOLD = 4;  // Changed from 3 to 7
    
    public ObjectivesPanel(Player player) {
        this.player = player;
        
        setLayout(new BorderLayout(5, 5)); // Reduced spacing
        setBackground(new Color(60, 80, 60));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(89, 116, 89), 1), // Thinner border
            BorderFactory.createEmptyBorder(5, 5, 5, 5) // Reduced padding
        ));
        
        // Title with smaller font
        JLabel titleLabel = new JLabel("Objectives", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12)); // Smaller font
        titleLabel.setForeground(Color.WHITE);
        
        // Content panel with compact layout
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Objective 1 - Role specific (more compact layout)
        String objective1Text = player.getRole().equalsIgnoreCase("Business Owner") 
            ? "Eco: " + player.getEcoScore() + "/" + ECO_SCORE_THRESHOLD
            : "Infl: " + player.getPublicInfluence() + "/" + PUBLIC_INFLUENCE_THRESHOLD;
            
        objective1Label = new JLabel(objective1Text);
        objective1Label.setFont(new Font("Arial", Font.PLAIN, 11)); // Smaller font
        objective1Label.setForeground(Color.WHITE);
        
        // Progress bar for objective 1 - shorter height and NO STRING PAINTED
        JProgressBar objective1Bar = player.getRole().equalsIgnoreCase("Business Owner")
            ? createCompactProgressBar(Color.GREEN, ECO_SCORE_THRESHOLD)
            : createCompactProgressBar(Color.BLUE, PUBLIC_INFLUENCE_THRESHOLD);
            
        if (player.getRole().equalsIgnoreCase("Business Owner")) {
            ecoScoreBar = objective1Bar;
            ecoScoreBar.setValue(player.getEcoScore());
        } else {
            publicInfluenceBar = objective1Bar;
            publicInfluenceBar.setValue(player.getPublicInfluence());
        }
        
        // Objective 2 - Tasks (previously Properties) - CHANGED FROM "Props" TO "Tasks"
        objective2Label = new JLabel("Tasks: " + player.getPropertyCount() + "/" + PROPERTY_THRESHOLD);
        objective2Label.setFont(new Font("Arial", Font.PLAIN, 11)); // Smaller font
        objective2Label.setForeground(Color.WHITE);
        
        // Progress bar for objective 2 - shorter height and NO STRING PAINTED
        propertiesBar = createCompactProgressBar(new Color(255, 165, 0), PROPERTY_THRESHOLD);
        propertiesBar.setValue(player.getPropertyCount());
        
        // Add components with less vertical space
        contentPanel.add(Box.createVerticalStrut(2)); // Reduced spacing
        contentPanel.add(objective1Label);
        contentPanel.add(Box.createVerticalStrut(1)); // Reduced spacing
        contentPanel.add(objective1Bar);
        contentPanel.add(Box.createVerticalStrut(2)); // Reduced spacing
        contentPanel.add(objective2Label);
        contentPanel.add(Box.createVerticalStrut(1)); // Reduced spacing
        contentPanel.add(propertiesBar);
        
        // Add to main panel
        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create a compact progress bar with reduced height and no string painted
     */
    private JProgressBar createCompactProgressBar(Color foreground, int maximum) {
        JProgressBar bar = new JProgressBar(0, maximum);
        bar.setValue(0);
        // Don't show percentage text
        bar.setStringPainted(false);
        bar.setForeground(foreground);
        bar.setBackground(Color.DARK_GRAY);
        bar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        bar.setPreferredSize(new Dimension(bar.getPreferredSize().width, 15)); // Reduced height
        bar.setMaximumSize(new Dimension(Integer.MAX_VALUE, 15)); // Limit maximum height
        return bar;
    }
    
    /**
     * Update the progress bars and labels with current player data
     */
    public void updateProgress() {
        if (player.getRole().equalsIgnoreCase("Business Owner")) {
            int ecoScore = player.getEcoScore();
            ecoScoreBar.setValue(ecoScore);
            objective1Label.setText("Eco: " + ecoScore + "/" + ECO_SCORE_THRESHOLD);
            
            if (ecoScore >= ECO_SCORE_THRESHOLD) {
                objective1Label.setForeground(Color.GREEN);
            }
        } else {
            int publicInfluence = player.getPublicInfluence();
            publicInfluenceBar.setValue(publicInfluence);
            objective1Label.setText("Infl: " + publicInfluence + "/" + PUBLIC_INFLUENCE_THRESHOLD);
            
            if (publicInfluence >= PUBLIC_INFLUENCE_THRESHOLD) {
                objective1Label.setForeground(Color.GREEN);
            }
        }
        
        int propertyCount = player.getPropertyCount();
        propertiesBar.setValue(propertyCount);
        objective2Label.setText("Tasks: " + propertyCount + "/" + PROPERTY_THRESHOLD);  // CHANGED FROM "Props" TO "Tasks"
        
        if (propertyCount >= PROPERTY_THRESHOLD) {
            objective2Label.setForeground(Color.GREEN);
        }
        
        repaint();
    }
}