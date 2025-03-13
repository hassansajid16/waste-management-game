package wastemanagement;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
/**
 * The login screen for player setup
 */
public class Login extends JFrame {
    private ArrayList<Player> players = new ArrayList<>();
    private int currentPlayerIndex = 0;
    private Map<String, Color> playerColors = new HashMap<>();
    private Map<String, JButton> colorButtonMap = new HashMap<>();
    
    // UI Components
    private JPanel mainPanel;
    private JTextField nameField;
    private JComboBox<String> roleComboBox;
    private JButton startGameButton;
    private JLabel playerNumberLabel;
    private JPanel playerCardsPanel;
    private JPanel currentPlayerCardPanel;
    
    // Colors
    private Color bgColor = new Color(45, 65, 45);  // Dark green background matching game board
    private Color accentColor = new Color(89, 116, 89);  // Matching Resource Square color
    private Color buttonColor = new Color(173, 216, 230);  // Light blue for buttons
    private Color panelColor = new Color(60, 80, 60);  // Slightly lighter than bg for panels
    private Color textColor = Color.WHITE;
    private Color headerColor = new Color(50, 70, 50);  // For header elements
    
    // Player colors
    private Color[] availableColors = {
        new Color(65, 105, 225),  // Royal Blue
        new Color(220, 20, 60),   // Crimson
        new Color(50, 205, 50),   // Lime Green
        new Color(255, 215, 0)    // Gold
    };
    
    // Images
    private ImageIcon logoIcon;
    private ImageIcon[] roleIcons;

    /**
     * Constructor for the login screen
     */
    public Login() {
        // Setup frame properties
        setTitle("Waste Management Game - Player Setup");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Try to load images
        loadImages();
        
        // Setup UI
        setupUI();
        
        // Show the frame
        setVisible(true);
    }
    
    /**
     * Load images for the UI
     */
    private void loadImages() {
        try {
            // Try to load images from resources
            logoIcon = createIconFromText("Waste Management", new Font("Arial", Font.BOLD, 24), Color.WHITE);
            
            // Role icons
            roleIcons = new ImageIcon[2];
            roleIcons[0] = createIconFromText("BO", new Font("Arial", Font.BOLD, 14), Color.WHITE);
            roleIcons[1] = createIconFromText("CL", new Font("Arial", Font.BOLD, 14), Color.WHITE);
        } catch (Exception e) {
            System.out.println("Error loading images: " + e.getMessage());
            logoIcon = null;
            roleIcons = null;
        }
    }
    
    /**
     * Create an icon from text
     */
    private ImageIcon createIconFromText(String text, Font font, Color textColor) {
        // Create a label with the text
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(textColor);
        label.setBackground(headerColor);
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        
        // Get preferred size
        label.setSize(label.getPreferredSize());
        
        // Create image
        BufferedImage image = new BufferedImage(
            label.getWidth(), label.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        // Paint label to image
        Graphics2D g2d = image.createGraphics();
        label.paint(g2d);
        g2d.dispose();
        
        // Return as icon
        return new ImageIcon(image);
    }
    
    /**
     * Setup the main UI
     */
    private void setupUI() {
        // Main content panel with background
        mainPanel = new JPanel(new BorderLayout(0, 10));
        mainPanel.setBackground(bgColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Create header panel
        setupHeaderPanel();
        
        // Create content panel
        setupContentPanel();
        
        // Create navigation panel
        setupNavigationPanel();
        
        // Add main panel to frame
        setContentPane(mainPanel);
    }
    
    /**
     * Setup the header panel
     */
    private void setupHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(headerColor);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Game title
        JLabel titleLabel = new JLabel("Waste Management Game", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(textColor);
        
        // Add logo if available
        if (logoIcon != null) {
            titleLabel.setIcon(logoIcon);
            titleLabel.setIconTextGap(15);
        }
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Player Setup", SwingConstants.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        
        // Add components to header
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Add header to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
    }
    
    /**
     * Setup the content panel
     */
    private void setupContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        contentPanel.setOpaque(false);
        
        // Left panel - current player setup
        JPanel leftPanel = createCurrentPlayerPanel();
        
        // Right panel - registered players
        JPanel rightPanel = createRegisteredPlayersPanel();
        
        // Add panels to content
        contentPanel.add(leftPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.EAST);
        
        // Add content to main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
    /**
     * Create panel for setting up current player
     */
    private JPanel createCurrentPlayerPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 15));
        panel.setOpaque(false);
        
        // Player number label
        playerNumberLabel = new JLabel("Player " + (currentPlayerIndex + 1), SwingConstants.CENTER);
        playerNumberLabel.setFont(new Font("Arial", Font.BOLD, 20));
        playerNumberLabel.setForeground(textColor);
        
