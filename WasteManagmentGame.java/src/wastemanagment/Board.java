package wastemanagment;

public class Board {
    public static final int BOARD_SIZE = 20;
    private Tile[] tiles;

    public Board() {
        tiles = new Tile[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (i % 5 == 0) {
                tiles[i] = new Tile("Pikitup Center");
            } else if (i % 7 == 0) {
                tiles[i] = new Tile("Composting Center");
            } else {
                tiles[i] = new Tile("Residential");
            }
        }
    }

    public Tile getTile(int position) {
        return tiles[position];
    }
}
