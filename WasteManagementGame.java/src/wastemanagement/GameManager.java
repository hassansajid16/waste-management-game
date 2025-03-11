package wastemanagement;

// import javax.swing.BorderFactory;
// import javax.swing.border.Border;
// import java.util.ArrayList;
// import javax.swing.*;
// import javax.swing.border.BorderFactory;
// import java.awt.Color;
// import java.awt.Component;
// import java.awt.Font;
// import java.awt.SwingConstants;
// import java.awt.BorderLayout;
// import java.awt.BoxLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;
import javax.swing.*;

public class GameManager {
    private ArrayList<Player> players;
    private Board board;
    private Dice dice;
    private JTextArea gameLog;
    private int currentPlayerIndex;
    private JLabel currentPlayerLabel;
    private JLabel[] playerLabels;
    private JPanel playerStatsPanel;

    public GameManager(JTextArea gameLog, JLabel currentPlayerLabel, JLabel[] playerLabels, JPanel playerStatsPanel) {
        players = new ArrayList<>();
        board = new Board();
        dice = new Dice();
        this.gameLog = gameLog;
        this.currentPlayerLabel = currentPlayerLabel;
        this.playerLabels = playerLabels;
        this.playerStatsPanel = playerStatsPanel;
        currentPlayerIndex = 0;
    }

    public void addPlayer(String name) {
        players.add(new Player(name));
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        if (players.isEmpty()) {
            return null;
        }
        return players.get(currentPlayerIndex);
    }

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
            JLabel recyclablesLabel = new JLabel("Recyclables: " + player.getRecyclables());
            JLabel foodLabel = new JLabel("Food Waste: " + player.getFoodWaste());
            
            // Style the labels
            JLabel[] labels = {posLabel, cashLabel, recyclablesLabel, foodLabel};
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

