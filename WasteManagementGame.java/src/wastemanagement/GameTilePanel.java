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
    
    // Styling constants
    private Color baseColor;
    private Color highlightColor = Color.YELLOW;
    private Font titleFont = new Font("Arial", Font.BOLD, 12);
    private Font positionFont = new Font("Arial", Font.PLAIN, 10);
    
    public GameTilePanel(String tileName, String tileType, int position) {
        this.tileName = tileName;
        this.tileType = tileType;
        this.position = position;
        
        // Set tile background color based on type
        setBaseColorByType(tileType);
        
        setPreferredSize(new Dimension(120, 90));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        
        // Enable proper rendering
        setOpaque(true);
    }
    
    private void setBaseColorByType(String type) {
        switch (type) {
            case "Resource Square":
                baseColor = new Color(89, 116, 89);
                break;
            case "Special Task":
                baseColor = new Color(142, 92, 174);
                break;
            case "Task":
                baseColor = new Color(50, 57, 57);
                break;
            case "Refill Resources":
                baseColor = new Color(89, 116, 89);
                break;
            case "Waste Exchange":
                baseColor = new Color(70, 130, 180); // Steel blue
                break;
            default:
                baseColor = new Color(70, 77, 77);
        }
    }
    
    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
        setBorder(highlighted ? 
                BorderFactory.createLineBorder(highlightColor, 3) : 
                BorderFactory.createLineBorder(Color.BLACK, 2));
        repaint();
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
            int offsetY = 40 + (row * 25);
            
            token.setOffset(offsetX, offsetY);
            index++;
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
        
        // Draw tile name
        g2d.setColor(Color.WHITE);
        g2d.setFont(titleFont);
        FontMetrics fm = g2d.getFontMetrics();
        int textX = (getWidth() - fm.stringWidth(tileName)) / 2;
        g2d.drawString(tileName, textX, 15);
        
        // Draw position number
        g2d.setFont(positionFont);
        String posText = "Position " + position;
        fm = g2d.getFontMetrics();
        textX = (getWidth() - fm.stringWidth(posText)) / 2;
        g2d.drawString(posText, textX, 30);
        
        // Draw all player tokens
        for (PlayerToken token : playerTokens.values()) {
            token.draw(g2d, 0, 0);
        }
    }
    
    public int getPosition() {
        return position;
    }
    
    public int getPlayerTokenCount() {
        return playerTokens.size();
    }
}