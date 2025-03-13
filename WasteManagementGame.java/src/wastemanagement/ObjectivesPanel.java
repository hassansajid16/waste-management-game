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
    private static final int PROPERTY_THRESHOLD = 3;
    
    public ObjectivesPanel(Player player) {
        this.player = player;
        
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(60, 80, 60));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(89, 116, 89), 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Title
        JLabel titleLabel = new JLabel("Objectives", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        
        // Content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);
        
        // Objective 1 - Role specific
        String objective1Text = player.getRole().equalsIgnoreCase("Business Owner") 
            ? "Eco Score: 0/" + ECO_SCORE_THRESHOLD
            : "Public Influence: 0/" + PUBLIC_INFLUENCE_THRESHOLD;
            
        objective1Label = new JLabel(objective1Text);
        objective1Label.setFont(new Font("Arial", Font.PLAIN, 14));
        objective1Label.setForeground(Color.WHITE);
        
        // Progress bar for objective 1
        JProgressBar objective1Bar = player.getRole().equalsIgnoreCase("Business Owner")
            ? createProgressBar(Color.GREEN, ECO_SCORE_THRESHOLD)
            : createProgressBar(Color.BLUE, PUBLIC_INFLUENCE_THRESHOLD);
            
        if (player.getRole().equalsIgnoreCase("Business Owner")) {
            ecoScoreBar = objective1Bar;
        } else {
            publicInfluenceBar = objective1Bar;
        }
        
        // Objective 2 - Properties
        objective2Label = new JLabel("Properties: 0/" + PROPERTY_THRESHOLD);
        objective2Label.setFont(new Font("Arial", Font.PLAIN, 14));
        objective2Label.setForeground(Color.WHITE);
        
        // Progress bar for objective 2
        propertiesBar = createProgressBar(new Color(255, 165, 0), PROPERTY_THRESHOLD); // Orange color
        
        // Add components
        contentPanel.add(Box.createVerticalStrut(10));
        contentPanel.add(objective1Label);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(objective1Bar);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(objective2Label);
        contentPanel.add(Box.createVerticalStrut(5));
        contentPanel.add(propertiesBar);
        
        // Add to main panel
        add(titleLabel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
    }
    
    private JProgressBar createProgressBar(Color foreground, int maximum) {
        JProgressBar bar = new JProgressBar(0, maximum);
        bar.setValue(0);
        bar.setStringPainted(true);
        bar.setForeground(foreground);
        bar.setBackground(Color.DARK_GRAY);
        bar.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        return bar;
    }
    
    public void updateProgress() {
        if (player.getRole().equalsIgnoreCase("Business Owner")) {
            int ecoScore = player.getEcoScore();
            ecoScoreBar.setValue(ecoScore);
            objective1Label.setText("Eco Score: " + ecoScore + "/" + ECO_SCORE_THRESHOLD);
            
            if (ecoScore >= ECO_SCORE_THRESHOLD) {
                objective1Label.setForeground(Color.GREEN);
            }
        } else {
            int publicInfluence = player.getPublicInfluence();
            publicInfluenceBar.setValue(publicInfluence);
            objective1Label.setText("Public Influence: " + publicInfluence + "/" + PUBLIC_INFLUENCE_THRESHOLD);
            
            if (publicInfluence >= PUBLIC_INFLUENCE_THRESHOLD) {
                objective1Label.setForeground(Color.GREEN);
            }
        }
        
        int propertyCount = player.getPropertyCount();
        propertiesBar.setValue(propertyCount);
        objective2Label.setText("Properties: " + propertyCount + "/" + PROPERTY_THRESHOLD);
        
        if (propertyCount >= PROPERTY_THRESHOLD) {
            objective2Label.setForeground(Color.GREEN);
        }
        
        repaint();
    }
}