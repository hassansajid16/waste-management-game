package wastemanagment;

public class Board {
    public static final int BOARD_SIZE = 15;
    private Tile[] tiles;

    public Board() {
        tiles = new Tile[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
        if (i == 0) {
        	tiles[i] = new Tile("Resource Square", "Resource Square");
        }
        else if ( i == BOARD_SIZE - 1) {
        	tiles[i] = new Tile("End", "End");
        }
        else if (i < 9 && i > 5) {
        	tiles[i] = new Tile(" ", " ");
        }
        	else if (i == 2 || i == 9 || i == 12) {
                tiles[i] = new Tile("Refill Resources", "Refill Resources");
            }else {
                tiles[i] = new Tile("Task", "Task");
            }
            
        }
    }

    public Tile getTile(int position) {
        return tiles[position];
    }
}
