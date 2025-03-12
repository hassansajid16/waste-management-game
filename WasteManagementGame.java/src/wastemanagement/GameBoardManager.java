package wastemanagement;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

/**
 * Manages the game board display and player token movements
 */
public class GameBoardManager {
    private JPanel boardPanel;
    private ArrayList<Player> players;
    private GameTilePanel[] tilePanels;
    private Map<Player, Integer> playerPositions = new HashMap<>();
    
    public GameBoardManager(ArrayList<Player> players) {
        this.players = players;
        
        // Initialize player positions
        for (Player player : players) {
            playerPositions.put(player, 0);
        }
        
        createBoardPanel();
        
        // Print debug info
        System.out.println("GameBoardManager initialized with " + players.size() + " players");
        for (Player player : players) {
            System.out.println("Player: " + player.getName() + 
                             ", Color: " + player.getPlayerColor() + 
                             ", Starting position: 0");
        }
    }
    
    private void createBoardPanel() {
        boardPanel = new JPanel(new GridLayout(4, 5, 10, 10));
        boardPanel.setBackground(new Color(45, 65, 45));
        boardPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        Board board = new Board();
        tilePanels = new GameTilePanel[Board.BOARD_SIZE];
        
        // Create tile panels
        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            Tile tile = board.getTile(i);
            tilePanels[i] = new GameTilePanel(tile.getName(), tile.getType(), i);
            boardPanel.add(tilePanels[i]);
        }
        
        // Add all players to start position
        for (Player player : players) {
            tilePanels[0].addPlayerToken(player);
        }
        
        // Debug info
        System.out.println("Board created with " + Board.BOARD_SIZE + " tiles");
        System.out.println("Players added to starting position (0): " + tilePanels[0].getPlayerTokenCount());
    }
    
    public JPanel getBoardPanel() {
        return boardPanel;
    }
    
    /**
     * Move a player to a new position
     */
    public void movePlayer(Player player, int newPosition) {
        if (player == null) {
            System.err.println("Error: Attempted to move null player");
            return;
        }
        
        if (newPosition < 0 || newPosition >= Board.BOARD_SIZE) {
            System.err.println("Error: Invalid position " + newPosition + 
                             " for player " + player.getName());
            return;
        }
        
        // Get the previous position
        int oldPosition = playerPositions.getOrDefault(player, 0);
        
        // Debug info
        System.out.println("Moving player " + player.getName() + 
                         " from position " + oldPosition + 
                         " to position " + newPosition);
        
        // Remove from old position
        tilePanels[oldPosition].removePlayerToken(player);
        
        // Add to new position
        tilePanels[newPosition].addPlayerToken(player);
        
        // Update stored position
        playerPositions.put(player, newPosition);
        
        // Highlight the new position
        highlightTile(newPosition);
        
        // Force repaint of affected tiles
        tilePanels[oldPosition].revalidate();
        tilePanels[oldPosition].repaint();
        tilePanels[newPosition].revalidate();
        tilePanels[newPosition].repaint();
        
        // Debug info
        System.out.println("Player tokens at old position (" + oldPosition + "): " + 
                         tilePanels[oldPosition].getPlayerTokenCount());
        System.out.println("Player tokens at new position (" + newPosition + "): " + 
                         tilePanels[newPosition].getPlayerTokenCount());
    }
    
    /**
     * Highlight a specific tile on the board
     */
    public void highlightTile(int position) {
        if (position < 0 || position >= Board.BOARD_SIZE) {
            System.err.println("Error: Attempted to highlight invalid position " + position);
            return;
        }
        
        // Reset all highlights
        for (GameTilePanel tilePanel : tilePanels) {
            tilePanel.setHighlighted(false);
        }
        
        // Highlight the specified tile
        tilePanels[position].setHighlighted(true);
        
        // Debug info
        System.out.println("Highlighted position: " + position);
    }
    
    /**
     * Reset all player positions to start
     */
    public void resetPlayerPositions() {
        // Clear all player tokens from tiles
        for (GameTilePanel tilePanel : tilePanels) {
            for (Player player : players) {
                tilePanel.removePlayerToken(player);
            }
        }
        
        // Reset position tracking
        playerPositions.clear();
        
        // Add all players to start position
        for (Player player : players) {
            tilePanels[0].addPlayerToken(player);
            playerPositions.put(player, 0);
        }
        
        // Highlight start position
        highlightTile(0);
        
        // Debug info
        System.out.println("Reset all players to starting position");
        System.out.println("Player tokens at position 0: " + tilePanels[0].getPlayerTokenCount());
    }
    
    /**
     * Update player positions based on their current game state
     */
    public void refreshPlayerPositions() {
        // Clear all tiles first
        for (GameTilePanel tile : tilePanels) {
            for (Player player : players) {
                tile.removePlayerToken(player);
            }
        }
        
        // Add each player to their current position
        for (Player player : players) {
            int position = player.getPosition();
            tilePanels[position].addPlayerToken(player);
            playerPositions.put(player, position);
        }
        
        // Debug info
        System.out.println("Refreshed all player positions on board");
    }
}