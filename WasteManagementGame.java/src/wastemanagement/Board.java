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
            else {
                tiles[i] = new Tile("Task", "Task");
            }
        }
    }

    public Tile getTile(int position) {
        return tiles[position];
    }
}