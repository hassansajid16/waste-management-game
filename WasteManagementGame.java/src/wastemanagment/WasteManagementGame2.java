package wastemanagment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class WasteManagementGame2 extends JFrame {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private GameManager gameManager;
    private JTextArea gameLog;
    private JLabel positionLabel, recyclablesLabel, foodWasteLabel;

    public WasteManagementGame2() {
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

        controlPanel.add(new JLabel("Player Info:"));
        controlPanel.add(new JLabel());
        controlPanel.add(positionLabel);
        controlPanel.add(recyclablesLabel);
        controlPanel.add(foodWasteLabel);

        JButton rollButton = new JButton("Roll Dice");
        
        JPanel boardPanel = new JPanel();
       boardPanel.setLayout(new GridLayout(5, 4));
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

        // Display an input dialog to get the player's name
        String name = JOptionPane.showInputDialog(this, "Enter your name:", "Player Name", JOptionPane.QUESTION_MESSAGE);

        // If the user cancels or enters no name, use a default name
        if (name == null || name.trim().isEmpty()) {
            name = "Player 1";
        }
        gameManager.addPlayer(name);
        rollButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameManager.playTurn(gameManager.getPlayers().get(0), positionLabel, recyclablesLabel, foodWasteLabel);
            currentPlayerTile(gameManager.getPlayers().get(0).getPosition(), tileLabels);
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
        SwingUtilities.invokeLater(() -> new WasteManagementGame2());
    }
}
