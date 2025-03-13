package wastemanagement;

public class Board {
    public static final int BOARD_SIZE = 20; // Keep the same board size
    private Tile[] tiles;

    public Board() {
        tiles = new Tile[BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (i == 0) {
                // Starting point remains Resource Square but is now the "GO" equivalent
                tiles[i] = new Tile("GO", "StartSquare");
            }
            else if (i == 5 || i == 10 || i == 15) {
                // Change Refill Resources to Community Chest equivalent
                tiles[i] = new Tile("Community Fund", "CommunityFund");
            }
            else if (i == 3 || i == 13) {
                // Keep Special Task as Chance equivalent
                tiles[i] = new Tile("Opportunity", "SpecialTask");
            }
            else if (i == 8 || i == 18) {
                // Change Waste Sorting to special minigame squares
                tiles[i] = new Tile("Eco Challenge", "EcoChallenge");
            }
            else if (i == 19) {
                // Exchange square becomes a premium property
                tiles[i] = new Tile("Victoria Yards", "Property");
            }
            else {
                // Regular properties named after Makers Valley locations
                switch(i) {
                    case 1:
                        tiles[i] = new Tile("Ellis Avenue", "Property");
                        break;
                    case 2:
                        tiles[i] = new Tile("Janie Street", "Property");
                        break;
                    case 4:
                        tiles[i] = new Tile("Marathon Street", "Property");
                        break;
                    case 6:
                        tiles[i] = new Tile("Andries Street", "Property");
                        break;
                    case 7:
                        tiles[i] = new Tile("Marjorie Street", "Property");
                        break;
                    case 9:
                        tiles[i] = new Tile("Albertina Street", "Property");
                        break;
                    case 11:
                        tiles[i] = new Tile("Frere Road", "Property");
                        break;
                    case 12:
                        tiles[i] = new Tile("Bezuidenhout Street", "Property");
                        break;
                    case 14:
                        tiles[i] = new Tile("Gordon Road", "Property");
                        break;
                    case 16:
                        tiles[i] = new Tile("Beacon Road", "Property");
                        break;
                    case 17:
                        tiles[i] = new Tile("Burford Road", "Property");
                        break;
                    default:
                        tiles[i] = new Tile("Makers Street", "Property");
                }
            }
        }
    }

    public Tile getTile(int position) {
        return tiles[position];
    }
}