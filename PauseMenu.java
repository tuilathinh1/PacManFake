import javax.swing.*;
import java.awt.*;

public class PauseMenu extends JPanel {
    public PauseMenu(PacmanGame game) {
        setOpaque(false); // Fully transparent background
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Create a semi-transparent panel for buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(0, 0, 0, 100)); // Semi-transparent black
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 2));

        JLabel titleLabel = new JLabel("TẠM DỪNG");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton resumeButton = new JButton("Tiếp tục");
        resumeButton.setFont(new Font("Arial", Font.PLAIN, 20));
        resumeButton.setBackground(Color.GREEN);
        resumeButton.setForeground(Color.BLACK);
        resumeButton.setFocusPainted(false);
        resumeButton.setPreferredSize(new Dimension(200, 50));
        resumeButton.addActionListener(e -> {
            game.setPaused(false);
            game.requestFocusInWindow();
        });

        JButton mainMenuButton = new JButton("Màn hình chính");
        mainMenuButton.setFont(new Font("Arial", Font.PLAIN, 20));
        mainMenuButton.setBackground(Color.CYAN);
        mainMenuButton.setForeground(Color.BLACK);
        mainMenuButton.setFocusPainted(false);
        mainMenuButton.setPreferredSize(new Dimension(200, 50));
        mainMenuButton.addActionListener(e -> {
            game.resetGame();
        });

        JButton exitButton = new JButton("Thoát");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 20));
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.setPreferredSize(new Dimension(200, 50));
        exitButton.addActionListener(e -> System.exit(0));

        // Add components to buttonPanel
        GridBagConstraints panelGbc = new GridBagConstraints();
        panelGbc.insets = new Insets(10, 10, 10, 10);
        panelGbc.gridx = 0;
        panelGbc.gridy = GridBagConstraints.RELATIVE;
        panelGbc.fill = GridBagConstraints.HORIZONTAL;

        buttonPanel.add(titleLabel, panelGbc);
        buttonPanel.add(resumeButton, panelGbc);
        buttonPanel.add(mainMenuButton, panelGbc);
        buttonPanel.add(exitButton, panelGbc);

        // Add buttonPanel to the main panel
        add(buttonPanel, gbc);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // No additional painting needed; game state is drawn by PacmanGame
    }
}