package wastemanagment;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;

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
        setSize(400, 600); // Portrait orientation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Logo at the top
        JLabel logoLabel = new JLabel("Logo", SwingConstants.CENTER);
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(logoLabel, BorderLayout.NORTH);

        // Main panel for inputs
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 1, 10, 10));

        // Player number label
        playerNumberLabel = new JLabel("Player " + (currentPlayerIndex + 1), SwingConstants.CENTER);
        inputPanel.add(playerNumberLabel);

        // Name input
        JLabel nameLabel = new JLabel("Enter the name of player " + (currentPlayerIndex + 1) + ":");
        nameField = new JTextField();
        inputPanel.add(nameLabel);
        inputPanel.add(nameField);

        // Role selection
        JLabel roleLabel = new JLabel("Enter the role of player " + (currentPlayerIndex + 1) + " (Business Owner or Community Leader):");
        String[] roles = {"Business Owner", "Community Leader"};
        roleComboBox = new JComboBox<>(roles);
        inputPanel.add(roleLabel);
        inputPanel.add(roleComboBox);

        // Color selection
        JLabel colorLabel = new JLabel("Choose color for player " + (currentPlayerIndex + 1) + ":");
        inputPanel.add(colorLabel);

        JPanel colorPanel = new JPanel(new GridLayout(1, 4, 10, 10));
        colorButtons = new JButton[4];
        colorButtons[0] = createColorButton(Color.BLUE);
        colorButtons[1] = createColorButton(Color.RED);
        colorButtons[2] = createColorButton(Color.GREEN);
        colorButtons[3] = createColorButton(Color.YELLOW);
        for (JButton button : colorButtons) {
            colorPanel.add(button);
        }
        inputPanel.add(colorPanel);

        add(inputPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel(new BorderLayout());

        // Start Game button
        startGameButton = new JButton("Start Game");
        startGameButton.setEnabled(false); // Disabled by default
        startGameButton.addActionListener(e -> startGame());
        buttonPanel.add(startGameButton, BorderLayout.CENTER);

        // Previous and Next buttons
        previousPlayerButton = new JButton("Previous Player");
        previousPlayerButton.setVisible(false); // Hidden for player 1
        previousPlayerButton.addActionListener(e -> showPreviousPlayer());
        buttonPanel.add(previousPlayerButton, BorderLayout.WEST);

        nextPlayerButton = new JButton("Next Player");
        nextPlayerButton.addActionListener(e -> showNextPlayer());
        buttonPanel.add(nextPlayerButton, BorderLayout.EAST);

        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private JButton createColorButton(Color color) {
        JButton button = new JButton();
        button.setBackground(color);
        button.setOpaque(true);
        button.setBorderPainted(false);
        button.addActionListener(e -> selectColor(color, button));
        return button;
    }

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
            startGameButton.setEnabled(true); // Enable Start Game button
        }

        if (players.size() < 4) {
            currentPlayerIndex++;
            playerNumberLabel.setText("Player " + (currentPlayerIndex + 1));
            nameField.setText("");
            roleComboBox.setSelectedIndex(0);
        } else {
            nextPlayerButton.setVisible(false); // Hide Next button for player 4
        }
    }

    private void showPreviousPlayer() {
        if (currentPlayerIndex > 0) {
            currentPlayerIndex--;
            playerNumberLabel.setText("Player " + (currentPlayerIndex + 1));
            nameField.setText(players.get(currentPlayerIndex).getName());
            roleComboBox.setSelectedItem(players.get(currentPlayerIndex).getRole());
            nextPlayerButton.setVisible(true);
            if (currentPlayerIndex == 0) {
                previousPlayerButton.setVisible(false);
            }
        }
    }

    private void showNextPlayer() {
        if (currentPlayerIndex < players.size()) {
            currentPlayerIndex++;
            playerNumberLabel.setText("Player " + (currentPlayerIndex + 1));
            nameField.setText("");
            roleComboBox.setSelectedIndex(0);
            previousPlayerButton.setVisible(true);
            if (currentPlayerIndex == 3) {
                nextPlayerButton.setVisible(false);
            }
        }
    }

    private void startGame() {
        if (players.size() >= 2) {
            dispose(); // Close the login window
            new WasteManagementGame(players); // Pass the list of players
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Login::new);
    }
}