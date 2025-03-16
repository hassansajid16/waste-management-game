package wastemanagement;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.*;

/**
 * The GameManager class handles the core game logic for the Waste Management Game
 */
public class GameManager {
    // Game components
    private ArrayList<Player> players;
    private Board board;
    private Dice dice;
    private JTextArea gameLog;
    private int currentPlayerIndex;
    private JLabel currentPlayerLabel;
    private JLabel[] playerLabels;
    private JPanel playerStatsPanel;
    private GameTilePanel[] tilePanels;
    
 
    // Update the property threshold constant
private static final int ECO_SCORE_THRESHOLD = 50;
private static final int PUBLIC_INFLUENCE_THRESHOLD = 50;
private static final int PROPERTY_THRESHOLD = 7; // Changed from 3 to 7

// Update these constants for higher costs
private static final int GAME_COST = 2000; // Increased from 200 to 2000
private static final int PROPERTY_BASE_PRICE = 2000; // Increased from 1000
private static final int PROPERTY_BASE_RENT = 100; // Increased from 50
private static final int GO_MONEY = 3000; // Increased from 2000


    
    // Property ownership tracking
    private Map<Integer, Player> propertyOwners;
    private Random random = new Random();

    /**
     * Constructor for GameManager
     */
    public GameManager(JTextArea gameLog, JLabel currentPlayerLabel, JLabel[] playerLabels, JPanel playerStatsPanel) {
        players = new ArrayList<>();
        board = new Board();
        dice = new Dice();
        this.gameLog = gameLog;
        this.currentPlayerLabel = currentPlayerLabel;
        this.playerLabels = playerLabels;
        this.playerStatsPanel = playerStatsPanel;
        currentPlayerIndex = 0;
        propertyOwners = new HashMap<>();
    }

    /**
     * Add a player to the game
     */
    public void addPlayer(String name) {
        players.add(new Player(name));
    }
// Add this method to GameManager.java
public void setTilePanels(GameTilePanel[] tilePanels) {
    this.tilePanels = tilePanels;
}
    /**
     * Get all players
     */
    public ArrayList<Player> getPlayers() {
        return players;
    }

    /**
     * Get the current player
     */
    public Player getCurrentPlayer() {
        if (players.isEmpty()) {
            return null;
        }
        return players.get(currentPlayerIndex);
    }

    /**
     * Move to the next player's turn
     */
    public void nextTurn() {
        if (players.isEmpty()) {
            return;
        }
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        Player nextPlayer = getCurrentPlayer();
        
        // Update game log with next player
        gameLog.append("\n----- " + nextPlayer.getName() + "'s Turn (" + nextPlayer.getRole() + ") -----\n");
        
        // Update current player label and stats panel
        updateCurrentPlayerDisplay();
        updateAllPlayerStats();
    }
    
    /**
     * Update the display to highlight the current player
     */
    public void updateCurrentPlayerDisplay() {
        if (players.isEmpty()) {
            return;
        }
        
        Player currentPlayer = getCurrentPlayer();
        
        // Update the current player label
        currentPlayerLabel.setText("Current Player: " + currentPlayer.getName() + " (" + currentPlayer.getRole() + ")");
        
        // Highlight current player in player labels
        for (int i = 0; i < playerLabels.length; i++) {
            if (playerLabels[i] != null) {
                if (i == currentPlayerIndex) {
                    playerLabels[i].setForeground(Color.GREEN);
                    playerLabels[i].setText("➤ " + players.get(i).getName());
                } else {
                    playerLabels[i].setForeground(Color.WHITE);
                    playerLabels[i].setText(players.get(i).getName());
                }
            }
        }
    }
    
    /**
     * Update the statistics panel for all players
     */
    public void updateAllPlayerStats() {
        // Clear the stats panel
        playerStatsPanel.removeAll();
        
        // Add a header
        JLabel headerLabel = new JLabel("Player Statistics", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 14));
        headerLabel.setForeground(Color.WHITE);
        playerStatsPanel.add(headerLabel);
        
