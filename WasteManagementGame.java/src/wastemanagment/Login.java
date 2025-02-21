package wastemanagment;

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

    public Login() {
        players = new ArrayList<>();
        availableColors = new HashSet<>();
        availableColors.add(Color.BLUE);
        availableColors.add(Color.RED);
        availableColors.add(Color.GREEN);
        availableColors.add(Color.YELLOW);

        setTitle("Player Login");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Main panel with some padding
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Logo Panel
        JPanel logoPanel = new JPanel();
        logoPanel.setPreferredSize(new Dimension(400, 100));
        logoPanel.setBackground(Color.LIGHT_GRAY);
        JLabel logoLabel = new JLabel("Logo");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoPanel.add(logoLabel);
        mainPanel.add(logoPanel, BorderLayout.NORTH);

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 1, 10, 10));
        
        // Player label
        playerNumberLabel = new JLabel("Player " + (currentPlayerIndex + 1));
        playerNumberLabel.setFont(new Font("Arial", Font.BOLD, 16));
        playerNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Name input with rounded border
        JLabel nameLabel = new JLabel("Enter the name of player " + (currentPlayerIndex + 1));
        nameField = new JTextField();
        nameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        
        // Role selection with custom styling
        JLabel roleLabel = new JLabel("Enter the role of player " + (currentPlayerIndex + 1));
        String[] roles = {"Business Owner", "Community Leader"};
        roleComboBox = new JComboBox<>(roles);
        
        // Color selection
        JLabel colorLabel = new JLabel("Choose colour of player " + (currentPlayerIndex + 1));
        JPanel colorPanel = new JPanel(new GridLayout(1, 4, 10, 10));
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
        
        previousPlayerButton = createStyledButton("Previous player");
        previousPlayerButton.setVisible(false);
        previousPlayerButton.addActionListener(e -> showPreviousPlayer());
        
        startGameButton = createStyledButton("Start Game");
        startGameButton.setEnabled(false);
        startGameButton.setBackground(new Color(144, 238, 144)); // Light green
        startGameButton.addActionListener(e -> startGame());
        
        nextPlayerButton = createStyledButton("Next player");
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
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)));
        return button;
    }

    // Keeping the existing helper methods
    private void selectColor(Color color, JButton button) {
        if (availableColors.contains(color)) {
            availableColors.remove(color);
            button.setEnabled(false);
            registerPlayer(color);
        }
    }

    private void registerPlayer(Color color) {
        String name = nameField.getText().trim();
        String role = (String) roleComboBox.getSelectedItem();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Player player = new Player(name);
        player.setRole(role);
        player.applyRole(role);
        players.add(player);

        if (players.size() >= 2) {
            startGameButton.setEnabled(true);
        }

        if (players.size() < 4) {
            currentPlayerIndex++;
            updateLabels();
            nameField.setText("");
            roleComboBox.setSelectedIndex(0);
        } else {
            nextPlayerButton.setVisible(false);
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
        if (currentPlayerIndex < 3) {
            currentPlayerIndex++;
            updateLabels();
            previousPlayerButton.setVisible(true);
            nextPlayerButton.setVisible(currentPlayerIndex < 3);
        }
    }

    private void startGame() {
        if (players.size() >= 2) {
            dispose();
            new WasteManagementGame(players);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}