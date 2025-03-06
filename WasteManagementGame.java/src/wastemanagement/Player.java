package wastemanagement;

public class Player {
    private String name;
    private int position;
    private int recyclables;
    private int foodWaste;
    private int cash;
private String role;

    public Player(String name) {
        this.name = name;
        this.position = 0;
        this.recyclables = 0;
        this.foodWaste = 0;
        this.cash = 0;
        this.role =  "null";
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
        if (type.equals("recyclables")) recyclables++;
        if (type.equals("foodWaste")) foodWaste++;
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
}

