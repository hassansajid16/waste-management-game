package wastemanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameUI extends JFrame {
    private static final long serialVersionUID = 1L;
    private GameManager gameManager;
    private JTextArea gameLog;
    private JLabel positionLabel, recyclablesLabel, foodWasteLabel, cashLabel;
    private JLabel[] tileLabels;
    private JPanel boardPanel, messagePanel, controlPanel;
    private JButton rollButton, yesButton, noButton;

    public GameUI() {
        setTitle("Waste Management Game");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(45, 65, 45));

        // Initialize game log first since it's needed by GameManager
        gameLog = new JTextArea(4, 30);
        gameLog.setEditable(false);
        gameLog.setFont(new Font("Arial", Font.PLAIN, 14)); // Reduce font size slightly
        gameLog.setLineWrap(true);
        gameLog.setWrapStyleWord(true);
        gameLog.setPreferredSize(new Dimension(600,150));
        gameLog.setMaximumSize(new Dimension(600, 150));

        
        // Initialize GameManager
        JLabel dummyCurrentPlayerLabel = new JLabel();
JLabel[] dummyPlayerLabels = new JLabel[0];
JPanel dummyStatsPanel = new JPanel();
gameManager = new GameManager(gameLog, dummyCurrentPlayerLabel, dummyPlayerLabels, dummyStatsPanel);

        // Create main content panel to hold board and message panel
        JPanel mainContent = new JPanel(new BorderLayout(0, 20));
        mainContent.setOpaque(false);
        
        // Initialize components
        createBoardPanel();
        createMessagePanel();
        createControlPanel();
        
        // Add panels to main content
        mainContent.add(boardPanel, BorderLayout.NORTH);
        
        // Create a wrapper panel for the message panel to center it
        JPanel messageWrapper = new JPanel(new GridBagLayout());
        messageWrapper.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 100, 0, 100); // Add some spacing on the sides
        messageWrapper.add(messagePanel, gbc);
        
        mainContent.add(messageWrapper, BorderLayout.CENTER);
        
        // Add components to frame
        add(mainContent, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        // Add players
        addPlayers();

        setVisible(true);
    }

    private void createBoardPanel() {
        boardPanel = new JPanel(new GridBagLayout());
        boardPanel.setBackground(new Color(45, 65, 45));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        tileLabels = new JLabel[Board.BOARD_SIZE];
        Board board = new Board();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.BOTH;

        // Create and add tiles
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            if (board.getTile(i).getType().trim().isEmpty()) continue;
            
            tileLabels[i] = new JLabel(board.getTile(i).getName(), SwingConstants.CENTER);
            tileLabels[i].setOpaque(true);
            tileLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            tileLabels[i].setPreferredSize(new Dimension(150, 80));
            tileLabels[i].setFont(new Font("Arial", Font.BOLD, 14));

            // Set tile colors based on type
            switch (board.getTile(i).getType()) {
                case "Resource Square":
                    tileLabels[i].setBackground(new Color(89, 116, 89));
                    break;
                case "Task":
                    tileLabels[i].setBackground(new Color(50, 57, 57));
                    break;
                case "Refill Resources":
                    tileLabels[i].setBackground(new Color(89, 116, 89));
                    break;
                default:
                    tileLabels[i].setBackground(new Color(70, 77, 77));
            }
            tileLabels[i].setForeground(Color.WHITE);

            gbc.gridx = i % 5;
            gbc.gridy = i / 5;
            boardPanel.add(tileLabels[i], gbc);
        }
    }

    private void createMessagePanel() {
        messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(new Color(200, 220, 200));
        messagePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Set preferred size for better control of the panel's dimensions
        messagePanel.setPreferredSize(new Dimension(600, 150));
        
        JScrollPane scrollPane = new JScrollPane(gameLog){
            @Override
            public Dimension getPreferredSize(){
                return new Dimension(600,150);
            }
        };
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setMinimumSize(new Dimension(600, 150));
        scrollPane.setPreferredSize(new Dimension(600, 150));
        scrollPane.getViewport().setPreferredSize(new Dimension(600, 150));
        scrollPane.getViewport().setScrollMode(JViewport.SIMPLE_SCROLL_MODE);

        messagePanel.setLayout(new BorderLayout());
        messagePanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void createControlPanel() {
        controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBackground(new Color(45, 65, 45));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        yesButton = createButton("Yes");
        rollButton = createButton("Roll Dice");
        noButton = createButton("No");

        buttonPanel.add(yesButton);
        buttonPanel.add(rollButton);
        buttonPanel.add(noButton);

        // Player info panel
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        infoPanel.setBackground(new Color(50, 57, 57));

        positionLabel = new JLabel("Position: 0");
        recyclablesLabel = new JLabel("Recyclables: 0");
        foodWasteLabel = new JLabel("Food Waste: 0");
        cashLabel = new JLabel("Cash: R0");

        // Style the labels
        Component[] labels = {positionLabel, recyclablesLabel, foodWasteLabel, cashLabel};
        for (Component label : labels) {
            ((JLabel)label).setForeground(Color.WHITE);
            ((JLabel)label).setFont(new Font("Arial", Font.BOLD, 14));
            infoPanel.add(label);
        }

        controlPanel.add(buttonPanel, BorderLayout.CENTER);
        controlPanel.add(infoPanel, BorderLayout.SOUTH);

        // Add roll button action
        rollButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Player currentPlayer = gameManager.getCurrentPlayer();
                gameManager.playTurn(currentPlayer, positionLabel, recyclablesLabel, foodWasteLabel, cashLabel);
                highlightCurrentTile(currentPlayer.getPosition());
                gameManager.nextTurn();
            }
        });
    }
    

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(173, 216, 230));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        return button;
    }

    private void highlightCurrentTile(int position) {
        // Reset all tile borders
        for (JLabel tile : tileLabels) {
            if (tile != null) {
                tile.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
        }
        // Highlight current tile
        if (tileLabels[position] != null) {
            tileLabels[position].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        }
    }

    private void addPlayers() {
        for (int i = 0; i < 4; i++) {
            String name = JOptionPane.showInputDialog(this, 
                "Enter name for Player " + (i + 1) + " (Leave blank to skip):", 
                "Player Name", 
                JOptionPane.QUESTION_MESSAGE);
            
            if (name == null || name.trim().isEmpty()) {
                break;
            } else {
                gameManager.addPlayer(name);
                String playerClass = JOptionPane.showInputDialog(this,
                    name + " Please enter your class: Business Owner or Community Leader ",
                    "Choose a Class",
                    JOptionPane.QUESTION_MESSAGE);
                
                while (!playerClass.equalsIgnoreCase("Business Owner") && 
                       !playerClass.equalsIgnoreCase("Community Leader")) {
                    playerClass = JOptionPane.showInputDialog(this,
                        "Incorrect " + name + " Please enter your class: Business Owner or Community Leader ",
                        "Choose a Class",
                        JOptionPane.QUESTION_MESSAGE);
                }
                
                gameManager.getPlayers().get(i).setRole(playerClass);
                gameManager.getPlayers().get(i).applyRole(playerClass);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GameUI();
            }
        });
    }
}