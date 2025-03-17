package wastemanagement;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 * Custom panel that represents a game board tile and can display player tokens
 */
public class GameTilePanel extends JPanel {
    private String tileName;
    private String tileType;
    private int position;
    private boolean highlighted = false;
    private Map<Player, PlayerToken> playerTokens = new HashMap<>();
    
    // New property related fields
    private Player owner;
    private boolean isProperty;
    private int propertyPrice;
    
    // Styling constants
    private Color baseColor;
    private Color highlightColor = Color.YELLOW;
    protected Font titleFont = new Font("Arial", Font.BOLD, 12);
    protected Font positionFont = new Font("Arial", Font.PLAIN, 10);
    
    public GameTilePanel(String tileName, String tileType, int position) {
        this.tileName = tileName;
        this.tileType = tileType;
        this.position = position;
        this.isProperty = tileType.equals("Property");
        
        // Set property price based on position
        if (isProperty) {
            this.propertyPrice = 1000 + (position * 20);
        }
        
        // Set tile background color based on type
        setBaseColorByType(tileType);
        
        setPreferredSize(new Dimension(120, 90));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        
        // Enable proper rendering
        setOpaque(true);
        
        // Debug print the tile name for troubleshooting
        System.out.println("Creating tile: " + position + " - " + tileName + " (" + tileType + ")");
    }
    
    private void setBaseColorByType(String type) {
        switch (type) {
            case "StartSquare":
                baseColor = new Color(89, 116, 89);  // Green for GO
                break;
            case "SpecialTask":
                baseColor = new Color(142, 92, 174);  // Purple for Quiz Challenge
                break;
            case "EcoChallenge":
                baseColor = new Color(46, 139, 87);   // Sea Green for Waste Sorting
                break;
            default:
                baseColor = new Color(70, 77, 77);    // Default dark slate
        }
    }
    
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
        setBorder(highlighted ? 
                BorderFactory.createLineBorder(highlightColor, 3) : 
                BorderFactory.createLineBorder(Color.BLACK, 2));
        repaint();
    }
    
    public void setOwner(Player owner) {
        this.owner = owner;
        System.out.println("Setting owner of " + tileName + " to " + (owner != null ? owner.getName() : "null"));
        repaint(); // Redraw to show ownership
    }
    
    public Player getOwner() {
        return owner;
    }
    
    public void addPlayerToken(Player player) {
        if (player == null) {
            System.err.println("Warning: Attempted to add null player token to position " + position);
            return;
        }
        
        // Debug info
        System.out.println("Adding token for " + player.getName() + 
                         " (color: " + player.getPlayerColor() + ") to position " + position);
        
        if (!playerTokens.containsKey(player)) {
            PlayerToken token = new PlayerToken(player, player.getPlayerColor());
            playerTokens.put(player, token);
            updateTokenPositions();
            repaint();
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Fill background
        g2d.setColor(baseColor);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // If there's an owner, show a colored ownership bar at the TOP instead of left side
        if (owner != null) {
            Color ownerColor = owner.getPlayerColor();
            if (ownerColor != null) {
                // Draw a colored bar at the TOP to indicate ownership
                g2d.setColor(ownerColor);
                g2d.fillRect(0, 0, getWidth(), 8);
                
                // Draw owner name at the bottom
                g2d.setFont(new Font("Arial", Font.BOLD, 12));
                FontMetrics fm = g2d.getFontMetrics();
                String ownerText = "In Charge: " + owner.getName();
                int textX = 8;
                int textY = getHeight() - 5;
                g2d.drawString(ownerText, textX, textY);
            }
        }
        
        // Draw tile name
        g2d.setColor(Color.WHITE);
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        
        // Break long names into multiple lines if needed
        if (fm.stringWidth(tileName) > getWidth() - 10) {
            String[] words = tileName.split(" ");
            String line1 = "", line2 = "";
            
            for (String word : words) {
                if (fm.stringWidth(line1 + word) < getWidth() - 10) {
                    line1 += (line1.isEmpty() ? "" : " ") + word;
                } else {
                    line2 += (line2.isEmpty() ? "" : " ") + word;
                }
            }
            
            int line1X = (getWidth() - fm.stringWidth(line1)) / 2;
            int line1Y = 15;
            g2d.drawString(line1, line1X, line1Y);
            
            if (!line2.isEmpty()) {
                int line2X = (getWidth() - fm.stringWidth(line2)) / 2;
                g2d.drawString(line2, line2X, line1Y + fm.getHeight());
            }
        } else {
            // Draw tile name on a single line
            int textX = (getWidth() - fm.stringWidth(tileName)) / 2;
            int textY = 15;
            g2d.drawString(tileName, textX, textY);
        }
        
        // Draw position number
        g2d.setFont(positionFont);
        String posText = "Position " + position;
        fm = g2d.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(posText)) / 2;
        int textY = 40; // Fixed position regardless of title
        g2d.drawString(posText, textX, textY);
        
      // Draw tile type with special handling for StartSquare
      String displayType = "";
      switch (tileType) {
          case "SpecialTask": 
              displayType = "Quiz Challenge - Rand 2000";
              break;
          case "EcoChallenge": 
              displayType = "Waste Sorting - Rand 2000";
              break;
          case "StartSquare": 
              displayType = "Deposit Waste Here";
              break;
          default: 
              displayType = tileType;
      }
        if (!tileType.equals("StartSquare")) {
            int typeX = (getWidth() - fm.stringWidth(displayType)) / 2;
            g2d.drawString(displayType, typeX, textY + 15);
        }
        
        // Draw all player tokens
        for (PlayerToken token : playerTokens.values()) {
            token.draw(g2d, 35, 25);
        }
    }
        
    public void removePlayerToken(Player player) {
        if (player == null) {
            System.err.println("Warning: Attempted to remove null player token from position " + position);
            return;
        }
        
        // Debug info
        System.out.println("Removing token for " + player.getName() + " from position " + position);
        
        if (playerTokens.containsKey(player)) {
            playerTokens.remove(player);
            updateTokenPositions();
            repaint();
        }
    }
    
    private void updateTokenPositions() {
        // Calculate positions for all tokens on this tile
        int tokenCount = playerTokens.size();
        
        if (tokenCount == 0) return;
        
        // Arrange tokens in grid pattern (2x2 max)
        int index = 0;
        for (PlayerToken token : playerTokens.values()) {
            int row = index / 2;
            int col = index % 2;
            
            int offsetX = 15 + (col * 25);
            int offsetY = 50 + (row * 25);
            
            token.setOffset(offsetX, offsetY);
            index++;
        }
    }
    
    public int getPosition() {
        return position;
    }
    
    public int getPlayerTokenCount() {
        return playerTokens.size();
    }
    
    public boolean isProperty() {
        return isProperty;
    }
    
    public int getPropertyPrice() {
        return propertyPrice;
    }
    
    public String getTileType() {
        return tileType;
    }
    
    public String getTileName() {
        return tileName;
    }
}