package wastemanagement;

public class Board {
    public static final int BOARD_SIZE = 20; // Increased from 15 to 20
    private Tile[] tiles;

    public Board() {
        tiles = new Tile[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (i == 0) {
                tiles[i] = new Tile("Resource Square", "Resource Square");
            }
            else if (i == 5 || i == 10 || i == 15) {
                tiles[i] = new Tile("Refill Resources", "Refill Resources");
            }
            else if (i == 3 || i == 8 || i == 13 || i == 18) {
                tiles[i] = new Tile("Special Task", "Special Task");
            }
            else if (i == 19) {
                // Replacing the "End" square with "Waste Exchange" square
                tiles[i] = new Tile("Waste Exchange", "Waste Exchange");
            }
            else {
                tiles[i] = new Tile("Task", "Task");
            }
        }
    }

    public Tile getTile(int position) {
        return tiles[position];
    }
}