        // Add stats for each player
        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            
            // Create a panel for this player's stats
            JPanel playerPanel = new JPanel();
            playerPanel.setLayout(new BoxLayout(playerPanel, BoxLayout.Y_AXIS));
            playerPanel.setBackground(new Color(50, 70, 50));
            playerPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            // Player name with role
            JLabel nameLabel = new JLabel(player.getName() + " (" + player.getRole() + ")");
            nameLabel.setFont(new Font("Arial", Font.BOLD, 12));
            nameLabel.setForeground(i == currentPlayerIndex ? Color.GREEN : Color.WHITE);
            
            // Player stats
            JLabel posLabel = new JLabel("Position: " + player.getPosition());
            JLabel cashLabel = new JLabel("Cash: R" + player.getCash());
            
            // Property count
            JLabel propsLabel = new JLabel("Properties: " + player.getPropertyCount());
            
            // Role-specific stats
            JLabel specialStatLabel;
            if (player.getRole().equalsIgnoreCase("Business Owner")) {
                specialStatLabel = new JLabel("Eco Score: " + player.getEcoScore());
            } else {
                specialStatLabel = new JLabel("Public Influence: " + player.getPublicInfluence());
            }
            
            // Optional stats for compatibility
            JLabel recyclablesLabel = new JLabel("Recyclables: " + player.getRecyclables());
            JLabel foodLabel = new JLabel("Food Waste: " + player.getFoodWaste());
            
            // Style the labels
            JLabel[] labels = {posLabel, cashLabel, propsLabel, specialStatLabel, recyclablesLabel, foodLabel};
            for (JLabel label : labels) {
                label.setForeground(Color.WHITE);
                label.setFont(new Font("Arial", Font.PLAIN, 11));
                playerPanel.add(label);
            }
            
            // Add the name label at the top
            playerPanel.add(nameLabel, 0);
            
            // Add this player's panel to the stats panel
            playerStatsPanel.add(playerPanel);
        }
        
        // Refresh the panel
        playerStatsPanel.revalidate();
        playerStatsPanel.repaint();
    }

    /**
     * Calculate property price based on position
     */
    private int getPropertyPrice(int position) {
        // Make properties progressively more expensive around the board
        return PROPERTY_BASE_PRICE + (position * 20);
    }

    /**
     * Calculate rent based on position
     */
    private int getPropertyRent(int position) {
        // Base rent plus position factor
        return PROPERTY_BASE_RENT + (position * 10);
    }

    /**
     * Check if a player has completed their objectives
     */
    // Update the checkObjectiveCompletion method
private void checkObjectiveCompletion(Player player) {
    // Check role-specific objective
    if (player.getRole().equalsIgnoreCase("Business Owner") && 
        player.getEcoScore() >= ECO_SCORE_THRESHOLD && 
        !player.hasCompletedObjective1()) {
        
        player.completeObjective1();
        gameLog.append("🏆 " + player.getName() + " completed the Eco-friendly Business objective!\n");
    }
    
    if (player.getRole().equalsIgnoreCase("Community Leader") && 
        player.getPublicInfluence() >= PUBLIC_INFLUENCE_THRESHOLD && 
        !player.hasCompletedObjective1()) {
        
        player.completeObjective1();
        gameLog.append("🏆 " + player.getName() + " completed the Community Influence objective!\n");
    }
    
    // Check task objective (requires completing at least 7 tasks) - CHANGED FROM "Property Developer" TO "Task Master"
    if (player.getPropertyCount() >= PROPERTY_THRESHOLD && !player.hasCompletedObjective2()) {
        player.completeObjective2();
        gameLog.append("🏆 " + player.getName() + " completed the Task Master objective!\n");
    }
    
    // Check if all objectives are complete for game end condition
    if (player.hasCompletedAllObjectives()) {
        gameLog.append("🎉 CONGRATULATIONS! " + player.getName() + " has completed all objectives and won the game!\n");
    }
}
/**
 * Process waste deposits at the Waste Management Facility
 * Add this method to your GameManager class
 */
