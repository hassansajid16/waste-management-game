package wastemanagement;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.border.TitledBorder; // Add this import
/**
 * A simplified mini-game that allows players to sort waste items into appropriate bins.
 * This version uses text-only representation for waste items.
 */
public class WasteSortingGame extends JFrame {
    // Constants
    private static final int GAME_DURATION = 60; // Game duration in seconds
    private static final int POINTS_FOR_CORRECT = 10;
    private static final int POINTS_FOR_INCORRECT = -5;
    
    // Game state
    private Player currentPlayer;
    private int score = 0;
    private int timeRemaining = GAME_DURATION;
    private Random random = new Random();
    private Timer gameTimer;
    private ArrayList<WasteItem> wasteItems = new ArrayList<>();
    private WasteItem currentItem;
    private int itemsProcessed = 0;
    private final JTextArea gameLog;
    
    // UI Components
    private JLabel timeLabel;
    private JLabel scoreLabel;
    private JLabel itemLabel;
    private JButton recyclablesButton;
    private JButton organicButton;
    private JButton generalWasteButton;
    private JButton hazardousButton;
    private JPanel gamePanel;
    
    // Waste categories
    public enum WasteCategory {
        RECYCLABLE, ORGANIC, GENERAL_WASTE, HAZARDOUS
    }
    
    // Simplified Waste item class
    private static class WasteItem {
        private final String name;
        private final WasteCategory category;
        
        public WasteItem(String name, WasteCategory category) {
            this.name = name;
            this.category = category;
        }
        
        public String getName() {
            return name;
        }
        
        public WasteCategory getCategory() {
            return category;
        }
    }
    
    /**
     * Creates a new waste sorting game for the specified player
     */
    public WasteSortingGame(Player player, JTextArea gameLog) {
        super("Waste Sorting Challenge");
        System.out.println("Creating Waste Sorting game for player: " + player.getName());
        
        this.currentPlayer = player;
        this.gameLog = gameLog;
        
        // Make this window appear on top
        setAlwaysOnTop(true);
        toFront();
        requestFocus();
        
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        try {
            setupGameData();
            setupUI();
            startGame();
        } catch (Exception e) {
            System.err.println("Error setting up Waste Sorting game: " + e.getMessage());
            e.printStackTrace();
            dispose(); // Clean up if setup fails
        }
    }
    
    /**
     * Sets up the waste item data for the game
     */
    private void setupGameData() {
        // Recyclable items
        wasteItems.add(new WasteItem("Plastic Bottle", WasteCategory.RECYCLABLE));
        wasteItems.add(new WasteItem("Newspaper", WasteCategory.RECYCLABLE));
        wasteItems.add(new WasteItem("Glass Jar", WasteCategory.RECYCLABLE));
        wasteItems.add(new WasteItem("Aluminum Can", WasteCategory.RECYCLABLE));
        wasteItems.add(new WasteItem("Cardboard Box", WasteCategory.RECYCLABLE));
        
        // Organic/Food waste items
        wasteItems.add(new WasteItem("Banana Peel", WasteCategory.ORGANIC));
        wasteItems.add(new WasteItem("Apple Core", WasteCategory.ORGANIC));
        wasteItems.add(new WasteItem("Coffee Grounds", WasteCategory.ORGANIC));
        wasteItems.add(new WasteItem("Vegetable Scraps", WasteCategory.ORGANIC));
        wasteItems.add(new WasteItem("Eggshells", WasteCategory.ORGANIC));
        
        // General waste items
        wasteItems.add(new WasteItem("Styrofoam Cup", WasteCategory.GENERAL_WASTE));
        wasteItems.add(new WasteItem("Candy Wrapper", WasteCategory.GENERAL_WASTE));
        wasteItems.add(new WasteItem("Used Tissue", WasteCategory.GENERAL_WASTE));
        wasteItems.add(new WasteItem("Plastic Bag", WasteCategory.GENERAL_WASTE));
        wasteItems.add(new WasteItem("Disposable Diaper", WasteCategory.GENERAL_WASTE));
        
        // Hazardous waste items
        wasteItems.add(new WasteItem("Battery", WasteCategory.HAZARDOUS));
        wasteItems.add(new WasteItem("Paint Can", WasteCategory.HAZARDOUS));
        wasteItems.add(new WasteItem("Light Bulb", WasteCategory.HAZARDOUS));
        wasteItems.add(new WasteItem("Motor Oil", WasteCategory.HAZARDOUS));
        wasteItems.add(new WasteItem("Electronic Waste", WasteCategory.HAZARDOUS));
    }
    
