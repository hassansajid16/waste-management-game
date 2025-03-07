package wastemanagement;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

public class WasteManagementGame extends JFrame {
    private static final long serialVersionUID = 1L;
    private GameManager gameManager;
    private JTextArea gameLog;
    private JLabel positionLabel, recyclablesLabel, foodWasteLabel, cashLabel;
    private JLabel currentPlayerLabel;
    private JLabel[] playerInfoLabels;
    private JPanel playerStatsPanel;
    private JLabel diceImage;
    private JLabel[] tileLabels;
    private Random random = new Random();
    private Timer gameTimer;
    private int gameSeconds = 0;
    private JLabel timerLabel;

    public WasteManagementGame(ArrayList<Player> players) {
        setTitle("Waste Management Game");
        setSize(1100, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(45, 65, 45));

        // Initialize components
        setupPlayerLabels(players);
        setupGameLog();
        setupGameBoard();
        
        // Initialize game manager after UI components
        gameManager = new GameManager(gameLog, currentPlayerLabel, playerInfoLabels, playerStatsPanel);
        
        // Add players to the game manager
        for (Player player : players) {
            gameManager.getPlayers().add(player);
        }
        
        setupControlPanel();
        setupGameTimer();
        
        // Setup players and start game
        if (!players.isEmpty()) {
            gameLog.append("\n----- " + players.get(0).getName() + "'s Turn (" + players.get(0).getRole() + ") -----\n");
            highlightTile(0);
            gameManager.updateCurrentPlayerDisplay();
            gameManager.updateAllPlayerStats();
        }
        
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void setupPlayerLabels(ArrayList<Player> players) {
        // Create left sidebar panel
        JPanel leftSidebarPanel = new JPanel();
        leftSidebarPanel.setLayout(new BorderLayout(0, 10));
        leftSidebarPanel.setBackground(new Color(50, 70, 50));
        leftSidebarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftSidebarPanel.setPreferredSize(new Dimension(200, getHeight()));
        
        // Current player label (at the top of sidebar)
        currentPlayerLabel = new JLabel("Current Player: ");
        currentPlayerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        currentPlayerLabel.setForeground(Color.WHITE);
        
        // Timer label
        timerLabel = new JLabel("Game Time: 00:00");
        timerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        timerLabel.setForeground(Color.WHITE);
        
        // Top panel for current player and timer
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(currentPlayerLabel, BorderLayout.CENTER);
        topPanel.add(timerLabel, BorderLayout.SOUTH);
        leftSidebarPanel.add(topPanel, BorderLayout.NORTH);
        
        // Player list panel (in the middle of sidebar)
        JPanel playerListPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        playerListPanel.setBackground(new Color(50, 70, 50));
        playerListPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            "Players",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            Color.WHITE
        ));
        
        // Create player labels
        playerInfoLabels = new JLabel[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playerInfoLabels[i] = new JLabel(players.get(i).getName());
            playerInfoLabels[i].setFont(new Font("Arial", Font.PLAIN, 14));
            playerInfoLabels[i].setForeground(Color.WHITE);
            playerListPanel.add(playerInfoLabels[i]);
        }
        
        leftSidebarPanel.add(playerListPanel, BorderLayout.CENTER);
        
