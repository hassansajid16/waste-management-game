package wastemanagment;

import javax.swing.*;
import java.util.ArrayList;

public class GameManager {
    private ArrayList<Player> players;
    private Board board;
    private Dice dice;
    private JTextArea gameLog;

    public GameManager(JTextArea gameLog) {
        players = new ArrayList<>();
        board = new Board();
        dice = new Dice();
        this.gameLog = gameLog;
    }

    public void addPlayer(String name) {
        players.add(new Player(name));
    }

    public void playTurn(Player player, JLabel positionLabel, JLabel recyclablesLabel, JLabel foodWasteLabel) {
        int roll = dice.roll();
        player.move(roll);

        Tile currentTile = board.getTile(player.getPosition());
        gameLog.append(player.getName() + " rolled a " + roll + " and landed on " + currentTile.getType() + "\n");

        switch (currentTile.getType()) {
            case "Residential":
                player.collectWaste("recyclables");
                gameLog.append(player.getName() + " collected recyclables.\n");
                break;
            case "Pikitup Center":
                player.depositWaste("Pikitup");
                gameLog.append(player.getName() + " deposited recyclables at Pikitup.\n");
                break;
            case "Composting Center":
                player.depositWaste("Composting");
                gameLog.append(player.getName() + " deposited food waste at Composting Center.\n");
                break;
            default:
                gameLog.append(player.getName() + " is on a neutral tile.\n");
                break;
        }
        
        positionLabel.setText("Position: " + player.getPosition());
        recyclablesLabel.setText("Recyclables: " + player.getRecyclables());
        foodWasteLabel.setText("Food Waste: " + player.getFoodWaste());
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }
}

