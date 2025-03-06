package wastemanagement;

import java.util.ArrayList;
import javax.swing.*;

public class GameManager {
    private ArrayList<Player> players;
    private Board board;
    private Dice dice;
    private JTextArea gameLog;
    private int currentPlayerIndex;

    public GameManager(JTextArea gameLog) {
        players = new ArrayList<>();
        board = new Board();
        dice = new Dice();
        this.gameLog = gameLog;
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
        gameLog.append("\n--- " + nextPlayer.getName() + "'s Turn (" + nextPlayer.getRole() + ") ---\n");
    }

    public void playTurn(Player player, JLabel positionLabel, JLabel recyclablesLabel, JLabel foodWasteLabel, JLabel cashLabel) {
        if (player == null) {
            return;
        }

        int roll = dice.roll();
        player.move(roll);

        Tile currentTile = board.getTile(player.getPosition());
        gameLog.append(player.getName() + " rolled a " + roll + " and landed on " + currentTile.getName() + "\n");

        int randomInt = 0;
        String wasteType = "Empty";
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
                    gameLog.append(player.getName() + " collected recyclables and R" + moneyCollect + ".\n");
                } else {
                    wasteType = "foodWaste";
                    player.collectWaste(wasteType);
                    gameLog.append(player.getName() + " collected food waste and R" + moneyCollect + ".\n");
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
                    gameLog.append(player.getName() + " collected recyclables and R" + moneyCollect + ".\n");
                } else {
                    wasteType = "foodWaste";
                    player.collectWaste(wasteType);
                    gameLog.append(player.getName() + " collected food waste and R" + moneyCollect + ".\n");
                }
                break;
                
            case " ":
                break;
                
            case "Task":
                // Handle task squares - for now just a notification
                gameLog.append(player.getName() + " landed on a task square. Resources needed to complete.\n");
                
                // Simulate a task cost
                int taskCost = (int)(Math.random() * 50) + 20;
                if (player.getCash() >= taskCost) {
                    player.updateCash(-taskCost);
                    gameLog.append(player.getName() + " spent R" + taskCost + " on a waste management task.\n");
                } else {
                    gameLog.append(player.getName() + " doesn't have enough money for this task (R" + taskCost + " needed).\n");
                }
                break;
                
            case "End":
                gameLog.append(player.getName() + " reached the end! Game completed!\n");
                // Could add game completion logic here
                break;
                
            default:
                gameLog.append(player.getName() + " is on a neutral tile.\n");
                break;
        }

        // Update UI
        updatePlayerInfoLabels(player, positionLabel, recyclablesLabel, foodWasteLabel, cashLabel);
    }
    
    private void updatePlayerInfoLabels(Player player, JLabel positionLabel, JLabel recyclablesLabel, 
                                      JLabel foodWasteLabel, JLabel cashLabel) {
        positionLabel.setText("Position: " + player.getPosition());
        recyclablesLabel.setText("Recyclables: " + player.getRecyclables());
        foodWasteLabel.setText("Food Waste: " + player.getFoodWaste());
        cashLabel.setText("Cash: R" + player.getCash());
    }
}