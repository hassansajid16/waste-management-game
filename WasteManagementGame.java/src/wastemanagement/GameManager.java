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
    
    // Game constants
    private static final int ECO_SCORE_THRESHOLD = 50;
    private static final int PUBLIC_INFLUENCE_THRESHOLD = 50;
    private static final int PROPERTY_BASE_PRICE = 100;
    private static final int PROPERTY_BASE_RENT = 50;
    private static final int GO_MONEY = 2000;
    
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
        
        // Check property objective (requires owning at least 3 properties)
        if (player.getPropertyCount() >= 3 && !player.hasCompletedObjective2()) {
            player.completeObjective2();
            gameLog.append("🏆 " + player.getName() + " completed the Property Developer objective!\n");
        }
        
        // Check if all objectives are complete for game end condition
        if (player.hasCompletedAllObjectives()) {
            gameLog.append("🎉 CONGRATULATIONS! " + player.getName() + " has completed all objectives and won the game!\n");
        }
    }

    /**
     * Process a player's turn based on dice roll and position
     */
    public void processTurn(Player player, int diceRoll, int oldPosition, int newPosition, JTextArea gameLog) {
        if (player == null) {
            return;
        }

        Tile currentTile = board.getTile(newPosition);
        String tileType = currentTile.getType();
        
        gameLog.append(player.getName() + " rolled a " + diceRoll + " and moved from position " + 
                     oldPosition + " to " + newPosition + " (" + currentTile.getName() + ")\n");
        
        // Check if player passed GO
        if (oldPosition > newPosition && oldPosition != 0) {
            // Player has passed GO (wrapped around the board)
            player.updateCash(GO_MONEY);
            gameLog.append("💰 " + player.getName() + " passed GO and collected R" + GO_MONEY + "!\n");
        }
        
        // Process based on tile type
        switch (tileType) {
            case "Property":
                handlePropertyTile(player, newPosition, currentTile.getName());
                break;
                
            case "StartSquare":
                // Landing on GO - Collect additional money
                player.updateCash(GO_MONEY);
                gameLog.append("💰 " + player.getName() + " landed on GO and collected R" + GO_MONEY + "!\n");
                break;
                
            case "CommunityFund":
                handleCommunityFund(player);
                break;
                
            case "SpecialTask":
                gameLog.append(player.getName() + " landed on a Special Task square!\n");
                SpecialTask.askQuestion(player, gameLog);
                
                // Special increase to role-specific scores for challenges
                if (player.getRole().equalsIgnoreCase("Business Owner")) {
                    player.increaseEcoScore(5);
                    gameLog.append("♻️ Eco Score increased to " + player.getEcoScore() + "\n");
                } else if (player.getRole().equalsIgnoreCase("Community Leader")) {
                    player.increasePublicInfluence(5);
                    gameLog.append("👥 Public Influence increased to " + player.getPublicInfluence() + "\n");
                }
                break;
                
            case "EcoChallenge":
                gameLog.append("🌱 " + player.getName() + " landed on an Eco Challenge square!\n");
                
                // Launch the eco challenge (waste sorting game)
                WasteSortingLauncher.launchSortingGame(player, gameLog);
                
                // Special increase to role-specific scores for challenges
                if (player.getRole().equalsIgnoreCase("Business Owner")) {
                    player.increaseEcoScore(8);
                    gameLog.append("♻️ Eco Score significantly increased to " + player.getEcoScore() + "\n");
                } else if (player.getRole().equalsIgnoreCase("Community Leader")) {
                    player.increasePublicInfluence(8);
                    gameLog.append("👥 Public Influence significantly increased to " + player.getPublicInfluence() + "\n");
                }
                break;
                
            default:
                gameLog.append(player.getName() + " is on a neutral tile.\n");
                break;
        }

        // Update UI
        updateAllPlayerStats();
        
        // Check if objectives have been completed
        checkObjectiveCompletion(player);
    }
    // Add these methods to the GameManager class

/**
 * Get the current player index
 */
public int getCurrentPlayerIndex() {
    return currentPlayerIndex;
}

/**
 * Set the current player index directly
 */
public void setCurrentPlayerIndex(int index) {
    if (index >= 0 && index < players.size()) {
        currentPlayerIndex = index;
    }
}
    /**
     * Handle landing on a property tile
     */
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
                } else {
                    gameLog.append("❌ " + player.getName() + " decided not to buy " + propertyName + ".\n");
                }
            } else {
                gameLog.append("❌ " + player.getName() + " doesn't have enough money to buy " + propertyName + ".\n");
            }
        } else if (propertyOwner != player) {
            // Property is owned by another player, pay rent
            int rent = getPropertyRent(position);
            gameLog.append("💸 " + player.getName() + " landed on " + propertyOwner.getName() + "'s property " + 
                         propertyName + ". Must pay R" + rent + " rent.\n");
            
            if (player.getCash() >= rent) {
                player.updateCash(-rent);
                propertyOwner.updateCash(rent);
                
                gameLog.append("💰 " + player.getName() + " paid R" + rent + " to " + propertyOwner.getName() + ".\n");
            } else {
                // Player can't pay full rent
                int availableCash = player.getCash();
                player.updateCash(-availableCash);
                propertyOwner.updateCash(availableCash);
                
                gameLog.append("⚠️ " + player.getName() + " could only pay R" + availableCash + " to " + 
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
        int bonus = random.nextInt(101) + 50;
        gameLog.append("💰 " + player.getName() + " landed on Community Fund and received R" + bonus + "!\n");
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