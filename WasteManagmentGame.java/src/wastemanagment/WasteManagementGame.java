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
    private JLabel positionLabel, recyclablesLabel, foodWasteLabel;

    public WasteManagementGame() {
        setTitle("Waste Management Game");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gameLog = new JTextArea();
        gameLog.setEditable(false);
        add(new JScrollPane(gameLog), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(4, 2));

        positionLabel = new JLabel("Position: 0");
        recyclablesLabel = new JLabel("Recyclables: 0");
        foodWasteLabel = new JLabel("Food Waste: 0");

        controlPanel.add(new JLabel("Player Info:"));
        controlPanel.add(new JLabel());
        controlPanel.add(positionLabel);
        controlPanel.add(recyclablesLabel);
        controlPanel.add(foodWasteLabel);

        JButton rollButton = new JButton("Roll Dice");
        rollButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                gameManager.playTurn(gameManager.getPlayers().get(0), positionLabel, recyclablesLabel, foodWasteLabel);
            }
        });

        controlPanel.add(rollButton);
        add(controlPanel, BorderLayout.SOUTH);

        gameManager = new GameManager(gameLog);
        gameManager.addPlayer("Alice");

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new WasteManagementGame());
    }
}
