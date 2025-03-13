package wastemanagement;
public class Tile {
    private String name;
    private String type;
    private int cost;  // Property cost
    private int rent;  // Rent amount

    public Tile(String name, String type) {
        this.name = name;
        this.type = type;

        // Assign costs and rent values for properties
        if (type.equals("Property")) {
            switch (name) {
                case "Ellis Avenue":
                case "Janie Street":
                    this.cost = 100; this.rent = 20;
                    break;
                case "Marathon Street":
                case "Andries Street":
                    this.cost = 150; this.rent = 30;
                    break;
                case "Marjorie Street":
                case "Albertina Street":
                    this.cost = 200; this.rent = 40;
                    break;
                case "Frere Road":
                case "Bezuidenhout Street":
                    this.cost = 250; this.rent = 50;
                    break;
                case "Gordon Road":
                case "Beacon Road":
                    this.cost = 300; this.rent = 60;
                    break;
                case "Burford Road":
                case "Victoria Yards":
                    this.cost = 400; this.rent = 75;
                    break;
                default:
                    this.cost = 100; this.rent = 20;
            }
        } else {
            this.cost = 0;
            this.rent = 0;
        }
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public int getCost() { return cost; }
    public int getRent() { return rent; }
}
