package wastemanagement;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int position;
    private int recyclables;
    private int foodWaste;
    private int cash;
    private String role;
    private Color playerColor;
    
    // New properties for Monopoly-style gameplay
    private List<Integer> ownedProperties;
    private int ecoScore;
    private int publicInfluence;
    private boolean hasCompletedObjective1;
    private boolean hasCompletedObjective2;

    public Player(String name) {
        this.name = name;
        this.position = 0;
        this.recyclables = 0;
        this.foodWaste = 0;
        this.cash = 0;
        this.role = "null";
        this.playerColor = Color.GRAY; // Default color
        
        // Initialize new properties
        this.ownedProperties = new ArrayList<>();
        this.ecoScore = 0;
        this.publicInfluence = 0;
        this.hasCompletedObjective1 = false;
        this.hasCompletedObjective2 = false;
    }
        
    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }
    
    public String setRole(String input) {
        role = input;
        return role;
    }
    
    public void move(int steps) {
        position = (position + steps) % Board.BOARD_SIZE;
    }

    public void collectWaste(String type) {
        switch(type) {
            case "recyclables":
                recyclables++;
                break;
            case "foodWaste":
                foodWaste++;
                break;
            case "useRecyclables":
                recyclables = Math.max(0, recyclables - 1);
                break;
            case "useFoodWaste":
                foodWaste = Math.max(0, foodWaste - 1);
                break;
        }
    }

    public void depositWaste(String centerType) {
        if (centerType.equals("Pikitup")) {
            recyclables = 0;
        }
        if (centerType.equals("Composting")) {
            foodWaste = 0;
        }
        if (centerType.equals(" ")) {
            position += 1;
        }
    }

    public int getRecyclables() {
        return recyclables;
    }

    public int getFoodWaste() {
        return foodWaste;
    }
    
    public int getCash() {
        return cash;
    }
    
    public String getRole() {
        return role;
    }
    
    public Color getPlayerColor() {
        return playerColor;
    }
    
    public void setPlayerColor(Color color) {
        this.playerColor = color;
    }
    
    public void applyRole(String role) {
        if(role.equalsIgnoreCase("Business Owner")) {
            cash = 7000;    //this is around £300
        }
        else if (role.equalsIgnoreCase("Community Leader")) {
            cash = 4000;	//this is around £170
        }
    }
    
    public void updateCash(int money) {
        cash += money;
    }
    
    // New methods for Monopoly-style gameplay
    
    public boolean ownsProperty(int propertyPosition) {
        return ownedProperties.contains(propertyPosition);
    }
    
    public void buyProperty(int propertyPosition) {
        if (!ownedProperties.contains(propertyPosition)) {
            ownedProperties.add(propertyPosition);
        }
    }
    
    public List<Integer> getOwnedProperties() {
        return ownedProperties;
    }
    
    public int getPropertyCount() {
        return ownedProperties.size();
    }
    
    public int getEcoScore() {
        return ecoScore;
    }
    
    public void increaseEcoScore(int amount) {
        this.ecoScore += amount;
    }
    
    public int getPublicInfluence() {
        return publicInfluence;
    }
    
    public void increasePublicInfluence(int amount) {
        this.publicInfluence += amount;
    }
    
    public boolean hasCompletedObjective1() {
        return hasCompletedObjective1;
    }
    
    public void completeObjective1() {
        this.hasCompletedObjective1 = true;
    }
    
    public boolean hasCompletedObjective2() {
        return hasCompletedObjective2;
    }
    
    public void completeObjective2() {
        this.hasCompletedObjective2 = true;
    }
    
    public boolean hasCompletedAllObjectives() {
        return hasCompletedObjective1 && hasCompletedObjective2;
    }
    
    @Override
    public String toString() {
        String baseInfo = name + " (" + role + ") - Position: " + position + 
               ", Recyclables: " + recyclables + 
               ", Food Waste: " + foodWaste + 
               ", Cash: Rand " + cash;
               
        // Add role-specific scores
        if (role.equalsIgnoreCase("Business Owner")) {
            baseInfo += ", Eco Score: " + ecoScore;
        } else if (role.equalsIgnoreCase("Community Leader")) {
            baseInfo += ", Public Influence: " + publicInfluence;
        }
        
        return baseInfo;
    }
}