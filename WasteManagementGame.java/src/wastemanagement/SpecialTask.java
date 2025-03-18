package wastemanagement;

import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class SpecialTask {
    private static final String[] QUESTIONS = {
        "Do residents of Makers Valley produce more waste per person compared to the city's average?", 
        "Is more than half of Johannesburg's total waste successfully recycled each year?",
        "Can turning organic waste into compost help minimize landfill use and benefit local farming?",
        "Does Pikitup have a goal of cutting landfill waste by at least 33% through the Separation at Source program?",
        "Is illegal dumping in Makers Valley an uncommon issue?",
        "Do informal waste collectors play a role in recycling by retrieving valuable materials from waste?",
        "Does uncollected waste in informal settlements contribute to a rise in respiratory and waterborne illnesses?",
        "Are Pikitup workers the sole providers of waste collection and processing in Johannesburg?",
        "Would placing separate waste bins in Makers Valley help tackle the problem of illegal dumping?",
        "Does Johannesburg have an endless capacity for landfill waste?", 
        "Is the City of Johannesburg and Pikitup spending more than Rand 80 million each year to manage illegal dumping?"
    };

    private static final boolean[] ANSWERS = {
        true, false, true, true, false, true, true, false, true, false, true
    };
    
    // Added detailed explanations for each answer
    private static final String[] EXPLANATIONS = {
        "The area generates around 0.8 kg per person daily, whereas the citywide average is 0.65 kg.",
        "Only about 10% of the city's waste is processed or recycled.",
        "Composting reduces landfill waste while also providing useful resources for agriculture.",
        "The initiative encourages residents to separate waste for better recycling.",
        "It remains a widespread problem, particularly in informal settlements and public areas.",
        "They recover and recycle between 20-25% of recyclable materials daily.",
        "The lack of proper waste management has led to a 25% increase in these health issues.",
        "Informal waste collectors and composting initiatives also contribute significantly.",
        "Providing designated bins for different types of waste can improve disposal habits and cleanliness.",
        "The city's landfill space is running out and may reach full capacity in less than eight years.",
        "Illegal dumping is a major financial burden, worsening the city's waste management problems."
    };

    public static void askQuestion(Player player, JTextArea gameLog, Runnable onCompleted) {
        Random rand = new Random();
        
        // Pick a random question
        int index = rand.nextInt(QUESTIONS.length);
        
        // Display question in game log
        gameLog.append("\n Special Task for " + player.getName() + ":\n");
        gameLog.append(QUESTIONS[index] + " (True/False)?\n");
        
        SwingUtilities.invokeLater(() -> {
            int choice = JOptionPane.showConfirmDialog(
                null, QUESTIONS[index], "BINOVATORS Initiative", JOptionPane.YES_NO_OPTION
            );
            
            boolean playerAnswer = (choice == JOptionPane.YES_OPTION);
            boolean correctAnswer = ANSWERS[index];
            
            // Include the detailed explanation in the response
            if(playerAnswer == correctAnswer){
                gameLog.append("Correct! " + (correctAnswer ? "Yes" : "No") + " - " + EXPLANATIONS[index] + "\n");
                gameLog.append(player.getName() + " completes the special task successfully.\n");
                
                // Existing reward code...
                
            } else {
                gameLog.append("Incorrect. The answer is " + (correctAnswer ? "Yes" : "No") + " - " + EXPLANATIONS[index] + "\n");
                gameLog.append("No reward for " + player.getName() + ".\n");
            }
            
            // Call the completion callback
            if (onCompleted != null) {
                onCompleted.run();
            }
        });
    }
    
    // Add original method for backward compatibility
    public static void askQuestion(Player player, JTextArea gameLog) {
        askQuestion(player, gameLog, null);
    }
    /**
     * Launches the waste sorting mini-game
     */
    private static void launchWasteSortingGame(Player player, JTextArea gameLog) {
        gameLog.append("\n " + player.getName() + " has encountered a Waste Sorting Challenge!\n");
        gameLog.append("Sort the waste items correctly to earn rewards.\n");
        
        // Launch the waste sorting game in the EDT
        SwingUtilities.invokeLater(() -> {
            new WasteSortingGame(player, gameLog);
        });
    }
}