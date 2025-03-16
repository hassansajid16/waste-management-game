package wastemanagement;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * An improved mini-game that allows players to sort waste items into appropriate bins.
 * Includes proper validation and feedback for bin selections.
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
    private int correctAnswers = 0;
    private int incorrectAnswers = 0;
    private final JTextArea gameLog;
    
    // UI Components
    private JLabel timeLabel;
    private JLabel scoreLabel;
    private JLabel itemLabel;
    private JLabel feedbackLabel;
    private JButton recyclablesButton;
    private JButton organicButton;
    private JButton generalWasteButton;
    private JButton hazardousButton;
    private JPanel gamePanel;
    
    // Item to category mapping
    private Map<String, WasteCategory> itemCategoryMap = new HashMap<>();
    
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
        
        setSize(650, 500);
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
        addWasteItem("Plastic Bottle", WasteCategory.RECYCLABLE);
        addWasteItem("Newspaper", WasteCategory.RECYCLABLE);
        addWasteItem("Glass Jar", WasteCategory.RECYCLABLE);
        addWasteItem("Aluminum Can", WasteCategory.RECYCLABLE);
        addWasteItem("Cardboard Box", WasteCategory.RECYCLABLE);
        addWasteItem("Metal Tin", WasteCategory.RECYCLABLE);
        addWasteItem("Paper Bag", WasteCategory.RECYCLABLE);
        
        // Organic/Food waste items
        addWasteItem("Banana Peel", WasteCategory.ORGANIC);
        addWasteItem("Apple Core", WasteCategory.ORGANIC);
        addWasteItem("Coffee Grounds", WasteCategory.ORGANIC);
        addWasteItem("Vegetable Scraps", WasteCategory.ORGANIC);
        addWasteItem("Eggshells", WasteCategory.ORGANIC);
        addWasteItem("Tea Bags", WasteCategory.ORGANIC);
        addWasteItem("Grass Clippings", WasteCategory.ORGANIC);
        
        // General waste items
        addWasteItem("Styrofoam Cup", WasteCategory.GENERAL_WASTE);
        addWasteItem("Candy Wrapper", WasteCategory.GENERAL_WASTE);
        addWasteItem("Used Tissue", WasteCategory.GENERAL_WASTE);
        addWasteItem("Plastic Bag", WasteCategory.GENERAL_WASTE);
        addWasteItem("Disposable Diaper", WasteCategory.GENERAL_WASTE);
        addWasteItem("Chip Packets", WasteCategory.GENERAL_WASTE);
        addWasteItem("Plastic Cutlery", WasteCategory.GENERAL_WASTE);
        
        // Hazardous waste items
        addWasteItem("Battery", WasteCategory.HAZARDOUS);
        addWasteItem("Paint Can", WasteCategory.HAZARDOUS);
        addWasteItem("Light Bulb", WasteCategory.HAZARDOUS);
        addWasteItem("Motor Oil", WasteCategory.HAZARDOUS);
        addWasteItem("Electronic Waste", WasteCategory.HAZARDOUS);
        addWasteItem("Medication", WasteCategory.HAZARDOUS);
        addWasteItem("Pesticides", WasteCategory.HAZARDOUS);
    }
    
    /**
     * Helper method to add waste items and build the item-category map
     */
    private void addWasteItem(String name, WasteCategory category) {
        wasteItems.add(new WasteItem(name, category));
        itemCategoryMap.put(name, category);
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
        
        JLabel playerLabel = new JLabel("Player: " + currentPlayer.getName() + " (" + currentPlayer.getRole() + ")");
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
        
        // Item display - improved with icon hint
        JPanel itemDisplayPanel = new JPanel(new BorderLayout(10, 10));
        itemDisplayPanel.setOpaque(true);
        itemDisplayPanel.setBackground(Color.WHITE);
        itemDisplayPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        itemLabel = new JLabel("", SwingConstants.CENTER);
        itemLabel.setFont(new Font("Arial", Font.BOLD, 28));
        
        // Add feedback label
        feedbackLabel = new JLabel("", SwingConstants.CENTER);
        feedbackLabel.setFont(new Font("Arial", Font.ITALIC, 14));
        feedbackLabel.setForeground(new Color(50, 50, 50));
        
        itemDisplayPanel.add(itemLabel, BorderLayout.CENTER);
        itemDisplayPanel.add(feedbackLabel, BorderLayout.SOUTH);
        
        itemPanel.add(itemDisplayPanel, BorderLayout.CENTER);
        
        // Progress panel
        JPanel progressPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        progressPanel.setOpaque(false);
        JLabel progressLabel = new JLabel("Items sorted: 0   |   Correct: 0   |   Incorrect: 0");
        progressLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        progressPanel.add(progressLabel);
        
        // Bin selection panel with improved look
        JPanel binPanel = new JPanel(new GridLayout(1, 4, 15, 0));
        binPanel.setOpaque(false);
        binPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Create bin buttons with proper colors and icons
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
        button.setForeground(Color.black);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Add action listener with proper category validation
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
            feedbackLabel.setText("Select the correct bin");
            feedbackLabel.setForeground(Color.GRAY);
            
            // Clear any highlight from previous sorting
            resetBinButtonStyles();
        }
    }
    
    /**
     * Reset all bin buttons to their default style
     */
    private void resetBinButtonStyles() {
        recyclablesButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        organicButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        generalWasteButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        hazardousButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
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
            
            // Update counters
            itemsProcessed++;
            if (correct) {
                correctAnswers++;
            } else {
                incorrectAnswers++;
            }
            
            // Update score label
            scoreLabel.setText("Score: " + score);
            
            // Visual feedback
            JButton correctButton = null;
            switch (currentItem.getCategory()) {
                case RECYCLABLE:
                    correctButton = recyclablesButton;
                    break;
                case ORGANIC:
                    correctButton = organicButton;
                    break;
                case GENERAL_WASTE:
                    correctButton = generalWasteButton;
                    break;
                case HAZARDOUS:
                    correctButton = hazardousButton;
                    break;
            }
            
            // Highlight correct button
            if (correctButton != null) {
                correctButton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.GREEN, 3),
                    BorderFactory.createEmptyBorder(8, 8, 8, 8)
                ));
            }
            
            // Update feedback label
            if (correct) {
                feedbackLabel.setText("Correct! " + currentItem.getName() + " belongs in the " + 
                                     selectedCategory.toString().toLowerCase().replace('_', ' ') + " bin.");
                feedbackLabel.setForeground(new Color(0, 150, 0));
            } else {
                feedbackLabel.setText("Incorrect. " + currentItem.getName() + " belongs in the " + 
                                     currentItem.getCategory().toString().toLowerCase().replace('_', ' ') + " bin.");
                feedbackLabel.setForeground(new Color(200, 0, 0));
            }
            
            // Wait a moment before showing next item
            Timer delayTimer = new Timer();
            delayTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    SwingUtilities.invokeLater(() -> showNextItem());
                    delayTimer.cancel();
                }
            }, 1500);
        }
    }
    
    private void endGame() {
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        
        // Calculate rewards based on score
        final int recyclablesEarned = Math.max(0, correctAnswers / 2);
        final int foodWasteEarned = Math.max(0, correctAnswers / 3);
        final int cashEarned = Math.max(0, score * 10);
        
        // Award resources to player
        for (int i = 0; i < recyclablesEarned; i++) {
            currentPlayer.collectWaste("recyclables");
        }
        
        for (int i = 0; i < foodWasteEarned; i++) {
            currentPlayer.collectWaste("foodWaste");
        }
        
        currentPlayer.updateCash(cashEarned);
        
        // Calculate accuracy percentage
        int accuracy = (itemsProcessed > 0) ? (correctAnswers * 100 / itemsProcessed) : 0;
        
        // Update game log in main game
        gameLog.append("\n " + currentPlayer.getName() + " completed the Waste Sorting Challenge!\n");
        gameLog.append("\n " + currentPlayer.getName() + " completed the Waste Sorting Challenge!\n");
        gameLog.append(" Items sorted: " + itemsProcessed + " | Correct: " + correctAnswers + 
                      " | Incorrect: " + incorrectAnswers + " | Accuracy: " + accuracy + "%\n");
        gameLog.append(" Final score: " + score + "\n");
        gameLog.append(" Rewards: " + recyclablesEarned + " recyclables, " + 
                     foodWasteEarned + " food waste, R" + cashEarned + " cash\n");
        
        // Add role specific bonuses
        if (currentPlayer.getRole().equalsIgnoreCase("Business Owner")) {
            int ecoBonus = Math.max(1, accuracy / 20);
            currentPlayer.increaseEcoScore(ecoBonus);
            gameLog.append(currentPlayer.getName() + " gained " + ecoBonus + " Eco Score points!\n");
        } else if (currentPlayer.getRole().equalsIgnoreCase("Community Leader")) {
            int influenceBonus = Math.max(1, accuracy / 20);
            currentPlayer.increasePublicInfluence(influenceBonus);
            gameLog.append(currentPlayer.getName() + " gained " + influenceBonus + " Public Influence points!\n");
        }
        
        // Show results dialog with more details
        SwingUtilities.invokeLater(() -> {
            JPanel resultsPanel = new JPanel();
            resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
            resultsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            
            JLabel titleLabel = new JLabel("Waste Sorting Challenge Results", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel scoreLabel = new JLabel("Final Score: " + score + " points", SwingConstants.CENTER);
            scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
            scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel statsLabel = new JLabel("<html><center>" +
                                          "Items Sorted: " + itemsProcessed + "<br>" +
                                          "Correct: " + correctAnswers + "<br>" +
                                          "Incorrect: " + incorrectAnswers + "<br>" +
                                          "Accuracy: " + accuracy + "%</center></html>", SwingConstants.CENTER);
            statsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            JLabel rewardsLabel = new JLabel("<html><center><b>Rewards:</b><br>" +
                                           "• " + recyclablesEarned + " recyclables<br>" +
                                           "• " + foodWasteEarned + " food waste<br>" +
                                           "• R" + cashEarned + " cash</center></html>", SwingConstants.CENTER);
            rewardsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            rewardsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            resultsPanel.add(titleLabel);
            resultsPanel.add(Box.createVerticalStrut(10));
            resultsPanel.add(scoreLabel);
            resultsPanel.add(Box.createVerticalStrut(10));
            resultsPanel.add(statsLabel);
            resultsPanel.add(Box.createVerticalStrut(15));
            resultsPanel.add(rewardsLabel);
            
            JOptionPane.showMessageDialog(this,
                resultsPanel,
                "Challenge Complete!",
                JOptionPane.INFORMATION_MESSAGE);
            
            dispose();
        });
    }

    
    // For testing without the main game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Player testPlayer = new Player("Test Player");
            testPlayer.setRole("Business Owner");
            JTextArea testLog = new JTextArea();
            new WasteSortingGame(testPlayer, testLog);
        });
    }
}