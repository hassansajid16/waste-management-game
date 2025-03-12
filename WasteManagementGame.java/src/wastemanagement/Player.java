package wastemanagement;

import java.awt.Color;

public class Player {
    private String name;
    private int position;
    private int recyclables;
    private int foodWaste;
    private int cash;
    private String role;
    private Color playerColor; // Added field to store the player's color

    public Player(String name) {
        this.name = name;
        this.position = 0;
        this.recyclables = 0;
        this.foodWaste = 0;
        this.cash = 0;
        this.role = "null";
        this.playerColor = Color.GRAY; // Default color
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
    
    // New methods for player color
    public Color getPlayerColor() {
        return playerColor;
    }
    
    public void setPlayerColor(Color color) {
        this.playerColor = color;
    }
    
    public void applyRole(String role) {
        if(role.equalsIgnoreCase("Business Owner")) {
            cash = 300;
        }
        else if (role.equalsIgnoreCase("Community Leader")) {
            cash = 200;	
        }
    }
    
    public void updateCash(int money) {
        cash += money;
    }
    
    @Override
    public String toString() {
        return name + " (" + role + ") - Position: " + position + 
               ", Recyclables: " + recyclables + 
               ", Food Waste: " + foodWaste + 
               ", Cash: R" + cash;
    }
}