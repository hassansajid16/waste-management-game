// package wastemanagement;

// import java.awt.*;
// import java.awt.event.*;
// import javax.swing.*;

// /**
//  * Dialog for selecting which objective mini-game to play
//  */
// public class ObjectiveGameDialog extends JDialog {
//     // Constants for game mechanics
//     private static final int MINIMUM_CASH = 200;
//     private static final Color BACKGROUND_COLOR = new Color(45, 65, 45);
//     private static final Color BUTTON_COLOR_QUIZ = new Color(76, 175, 80);     // Green
//     private static final Color BUTTON_COLOR_SORTING = new Color(33, 150, 243); // Blue
    
//     // Game state variables
//     private boolean gameSelected = false;
//     private String selectedGame = "";
    
//     /**
//      * Constructor for the Objective Game Dialog
//      * @param parent Parent frame (can be null)
//      * @param player Player selecting the game
//      * @param gameLog Game log to record game actions
//      */
//     public ObjectiveGameDialog(JFrame parent, Player player, JTextArea gameLog) {
//         // Call parent constructor with modal setting
//         super(parent, "Select Objective Game", true);
        
//         // Set dialog properties
//         setSize(600, 500);
//         setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
//         setLocationRelativeTo(parent);
        
//         // Create main panel with border layout
//         JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
//         mainPanel.setBackground(BACKGROUND_COLOR);
//         mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
//         // Check if player has enough cash
//         if (player.getCash() < MINIMUM_CASH) {
//             setupInsufficientFundsPanel(mainPanel, player);
//         } else {
//             setupGameSelectionPanel(mainPanel, player, gameLog);
//         }
        
//         // Set content pane
//         setContentPane(mainPanel);
//     }
    
//     /**
//      * Setup panel for insufficient funds
//      */
//     private void setupInsufficientFundsPanel(JPanel panel, Player player) {
//         // Title label
//         JLabel titleLabel = new JLabel("Insufficient Funds", SwingConstants.CENTER);
//         titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//         titleLabel.setForeground(Color.WHITE);
        
//         // Info message
//         JLabel infoLabel = new JLabel(
//             "<html><center>" +
//             "You need R" + MINIMUM_CASH + " to start an objective game.<br><br>" +
//             "Your current cash: R" + player.getCash() + "<br><br>" +
//             "You need R" + (MINIMUM_CASH - player.getCash()) + " more.</center></html>", 
//             SwingConstants.CENTER
//         );
//         infoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
//         infoLabel.setForeground(Color.WHITE);
        
//         // Close button
//         JButton closeButton = new JButton("Close");
//         closeButton.setFont(new Font("Arial", Font.BOLD, 14));
//         closeButton.setBackground(Color.LIGHT_GRAY);
//         closeButton.addActionListener(e -> dispose());
        
//         // Layout setup
//         panel.setLayout(new BorderLayout(0, 20));
//         panel.add(titleLabel, BorderLayout.NORTH);
//         panel.add(infoLabel, BorderLayout.CENTER);
        
//         JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
//         buttonPanel.setOpaque(false);
//         buttonPanel.add(closeButton);
//         panel.add(buttonPanel, BorderLayout.SOUTH);
//     }
    
//     /**
//      * Setup game selection panel
//      */
//     private void setupGameSelectionPanel(JPanel panel, Player player, JTextArea gameLog) {
//         // Title label
//         JLabel titleLabel = new JLabel("Select Objective Game", SwingConstants.CENTER);
//         titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
//         titleLabel.setForeground(Color.WHITE);
        
//         // Game selection panel
//         JPanel gameSelectionPanel = new JPanel(new GridLayout(1, 2, 20, 0));
//         gameSelectionPanel.setOpaque(false);
        