    public void playTurn(Player player, JLabel positionLabel, JLabel recyclablesLabel, JLabel foodWasteLabel, JLabel cashLabel) {
        if (player == null) {
            return;
        }

        int roll = dice.roll();
        int oldPosition = player.getPosition();
        player.move(roll);
        int newPosition = player.getPosition();

        Tile currentTile = board.getTile(newPosition);
        gameLog.append(player.getName() + " rolled a " + roll + " and moved from position " + 
                     oldPosition + " to " + newPosition + " (" + currentTile.getName() + ")\n");

        int randomInt = 0;
        String wasteType;
        int moneyCollect = 0;
        
        switch (currentTile.getType()) {
            case "Refill Resources":
                randomInt = (int) (Math.random() * 2) + 1;
                if (player.getRole().equalsIgnoreCase("business owner")) {
                    moneyCollect = 150;
                    player.updateCash(moneyCollect);
                } else if (player.getRole().equalsIgnoreCase("community leader")) {
                    moneyCollect = 100;
                    player.updateCash(moneyCollect);
                }
                
                if (randomInt == 1) {
                    wasteType = "recyclables";
                    player.collectWaste(wasteType);
                    gameLog.append("📦 " + player.getName() + " collected recyclables and R" + moneyCollect + ".\n");
                } else {
                    wasteType = "foodWaste";
                    player.collectWaste(wasteType);
                    gameLog.append("🍎 " + player.getName() + " collected food waste and R" + moneyCollect + ".\n");
                }
                break;
                
            case "Resource Square":
                randomInt = (int) (Math.random() * 2) + 1;
                if (player.getRole().equalsIgnoreCase("business owner")) {
                    moneyCollect = 300;
                    player.updateCash(moneyCollect);
                } else if (player.getRole().equalsIgnoreCase("community leader")) {
                    moneyCollect = 200;
                    player.updateCash(moneyCollect);
                }
                
                if (randomInt == 1) {
                    wasteType = "recyclables";
                    player.collectWaste(wasteType);
                    player.collectWaste(wasteType); // Double collection at resource square
                    gameLog.append("📦📦 " + player.getName() + " collected 2 recyclables and R" + moneyCollect + ".\n");
                } else {
                    wasteType = "foodWaste";
                    player.collectWaste(wasteType);
                    player.collectWaste(wasteType); // Double collection at resource square
                    gameLog.append("🍎🍎 " + player.getName() + " collected 2 food waste and R" + moneyCollect + ".\n");
                }
                break;
                
            case "Special Task":
                // Handle special task squares - require resources
                int requiredRecyclables = 1;
                int requiredFoodWaste = 1;
                int bonusReward = 200;
                
                gameLog.append("⭐ " + player.getName() + " landed on a Special Task square!\n");
                gameLog.append("   This task requires " + requiredRecyclables + " recyclables and " + 
                             requiredFoodWaste + " food waste.\n");
                
                if (player.getRecyclables() >= requiredRecyclables && 
                    player.getFoodWaste() >= requiredFoodWaste) {
                    
                    // Consume resources
                    for (int i = 0; i < requiredRecyclables; i++) {
                        player.collectWaste("useRecyclables");
                    }
                    for (int i = 0; i < requiredFoodWaste; i++) {
                        player.collectWaste("useFoodWaste");
                    }
                    
                    // Give reward
                    player.updateCash(bonusReward);
                    gameLog.append("🎉 Task completed! " + player.getName() + " received R" + bonusReward + ".\n");
                } else {
                    gameLog.append("❌ " + player.getName() + " doesn't have enough resources for this task.\n");
                }
                break;
                
            case "Waste Exchange":
                // Handle waste exchange - convert waste to money
                int recycleValue = 50; // Value per recyclable
                int compostValue = 40; // Value per food waste
                
                gameLog.append("♻️ " + player.getName() + " landed on the Waste Exchange!\n");
                
                int totalRecyclables = player.getRecyclables();
                int totalFoodWaste = player.getFoodWaste();
                
                if (totalRecyclables > 0 || totalFoodWaste > 0) {
                    int recyclingMoney = totalRecyclables * recycleValue;
                    int compostingMoney = totalFoodWaste * compostValue;
                    int totalMoney = recyclingMoney + compostingMoney;
                    
                    // Exchange all waste for money
                    for (int i = 0; i < totalRecyclables; i++) {
                        player.collectWaste("useRecyclables");
                    }
                    for (int i = 0; i < totalFoodWaste; i++) {
                        player.collectWaste("useFoodWaste");
                    }
                    
                    player.updateCash(totalMoney);
                    
                    gameLog.append("💰 Exchange complete! Converted " + totalRecyclables + 
                                 " recyclables and " + totalFoodWaste + " food waste into R" + 
                                 totalMoney + ".\n");
                } else {
                    gameLog.append("❌ " + player.getName() + " has no waste to exchange.\n");
                }
                break;
                
            case "Task":
                // Handle task squares - costs money
                int taskCost = (int)(Math.random() * 50) + 50;
                gameLog.append("📋 " + player.getName() + " landed on a Task square. Cost: R" + taskCost + "\n");
                
                if (player.getCash() >= taskCost) {
                    player.updateCash(-taskCost);
                    
                    // Determine reward for completing the task
                    int rewardType = (int)(Math.random() * 3);
                    
                    if (rewardType == 0) {
                        // Get recyclables
                        player.collectWaste("recyclables");
                        player.collectWaste("recyclables");
                        gameLog.append("✅ Task completed! " + player.getName() + " gained 2 recyclables.\n");
                    } else if (rewardType == 1) {
                        // Get food waste
                        player.collectWaste("foodWaste");
                        player.collectWaste("foodWaste");
                        gameLog.append("✅ Task completed! " + player.getName() + " gained 2 food waste.\n");
                    } else {
                        // Get both
                        player.collectWaste("recyclables");
                        player.collectWaste("foodWaste");
                        gameLog.append("✅ Task completed! " + player.getName() + " gained 1 recyclable and 1 food waste.\n");
                    }
                } else {
                    gameLog.append("❌ " + player.getName() + " doesn't have enough money for this task (R" + taskCost + " needed).\n");
                }
                break;
                
            default:
                gameLog.append(player.getName() + " is on a neutral tile.\n");
                break;
        }

        // Update UI
        updatePlayerInfoLabels(player, positionLabel, recyclablesLabel, foodWasteLabel, cashLabel);
        updateAllPlayerStats();
    }
    
    private void updatePlayerInfoLabels(Player player, JLabel positionLabel, JLabel recyclablesLabel, 
                                      JLabel foodWasteLabel, JLabel cashLabel) {
        positionLabel.setText("Position: " + player.getPosition());
        recyclablesLabel.setText("Recyclables: " + player.getRecyclables());
        foodWasteLabel.setText("Food Waste: " + player.getFoodWaste());
        cashLabel.setText("Cash: R" + player.getCash());
    }
}