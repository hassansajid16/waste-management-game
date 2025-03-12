package wastemanagement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URL;
import javax.swing.*;

public class SplashScreen extends JFrame {
    private static final long serialVersionUID = 1L;
    
    // UI Components
    private JPanel mainPanel;
    private JLabel logoLabel;
    private JLabel taglineLabel;
    private JButton startButton;
    
    // Colors matching the game's palette
    private Color bgColor = new Color(45, 65, 45);  // Dark green background
    private Color accentColor = new Color(89, 116, 89);  // Accent color
    private Color buttonColor = new Color(173, 216, 230);  // Light blue for buttons
    
    public SplashScreen() {
        // Setup frame
        setTitle("Waste Management Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Center on screen
        
        // Setup UI
        setupUI();
        
        // Show the frame
        setVisible(true);
    }
    
    private void setupUI() {
        // Main panel with gradient background
        mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Create gradient background (green to slightly lighter green)
                GradientPaint gradient = new GradientPaint(
                    0, 0, bgColor,
                    0, getHeight(), new Color(60, 85, 60)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        
        mainPanel.setLayout(new BorderLayout());
        
        // Logo panel (center of the screen)
        JPanel logoPanel = new JPanel(new BorderLayout());
        logoPanel.setOpaque(false);
        logoPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        
        // Load logo
        logoLabel = new JLabel();
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        boolean logoLoaded = false;
        
        try {
            // Method 1: Try to load from resources folder using resource stream
            String resourcePath = "/Resources/logo.jpg";
            URL logoUrl = getClass().getResource(resourcePath);
            
            if (logoUrl != null) {
                ImageIcon originalIcon = new ImageIcon(logoUrl);
                Image scaledImage = originalIcon.getImage().getScaledInstance(
                    Math.min(originalIcon.getIconWidth(), 300), 
                    Math.min(originalIcon.getIconHeight(), 300), 
                    Image.SCALE_SMOOTH
                );
                logoLabel.setIcon(new ImageIcon(scaledImage));
                logoLoaded = true;
            } else {
                System.out.println("Couldn't find logo using resource path: " + resourcePath);
            }
            
            // Method 2: If Method 1 failed, try direct file path
            if (!logoLoaded) {
                String absolutePath = "C:\\Users\\lucia\\CSC2058-2425-G31\\WasteManagementGame.java\\src\\Resources\\logo.png";
                File logoFile = new File(absolutePath);
                
                if (logoFile.exists()) {
                    ImageIcon originalIcon = new ImageIcon(absolutePath);
                    Image scaledImage = originalIcon.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH);
                    logoLabel.setIcon(new ImageIcon(scaledImage));
                    logoLoaded = true;
                    System.out.println("Loaded logo from absolute path: " + absolutePath);
                } else {
                    System.out.println("File doesn't exist at path: " + absolutePath);
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading logo: " + e.getMessage());
            e.printStackTrace();
        }
        
        // Fallback if logo still not loaded
        if (!logoLoaded) {
            logoLabel.setText("BINOVATORS OF MAKERS VALLEY");
            logoLabel.setFont(new Font("Arial", Font.BOLD, 28));
            logoLabel.setForeground(Color.WHITE);
        }
        
        logoPanel.add(logoLabel, BorderLayout.CENTER);
        
        // Tagline label
        taglineLabel = new JLabel("Innovating waste, transforming tomorrow", SwingConstants.CENTER);
        taglineLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        taglineLabel.setForeground(new Color(220, 220, 220));
        taglineLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        
        logoPanel.add(taglineLabel, BorderLayout.SOUTH);
        
        // Start button panel (bottom of the screen)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        
        startButton = new JButton("START");
        startButton.setFont(new Font("Arial", Font.BOLD, 18));
        startButton.setForeground(Color.BLACK);
        startButton.setBackground(buttonColor);
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(new Dimension(150, 50));
        startButton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK, 2),
            BorderFactory.createEmptyBorder(5, 20, 5, 20)
        ));
        
        // Add hover effect
        startButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startButton.setBackground(new Color(153, 204, 255));
                startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startButton.setBackground(buttonColor);
            }
        });
        
        // Add action to start button
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close this splash screen
                dispose();
                
                // Open the login page
                SwingUtilities.invokeLater(() -> new Login());
            }
        });
        
        buttonPanel.add(startButton);
        
        // Add components to main panel
        mainPanel.add(logoPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        // Add main panel to frame
        setContentPane(mainPanel);
    }
}