private void processWasteDeposits(Player player, JTextArea gameLog) {
    int recyclables = player.getRecyclables();
    int foodWaste = player.getFoodWaste();
    
    if (recyclables > 0 || foodWaste > 0) {
        gameLog.append("♻️ " + player.getName() + " deposits " + recyclables + 
                     " recyclables and " + foodWaste + " food waste at the Waste Management Facility!\n");
        
        // Award role-specific points based on waste deposits
        if (player.getRole().equalsIgnoreCase("Business Owner")) {
            int ecoPoints = (recyclables + foodWaste) * 2;
            player.increaseEcoScore(ecoPoints);
            gameLog.append("♻️ Eco Score increased by " + ecoPoints + " to " + player.getEcoScore() + "\n");
        } else if (player.getRole().equalsIgnoreCase("Community Leader")) {
            int influencePoints = (recyclables + foodWaste) * 2;
            player.increasePublicInfluence(influencePoints);
            gameLog.append("👥 Public Influence increased by " + influencePoints + " to " + player.getPublicInfluence() + "\n");
        }
        
        // Reset player's waste storage
        while (player.getRecyclables() > 0) {
            player.collectWaste("useRecyclables");
        }
        
        while (player.getFoodWaste() > 0) {
            player.collectWaste("useFoodWaste");
        }
    } else {
        gameLog.append("🗑️ " + player.getName() + " has no waste to deposit at the Waste Management Facility.\n");
    }
}
/**
 * Process a player's turn based on dice roll and position
 */
/**
 * Process a player's turn based on dice roll and position
 */
/**
 * Process a player's turn based on dice roll and position
 * Update this method in your GameManager class
 */
