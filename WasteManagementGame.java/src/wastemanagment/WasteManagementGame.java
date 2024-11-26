package wastemanagment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class WasteManagementGame extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GameManager gameManager;
    private JTextArea gameLog;
    private JLabel positionLabel, recyclablesLabel, foodWasteLabel, cashLabel;

    public WasteManagementGame() {
        setTitle("Waste Management Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gameLog = new JTextArea();
        gameLog.setEditable(false);
        add(new JScrollPane(gameLog), BorderLayout.EAST);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 3));

        positionLabel = new JLabel("Position: 0");
        recyclablesLabel = new JLabel("Recyclables: 0");
        foodWasteLabel = new JLabel("Food Waste: 0");
        cashLabel = new JLabel("Cash: 0");
        controlPanel.add(new JLabel("Player Info:"));
        controlPanel.add(new JLabel());
        controlPanel.add(positionLabel);
        controlPanel.add(recyclablesLabel);
        controlPanel.add(foodWasteLabel);
        controlPanel.add(cashLabel);
        JButton rollButton = new JButton("Roll Dice");
        
        JPanel boardPanel = new JPanel();
       boardPanel.setLayout(new GridLayout(3, 5));
      JLabel[] tileLabels = new JLabel[Board.BOARD_SIZE];
      //initialise board
      Board Board = new Board();
      for(int i = 0; i < wastemanagment.Board.BOARD_SIZE; i++) {
    	  tileLabels[i] = new JLabel(Board.getTile(i).getName(), SwingConstants.CENTER);
    	  tileLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
    	  tileLabels[i].setOpaque(true);
    	  tileLabels[i].setBackground(Color.LIGHT_GRAY);
    	  boardPanel.add(tileLabels[i]);
      }
        add(boardPanel, BorderLayout.CENTER);
         gameManager = new GameManager(gameLog);

         // Add up to 4 players with custom names

         for (int i = 0; i < 4; i++) {

             String name = JOptionPane.showInputDialog(this, "Enter the name for Player " + (i + 1) + " (Leave blank to skip):", "Player Name", JOptionPane.QUESTION_MESSAGE);

             if (name == null || name.trim().isEmpty()) {

                 break; // Exit adding players if input is empty

             }     else {
            	                gameManager.addPlayer(name);
 String playerClass = JOptionPane.showInputDialog(this,  name + " Please enter your class: Business Owner or Community Leader ", "Choose a Class", JOptionPane.QUESTION_MESSAGE);     
 while((!playerClass.equalsIgnoreCase("Business Owner") && !playerClass.equalsIgnoreCase("Community Leader") )) {
		 playerClass = JOptionPane.showInputDialog(this,"Incorrect " +  name + " Please enter your class: Business Owner or Community Leader ", "Choose a Class", JOptionPane.QUESTION_MESSAGE);     

 }
 gameManager.getPlayers().get(i).setRole(playerClass);
       gameManager.getPlayers().get(i).applyRole(playerClass);
}

     
         }
      
        rollButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Player currentPlayer = gameManager.getCurrentPlayer();
                gameManager.playTurn(currentPlayer, positionLabel, recyclablesLabel, foodWasteLabel, cashLabel);
            currentPlayerTile(gameManager.getPlayers().get(0).getPosition(), tileLabels);
            gameManager.nextTurn();
            }
        });

        controlPanel.add(rollButton);
        add(controlPanel, BorderLayout.SOUTH);

       
 
        setVisible(true);
    }
public void currentPlayerTile(int playerPosition, JLabel[] tileLabels) {
	for (int i = 0; i < tileLabels.length ; i++ ) {
		tileLabels[i].setBackground(Color.LIGHT_GRAY);
	}
	tileLabels[playerPosition].setBackground(Color.YELLOW); 
}
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WasteManagementGame());
    }
}
