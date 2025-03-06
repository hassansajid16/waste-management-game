package wastemanagement;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class WasteManagementGame extends JFrame {
    private static final long serialVersionUID = 1L;
    private GameManager gameManager;
    private JTextArea gameLog;
    private JLabel positionLabel, recyclablesLabel, foodWasteLabel, cashLabel;
    private JLabel diceImage;
    private JLabel[] tileLabels;
    private Random random = new Random();

    public WasteManagementGame(ArrayList<Player> players) {
        setTitle("Waste Management Game");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(45, 65, 45));

        // Initialize game log
        gameLog = new JTextArea();
        gameLog.setEditable(false);
        gameLog.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Initialize game manager with players from login
        gameManager = new GameManager(gameLog);
        for (Player player : players) {
            gameManager.getPlayers().add(player);
        }

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setOpaque(false);
        
        // Create board panel
        JPanel boardPanel = new JPanel(new GridLayout(3, 5, 10, 10));
        boardPanel.setBackground(new Color(45, 65, 45));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        tileLabels = new JLabel[Board.BOARD_SIZE];
        Board board = new Board();
        
        // Create tiles
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            if (board.getTile(i).getType().trim().isEmpty()) continue;
            
            tileLabels[i] = new JLabel(board.getTile(i).getName(), SwingConstants.CENTER);
            tileLabels[i].setOpaque(true);
            tileLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            tileLabels[i].setPreferredSize(new Dimension(120, 80));
            tileLabels[i].setFont(new Font("Arial", Font.BOLD, 14));

            // Set tile colors based on type
            switch (board.getTile(i).getType()) {
                case "Resource Square":
                    tileLabels[i].setBackground(new Color(89, 116, 89));
                    break;
                case "Task":
                    tileLabels[i].setBackground(new Color(50, 57, 57));
                    break;
                case "Refill Resources":
                    tileLabels[i].setBackground(new Color(89, 116, 89));
                    break;
                default:
                    tileLabels[i].setBackground(new Color(70, 77, 77));
            }
            tileLabels[i].setForeground(Color.WHITE);
            boardPanel.add(tileLabels[i]);
        }
        
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        
        // Dice and log panel
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setOpaque(false);
        
        // Wrap gameLog in a scroll pane
        JScrollPane logScrollPane = new JScrollPane(gameLog);
        logScrollPane.setPreferredSize(new Dimension(300, 400));
        rightPanel.add(logScrollPane, BorderLayout.CENTER);
        
        // Dice panel
        JPanel dicePanel = new JPanel(new BorderLayout());
        dicePanel.setOpaque(false);
        
        // Initialize dice image
        diceImage = new JLabel();
        diceImage.setHorizontalAlignment(SwingConstants.CENTER);
        diceImage.setPreferredSize(new Dimension(80, 80));
        setDiceImage(1);
        
        // Roll button
        JButton rollButton = new JButton("Roll Dice");
        rollButton.setFont(new Font("Arial", Font.BOLD, 16));
        rollButton.setBackground(new Color(173, 216, 230));
        rollButton.setForeground(Color.BLACK);
        rollButton.setFocusPainted(false);
        rollButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        rollButton.addActionListener(e -> rollDice());
        
        dicePanel.add(diceImage, BorderLayout.CENTER);
        dicePanel.add(rollButton, BorderLayout.SOUTH);
        
        rightPanel.add(dicePanel, BorderLayout.SOUTH);
        
        // Player info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        infoPanel.setBackground(new Color(60, 80, 60));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        positionLabel = new JLabel("Position: 0");
        recyclablesLabel = new JLabel("Recyclables: 0");
        foodWasteLabel = new JLabel("Food Waste: 0");
        cashLabel = new JLabel("Cash: R0");

        // Style the labels
        JLabel[] labels = {positionLabel, recyclablesLabel, foodWasteLabel, cashLabel};
        for (JLabel label : labels) {
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            infoPanel.add(label);
        }
        
        // Add panels to frame
        mainPanel.add(rightPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
        
        // Initial log message
        gameLog.append("Welcome to Waste Management Game!\n");
        gameLog.append("Each player will take turns rolling the dice.\n");
        if (!players.isEmpty()) {
            gameLog.append("\n--- " + players.get(0).getName() + "'s Turn (" + players.get(0).getRole() + ") ---\n");
            highlightTile(0);
        }
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void setDiceImage(int value) {
        try {
            // Try to load images from resources
            ImageIcon icon = new ImageIcon(getClass().getResource("/resources/dice" + value + ".png"));
            diceImage.setIcon(icon);
        } catch (Exception e) {
            // Fallback if image not found - create a visual dice representation
            diceImage.setIcon(null);
            diceImage.setText(String.valueOf(value));
            diceImage.setFont(new Font("Arial", Font.BOLD, 48));
            diceImage.setBackground(Color.WHITE);
            diceImage.setOpaque(true);
            diceImage.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        }
    }
    
    private void rollDice() {
        // Disable interaction during animation
        setEnabled(false);
        
        // Animate the dice rolling
        new Thread(() -> {
            try {
                // Quick animation
                for (int i = 0; i < 8; i++) {
                    final int randomValue = random.nextInt(6) + 1;
                    SwingUtilities.invokeLater(() -> setDiceImage(randomValue));
                    Thread.sleep(100);
                }
                
                // Final dice value
                final int diceValue = random.nextInt(6) + 1;
                
                // Update UI on EDT
                SwingUtilities.invokeLater(() -> {
                    // Set final dice value
                    setDiceImage(diceValue);
                    
                    // Get current player and move
                    Player currentPlayer = gameManager.getCurrentPlayer();
                    gameManager.playTurn(currentPlayer, positionLabel, recyclablesLabel, foodWasteLabel, cashLabel);
                    
                    // Update visual position
                    highlightTile(currentPlayer.getPosition());
                    
                    // Next player
                    gameManager.nextTurn();
                    
                    // Re-enable interaction
                    setEnabled(true);
                });
                
            } catch (InterruptedException e) {
                System.out.println("Dice animation interrupted: " + e);
                setEnabled(true);
            }
        }).start();
    }

    private void highlightTile(int position) {
        // Reset all tile borders
        for (int i = 0; i < tileLabels.length; i++) {
            if (tileLabels[i] != null) {
                tileLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
        }
        
        // Highlight current tile
        if (tileLabels[position] != null) {
            tileLabels[position].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        }
    }
}