public void processTurn(Player player, int diceRoll, int oldPosition, int newPosition, JTextArea gameLog) {
    if (player == null) {
        return;
    }

    Tile currentTile = board.getTile(newPosition);
    String tileType = currentTile.getType();
    
    gameLog.append(player.getName() + " rolled a " + diceRoll + " and moved from position " + 
                 oldPosition + " to " + newPosition + " (" + currentTile.getName() + ")\n");
    
    // Check if player passed GO (Waste Management Facility)
    if (oldPosition > newPosition && oldPosition != 0) {
        // Player has passed GO (wrapped around the board)
        player.updateCash(GO_MONEY);
        gameLog.append("💰 " + player.getName() + " passed Waste Management Facility and collected R" + GO_MONEY + "!\n");
        
        // Process waste deposits when passing GO
        processWasteDeposits(player, gameLog);
    }
    
    // Process based on tile type
    switch (tileType) {
        case "StartSquare":
            // Landing on Waste Management Facility - Collect additional money
            player.updateCash(GO_MONEY);
            gameLog.append("💰 " + player.getName() + " landed on Waste Management Facility and collected R" + GO_MONEY + "!\n");
            
            // Process waste deposits when landing on GO
            processWasteDeposits(player, gameLog);
            break;
            
        case "SpecialTask":
            handleSpecialTask(player, gameLog);
            break;
            
        case "EcoChallenge":
            handleEcoChallenge(player, gameLog);
            break;
            
        default:
            gameLog.append(player.getName() + " is on a neutral tile.\n");
            break;
    }

    // Update UI
    updateAllPlayerStats();
    
    // Check if objectives have been completed
    checkObjectiveCompletion(player);
    
    // After processing turn, update property ownership display
    if (tilePanels != null) {
        updatePropertyDisplay(tilePanels);
    }
}
private void handleSpecialTask(Player player, JTextArea gameLog) {
    int position = player.getPosition();
    String tileName = board.getTile(position).getName();
    
    // First check if this square is already owned by another player
    Player currentOwner = propertyOwners.get(position);
    if (currentOwner != null && currentOwner != player) {
        // Square already has an owner who isn't the current player
        // Handle donation instead of takeover
        handleDonation(player, currentOwner, position, tileName);
        return;
    }
    
    // Original code continues for unowned squares
    gameLog.append("[QUIZ] " + player.getName() + " landed on a Special Task square!\n");
    
    // FIXED: Check if player has enough money BEFORE showing the dialog
    if (player.getCash() < 2000) {
        // Not enough money - just log the message and return
        gameLog.append("[FAILED] " + player.getName() + " doesn't have enough money (R2000) to participate!\n");
        return;
    }
    
    // Only show dialog if they have enough money
    int choice = JOptionPane.showConfirmDialog(
        null,
        "Would you like to take part in the BinNovaters Initiative for R2000?",
        "Special Task",
        JOptionPane.YES_NO_OPTION
    );
    
    if (choice == JOptionPane.YES_OPTION) {
        // We already checked they have enough money, so we can proceed
        player.updateCash(-2000);
        gameLog.append("[CASH] " + player.getName() + " spent R2000 to join the BinNovaters Initiative!\n");
        
        // Mark task as completed
        player.buyProperty(position);
        
        // IMPORTANT: Update ownership display immediately
        if (tilePanels != null && position < tilePanels.length) {
            tilePanels[position].setOwner(player);
            // Force the tile to repaint
            tilePanels[position].repaint();
            System.out.println("Set owner for position " + position + " to " + player.getName());
        } else {
            System.out.println("Failed to set owner - tilePanels null or invalid position: " + position);
        }
        
        // Store property ownership
        propertyOwners.put(position, player);
        
        // Launch the quiz
        SpecialTask.askQuestion(player, gameLog);
        
        // Special increase to role-specific scores for challenges
        if (player.getRole().equalsIgnoreCase("Business Owner")) {
            player.increaseEcoScore(5);
            gameLog.append("[ECO] Eco Score increased to " + player.getEcoScore() + "\n");
        } else if (player.getRole().equalsIgnoreCase("Community Leader")) {
            player.increasePublicInfluence(5);
            gameLog.append("[INFLUENCE] Public Influence increased to " + player.getPublicInfluence() + "\n");
        }
        
        // Update stats to refresh UI
        updateAllPlayerStats();
        
        // Update property display again after the quiz completion
        updatePropertyDisplay(tilePanels);
    } else {
        gameLog.append("[INFO] " + player.getName() + " declined to participate in the BinNovaters Initiative.\n");
    }
}
private void handleEcoChallenge(Player player, JTextArea gameLog) {
    int position = player.getPosition();
    String tileName = board.getTile(position).getName();
    
    // First check if this square is already owned by another player
    Player currentOwner = propertyOwners.get(position);
    if (currentOwner != null && currentOwner != player) {
        // Square already has an owner who isn't the current player
        // Handle donation instead of takeover
        handleDonation(player, currentOwner, position, tileName);
        return;
    }
    
    // Original code continues for unowned squares
    gameLog.append("[SORTING] " + player.getName() + " landed on an Eco Challenge square!\n");
    
    // FIXED: Check if player has enough money BEFORE showing the dialog
    if (player.getCash() < 2000) {
        // Not enough money - just log the message and return
        gameLog.append("[FAILED] " + player.getName() + " doesn't have enough money (R2000) to participate!\n");
        return;
    }
    
    // Only show dialog if they have enough money
    int choice = JOptionPane.showConfirmDialog(
        null,
        "Would you like to take part in the BinNovaters Initiative for R2000?",
        "Eco Challenge",
        JOptionPane.YES_NO_OPTION
    );
    
    if (choice == JOptionPane.YES_OPTION) {
        // We already checked they have enough money, so we can proceed
        player.updateCash(-2000);
        gameLog.append("[CASH] " + player.getName() + " spent R2000 to join the BinNovaters Initiative!\n");
        
        // Mark task as completed
        player.buyProperty(position);
        
        // IMPORTANT: Update ownership display immediately
        if (tilePanels != null && position < tilePanels.length) {
            tilePanels[position].setOwner(player);
            // Force the tile to repaint
            tilePanels[position].repaint();
            System.out.println("Set owner for position " + position + " to " + player.getName());
        } else {
            System.out.println("Failed to set owner - tilePanels null or invalid position: " + position);
        }
        
        // Store property ownership
        propertyOwners.put(position, player);
        
        // Launch the waste sorting game
        WasteSortingLauncher.launchSortingGame(player, gameLog);
        
        // Special increase to role-specific scores for challenges
        if (player.getRole().equalsIgnoreCase("Business Owner")) {
            player.increaseEcoScore(8);
            gameLog.append("[ECO] Eco Score significantly increased to " + player.getEcoScore() + "\n");
        } else if (player.getRole().equalsIgnoreCase("Community Leader")) {
            player.increasePublicInfluence(8);
            gameLog.append("[INFLUENCE] Public Influence significantly increased to " + player.getPublicInfluence() + "\n");
        }
        
        // Update stats to refresh UI
        updateAllPlayerStats();
        
        // Update property display again after the game completion
        updatePropertyDisplay(tilePanels);
    } else {
        gameLog.append("[INFO] " + player.getName() + " declined to participate in the BinNovaters Initiative.\n");
    }
}
    // Add these methods to the GameManager class