//         // Quiz Challenge Button
//         JButton quizButton = createGameButton(
//             "Quiz Challenge", 
//             "Test your knowledge about waste management and\n" +
//             "sustainable practices in Makers Valley.\n\n" +
//             "Cost: R" + MINIMUM_CASH, 
//             BUTTON_COLOR_QUIZ
//         );
//         quizButton.addActionListener(e -> {
//             // Perform quiz challenge
//             performGameAction(player, gameLog, "quiz");
//         });
        
//         // Waste Sorting Challenge Button
//         JButton wasteSortingButton = createGameButton(
//             "Waste Sorting", 
//             "Sort waste items into correct bins and\n" +
//             "demonstrate your recycling skills.\n\n" +
//             "Cost: R" + MINIMUM_CASH, 
//             BUTTON_COLOR_SORTING
//         );
//         wasteSortingButton.addActionListener(e -> {
//             // Perform waste sorting challenge
//             performGameAction(player, gameLog, "waste");
//         });
        
//         // Add buttons to panel
//         gameSelectionPanel.add(quizButton);
//         gameSelectionPanel.add(wasteSortingButton);
        
//         // Player info
//         JLabel playerInfoLabel = new JLabel(
//             "Player: " + player.getName() + " | Cash: R" + player.getCash(), 
//             SwingConstants.CENTER
//         );
//         playerInfoLabel.setFont(new Font("Arial", Font.BOLD, 16));
//         playerInfoLabel.setForeground(Color.WHITE);
        
//         // Combine components
//         panel.setLayout(new BorderLayout(0, 20));
//         panel.add(titleLabel, BorderLayout.NORTH);
//         panel.add(gameSelectionPanel, BorderLayout.CENTER);
//         panel.add(playerInfoLabel, BorderLayout.SOUTH);
//     }
    
//     /**
//      * Create a styled game selection button
//      */
//     private JButton createGameButton(String title, String description, Color baseColor) {
//         JButton button = new JButton(
//             "<html><center><b>" + title + "</b><br>" +
//             "<small>" + description.replace("\n", "<br>") + "</small></center></html>"
//         );
        
//         // Button styling
//         button.setFont(new Font("Arial", Font.BOLD, 16));
//         button.setBackground(baseColor);
//         button.setForeground(Color.WHITE);
//         button.setPreferredSize(new Dimension(250, 300));
//         button.setFocusPainted(false);
//         button.setBorderPainted(true);
//         button.setBorder(BorderFactory.createCompoundBorder(
//             BorderFactory.createLineBorder(Color.WHITE, 2),
//             BorderFactory.createEmptyBorder(10, 10, 10, 10)
//         ));
        
//         // Hover effects
//         button.addMouseListener(new MouseAdapter() {
//             public void mouseEntered(MouseEvent evt) {
//                 button.setBackground(baseColor.darker());
//             }
            
//             public void mouseExited(MouseEvent evt) {
//                 button.setBackground(baseColor);
//             }
//         });
        
//         return button;
//     }
    
//     /**
//      * Perform the selected game action
//      */
//     private void performGameAction(Player player, JTextArea gameLog, String gameType) {
//         // Deduct game cost
//         player.updateCash(-MINIMUM_CASH);
        
//         // Log game selection
//         gameLog.append("💰 " + player.getName() + " spent R" + MINIMUM_CASH + " to play " + 
//                      (gameType.equals("quiz") ? "Quiz Challenge" : "Waste Sorting Challenge") + "!\n");
        
//         // Close this dialog
//         dispose();
        
//         // Launch appropriate game
//         if (gameType.equals("quiz")) {
//             SpecialTask.askQuestion(player, gameLog);
//         } else {
//             WasteSortingLauncher.launchSortingGame(player, gameLog);
//         }
//     }
    
//     /**
//      * Check if a game was selected
//      * @return true if a game was selected, false otherwise
//      */
//     public boolean wasGameSelected() {
//         return gameSelected;
//     }
    
//     /**
//      * Get the selected game type
//      * @return String representing the selected game type
//      */
//     public String getSelectedGame() {
//         return selectedGame;
//     }
// }