        // Current player card
        currentPlayerCardPanel = new JPanel();
        refreshCurrentPlayerCard();
        
        panel.add(playerNumberLabel, BorderLayout.NORTH);
        panel.add(currentPlayerCardPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Refresh the current player card
     */
    private void refreshCurrentPlayerCard() {
        currentPlayerCardPanel.removeAll();
        currentPlayerCardPanel.setLayout(new BorderLayout(10, 15));
        currentPlayerCardPanel.setOpaque(false);
        
        // Create input fields panel
        JPanel inputPanel = new JPanel(new GridLayout(6, 1, 0, 10));
        inputPanel.setBackground(panelColor);
        inputPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Name section
        JLabel nameLabel = new JLabel("Name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(textColor);
        inputPanel.add(nameLabel);
        
        nameField = new JTextField();
        nameField.setFont(new Font("Arial", Font.PLAIN, 14));
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        inputPanel.add(nameField);
        
        // Role section
        JLabel roleLabel = new JLabel("Role:");
        roleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        roleLabel.setForeground(textColor);
        inputPanel.add(roleLabel);
        
        String[] roles = {"Business Owner", "Community Leader"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        roleComboBox.setBackground(Color.WHITE);
        roleComboBox.setBorder(BorderFactory.createLineBorder(accentColor));
        
        // Add icons to roles if available
        if (roleIcons != null) {
            roleComboBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value, 
                        int index, boolean isSelected, boolean cellHasFocus) {
                    JLabel label = (JLabel) super.getListCellRendererComponent(
                            list, value, index, isSelected, cellHasFocus);
                    
                    if (value.equals("Business Owner")) {
                        label.setIcon(roleIcons[0]);
                    } else if (value.equals("Community Leader")) {
                        label.setIcon(roleIcons[1]);
                    }
                    
                    return label;
                }
            });
        }
        
        inputPanel.add(roleComboBox);
        
        // Color selection section
        JLabel colorLabel = new JLabel("Color (click to select):");
        colorLabel.setFont(new Font("Arial", Font.BOLD, 14));
        colorLabel.setForeground(textColor);
        inputPanel.add(colorLabel);
        
        // Create color selection panel
        JPanel colorPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        colorPanel.setOpaque(false);
        
        colorButtonMap.clear();
        for (int i = 0; i < availableColors.length; i++) {
            final Color color = availableColors[i];
            
            // Check if color is already taken
            boolean colorTaken = playerColors.containsValue(color);
            
            // Create color button with current availability state
            JButton colorButton = createColorButton(color);
            colorButton.setEnabled(!colorTaken);
            
            // Add action to select color and automatically add the player
            final String colorName = getColorName(color);
            colorButton.addActionListener(e -> {
                selectPlayerColor(colorName, color);
            });
            
            colorButtonMap.put(colorName, colorButton);
            colorPanel.add(colorButton);
        }
        
        // Add color panel to input panel
        inputPanel.add(colorPanel);
        
        // Add components to player card
        currentPlayerCardPanel.add(inputPanel, BorderLayout.CENTER);
        
        // Instructions label
        JLabel instructionsLabel = new JLabel("Enter name, select role, then click a color to add player", SwingConstants.CENTER);
        instructionsLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        instructionsLabel.setForeground(textColor);
        currentPlayerCardPanel.add(instructionsLabel, BorderLayout.SOUTH);
        
        currentPlayerCardPanel.revalidate();
        currentPlayerCardPanel.repaint();
    }
    
    /**
     * Create a button for color selection
     */
    private JButton createColorButton(Color color) {
        final int SIZE = 37;
        
        JButton button = new JButton();
        button.setBackground(color);
        button.setPreferredSize(new Dimension(SIZE, SIZE));
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(getColorName(color));
        
        return button;
    }
    
    /**
     * Get the name of a color
     */
    private String getColorName(Color color) {
        if (color.equals(new Color(65, 105, 225))) return "Blue";
        if (color.equals(new Color(220, 20, 60))) return "Red";
        if (color.equals(new Color(50, 205, 50))) return "Green";
        if (color.equals(new Color(255, 215, 0))) return "Yellow";
        return "Unknown";
    }
    