        // Player stats panel (at the bottom of sidebar)
        playerStatsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        playerStatsPanel.setBackground(new Color(50, 70, 50));
        playerStatsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            "Player Statistics",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            Color.WHITE
        ));
        
        JScrollPane statsScrollPane = new JScrollPane(playerStatsPanel);
        statsScrollPane.setBackground(new Color(50, 70, 50));
        statsScrollPane.setBorder(null);
        statsScrollPane.setPreferredSize(new Dimension(180, 300));
        
        leftSidebarPanel.add(statsScrollPane, BorderLayout.SOUTH);
        
        add(leftSidebarPanel, BorderLayout.WEST);
    }
    
    private void setupGameLog() {
        // Initialize game log
        gameLog = new JTextArea(15, 30);
        gameLog.setEditable(false);
        gameLog.setFont(new Font("Arial", Font.PLAIN, 14));
        gameLog.setBackground(new Color(240, 245, 240));
        gameLog.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
            
        JScrollPane scrollPane = new JScrollPane(gameLog);
        scrollPane.setPreferredSize(new Dimension(300, 400));
        
        // Welcome message
        gameLog.append("Welcome to Waste Management Game!\n");
        gameLog.append("Each player will take turns rolling the dice.\n");
        gameLog.append("\nGame Rules:\n");
        gameLog.append("- Resource Square: Collect 2x resources and money\n");
        gameLog.append("- Refill Resources: Collect resources and money\n");
        gameLog.append("- Task: Pay money to complete tasks for resources\n");
        gameLog.append("- Special Task: Use resources to complete for bonus rewards\n");
        gameLog.append("- Waste Exchange: Convert your waste to money\n\n");
    }
    
    private void setupGameBoard() {
        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setOpaque(false);
        
        // Create board panel
        JPanel boardPanel = new JPanel(new GridLayout(4, 5, 10, 10));
        boardPanel.setBackground(new Color(45, 65, 45));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        tileLabels = new JLabel[Board.BOARD_SIZE];
        Board board = new Board();
        
        // Create tiles
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            tileLabels[i] = new JLabel(board.getTile(i).getName(), SwingConstants.CENTER);
            tileLabels[i].setOpaque(true);
            tileLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            tileLabels[i].setPreferredSize(new Dimension(100, 70));
            tileLabels[i].setFont(new Font("Arial", Font.BOLD, 14));

            // Set tile colors based on type
            switch (board.getTile(i).getType()) {
                case "Resource Square":
                    tileLabels[i].setBackground(new Color(89, 116, 89));
                    break;
                case "Special Task":
                    tileLabels[i].setBackground(new Color(142, 92, 174));
                    break;
                case "Task":
                    tileLabels[i].setBackground(new Color(50, 57, 57));
                    break;
                case "Refill Resources":
                    tileLabels[i].setBackground(new Color(89, 116, 89));
                    break;
                case "Waste Exchange":
                    tileLabels[i].setBackground(new Color(70, 130, 180)); // Steel blue
                    break;
                default:
                    tileLabels[i].setBackground(new Color(70, 77, 77));
            }
            
            // Add position number to each tile
            tileLabels[i].setText("<html>" + board.getTile(i).getName() + "<br><small>Position " + i + "</small></html>");
            tileLabels[i].setForeground(Color.WHITE);
            boardPanel.add(tileLabels[i]);
        }
        
        // Dice and log panel
        JPanel rightPanel = new JPanel(new BorderLayout(10, 10));
        rightPanel.setOpaque(false);
        
        // Wrap gameLog in a scroll pane
        JScrollPane logScrollPane = new JScrollPane(gameLog);
        logScrollPane.setPreferredSize(new Dimension(300, 400));
        rightPanel.add(logScrollPane, BorderLayout.CENTER);
        
        // Dice panel
        JPanel dicePanel = new JPanel(new BorderLayout());
        dicePanel.setOpaque(false);
        
        // Initialize dice image
        diceImage = new JLabel("1", SwingConstants.CENTER);
        diceImage.setFont(new Font("Arial", Font.BOLD, 48));
        diceImage.setBackground(Color.WHITE);
        diceImage.setOpaque(true);
        diceImage.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        diceImage.setPreferredSize(new Dimension(80, 80));
        
        // Roll button
        JButton rollButton = new JButton("Roll Dice");
        rollButton.setFont(new Font("Arial", Font.BOLD, 16));
        rollButton.setBackground(new Color(173, 216, 230));
        rollButton.setForeground(Color.BLACK);
        rollButton.setFocusPainted(false);
        rollButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(8, 20, 8, 20)
        ));
        
        rollButton.addActionListener(e -> rollDice());
        
        dicePanel.add(diceImage, BorderLayout.CENTER);
        dicePanel.add(rollButton, BorderLayout.SOUTH);
        
        rightPanel.add(dicePanel, BorderLayout.SOUTH);
        
        // Add panels to main layout
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);
        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupControlPanel() {
        // Player info panel at bottom
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        infoPanel.setBackground(new Color(60, 80, 60));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        positionLabel = new JLabel("Position: 0");
        recyclablesLabel = new JLabel("Recyclables: 0");
        foodWasteLabel = new JLabel("Food Waste: 0");
        cashLabel = new JLabel("Cash: R0");

        // Style the labels
        JLabel[] labels = {positionLabel, recyclablesLabel, foodWasteLabel, cashLabel};
        for (JLabel label : labels) {
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            infoPanel.add(label);
        }
        
        // Add help button
        JButton helpButton = new JButton("Game Help");
        helpButton.setFont(new Font("Arial", Font.BOLD, 12));
        helpButton.setBackground(new Color(173, 216, 230));
        helpButton.addActionListener(e -> showGameHelp());
        infoPanel.add(helpButton);
        
        add(infoPanel, BorderLayout.SOUTH);
    }
    
    private void setupGameTimer() {
        // Create a timer that updates every second
        gameTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameSeconds++;
                updateTimerLabel();
            }
        });
        gameTimer.start();
    }
    
    private void updateTimerLabel() {
        int minutes = gameSeconds / 60;
        int seconds = gameSeconds % 60;
        timerLabel.setText(String.format("Game Time: %02d:%02d", minutes, seconds));
    }
    
    private void showGameHelp() {
        JOptionPane.showMessageDialog(this,
            "Waste Management Game - Help\n\n" +
            "Goal: Collect resources and complete tasks to earn money.\n\n" +
            "Tile Types:\n" +
            "• Resource Square: Collect 2x resources and money\n" +
            "• Refill Resources: Collect resources and money\n" +
            "• Task: Pay money to complete tasks and get resources\n" +
            "• Special Task: Use resources to earn bonus rewards\n" +
            "• Waste Exchange: Convert your waste to money\n\n" +
            "Player Roles:\n" +
            "• Business Owner: Starts with R300, earns more from squares\n" +
            "• Community Leader: Starts with R200\n\n" +
            "How to Play:\n" +
            "1. Roll the dice on your turn to move around the board\n" +
            "2. Collect resources and money\n" +
            "3. Complete tasks to earn more resources\n" +
            "4. Convert resources to money at the Waste Exchange\n",
            "Game Help",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    private void setDiceImage(int value) {
        try {
            // Try to load images from resources
            ImageIcon icon = new ImageIcon(getClass().getResource("/resources/dice" + value + ".png"));
            diceImage.setIcon(icon);
            diceImage.setText("");
        } catch (Exception e) {
            // Fallback if image not found - create a visual dice representation
            diceImage.setIcon(null);
            diceImage.setText(String.valueOf(value));
            diceImage.setFont(new Font("Arial", Font.BOLD, 48));
            diceImage.setBackground(Color.WHITE);
            diceImage.setOpaque(true);
            diceImage.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        }
    }
    
    private void rollDice() {
        // Disable interaction during animation
        setEnabled(false);
        
        // Animate the dice rolling
        new Thread(() -> {
            try {
                // Quick animation
                for (int i = 0; i < 8; i++) {
                    final int randomValue = random.nextInt(6) + 1;
                    SwingUtilities.invokeLater(() -> setDiceImage(randomValue));
                    Thread.sleep(100);
                }
                
                // Final dice value
                final int diceValue = random.nextInt(6) + 1;
                
                // Update UI on EDT
                SwingUtilities.invokeLater(() -> {
                    // Set final dice value
                    setDiceImage(diceValue);
                    
                    // Get current player and move
                    Player currentPlayer = gameManager.getCurrentPlayer();
                    if (currentPlayer != null) {
                        gameManager.playTurn(currentPlayer, positionLabel, recyclablesLabel, foodWasteLabel, cashLabel);
                        
                        // Update visual position
                        highlightTile(currentPlayer.getPosition());
                        
                        // Next player
                        gameManager.nextTurn();
                    }
                    
                    // Re-enable interaction
                    setEnabled(true);
                });
                
            } catch (InterruptedException e) {
                System.out.println("Dice animation interrupted: " + e);
                setEnabled(true);
            }
        }).start();
    }

    private void highlightTile(int position) {
        // Reset all tile borders
        for (int i = 0; i < tileLabels.length; i++) {
            if (tileLabels[i] != null) {
                tileLabels[i].setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
            }
        }
        
        // Highlight current tile
        if (tileLabels[position] != null) {
            tileLabels[position].setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
        }
    }
    
    // Clean up resources when the game closes
    @Override
    public void dispose() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        super.dispose();
    }
}