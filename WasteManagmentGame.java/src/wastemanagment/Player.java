package wastemanagment;

public class Player {
    private String name;
    private int position;
    private int recyclables;
    private int foodWaste;

    public Player(String name) {
        this.name = name;
        this.position = 0;
        this.recyclables = 0;
        this.foodWaste = 0;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void move(int steps) {
        position = (position + steps) % Board.BOARD_SIZE;
    }

    public void collectWaste(String type) {
        if (type.equals("recyclables")) recyclables++;
        if (type.equals("foodWaste")) foodWaste++;
    }

    public void depositWaste(String centerType) {
        if (centerType.equals("Pikitup")) recyclables = 0;
        if (centerType.equals("Composting")) foodWaste = 0;
    }

    public int getRecyclables() {
        return recyclables;
    }

    public int getFoodWaste() {
        return foodWaste;
    }
}

