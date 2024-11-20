package wastemanagment;

public class Board {
    public static final int BOARD_SIZE = 20;
    private Tile[] tiles;

    public Board() {
        tiles = new Tile[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
        if (i == 0) {
        	tiles[i] = new Tile("Start", "Start");
        }
        else if ( i == BOARD_SIZE - 1) {
        	tiles[i] = new Tile("End", "End");
        }
        	else if (i % 5 == 0) {
                tiles[i] = new Tile("Pikitup Center", "Pikitup Center");
            } else if (i % 7 == 0) {
                tiles[i] = new Tile("Composting Center", "Composting Center");
            } else {
                tiles[i] = new Tile("Residential", " Residential");
            }
            
        }
    }

    public Tile getTile(int position) {
        return tiles[position];
    }
}
