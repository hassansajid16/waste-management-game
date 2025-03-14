package wastemanagement;

public class Board {
    public static final int BOARD_SIZE = 20; // Keep the same board size
    private Tile[] tiles;

    public Board() {
        tiles = new Tile[BOARD_SIZE];
      

   
                tiles = new Tile[BOARD_SIZE];
        
                for (int i = 0; i < BOARD_SIZE; i++) {
                    if (i == 0) {
                        // GO square
                        tiles[i] = new Tile("StartSquare", "StartSquare");
                    } else {
                        // Regular properties named after Makers Valley locations
                        switch (i) {
                            case 1: tiles[i] = new Tile("Kimberely Road", "Property"); break;
                            case 2: tiles[i] = new Tile("Gordon Road", "Property"); break;
                            case 3: tiles[i] = new Tile("Queen Street", "Property"); break;
                            case 4: tiles[i] = new Tile("Fuller Street", "Property"); break;
                            case 5: tiles[i] = new Tile("Johaness Street", "Property"); break;
                            case 6: tiles[i] = new Tile("Charles Street", "Property"); break;
                            case 7: tiles[i] = new Tile("Princes Street", "Property"); break;
                            case 8: tiles[i] = new Tile("Bezuidenhout Street", "Property"); break;
                            case 9: tiles[i] = new Tile("Sydney Road", "Property"); break;
                            case 10: tiles[i] = new Tile("Thames Road", "Property"); break;
                            case 11: tiles[i] = new Tile("Appolonia Street", "Property"); break;
                            case 12: tiles[i] = new Tile("Cornelia Street", "Property"); break;
                            case 13: tiles[i] = new Tile("Frere Road", "Property"); break;
                            case 14: tiles[i] = new Tile("Jacoba Street", "Property"); break;
                            case 15: tiles[i] = new Tile("Terrace Road", "Property"); break;
                            case 16: tiles[i] = new Tile("Nourse Street", "Property"); break;
                            case 17: tiles[i] = new Tile("Sports Avenue", "Property"); break;
                            case 18: tiles[i] = new Tile("Jules Street", "Property"); break;
                            case 19: tiles[i] = new Tile("Victoria Road", "Property"); break;
                            default: tiles[i] = new Tile("Makers Street", "Property");
                        }
                    }
                }
            }
        
            public Tile getTile(int position) {
                return tiles[position];
            }
        }
        