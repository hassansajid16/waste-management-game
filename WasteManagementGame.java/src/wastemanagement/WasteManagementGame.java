package wastemanagement;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.swing.*;

/**
 * Main Game Class for the Waste Management Game
 * This represents a Monopoly-style board game that simulates
 * waste management challenges in Makers Valley, Johannesburg
 */
public class WasteManagementGame extends JFrame {
    // Game constants
    private static final long serialVersionUID = 1L;
    private static final int WINDOW_WIDTH = 1200;
    private static final int WINDOW_HEIGHT = 800;
    private static final Color BG_COLOR = new Color(45, 65, 45);
    
    // Game components
    private GameManager gameManager;
    private GameBoardManager boardManager;
    private ArrayList<Player> players;
    private Map<String, Color> playerColors;
    private Random random = new Random();
    
    // UI Components
    private JTextArea gameLog;
    private JLabel diceImage;
    private JLabel currentPlayerLabel;
    private JLabel timerLabel;
    private JLabel[] playerInfoLabels;
    private JPanel playerStatsPanel;
    private Map<Player, ObjectivesPanel> playerObjectivesPanels;
    private GameTilePanel[] tilePanels;
    
    // Game state
    private Timer gameTimer;
    private int gameSeconds = 0;
    private boolean gameEnded = false;
    
    /**
     * Constructor with players and player colors
     */
    public WasteManagementGame(ArrayList<Player> players, Map<String, Color> playerColors) {
        // Setup basic window properties
        setTitle("Makers Valley Waste Management Game");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BG_COLOR);
        
        // Store player data
        this.players = players;
        this.playerColors = playerColors;
        this.playerObjectivesPanels = new HashMap<>();
        
        // Assign colors to players if not already assigned
        assignColorsToPlayers(players, playerColors);
        
        // Initialize UI components
        setupUI();
        
        // Display the window
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    /**
     * Constructor with just players (creates default colors)
     */
    public WasteManagementGame(ArrayList<Player> players) {
        this(players, createDefaultPlayerColors(players));
    }
    
    /**
     * Setup all UI components
     */
    private void setupUI() {
        // Setup UI components in proper order
        setupLeftSidebar();
        setupRightSidebar();
        setupGameBoard();
        setupBottomPanel();
        
        // Initialize game manager after UI components
        gameManager = new GameManager(gameLog, currentPlayerLabel, playerInfoLabels, playerStatsPanel);
        
        // Add players to game manager
        for (Player player : players) {
            gameManager.getPlayers().add(player);
        }
        
        // Initialize game and start timer
        initializeGame();
        setupGameTimer();
    }
    
    /**
     * Initialize game state
     */
    private void initializeGame() {
        if (!players.isEmpty()) {
            // Welcome message and game start
            gameLog.append("Welcome to the Makers Valley Waste Management Game!\n\n");
            gameLog.append("This game simulates the challenges of implementing an effective\n");
            gameLog.append("waste management system in Makers Valley, Johannesburg.\n\n");
            gameLog.append("Game Objectives:\n");
            gameLog.append("1. Role-specific goal: Build your Eco Score (Business Owner) or\n");
            gameLog.append("   Public Influence (Community Leader) to 50 points\n");
            gameLog.append("2. Acquire at least 3 properties to establish waste management infrastructure\n\n");
            
            gameLog.append("Starting the game with " + players.size() + " players...\n");
            gameLog.append("\n----- " + players.get(0).getName() + "'s Turn (" + players.get(0).getRole() + ") -----\n");
            
            // Highlight starting position and update displays
            boardManager.highlightTile(0);
            gameManager.updateCurrentPlayerDisplay();
            gameManager.updateAllPlayerStats();
            updateObjectivesDisplay();
        }
    }
    