/**
 * Get the current player index
 */
public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
}
/**
 * Handle donation when a player lands on a square owned by another player
 */
private void handleDonation(Player player, Player owner, int position, String tileName) {
    // Base suggested donation amount
    int suggestedDonation = 100; 
    
    // Show dialog to ask for donation amount
    String[] options = {
        "Donate R" + suggestedDonation, 
        "Donate R" + (suggestedDonation/2), 
        "Donate Custom Amount", 
        "Skip Donation"
    };
    
    int choice = JOptionPane.showOptionDialog(
        null,
        tileName + " is already managed by " + owner.getName() + ".\n" +
        "Would you like to make a donation to support this initiative?",
        "Support " + tileName,
        JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE,
        null,
        options,
        options[0]
    );
    
    int donationAmount = 0;
    
    switch (choice) {
        case 0: // Full suggested donation
            donationAmount = suggestedDonation;
            break;
        case 1: // Half donation
            donationAmount = suggestedDonation / 2;
            break;
        case 2: // Custom donation
            String input = JOptionPane.showInputDialog(
                null,
                "Enter donation amount (R0-R" + player.getCash() + "):",
                "Custom Donation",
                JOptionPane.PLAIN_MESSAGE
            );
            try {
                donationAmount = Integer.parseInt(input);
                // Ensure donation is within valid range
                donationAmount = Math.max(0, Math.min(donationAmount, player.getCash()));
            } catch (NumberFormatException e) {
                donationAmount = 0; // Invalid input, no donation
            }
            break;
        default: // Skip donation
            donationAmount = 0;
            break;
    }
    
    // Process the donation
    if (donationAmount > 0) {
        // Check if player has enough cash
        if (player.getCash() >= donationAmount) {
            player.updateCash(-donationAmount);
            owner.updateCash(donationAmount);
            
            gameLog.append("🤝 " + player.getName() + " donated R" + donationAmount + " to " + 
                         owner.getName() + " for " + tileName + ".\n");
                         
            // Give a small role-specific bonus to both players
            if (player.getRole().equalsIgnoreCase("Business Owner")) {
                player.increaseEcoScore(1);
                gameLog.append("♻️ " + player.getName() + " gained 1 Eco Score point for donating!\n");
            } else if (player.getRole().equalsIgnoreCase("Community Leader")) {
                player.increasePublicInfluence(1);
                gameLog.append("👥 " + player.getName() + " gained 1 Public Influence point for donating!\n");
            }
            
            if (owner.getRole().equalsIgnoreCase("Business Owner")) {
                owner.increaseEcoScore(2);
                gameLog.append("♻️ " + owner.getName() + " gained 2 Eco Score points from the donation!\n");
            } else if (owner.getRole().equalsIgnoreCase("Community Leader")) {
                owner.increasePublicInfluence(2);
                gameLog.append("👥 " + owner.getName() + " gained 2 Public Influence points from the donation!\n");
            }
        } else {
            // Player doesn't have enough cash
            gameLog.append("❌ " + player.getName() + " doesn't have enough cash to donate R" + 
                         donationAmount + ".\n");
        }
    } else {
        gameLog.append("❌ " + player.getName() + " chose not to donate to " + 
                     owner.getName() + " for " + tileName + ".\n");
    }
    
    // Update UI
    updateAllPlayerStats();
}
/**
 * Set the current player index directly
 */
public void setCurrentPlayerIndex(int index) {
    if (index >= 0 && index < players.size()) {
        currentPlayerIndex = index;
    }
}
    // Fix #5: Update property selling/buying text in GameManager.java - handlePropertyTile
