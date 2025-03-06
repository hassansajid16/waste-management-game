package wastemanagement;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Login extends JFrame {
    private ArrayList<Player> players;
    private int currentPlayerIndex = 0;
    private Set<Color> availableColors;
    private JTextField nameField;
    private JComboBox<String> roleComboBox;
    private JButton[] colorButtons;
    private JButton startGameButton;
    private JButton previousPlayerButton;
    private JButton nextPlayerButton;
    private JLabel playerNumberLabel;
    private JLabel welcomeLabel;

    public Login() {
        players = new ArrayList<>();
        availableColors = new HashSet<>();
        availableColors.add(Color.BLUE);
        availableColors.add(Color.RED);
        availableColors.add(Color.GREEN);
        availableColors.add(Color.YELLOW);

        setTitle("Waste Management Game - Player Setup");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main panel with some padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(240, 245, 240));
        
        // Logo Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBackground(new Color(45, 85, 45));
        headerPanel.setPreferredSize(new Dimension(500, 100));
        
        welcomeLabel = new JLabel("Waste Management Game", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        headerPanel.add(welcomeLabel, BorderLayout.CENTER);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 1, 10, 10));
        inputPanel.setBackground(new Color(240, 245, 240));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        
        // Player label
        playerNumberLabel = new JLabel("Player " + (currentPlayerIndex + 1), SwingConstants.CENTER);
        playerNumberLabel.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Name input with rounded border
        JLabel nameLabel = new JLabel("Enter your name:");
        nameField = new JTextField();
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)));
        
        // Role selection with custom styling
        JLabel roleLabel = new JLabel("Choose your role:");
        String[] roles = {"Business Owner", "Community Leader"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBackground(Color.WHITE);
        
        // Color selection
        JLabel colorLabel = new JLabel("Choose your color:");
        JPanel colorPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        colorPanel.setBackground(new Color(240, 245, 240));
        colorButtons = new JButton[4];
        Color[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW};
        
        for (int i = 0; i < colors.length; i++) {
            colorButtons[i] = createRoundColorButton(colors[i]);
            colorPanel.add(colorButtons[i]);
        }

        // Add components to input panel
        inputPanel.add(playerNumberLabel);
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(roleLabel);
        inputPanel.add(roleComboBox);
        inputPanel.add(colorLabel);
        inputPanel.add(colorPanel);
        
        mainPanel.add(inputPanel, BorderLayout.CENTER);

        // Navigation Panel
        JPanel navPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        navPanel.setBackground(new Color(240, 245, 240));
        
        previousPlayerButton = createStyledButton("Previous");
        previousPlayerButton.setVisible(false);
        previousPlayerButton.addActionListener(e -> showPreviousPlayer());
        
        startGameButton = createStyledButton("Start Game");
        startGameButton.setEnabled(false);
        startGameButton.setBackground(new Color(100, 200, 100));
        startGameButton.setForeground(Color.WHITE);
        startGameButton.addActionListener(e -> startGame());
        
        nextPlayerButton = createStyledButton("Next");
        nextPlayerButton.addActionListener(e -> showNextPlayer());
        
        navPanel.add(previousPlayerButton);
        navPanel.add(startGameButton);
        navPanel.add(nextPlayerButton);
        
        mainPanel.add(navPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JButton createRoundColorButton(Color color) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 20, 20));
                g2.dispose();
            }
        };
        button.setBackground(color);
        button.setPreferredSize(new Dimension(40, 40));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.addActionListener(e -> selectColor(color, button));
        return button;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(new Color(200, 200, 200));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        return button;
    }

    private void selectColor(Color color, JButton button) {
        if (availableColors.contains(color)) {
            String name = nameField.getText().trim();
            String role = (String) roleComboBox.getSelectedItem();

            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a valid name.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Register the player
            Player player = new Player(name);
            player.setRole(role);
            player.applyRole(role);
            players.add(player);
            
            // Remove this color from available colors
            availableColors.remove(color);
            button.setEnabled(false);

            // Enable start game if we have at least 2 players
            if (players.size() >= 2) {
                startGameButton.setEnabled(true);
            }

            // Move to next player if applicable
            if (players.size() < 4) {
                currentPlayerIndex++;
                updateLabels();
                nameField.setText("");
                roleComboBox.setSelectedIndex(0);
                
                // Update navigation button visibility
                previousPlayerButton.setVisible(true);
                if (players.size() == 4) {
                    nextPlayerButton.setVisible(false);
                }
            } else {
                nextPlayerButton.setVisible(false);
            }
        }
    }

    private void updateLabels() {
        playerNumberLabel.setText("Player " + (currentPlayerIndex + 1));
        nameField.setText("");
        roleComboBox.setSelectedIndex(0);
    }

    private void showPreviousPlayer() {
        if (currentPlayerIndex > 0) {
            currentPlayerIndex--;
            updateLabels();
            nextPlayerButton.setVisible(true);
            previousPlayerButton.setVisible(currentPlayerIndex > 0);
        }
    }

    private void showNextPlayer() {
        if (currentPlayerIndex < 3 && players.size() > currentPlayerIndex) {
            // If we already have a player registered at this index, move forward
            currentPlayerIndex++;
            updateLabels();
            previousPlayerButton.setVisible(true);
            nextPlayerButton.setVisible(currentPlayerIndex < 3);
        } else {
            // We need to register the current player first
            JOptionPane.showMessageDialog(this, 
                "Please enter player information and select a color to continue.", 
                "Complete Player Setup", 
                JOptionPane.INFORMATION_MESSAGE);
        }
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
}