    /**
     * Sets up the user interface for the game
     */
    private void setupUI() {
        // Main panel
        gamePanel = new JPanel(new BorderLayout(10, 10));
        gamePanel.setBackground(new Color(240, 250, 240));
        gamePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header panel with score and time
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        // Player and score info
        JPanel infoPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        infoPanel.setOpaque(false);
        
        JLabel playerLabel = new JLabel("Player: " + currentPlayer.getName());
        playerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        infoPanel.add(playerLabel);
        infoPanel.add(scoreLabel);
        
        // Timer
        timeLabel = new JLabel("Time: " + timeRemaining + "s");
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        headerPanel.add(infoPanel, BorderLayout.WEST);
        headerPanel.add(timeLabel, BorderLayout.EAST);
        
        // Waste item display panel
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.setOpaque(false);
        itemPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(100, 150, 100), 2),
                "Sort This Item",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14)
            ),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Item display - simplified text-only version
        itemLabel = new JLabel("", SwingConstants.CENTER);
        itemLabel.setFont(new Font("Arial", Font.BOLD, 24));
        itemLabel.setOpaque(true);
        itemLabel.setBackground(Color.WHITE);
        itemLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        itemPanel.add(itemLabel, BorderLayout.CENTER);
        
        // Bin selection panel
        JPanel binPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        binPanel.setOpaque(false);
        binPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Create bin buttons with proper colors
        recyclablesButton = createBinButton("Recyclables", new Color(0, 150, 200), WasteCategory.RECYCLABLE);
        organicButton = createBinButton("Organic", new Color(0, 150, 0), WasteCategory.ORGANIC);
        generalWasteButton = createBinButton("General Waste", new Color(150, 150, 150), WasteCategory.GENERAL_WASTE);
        hazardousButton = createBinButton("Hazardous", new Color(200, 50, 50), WasteCategory.HAZARDOUS);
        
        binPanel.add(recyclablesButton);
        binPanel.add(organicButton);
        binPanel.add(generalWasteButton);
        binPanel.add(hazardousButton);
        
        // Instructions panel
        JPanel instructionsPanel = new JPanel();
        instructionsPanel.setOpaque(false);
        
        JLabel instructionsLabel = new JLabel("<html><center>Sort waste items into the correct bins!<br>" +
                                              "+" + POINTS_FOR_CORRECT + " points for correct sorting<br>" +
                                              POINTS_FOR_INCORRECT + " points for incorrect sorting</center></html>");
        instructionsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        instructionsPanel.add(instructionsLabel);
        
        // Add components to main panel
        gamePanel.add(headerPanel, BorderLayout.NORTH);
        gamePanel.add(itemPanel, BorderLayout.CENTER);
        gamePanel.add(binPanel, BorderLayout.SOUTH);
        
        // Set content pane
        setContentPane(gamePanel);
        
        // Handle window closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                endGame();
            }
        });
    }
    
    /**
     * Creates a styled button for a waste bin
     */
    private JButton createBinButton(String name, Color color, WasteCategory category) {
        JButton button = new JButton(name);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(10, 5, 10, 5)
        ));
        
        // Add action listener with lambda
        button.addActionListener(e -> {
            if (currentItem != null) {
                checkSorting(category);
            }
        });
        
        return button;
    }
    
    /**
     * Starts the waste sorting game
     */
    private void startGame() {
        // Show the first waste item
        showNextItem();
        
        // Start the timer
        gameTimer = new Timer();
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (timeRemaining > 0) {
                    timeRemaining--;
                    updateTimeLabel();
                    
                    // When time runs out, end the game
                    if (timeRemaining <= 0) {
                        SwingUtilities.invokeLater(() -> endGame());
                    }
                }
            }
        }, 1000, 1000);
    }
    
    /**
     * Updates the time remaining label
     */
    private void updateTimeLabel() {
        SwingUtilities.invokeLater(() -> {
            timeLabel.setText("Time: " + timeRemaining + "s");
            
            // Make the timer red when time is running out
            if (timeRemaining <= 10) {
                timeLabel.setForeground(Color.RED);
            }
        });
    }
    
    /**
     * Displays the next random waste item
     */
    private void showNextItem() {
        if (wasteItems.size() > 0) {
            int index = random.nextInt(wasteItems.size());
            currentItem = wasteItems.get(index);
            
            // Update UI with item name
            itemLabel.setText(currentItem.getName());
            
            // Set background color based on category (for visual hint)
            switch (currentItem.getCategory()) {
                case RECYCLABLE:
                    itemLabel.setBackground(new Color(220, 240, 255)); // Light blue
                    break;
                case ORGANIC:
                    itemLabel.setBackground(new Color(220, 255, 220)); // Light green
                    break;
                case GENERAL_WASTE:
                    itemLabel.setBackground(new Color(240, 240, 240)); // Light gray
                    break;
                case HAZARDOUS:
                    itemLabel.setBackground(new Color(255, 220, 220)); // Light red
                    break;
                default:
                    itemLabel.setBackground(Color.WHITE);
            }
        }
    }
    
    /**
     * Checks if the player sorted the waste correctly
     * @param selectedCategory The category selected by the player
     */
    private void checkSorting(WasteCategory selectedCategory) {
        if (currentItem != null) {
            boolean correct = (selectedCategory == currentItem.getCategory());
            
            // Update score
            int points = correct ? POINTS_FOR_CORRECT : POINTS_FOR_INCORRECT;
            score += points;
            
            // Update score label
            scoreLabel.setText("Score: " + score);
            
            // Visual feedback
            flashFeedback(correct);
            
            // Show next item
            itemsProcessed++;
            showNextItem();
        }
    }
    
    /**
     * Provides visual feedback for sorting attempt
     * @param correct Whether the sorting was correct
     */
    private void flashFeedback(boolean correct) {
        final Color originalColor = gamePanel.getBackground();
        Color feedbackColor = correct ? new Color(200, 255, 200) : new Color(255, 200, 200);
        
        gamePanel.setBackground(feedbackColor);
        
        // Reset after a short delay
        Timer flashTimer = new Timer();
        flashTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> gamePanel.setBackground(originalColor));
                flashTimer.cancel();
            }
        }, 300);
    }
    
    /**
     * Ends the game and awards resources based on performance
     */
    private void endGame() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        
        // Calculate rewards (make all variables final for lambda use)
        final int recyclablesEarned = Math.max(0, (int)(score / 10.0));
        final int foodWasteEarned = Math.max(0, (int)(score / 15.0));
        final int cashEarned = Math.max(0, score * 5);
        final int finalItemsProcessed = itemsProcessed;
        final int finalScore = score;
        
        // Award resources to player
        for (int i = 0; i < recyclablesEarned; i++) {
            currentPlayer.collectWaste("recyclables");
        }
        
        for (int i = 0; i < foodWasteEarned; i++) {
            currentPlayer.collectWaste("foodWaste");
        }
        
        currentPlayer.updateCash(cashEarned);
        
        // Update game log in main game
        gameLog.append("\n " + currentPlayer.getName() + " successfully completed the BinNovaters Initiative!\n");
        gameLog.append(" Items processed: " + finalItemsProcessed + ", Final score: " + finalScore + "\n");
        gameLog.append(" Rewards: " + recyclablesEarned + " recyclables, " + 
                     foodWasteEarned + " food waste, R" + cashEarned + " cash\n");
        
        // Show results dialog
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this,
                "Challenge complete!\n\n" +
                "Items Processed: " + finalItemsProcessed + "\n" +
                "Final Score: " + finalScore + "\n\n" +
                "Rewards:\n" +
                "• " + recyclablesEarned + " recyclables\n" +
                "• " + foodWasteEarned + " food waste\n" +
                "• R" + cashEarned + " cash",
                "Waste Sorting Results",
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
        });
    }
    
    // For testing without the main game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Player testPlayer = new Player("Test Player");
            JTextArea testLog = new JTextArea();
            new WasteSortingGame(testPlayer, testLog);
        });
    }
}