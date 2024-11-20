package wastemanagment;

public class Tile {
    private String type;
private String name;
    public Tile(String type, String name) {
        this.type = type;
    this.name = name;
    }

    public String getType() {
        return type;
    }
    public  String getName() {
    	return name;
    }
}
