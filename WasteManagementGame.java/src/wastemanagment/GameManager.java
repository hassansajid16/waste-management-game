package wastemanagment;

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

        return players.get(currentPlayerIndex);

    }

 

    public void nextTurn() {

        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();

    }

 

    public void playTurn(Player player, JLabel positionLabel, JLabel recyclablesLabel, JLabel foodWasteLabel, JLabel cashLabel) {

        int roll = dice.roll();

        player.move(roll);


        Tile currentTile = board.getTile(player.getPosition());
        gameLog.append(player.getName() + " rolled a " + roll + " and landed on " + currentTile.getName() + "\n");

 
int randomInt = 0;
String wasteType = "Empty";
	int moneyCollect = 0;
        switch (currentTile.getType()) {
//refill resources squares
            case "Refill Resources":
                 randomInt = (int) (Math.random() * 2) + 1;
            	if (player.getRole().equalsIgnoreCase("business owner")) {
                		moneyCollect = 150;
                		player.updateCash(moneyCollect);
                	}else if (player.getRole().equalsIgnoreCase("community leader")) {
                		moneyCollect = 100;
                		player.updateCash(moneyCollect);}
                if(randomInt == 1) {
            		                wasteType = "recyclables";
                	player.collectWaste(wasteType);
                	
            		                randomInt = (int) (Math.random() * 2) + 1;
                }            	else {
            		wasteType = "foodWaste";
            		player.collectWaste(wasteType);
            		wasteType = "Food Waste";
            		randomInt = (int) (Math.random() * 2) + 1;
            	}
                gameLog.append(player.getName() + " collected " + wasteType +  " and R" + moneyCollect + ".\n");

                break;
            //start square
            case "Resource Square":
            	  randomInt = (int) (Math.random() * 2) + 1;
             	if (player.getRole().equalsIgnoreCase("business owner")) {
                 		moneyCollect = 300;
                 		player.updateCash(moneyCollect);
                 	}else if (player.getRole().equalsIgnoreCase("community leader")) {
                 		moneyCollect = 200;
                 		player.updateCash(moneyCollect);}
                 if(randomInt == 1) {
             		                wasteType = "recyclables";
                 	player.collectWaste(wasteType);
                 	
             		                randomInt = (int) (Math.random() * 2) + 1;
                 }            	else {
             		wasteType = "foodWaste";
             		player.collectWaste(wasteType);
             		wasteType = "Food Waste";
             		randomInt = (int) (Math.random() * 2) + 1;
             	}
                 gameLog.append(player.getName() + " collected " + wasteType +  " and R" + moneyCollect + ".\n");
            	break;
            case " ":
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
        cashLabel.setText("Cash: R" + player.getCash());
    }

}