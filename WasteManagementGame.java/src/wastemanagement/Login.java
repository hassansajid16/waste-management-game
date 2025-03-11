package wastemanagement;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class Login extends JFrame {
    private ArrayList<Player> players;
    private int currentPlayerIndex = 0;
    private Map<String, Color> playerColors;
    private Map<String, JButton> colorButtonMap;
    
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
    
    // Images (paths should be adjusted based on your project structure)
    private ImageIcon logoIcon;
    private ImageIcon[] roleIcons;

    public Login() {
        players = new ArrayList<>();
        playerColors = new HashMap<>();
        colorButtonMap = new HashMap<>();
        
        // Load images
        loadImages();
        
        // Setup frame
        setTitle("Waste Management Game - Player Setup");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Setup UI
        setupUI();
        
        // Center on screen and show
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void loadImages() {
        // Try to load images (with fallbacks if not found)
        try {
            // Main logo
            logoIcon = new ImageIcon(getClass().getResource("/Resources/waste_logo.png"));
            if (logoIcon.getIconWidth() <= 0) {
                // Fallback to text if image not found
                logoIcon = null;
            }
            
            // Role icons
            roleIcons = new ImageIcon[2];
            roleIcons[0] = new ImageIcon(getClass().getResource("/Resources/business_icon.png"));
            roleIcons[1] = new ImageIcon(getClass().getResource("/Resources/community_icon.png"));
            
            // Fallback if icons are invalid
            if (roleIcons[0] == null || roleIcons[0].getIconWidth() <= 0) {
                roleIcons = null;
            }
        } catch (Exception e) {
            System.out.println("Could not load some images: " + e.getMessage());
            logoIcon = null;
            roleIcons = null;
        }
    }
    
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
            // Resize logo to appropriate size
            Image img = logoIcon.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
            ImageIcon resizedIcon = new ImageIcon(img);
            titleLabel.setIcon(resizedIcon);
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
    
    private void setupContentPanel() {
        JPanel contentPanel = new JPanel(new BorderLayout(20, 0));
        contentPanel.setOpaque(false);
        
        // Setup left panel - current player setup
        JPanel leftPanel = createCurrentPlayerPanel();
        
        // Setup right panel - registered players
        JPanel rightPanel = createRegisteredPlayersPanel();
        
        // Add panels to content
        contentPanel.add(leftPanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.EAST);
        
        // Add content to main panel
        mainPanel.add(contentPanel, BorderLayout.CENTER);
    }
    
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
    
    private JButton createColorButton(Color color) {
        final int SIZE = 50;
        
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int diameter = Math.min(width, height);
                
                // Choose color based on enabled state
                Color baseColor = isEnabled() ? color : darkenColor(color, 0.6f);
                
                // Draw shadow effect for depth
                if (isEnabled()) {
                    g2.setColor(new Color(0, 0, 0, 60));
                    g2.fillOval((width - diameter) / 2 + 2, 
                                (height - diameter) / 2 + 2, 
                                diameter - 4, 
                                diameter - 4);
                }
                
                // Draw colored circle with gradient for 3D effect
                Paint gradient = new GradientPaint(
                    0, 0, 
                    brightenColor(baseColor, 0.3f),
                    diameter, diameter, 
                    darkenColor(baseColor, 0.2f)
                );
                g2.setPaint(gradient);
                
                g2.fillOval((width - diameter) / 2, 
                            (height - diameter) / 2, 
                            diameter, 
                            diameter);
                
                // Draw glossy highlight
                g2.setColor(new Color(255, 255, 255, 130));
                g2.fillOval((width - diameter) / 2 + 5, 
                            (height - diameter) / 2 + 5, 
                            diameter / 2, 
                            diameter / 2);
                
                // Draw border
                g2.setColor(darkenColor(baseColor, 0.4f));
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawOval((width - diameter) / 2, 
                            (height - diameter) / 2, 
                            diameter, 
                            diameter);
                
                g2.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(SIZE, SIZE);
            }
        };
        
        button.setBackground(color);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(getColorName(color));
        
        return button;
    }
    
    // Helper method to brighten a color
    private Color brightenColor(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[1] = Math.max(0, Math.min(1, hsb[1] - factor * 0.7f));  // Less saturation reduction
        hsb[2] = Math.min(1, hsb[2] + factor);  // More brightness
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
    
    // Helper method to darken a color
    private Color darkenColor(Color color, float factor) {
        float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
        hsb[2] = Math.max(0, hsb[2] - factor);  // Reduce brightness
        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
    
    private String getColorName(Color color) {
        if (color.equals(new Color(65, 105, 225))) return "Blue";
        if (color.equals(new Color(220, 20, 60))) return "Red";
        if (color.equals(new Color(50, 205, 50))) return "Green";
        if (color.equals(new Color(255, 215, 0))) return "Yellow";
        return "Unknown";
    }
    
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
    
    private void setupNavigationPanel() {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        navPanel.setOpaque(false);
        navPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        // Start game button (keep existing code)
        startGameButton = createNavigationButton("Start Game");
        startGameButton.setEnabled(false);
        startGameButton.setBackground(new Color(100, 180, 100));
        startGameButton.setForeground(Color.WHITE);
        startGameButton.addActionListener(e -> startGame());
        
        // Add only the start game button
        navPanel.add(startGameButton);
        
        // Add to main panel
        mainPanel.add(navPanel, BorderLayout.SOUTH);
    }
    
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
        players.add(player);
        
        // Store the color as used
        playerColors.put(colorName, color);
        
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
    
    private void addPlayerCard(Player player, Color playerColor) {
        JPanel card = new JPanel(new BorderLayout(10, 0));
        card.setBackground(new Color(45, 55, 45));
        
        // Enhanced card design with rounded corners
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 5, 0, 0, playerColor),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        card.setMaximumSize(new Dimension(250, 80));
        card.setPreferredSize(new Dimension(250, 80));
        
        // Color indicator with perfectly round circle
        JPanel colorIndicator = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int diameter = Math.min(getWidth(), getHeight()) - 4;
                
                // Draw shadow for depth
                g2d.setColor(new Color(0, 0, 0, 60));
                g2d.fillOval(3, 3, diameter, diameter);
                
                // Create glossy effect with gradient
                Paint gradient = new GradientPaint(
                    0, 0, 
                    brightenColor(playerColor, 0.2f),
                    diameter, diameter, 
                    darkenColor(playerColor, 0.2f)
                );
                g2d.setPaint(gradient);
                g2d.fillOval(0, 0, diameter, diameter);
                
                // Highlight for 3D effect
                g2d.setColor(new Color(255, 255, 255, 120));
                g2d.fillOval(3, 3, diameter/2, diameter/2);
                
                // Draw border
                g2d.setColor(darkenColor(playerColor, 0.3f));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawOval(0, 0, diameter, diameter);
                
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(26, 26);
            }
        };
        colorIndicator.setOpaque(false);
        
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
    

    
    private void startGame() {
        if (players.size() >= 2) {
            dispose();
            new WasteManagementGame(players);
        } else {
            JOptionPane.showMessageDialog(this, 
                "You need at least 2 players to start the game.",
                "More Players Needed", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> new Login());
    }
}