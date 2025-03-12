package wastemanagement;

import java.awt.*;

/**
 * Represents a player's token on the game board
 */
public class PlayerToken {
    private Player player;
    private Color color;
    private int size = 20; // Default size of the token
    private int offsetX = 0; // For positioning multiple tokens on the same tile
    private int offsetY = 0;

    public PlayerToken(Player player, Color color) {
        this.player = player;
        this.color = color != null ? color : Color.GRAY; // Fallback to gray if color is null
    }

    public Player getPlayer() {
        return player;
    }

    public Color getColor() {
        return color;
    }

    public void setOffset(int offsetX, int offsetY) {
        this.offsetX = offsetX;
        this.offsetY = offsetY;
    }

    /**
     * Draws the player token on a given graphics context
     */
    public void draw(Graphics2D g2d, int x, int y) {
        // Save original settings
        Stroke originalStroke = g2d.getStroke();
        Paint originalPaint = g2d.getPaint();
        
        // Position with offset (for multiple players on same tile)
        int tokenX = x + offsetX;
        int tokenY = y + offsetY;
        
        // Create a glossy 3D effect with gradient
        Paint gradient = new GradientPaint(
            tokenX, tokenY, 
            brightenColor(color, 0.2f),
            tokenX + size, tokenY + size, 
            darkenColor(color, 0.2f)
        );
        
        // Draw shadow for depth
        g2d.setColor(new Color(0, 0, 0, 60));
        g2d.fillOval(tokenX + 2, tokenY + 2, size, size);
        
        // Draw the colored circle
        g2d.setPaint(gradient);
        g2d.fillOval(tokenX, tokenY, size, size);
        
        // Draw highlight for 3D effect
        g2d.setColor(new Color(255, 255, 255, 120));
        g2d.fillOval(tokenX + 3, tokenY + 3, size/2, size/2);
        
        // Draw border
        g2d.setColor(darkenColor(color, 0.3f));
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(tokenX, tokenY, size, size);
        
        // Draw player initial
        g2d.setColor(getTextColor(color));
        g2d.setFont(new Font("Arial", Font.BOLD, 12));
        String initial = player.getName().substring(0, 1).toUpperCase();
        FontMetrics fm = g2d.getFontMetrics();
        int textX = tokenX + (size - fm.stringWidth(initial)) / 2;
        int textY = tokenY + ((size + fm.getAscent()) / 2);
        g2d.drawString(initial, textX, textY);
        
        // Restore original settings
        g2d.setStroke(originalStroke);
        g2d.setPaint(originalPaint);
    }
    
    // Helper method to brighten a color
    private Color brightenColor(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[1] = Math.max(0, Math.min(1, hsb[1] - factor * 0.7f));
        hsb[2] = Math.min(1, hsb[2] + factor);
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
    
    // Helper method to darken a color
    private Color darkenColor(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[2] = Math.max(0, hsb[2] - factor);
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
    
    // Determine if text should be white or black based on background color brightness
    private Color getTextColor(Color backgroundColor) {
        double brightness = (backgroundColor.getRed() * 0.299 + 
                           backgroundColor.getGreen() * 0.587 + 
                           backgroundColor.getBlue() * 0.114) / 255;
        
        return brightness > 0.5 ? Color.BLACK : Color.WHITE;
    }
}