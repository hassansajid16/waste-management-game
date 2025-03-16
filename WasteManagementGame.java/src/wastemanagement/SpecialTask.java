package wastemanagement;

import java.util.Random;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class SpecialTask {
    private static final String[] QUESTIONS = {
        "Pikitup is responsible for waste management services in Johannesburg, including Makers Valley.",
        "Pikitup operates waste collection services in Makers Valley on a daily basis.",
        "There is an abundance of public waste bins provided by Pikitup in Makers Valley.",
        "Pikitup provides separate waste collection for recyclable and non-recyclable waste in Makers Valley.",
        "Pikitup has a recycling incentive program for residents who actively recycle their waste.",
        "Pikitup's goal is to achieve zero waste to landfill by 2030.",
        "Pikitup has a mobile app where residents can report illegal dumping and missed waste collection.",
        "Pikitup partners with local businesses to raise awareness about proper waste disposal.",
        "Pikitup offers composting workshops to educate residents about organic waste management.",
        "Makers Valley has a centralized public waste disposal system for residents to dispose of their waste.",
        "Pikitup has a dedicated fleet of waste collection trucks serving the Makers Valley area.",
        "Pikitup offers free waste disposal bags for garden refuse and recyclables in Makers Valley.",
        "Makers Valley has a problem with illegal dumping due to inconsistent waste collection services.",
        "Pikitup conducts regular waste audits in Makers Valley to analyze waste disposal habits.",
        "There are no public waste management challenges in Makers Valley.",
        "Pikitup encourages residents of Makers Valley to reduce their waste footprint through education programs.",
        "The community of Makers Valley experiences frequent missed waste collection by Pikitup.",
        "Pikitup provides a separate waste collection service for hazardous waste in Makers Valley.",
        "Makers Valley does not have enough waste collection points for all its residents.",
        "Pikitup maintains the waste collection schedule regularly in Makers Valley.",
        "Makers Valley has a growing recycling culture due to Pikitup's efforts.",
        "Pikitup offers a large recycling bin to each household in Makers Valley.",
        "Pikitup manages waste collection on weekends in Makers Valley to avoid waste buildup.",
        "There is an initiative in Makers Valley to encourage reducing single-use plastics through Pikitup programs.",
        "Makers Valley does not have adequate facilities for electronic waste disposal.",
        "Pikitup regularly cleans up areas prone to illegal dumping in Makers Valley.",
        "Pikitup educates the community of Makers Valley about the benefits of sorting waste at home.",
        "Makers Valley residents are actively involved in keeping their streets clean with Pikitup's support.",
        "Makers Valley has implemented measures to reduce littering and encourage responsible waste disposal.",
        "Pikitup's collection services in Makers Valley are extended to public areas and parks.",
        "Makers Valley residents complain about missed waste collection due to Pikitup's service gaps.",
        "Pikitup collaborates with the community in Makers Valley to combat illegal dumping.",
        "Pikitup provides green waste collection services in Makers Valley on a weekly basis.",
        "Pikitup encourages the reduction of waste by offering drop-off points for recyclables in Makers Valley.",
        "Pikitup has successfully reduced landfill waste in Makers Valley through its recycling initiatives.",
        "Makers Valley has community clean-up campaigns supported by Pikitup.",
        "Pikitup has reduced waste overflow in high-density areas of Makers Valley.",
        "Makers Valley residents are encouraged to use Pikitup's mobile app for waste-related queries.",
        "Pikitup handles the collection of hazardous waste such as paint and batteries in Makers Valley.",
        "Makers Valley has efficient waste sorting programs in place thanks to Pikitup.",
        "Pikitup conducts waste separation at the curb in Makers Valley.",
        "Makers Valley has seen a decrease in waste-related complaints since Pikitup started its initiatives.",
        "Pikitup provides waste disposal services for major events in Makers Valley.",
        "Pikitup's recycling efforts have reduced the amount of waste sent to landfills in Makers Valley.",
        "Makers Valley has dedicated collection times for organic waste through Pikitup."
    };

    private static final boolean[] ANSWERS = {
        true, false, false, true, true, true, true, true, true, true,
        true, false, true, true, false, true, true, false, true, false,
        true, false, false, true, false, true, true, true, true, true,
        true, false, true, false, true, true, true, true, true, true,
        true, false, true, true, true, true, true, true, true, true
    };

    public static void askQuestion(Player player, JTextArea gameLog) {
        Random rand = new Random();
        
        // Pick a random question
        int index = rand.nextInt(QUESTIONS.length);
        
        // Display question in game log
        gameLog.append("\n Special Task for " + player.getName() + ":\n");
        gameLog.append(QUESTIONS[index] + " (True/False)?\n");
        
        SwingUtilities.invokeLater(() -> {
            int choice = JOptionPane.showConfirmDialog(
                null, QUESTIONS[index], "BinNovaters Initiative", JOptionPane.YES_NO_OPTION
            );
            
            boolean playerAnswer = (choice == JOptionPane.YES_OPTION);
            boolean correctAnswer = ANSWERS[index];
            
            if(playerAnswer == correctAnswer){
                gameLog.append("Correct! " + player.getName() + " completes the special task.\n");
                
                // Award cash for correct answer
                player.updateCash(1000);
                gameLog.append(" Earned R1000 for your knowledge about waste management!\n");
                
                // Random amount of recyclables and food waste
                int recyclablesEarned = rand.nextInt(3) + 1;
                int foodWasteEarned = rand.nextInt(3) + 1;
                
                // Add the earned resources to the player
                for(int i = 0; i < recyclablesEarned; i++) {
                    player.collectWaste("recyclables");
                }
                
                for(int i = 0; i < foodWasteEarned; i++) {
                    player.collectWaste("foodWaste");
                }
                
                gameLog.append("Also Collected " + recyclablesEarned + " recyclables and " + 
                             foodWasteEarned + " units of food waste!\n");
                
                // Role-specific bonus
                if (player.getRole().equalsIgnoreCase("Business Owner")) {
                    player.increaseEcoScore(5);
                    gameLog.append("Eco Score increased to " + player.getEcoScore() + "\n");
                } else if (player.getRole().equalsIgnoreCase("Community Leader")) {
                    player.increasePublicInfluence(5);
                    gameLog.append("Public Influence increased to " + player.getPublicInfluence() + "\n");
                }
            } else {
                gameLog.append("Incorrect. No reward for " + player.getName() + ".\n");
            }
        });
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