private void handlePropertyTile(Player player, int position, String propertyName) {
    Player propertyOwner = propertyOwners.get(position);
    
    if (propertyOwner == null) {
        // Property is not owned, player can buy it
        int propertyPrice = getPropertyPrice(position);
        
        gameLog.append(" " + player.getName() + " landed on " + propertyName + " (Price: R" + propertyPrice + ")\n");
        
        // Check if player has enough money
        if (player.getCash() >= propertyPrice) {
            // Ask if player wants to buy the property
            int choice = JOptionPane.showConfirmDialog(
                null,
                "Would you like to buy " + propertyName + " for R" + propertyPrice + "?",
                "Property Purchase",
                JOptionPane.YES_NO_OPTION
            );
            
            if (choice == JOptionPane.YES_OPTION) {
                player.updateCash(-propertyPrice);
                player.buyProperty(position);
                propertyOwners.put(position, player);
                
                gameLog.append("💰 " + player.getName() + " purchased " + propertyName + " for R" + propertyPrice + "!\n");
                
                // Increase role-specific scores when buying property
                if (player.getRole().equalsIgnoreCase("Business Owner")) {
                    player.increaseEcoScore(5);
                    gameLog.append("♻️ Eco Score increased to " + player.getEcoScore() + "\n");
                } else if (player.getRole().equalsIgnoreCase("Community Leader")) {
                    player.increasePublicInfluence(5);
                    gameLog.append("👥 Public Influence increased to " + player.getPublicInfluence() + "\n");
                }
                
                // Update UI
                updateAllPlayerStats();
            } else {
                gameLog.append("❌ " + player.getName() + " decided not to buy " + propertyName + ".\n");
            }
        } else {
            gameLog.append("❌ " + player.getName() + " doesn't have enough money to buy " + propertyName + ".\n");
        }
    } else if (propertyOwner != player) {
        // Property is owned by another player, make a donation
        int donation = getPropertyRent(position);
        gameLog.append("🤝 " + player.getName() + " landed on " + propertyOwner.getName() + "'s property " + 
                     propertyName + ". Must donate R" + donation + ".\n");
        
        if (player.getCash() >= donation) {
            player.updateCash(-donation);
            propertyOwner.updateCash(donation);
            
            gameLog.append("💰 " + player.getName() + " donated R" + donation + " to " + propertyOwner.getName() + ".\n");
        } else {
            // Player can't pay full donation
            int availableCash = player.getCash();
            player.updateCash(-availableCash);
            propertyOwner.updateCash(availableCash);
            
            gameLog.append("⚠️ " + player.getName() + " could only donate R" + availableCash + " to " + 
                         propertyOwner.getName() + ".\n");
        }
    } else {
        // Player owns this property
        gameLog.append("✅ " + player.getName() + " landed on their own property " + propertyName + ".\n");
    }
}
    /**
     * Handle landing on a Community Fund square
     */
    private void handleCommunityFund(Player player) {
        // Random bonus between 50 and 150
        int bonus = random.nextInt(1001) + 500;
        gameLog.append(" " + player.getName() + " landed on Community Fund and received R" + bonus + "!\n");
        player.updateCash(bonus);
        
        // Also increase role-specific scores
        if (player.getRole().equalsIgnoreCase("Business Owner")) {
            int ecoBonus = random.nextInt(6) + 3; // 3-8 points
            player.increaseEcoScore(ecoBonus);
            gameLog.append("♻️ Eco Score increased by " + ecoBonus + " to " + player.getEcoScore() + "\n");
        } else if (player.getRole().equalsIgnoreCase("Community Leader")) {
            int influenceBonus = random.nextInt(6) + 3; // 3-8 points
            player.increasePublicInfluence(influenceBonus);
            gameLog.append("👥 Public Influence increased by " + influenceBonus + " to " + player.getPublicInfluence() + "\n");
        }
    }
    
    /**
     * Update the UI for property ownership
     */
    public void updatePropertyDisplay(GameTilePanel[] tilePanels) {
        for (Map.Entry<Integer, Player> entry : propertyOwners.entrySet()) {
            int position = entry.getKey();
            Player owner = entry.getValue();
            if (position < tilePanels.length && tilePanels[position] != null) {
                tilePanels[position].setOwner(owner);
            }
        }
    }
    
    /**
     * Check if a player owns a property
     */
    public Player getPropertyOwner(int position) {
        return propertyOwners.get(position);
    }
    
    /**
     * Get the player's current JTextArea for game logs
     */
    public JTextArea getGameLog() {
        return gameLog;
    }
}