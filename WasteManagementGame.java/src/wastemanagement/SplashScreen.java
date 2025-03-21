package wastemanagement;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

//Splash screen that displays when the game first launches
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
    
    //Constructor
    public SplashScreen() {
        // Setup frame
        setTitle("Waste Management Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         // Center on screen
        setLocationRelativeTo(null); 
        
        // Setup UI
        setupUI();
        
        // Show the frame
        setVisible(true);
    }
    
    //Setup the UI components
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
        
        // Try loading the logo first
        ImageIcon logoImg = null;
        try {
            // Path for the logo - update this to your actual path
            String logoPath = "C:\\Users\\khaga\\CSC2058-2425-G31\\WasteManagementGame.java\\src\\Resources\\logo.png";
            
            // Try different ways to load the image
            File file = new File(logoPath);
            if (file.exists()) {
                logoImg = new ImageIcon(file.getAbsolutePath());
            } else {
                // Try as a resource
                logoImg = new ImageIcon(getClass().getResource("/" + logoPath));
            }
            
            // Scale if needed
            if (logoImg != null && logoImg.getIconWidth() > 0) {
                Image img = logoImg.getImage();
                Image scaledImg = img.getScaledInstance(300, -1, Image.SCALE_SMOOTH);
                logoImg = new ImageIcon(scaledImg);
            }
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
            logoImg = null;
        }
        
        // Create title text or image
        if (logoImg != null && logoImg.getIconWidth() > 0) {
            logoLabel = new JLabel(logoImg, SwingConstants.CENTER);
        } else {
            // Fallback to text title
            logoLabel = new JLabel("WASTE MANAGEMENT GAME", SwingConstants.CENTER);
            logoLabel.setFont(new Font("Arial", Font.BOLD, 32));
            logoLabel.setForeground(Color.WHITE);
        }
        
        // Tagline label
        taglineLabel = new JLabel("Tackling Waste Management in Makers Valley", SwingConstants.CENTER);
        taglineLabel.setFont(new Font("Arial", Font.ITALIC, 20));
        taglineLabel.setForeground(new Color(220, 220, 220));
        taglineLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 40, 0));
        
        // Add logo and tagline to logo panel
        logoPanel.add(logoLabel, BorderLayout.CENTER);
        logoPanel.add(taglineLabel, BorderLayout.SOUTH);
        
        // Start button panel (bottom of the screen)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 50, 0));
        
        // Create start button
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
        
        // Add action to start button - launches the login screen
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Close this splash screen
                dispose();
                
                // Open the login page
                SwingUtilities.invokeLater(() -> {
                    try {
                        new Login();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null,
                            "Error starting the login screen: " + ex.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        });
        
        // Add start button to button panel
        buttonPanel.add(startButton);
        
        // Add subtitle panel
        JPanel subtitlePanel = new JPanel();
        subtitlePanel.setOpaque(false);
        JLabel subtitle2 = new JLabel("A Game About Sustainable Waste Management Solutions", SwingConstants.CENTER);
        subtitle2.setFont(new Font("Arial", Font.PLAIN, 16));
        subtitle2.setForeground(Color.WHITE);
        subtitlePanel.add(subtitle2);
        
        // Add components to main panel
        mainPanel.add(logoPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(subtitlePanel, BorderLayout.NORTH);
        
        // Add main panel to frame
        setContentPane(mainPanel);
    }
    
    // Main method for direct testing
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new SplashScreen();
        });
    }
}