    /**
     * Setup the left sidebar with player information
     */
    private void setupLeftSidebar() {
        // Create left sidebar panel
        JPanel leftSidebar = new JPanel();
        leftSidebar.setLayout(new BorderLayout(0, 10));
        leftSidebar.setBackground(new Color(50, 70, 50));
        leftSidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        leftSidebar.setPreferredSize(new Dimension(220, getHeight()));
        
        // Current player label (top of sidebar)
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
        
        // Player list panel (middle of sidebar)
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
        
        // Player stats panel (bottom of sidebar)
        playerStatsPanel = new JPanel();
        playerStatsPanel.setLayout(new BoxLayout(playerStatsPanel, BoxLayout.Y_AXIS));
        playerStatsPanel.setBackground(new Color(50, 70, 50));
        playerStatsPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE),
            "Player Statistics",
            javax.swing.border.TitledBorder.CENTER,
            javax.swing.border.TitledBorder.TOP,
            new Font("Arial", Font.BOLD, 14),
            Color.WHITE
        ));
        
        // Create scrollable stats panel
        JScrollPane statsScrollPane = new JScrollPane(playerStatsPanel);
        statsScrollPane.setBackground(new Color(50, 70, 50));
        statsScrollPane.setBorder(null);
        statsScrollPane.setPreferredSize(new Dimension(200, 300));
        
        // Add components to left sidebar
        leftSidebar.add(topPanel, BorderLayout.NORTH);
        leftSidebar.add(playerListPanel, BorderLayout.CENTER);
        leftSidebar.add(statsScrollPane, BorderLayout.SOUTH);
        
        // Add left sidebar to main window
        add(leftSidebar, BorderLayout.WEST);
    }
    
    /**
     * Setup the game board (center panel)
     */
    private void setupGameBoard() {
        // Main layout panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setOpaque(false);
        
        // Create board manager with players
        boardManager = new GameBoardManager(players);
        JPanel boardPanel = boardManager.getBoardPanel();
        
        // Save references to tile panels for property updates
        tilePanels = new GameTilePanel[Board.BOARD_SIZE];
        for (Component comp : boardPanel.getComponents()) {
            if (comp instanceof GameTilePanel) {
                GameTilePanel tilePanel = (GameTilePanel) comp;
                tilePanels[tilePanel.getPosition()] = tilePanel;
            }
        }
        
        // Dice panel
        JPanel dicePanel = new JPanel(new BorderLayout());
        dicePanel.setOpaque(false);
        dicePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Dice image
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
        
        // Add roll button action
        rollButton.addActionListener(e -> rollDice());
        
        dicePanel.add(diceImage, BorderLayout.CENTER);
        dicePanel.add(rollButton, BorderLayout.SOUTH);
        
        // Add components to main panel
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        mainPanel.add(dicePanel, BorderLayout.SOUTH);
        
        // Add main panel to window
        add(mainPanel, BorderLayout.CENTER);
    }
    
    /**
     * Setup the right sidebar with objectives and game log
     */
    private void setupRightSidebar() {
        // Game log
        gameLog = new JTextArea(15, 30);
        gameLog.setEditable(false);
        gameLog.setFont(new Font("Arial", Font.PLAIN, 14));
        gameLog.setBackground(new Color(240, 245, 240));
        gameLog.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        JScrollPane gameLogScroll = new JScrollPane(gameLog);
        gameLogScroll.setPreferredSize(new Dimension(250, 300));
        
        // Objectives panel
        setupObjectivesPanel();
        
        // Right sidebar panel
        JPanel rightSidebar = new JPanel(new BorderLayout(0, 10));
        rightSidebar.setOpaque(false);
        rightSidebar.setPreferredSize(new Dimension(250, getHeight()));
        rightSidebar.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Add components to right sidebar
        rightSidebar.add(objectivesContainer, BorderLayout.NORTH);
        rightSidebar.add(gameLogScroll, BorderLayout.CENTER);
        
        // Add right sidebar to main window
        add(rightSidebar, BorderLayout.EAST);
    }
    
    /**
     * Setup the bottom control panel
     */
    private void setupBottomPanel() {
        // Create bottom panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        bottomPanel.setBackground(new Color(60, 80, 60));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Help button
        JButton helpButton = new JButton("Game Help");
        helpButton.setFont(new Font("Arial", Font.BOLD, 14));
        helpButton.setBackground(new Color(173, 216, 230));
        helpButton.addActionListener(e -> showGameHelp());
        
        // End game button
        JButton endGameButton = new JButton("End Game");
        endGameButton.setFont(new Font("Arial", Font.BOLD, 14));
        endGameButton.setBackground(new Color(255, 150, 150));
        endGameButton.addActionListener(e -> confirmEndGame());
        
        // Add buttons to panel
        bottomPanel.add(helpButton);
        bottomPanel.add(endGameButton);
        
        // Add bottom panel to main window
        add(bottomPanel, BorderLayout.SOUTH);
    }
    private JPanel objectivesContainer;
    /**
     * Setup the objectives panel
     */
    private void setupObjectivesPanel() {
        objectivesContainer = new JPanel();
        objectivesContainer.setLayout(new BoxLayout(objectivesContainer, BoxLayout.Y_AXIS));
        objectivesContainer.setBackground(new Color(45, 65, 45));
        objectivesContainer.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Title for objectives
        JLabel objectivesTitle = new JLabel("Game Objectives", SwingConstants.CENTER);
        objectivesTitle.setFont(new Font("Arial", Font.BOLD, 16));
        objectivesTitle.setForeground(Color.WHITE);
        objectivesTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        objectivesTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        objectivesContainer.add(objectivesTitle);
        
        // Create an objectives panel for each player
        for (Player player : players) {
            ObjectivesPanel panel = new ObjectivesPanel(player);
            playerObjectivesPanels.put(player, panel);
            
            // Add some spacing between player panels
            if (objectivesContainer.getComponentCount() > 1) {
                objectivesContainer.add(Box.createVerticalStrut(10));
            }
            
            objectivesContainer.add(panel);
        }
    }
    
    /**
     * Setup the game timer
     */
    private void setupGameTimer() {
        gameTimer = new Timer(1000, e -> {
            gameSeconds++;
            updateTimerLabel();
        });
        gameTimer.start();
    }
    
    /**
     * Update the timer label with current game time
     */
    private void updateTimerLabel() {
        int minutes = gameSeconds / 60;
        int seconds = gameSeconds % 60;
        timerLabel.setText(String.format("Game Time: %02d:%02d", minutes, seconds));
    }
    
    /**
     * Update the objectives display for all players
     */
    private void updateObjectivesDisplay() {
        for (Player player : players) {
            ObjectivesPanel panel = playerObjectivesPanels.get(player);
            if (panel != null) {
                panel.updateProgress();
            }
        }
    }
    
    /**
     * Roll the dice and process the player's turn
     */
    private void rollDice() {
        if (gameEnded) return;
        
        // Disable interaction during animation
        setEnabled(false);
        
        // Animate the dice roll
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
                        // Get old position before moving
                        int oldPosition = currentPlayer.getPosition();
                        
                        // Move player (updates position in Player object)
                        currentPlayer.move(diceValue);
                        int newPosition = currentPlayer.getPosition();
                        
                        // Move the player token on the board
                        boardManager.movePlayer(currentPlayer, newPosition);
                        
                        // Process the turn in game manager
                        gameManager.processTurn(currentPlayer, diceValue, oldPosition, newPosition, gameLog);
                        
                        // Update property ownership display
                        gameManager.updatePropertyDisplay(tilePanels);
                        
                        // Update objectives and check for winners
                        updateObjectivesDisplay();
                        checkForWinner(currentPlayer);
                        
                        // If game didn't end, move to next player
                        if (!gameEnded) {
                            gameManager.nextTurn();
                        }
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
    
    /**
     * Set the dice image for a specific value
     */
    private void setDiceImage(int value) {
        diceImage.setIcon(null);
        diceImage.setText(String.valueOf(value));
        diceImage.setFont(new Font("Arial", Font.BOLD, 48));
        diceImage.setBackground(Color.WHITE);
        diceImage.setOpaque(true);
    }
    
    /**
     * Check if a player has won the game
     */
    private void checkForWinner(Player player) {
        if (player.hasCompletedAllObjectives() && !gameEnded) {
            gameEnded = true;
            
            // Winner announcement
            String winMessage = "🎉 CONGRATULATIONS! " + player.getName() + 
                              " has completed all objectives and won the game!\n\n";
            
            gameLog.append("\n" + winMessage);
            
            // Show end game dialog
            showEndGameResults(player);
        }
    }
    
    /**
     * Show the end game results with a future headlines feature
     */
    private void showEndGameResults(Player winner) {
        // Stop the timer
        if (gameTimer != null) {
            gameTimer.stop();
        }
        
        // Build headline sequence
        StringBuilder headlines = new StringBuilder();
        headlines.append("🌟 MAKERS VALLEY TRANSFORMATION: 2025-2030 🌟\n\n");
        
        headlines.append("📰 2025: ").append(winner.getName())
                .append(" leads waste management initiative in Makers Valley\n");
        
        headlines.append("📰 2026: Recycling rates increase by 35% in Makers Valley\n");
        
        headlines.append("📰 2027: New composting facility opens, diverting 500 tons of organic waste from landfills\n");
        
        headlines.append("📰 2028: Makers Valley waste collection coverage reaches 95% of households\n");
        
        headlines.append("📰 2029: Community income from recycling enterprises tops R2 million\n");
        
        headlines.append("📰 2030: Makers Valley recognized as South Africa's model for sustainable waste management\n\n");
        
        headlines.append("FINAL STATE OF PLAY:\n");
        
        // Add player statistics
        for (Player p : players) {
            headlines.append(p.getName()).append(" (").append(p.getRole()).append(")\n");
            headlines.append("• Position: ").append(p.getPosition()).append("\n");
            headlines.append("• Cash: R").append(p.getCash()).append("\n");
            headlines.append("• Properties: ").append(p.getPropertyCount()).append("\n");
            
            if (p.getRole().equalsIgnoreCase("Business Owner")) {
                headlines.append("• Eco Score: ").append(p.getEcoScore()).append("\n");
            } else {
                headlines.append("• Public Influence: ").append(p.getPublicInfluence()).append("\n");
            }
            
            headlines.append("\n");
        }
        
        // Display result in dialog
        JTextArea resultText = new JTextArea(headlines.toString());
        resultText.setEditable(false);
        resultText.setFont(new Font("Arial", Font.PLAIN, 14));
        resultText.setLineWrap(true);
        resultText.setWrapStyleWord(true);
        
        JScrollPane scrollPane = new JScrollPane(resultText);
        scrollPane.setPreferredSize(new Dimension(500, 400));
        
        JOptionPane.showMessageDialog(this, scrollPane, 
                                     "Game Over - " + winner.getName() + " Wins!", 
                                     JOptionPane.INFORMATION_MESSAGE);
    }
    
    /**
     * Show game help dialog
     */
    private void showGameHelp() {
        JOptionPane.showMessageDialog(this,
            "Makers Valley Waste Management Game - Help\n\n" +
            "Goal: Complete your objectives by buying properties and enhancing your role scores.\n\n" +
            "Tile Types:\n" +
            "• GO: Collect R200 when passing or landing\n" +
            "• Properties: Buy to earn rent from other players\n" +
            "• Community Fund: Get cash and score bonuses\n" +
            "• Opportunity: Answer questions for score bonuses\n" +
            "• Eco Challenge: Complete eco challenges for big bonuses\n\n" +
            "Player Roles:\n" +
            "• Business Owner: Track Eco Score, starts with R300\n" +
            "• Community Leader: Track Public Influence, starts with R200\n\n" +
            "Objectives:\n" +
            "1. Role Objective: Reach 50 points in your role-specific score\n" +
            "2. Property Objective: Acquire at least 3 properties\n\n" +
            "How to Play:\n" +
            "1. Roll the dice on your turn to move around the board\n" +
            "2. Buy properties when you land on them\n" +
            "3. Complete challenges to increase your role score\n" +
            "4. Pay rent when landing on others' properties\n" +
            "5. First player to complete both objectives wins!",
            "Game Help",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Confirm if the player wants to end the game
     */
    private void confirmEndGame() {
        int choice = JOptionPane.showConfirmDialog(this,
            "Are you sure you want to end the game?",
            "End Game Confirmation",
            JOptionPane.YES_NO_OPTION);
            
        if (choice == JOptionPane.YES_OPTION) {
            // Find player with highest objectives completion
            Player bestPlayer = findBestPlayer();
            gameEnded = true;
            
            // Show end game results
            showEndGameResults(bestPlayer);
        }
    }
    
    /**
     * Find the player with the best progress toward objectives
     */
    private Player findBestPlayer() {
        Player bestPlayer = players.get(0);
        int bestScore = calculatePlayerScore(bestPlayer);
        
        for (int i = 1; i < players.size(); i++) {
            Player player = players.get(i);
            int score = calculatePlayerScore(player);
            
            if (score > bestScore) {
                bestPlayer = player;
                bestScore = score;
            }
        }
        
        return bestPlayer;
    }
    
    /**
     * Calculate a score to compare players' progress
     */
    private int calculatePlayerScore(Player player) {
        int score = 0;
        
        // Add property value
        score += player.getPropertyCount() * 20;
        
        // Add cash value
        score += player.getCash() / 20;
        
        // Add role-specific score
        if (player.getRole().equalsIgnoreCase("Business Owner")) {
            score += player.getEcoScore() * 2;
        } else {
            score += player.getPublicInfluence() * 2;
        }
        
        // Add bonus for completed objectives
        if (player.hasCompletedObjective1()) {
            score += 30;
        }
        
        if (player.hasCompletedObjective2()) {
            score += 30;
        }
        
        return score;
    }
    
    /**
     * Helper method to create default colors if none provided
     */
    private static Map<String, Color> createDefaultPlayerColors(ArrayList<Player> players) {
        Map<String, Color> colors = new HashMap<>();
        Color[] defaultColors = {
            new Color(65, 105, 225),  // Royal Blue
            new Color(220, 20, 60),   // Crimson
            new Color(50, 205, 50),   // Lime Green
            new Color(255, 215, 0)    // Gold
        };
        
        for (int i = 0; i < players.size(); i++) {
            colors.put(players.get(i).getName(), defaultColors[i % defaultColors.length]);
        }
        
        return colors;
    }
    
    /**
     * Helper method to assign colors to players
     */
    private void assignColorsToPlayers(ArrayList<Player> players, Map<String, Color> colors) {
        for (Player player : players) {
            // Get color directly by player name
            Color playerColor = colors.get(player.getName());
            if (playerColor != null) {
                player.setPlayerColor(playerColor);
            } else {
                // If no color is assigned, use a default color
                player.setPlayerColor(Color.GRAY);
            }
        }
    }
    
    /**
     * Get game log text area
     */
    public JTextArea getGameLog() {
        return gameLog;
    }
    
    /**
     * Update player statistics
     */
    public void updatePlayerStats() {
        gameManager.updateAllPlayerStats();
        updateObjectivesDisplay();
    }
    
    /**
     * Clean up resources when the game closes
     */
    @Override
    public void dispose() {
        if (gameTimer != null) {
            gameTimer.stop();
        }
        super.dispose();
    }
    
    /**
     * Main method for testing
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ArrayList<Player> testPlayers = new ArrayList<>();
            
            Player p1 = new Player("Alice");
            p1.setRole("Business Owner");
            p1.applyRole("Business Owner");
            
            Player p2 = new Player("Bob");
            p2.setRole("Community Leader");
            p2.applyRole("Community Leader");
            
            testPlayers.add(p1);
            testPlayers.add(p2);
            
            new WasteManagementGame(testPlayers);
        });
    }
}