package wastemanagement;

import java.awt.*;
import javax.swing.*;

/**
 * A helper class to launch the waste sorting mini-game reliably
 */
public class WasteSortingLauncher {
    
    /**
     * Launches the waste sorting mini-game with reliable visibility
     */
    public static void launchSortingGame(Player player, JTextArea gameLog) {
        gameLog.append("\n " + player.getName() + " is starting the Waste Sorting Challenge!\n");
        
        // Create a separate launcher thread
        Thread gameThread = new Thread(() -> {
            try {
                // Small delay to ensure the main game UI is updated first
                Thread.sleep(100);
                
                // Launch on the EDT
                SwingUtilities.invokeLater(() -> {
                    try {
                        // Find the parent frame first
                        Frame[] frames = Frame.getFrames();
                        Frame parentFrame = null;
                        for (Frame frame : frames) {
                            if (frame.isVisible() && frame instanceof JFrame) {
                                parentFrame = frame;
                                break;
                            }
                        }
                        
                        // Create dialog with parent frame reference
                        JDialog dialog = new JDialog(parentFrame, "Waste Sorting Challenge - " + player.getName(), true);
                        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
                        dialog.setSize(600, 500);
                        dialog.setLocationRelativeTo(parentFrame);
                        
                        // Create simplified mini-game panel
                        JPanel gamePanel = createSimplifiedGamePanel(player, gameLog, dialog);
                        dialog.setContentPane(gamePanel);
                        
                        // Force visibility (with modal true, this will block until dialog closes)
                        dialog.setVisible(true);
                        
                        System.out.println("Waste Sorting dialog displayed successfully for: " + player.getName());
                    } catch (Exception e) {
                        System.err.println("Error creating sorting game dialog: " + e.getMessage());
                        e.printStackTrace();
                        
                        // Give default rewards if game fails
                        SwingUtilities.invokeLater(() -> {
                            gameLog.append(" Sorting game couldn't launch. Player receives default rewards.\n");
                            player.collectWaste("recyclables");
                            player.collectWaste("foodWaste");
                            player.updateCash(50);
                            gameLog.append(" Default rewards: 1 recyclable, 1 food waste, R50 cash\n");
                        });
                    }
                });
            } catch (Exception e) {
                System.err.println("Error in waste sorting launch thread: " + e.getMessage());
                e.printStackTrace();
            }
        });
        
        gameThread.setDaemon(true);
        gameThread.start();
    }

    /**
     * Creates a simplified waste sorting game panel
     */
    private static JPanel createSimplifiedGamePanel(Player player, JTextArea gameLog, JDialog dialog) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(240, 250, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JLabel titleLabel = new JLabel("Sort the waste into the correct bins!", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);
        
        // Create a panel for the main content
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.setOpaque(false);
        
        // Waste items
        String[] wasteItems = {
            "Plastic Bottle", "Newspaper", "Glass Jar", "Aluminum Can", "Cardboard Box",
            "Banana Peel", "Apple Core", "Coffee Grounds", "Vegetable Scraps", "Eggshells",
            "Styrofoam Cup", "Candy Wrapper", "Used Tissue", "Plastic Bag", "Disposable Diaper",
            "Battery", "Paint Can", "Light Bulb", "Motor Oil", "Electronic Waste"
        };
        
        JComboBox<String> itemSelector = new JComboBox<>(wasteItems);
        contentPanel.add(itemSelector, BorderLayout.NORTH);
        
        // Item display
        JLabel itemLabel = new JLabel("Select an item and sort it", JLabel.CENTER);
        itemLabel.setFont(new Font("Arial", Font.BOLD, 16));
        itemLabel.setPreferredSize(new Dimension(300, 150));
        itemLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        contentPanel.add(itemLabel, BorderLayout.CENTER);
        
        itemSelector.addActionListener(e -> {
            String selectedItem = (String)itemSelector.getSelectedItem();
            itemLabel.setText(selectedItem);
        });
        
        // Bin buttons
        JPanel binPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        
        JButton recyclablesBtn = createBinButton("Recyclables", new Color(0, 150, 200));
        JButton organicBtn = createBinButton("Organic", new Color(0, 150, 0));
        JButton generalBtn = createBinButton("General Waste", new Color(150, 150, 150));
        JButton hazardousBtn = createBinButton("Hazardous", new Color(200, 50, 50));
        
        binPanel.add(recyclablesBtn);
        binPanel.add(organicBtn);
        binPanel.add(generalBtn);
        binPanel.add(hazardousBtn);
        
        contentPanel.add(binPanel, BorderLayout.SOUTH);
        
        // Add content panel
        panel.add(contentPanel, BorderLayout.CENTER);
        
        // Finish button
        JButton finishButton = new JButton("Finish Sorting");
        finishButton.setFont(new Font("Arial", Font.BOLD, 14));
        finishButton.addActionListener(e -> {
            // Award resources to player
            int recyclablesEarned = 2;
            int foodWasteEarned = 1;
            int cashEarned = 100;
            
            for (int i = 0; i < recyclablesEarned; i++) {
                player.collectWaste("recyclables");
            }
            
            for (int i = 0; i < foodWasteEarned; i++) {
                player.collectWaste("foodWaste");
            }
            
            player.updateCash(cashEarned);
            
            // Update game log
            SwingUtilities.invokeLater(() -> {
                gameLog.append("\n " + player.getName() + " completed the Waste Sorting Challenge!\n");
                gameLog.append(" Rewards: " + recyclablesEarned + " recyclables, " + 
                             foodWasteEarned + " food waste, R" + cashEarned + " cash\n");
            });
            
            // Close the dialog
            dialog.dispose();
        });
        
        panel.add(finishButton, BorderLayout.SOUTH);
        
        return panel;
    }
    
    /**
     * Creates a styled button for a waste bin
     */
    private static JButton createBinButton(String name, Color color) {
        JButton button = new JButton(name);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        
        button.addActionListener(e -> {
            JOptionPane.showMessageDialog(button, 
                "Item sorted into " + name + " bin!", 
                "Sorting Result", 
                JOptionPane.INFORMATION_MESSAGE);
        });
        
        return button;
    }
}