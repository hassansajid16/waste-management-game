package wastemanagement;

import javax.swing.*;

public class WasteSortingLauncher {
        // Add new method with callback parameter
        public static void launchSortingGame(Player player, JTextArea gameLog, Runnable onCompleted) {
            try {
                // Log start of activity
                gameLog.append("\n" + player.getName() + " is starting the Waste Sorting Challenge!\n");
                System.out.println("Attempting to launch waste sorting game for " + player.getName());
                
                // Launch game on EDT
                SwingUtilities.invokeLater(() -> {
                    try {
                        // Create and show the game directly with callback
                        WasteSortingGame game = new WasteSortingGame(player, gameLog, onCompleted);
                        game.setVisible(true);
                        System.out.println("Waste Sorting game created successfully");
                    } catch (Throwable e) {
                        // Catch and log any error during game creation
                        System.err.println("ERROR creating waste sorting game: " + e.getMessage());
                        e.printStackTrace();
                        
                        // Provide default rewards if game fails to launch
                        provideDefaultRewards(player, gameLog);
                        
                        // Call callback
                        if (onCompleted != null) {
                            onCompleted.run();
                        }
                    }
                });
            } catch (Throwable e) {
                // Catch and log any error in the launcher itself
                System.err.println("CRITICAL ERROR in waste sorting launcher: " + e.getMessage());
                e.printStackTrace();
                
                // Provide default rewards if launcher fails
                SwingUtilities.invokeLater(() -> {
                    provideDefaultRewards(player, gameLog);
                    
                    // Call callback
                    if (onCompleted != null) {
                        onCompleted.run();
                    }
                });
            }
        }
        
        // Keep original method for backward compatibility
        public static void launchSortingGame(Player player, JTextArea gameLog) {
            launchSortingGame(player, gameLog, null);
        }
    //  Provides default rewards when game fails to launch
     
    private static void provideDefaultRewards(Player player, JTextArea gameLog) {
        gameLog.append("Waste Sorting game couldn't launch. " + player.getName() + " receives default rewards.\n");
        player.collectWaste("recyclables");
        player.collectWaste("foodWaste");
        player.updateCash(100);
        gameLog.append("Default rewards: 1 recyclable, 1 food waste, Rand 100 cash\n");
        
        // Add role-specific bonus
        if (player.getRole().equalsIgnoreCase("Business Owner")) {
            player.increaseEcoScore(2);
            gameLog.append(player.getName() + " gained 2 Eco Score points.\n");
        } else if (player.getRole().equalsIgnoreCase("Community Leader")) {
            player.increasePublicInfluence(2);
            gameLog.append(player.getName() + " gained 2 Public Influence points.\n");
        }
        // Show message to player
        JOptionPane.showMessageDialog(null,
            "Unfortunately, the Waste Sorting mini-game could not be launched.\n" +
            "You have been awarded default rewards:\n" +
            "• 1 recyclable\n" +
            "• 1 food waste\n" +
            "• Rand 100 cash",
            "Mini-Game Not Available",
            JOptionPane.INFORMATION_MESSAGE);
    }
}