    /**
     * Create panel for registered players
     */
    private JPanel createRegisteredPlayersPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(250, 400));
        panel.setBackground(panelColor);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(accentColor, 2),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Header label
        JLabel headerLabel = new JLabel("Registered Players", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerLabel.setForeground(textColor);
        
        // Player cards panel with scrolling
        playerCardsPanel = new JPanel();
        playerCardsPanel.setLayout(new BoxLayout(playerCardsPanel, BoxLayout.Y_AXIS));
        playerCardsPanel.setOpaque(false);
        
        JScrollPane scrollPane = new JScrollPane(playerCardsPanel);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        
        // Add components
        panel.add(headerLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Setup the navigation panel
     */
    private void setupNavigationPanel() {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Start game button
        startGameButton = createNavigationButton("Start Game");
        startGameButton.setEnabled(false);
        startGameButton.setBackground(new Color(100, 180, 100));
        startGameButton.setForeground(Color.WHITE);
        startGameButton.addActionListener(e -> startGame());
        
        // Add start game button
        navPanel.add(startGameButton);
        
        // Add to main panel
        mainPanel.add(navPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Create a navigation button
     */
    private JButton createNavigationButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(buttonColor);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        return button;
    }
    
    /**
     * Select a color for the current player
     */
    private void selectPlayerColor(String colorName, Color color) {
        // Validate name
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Please enter a name for the player.", 
                "Input Required", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get role from combo box
        String role = (String) roleComboBox.getSelectedItem();
        
        // Create player
        Player player = new Player(name);
        player.setRole(role);
        player.applyRole(role);
        player.setPlayerColor(color);
        players.add(player);
        
        playerColors.put(name, color);
        
        // Add player card to the registered players panel
        addPlayerCard(player, color);
        
        // Disable the selected color button
        JButton selectedButton = colorButtonMap.get(colorName);
        if (selectedButton != null) {
            selectedButton.setEnabled(false);
        }
        
        // Enable start game button if enough players
        startGameButton.setEnabled(players.size() >= 2);
        
        // Update player number label
        if (players.size() < 4) {
            playerNumberLabel.setText("Player " + (players.size() + 1));
        }
        
        // Clear inputs
        nameField.setText("");
        roleComboBox.setSelectedIndex(0);
        
        // Check max players
        if (players.size() >= 4) {
            currentPlayerCardPanel.setVisible(false);
            playerNumberLabel.setText("Maximum Players Reached");
        }
        
        // Refresh color buttons to update their state
        refreshCurrentPlayerCard();
    }
    
    /**
     * Add a player card to the registered players panel
     */
    private void addPlayerCard(Player player, Color playerColor) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(new Color(45, 55, 45));
        
        // Enhanced card design
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, playerColor),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(250, 80));
        card.setPreferredSize(new Dimension(250, 80));
        
        // Color indicator
        JPanel colorIndicator = new JPanel();
        colorIndicator.setBackground(playerColor);
        colorIndicator.setPreferredSize(new Dimension(20, 20));
        
        // Player info with better spacing
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 2));
        infoPanel.setOpaque(false);
        
        JLabel nameLabel = new JLabel(player.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
        nameLabel.setForeground(Color.WHITE);
        
        JLabel roleLabel = new JLabel(player.getRole());
        roleLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        roleLabel.setForeground(Color.LIGHT_GRAY);
        
        infoPanel.add(nameLabel);
        infoPanel.add(roleLabel);
        
        // Create wrapper for color indicator to center it vertically
        JPanel colorWrapper = new JPanel(new GridBagLayout());
        colorWrapper.setOpaque(false);
        colorWrapper.add(colorIndicator);
        
        // Add components to card
        card.add(colorWrapper, BorderLayout.WEST);
        card.add(infoPanel, BorderLayout.CENTER);
        
        // Add padding between cards
        JPanel paddedCard = new JPanel(new BorderLayout());
        paddedCard.setOpaque(false);
        paddedCard.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        paddedCard.add(card, BorderLayout.CENTER);
        
        // Add to player cards panel
        playerCardsPanel.add(paddedCard);
        playerCardsPanel.revalidate();
        playerCardsPanel.repaint();
    }
    
    /**
     * Start the game with the registered players
     */
    private void startGame() {
        if (players.size() >= 2) {
            // Log the players and their colors
            System.out.println("Starting game with " + players.size() + " players:");
            for (Player player : players) {
                System.out.println(player.getName() + " (" + player.getRole() + ") - Color: " + 
                                  player.getPlayerColor());
            }
            
            // Close this window
            dispose();
            
            // Launch the main game with players and colors
            SwingUtilities.invokeLater(() -> {
                try {
                    new WasteManagementGame(players, playerColors);
                } catch (Exception e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(null,
                        "Error starting the game: " + e.getMessage() + "\n" +
                        "Please try restarting the application.",
                        "Game Launch Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            });
        } else {
            JOptionPane.showMessageDialog(this, 
                "You need at least 2 players to start the game.",
                "More Players Needed", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
}