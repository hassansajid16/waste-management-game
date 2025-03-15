package wastemanagement;

public class Board {
    public static final int BOARD_SIZE = 20; // Keep the same board size
    private Tile[] tiles;

    public Board() {
        tiles = new Tile[BOARD_SIZE];
      
        for (int i = 0; i < BOARD_SIZE; i++) {
            if (i == 0) {
                // GO square - keep this unchanged
                tiles[i] = new Tile("Waste Management Facility", "StartSquare");
            } else {
                // Alternating between SpecialTask and EcoChallenge
                String tileType;
                
                // Even positions get SpecialTask, odd positions get EcoChallenge
                if (i % 2 == 0) {
                    tileType = "SpecialTask"; // Quiz game
                } else {
                    tileType = "EcoChallenge"; // Waste sorting game
                }
                
                // Assign names to each position
                switch (i) {
                    case 1: tiles[i] = new Tile("Kimberely Road", tileType); break;
                    case 2: tiles[i] = new Tile("Gordon Road", tileType); break;
                    case 3: tiles[i] = new Tile("Queen Street", tileType); break;
                    case 4: tiles[i] = new Tile("Fuller Street", tileType); break;
                    case 5: tiles[i] = new Tile("Johaness Street", tileType); break;
                    case 6: tiles[i] = new Tile("Charles Street", tileType); break;
                    case 7: tiles[i] = new Tile("Princes Street", tileType); break;
                    case 8: tiles[i] = new Tile("Bezuidenhout Street", tileType); break;
                    case 9: tiles[i] = new Tile("Sydney Road", tileType); break;
                    case 10: tiles[i] = new Tile("Thames Road", tileType); break;
                    case 11: tiles[i] = new Tile("Appolonia Street", tileType); break;
                    case 12: tiles[i] = new Tile("Cornelia Street", tileType); break;
                    case 13: tiles[i] = new Tile("Frere Road", tileType); break;
                    case 14: tiles[i] = new Tile("Jacoba Street", tileType); break;
                    case 15: tiles[i] = new Tile("Terrace Road", tileType); break;
                    case 16: tiles[i] = new Tile("Nourse Street", tileType); break;
                    case 17: tiles[i] = new Tile("Sports Avenue", tileType); break;
                    case 18: tiles[i] = new Tile("Jules Street", tileType); break;
                    case 19: tiles[i] = new Tile("Victoria Road", tileType); break;
                    default: tiles[i] = new Tile("Makers Street", tileType);
                }
            }
        }
    }
        
    public Tile getTile(int position) {
        return tiles[position];
    }
}