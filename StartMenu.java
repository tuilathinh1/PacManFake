import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;

public class StartMenu extends JPanel {
    private Image backgroundImage;

    public StartMenu(PacmanGame game) {
        // Load the background image
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/BackGround.jpg")); // Adjust path as needed
        } catch (IOException e) {
            e.printStackTrace();
            backgroundImage = null; // Fallback if image fails to load
        }

        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridx = 0;
        gbc.gridy = GridBagConstraints.RELATIVE;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("PACMAN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.PLAIN, 24));
        startButton.setBackground(Color.YELLOW);
        startButton.setForeground(Color.BLACK);
        startButton.setFocusPainted(false);
        startButton.addActionListener(e -> {
            game.setGameStarted(true);
            game.remove(this);
            game.requestFocusInWindow();
            game.repaint();
        });

        JButton instructionsButton = new JButton("Guideline");
        instructionsButton.setFont(new Font("Arial", Font.PLAIN, 24));
        instructionsButton.setBackground(Color.CYAN);
        instructionsButton.setForeground(Color.BLACK);
        instructionsButton.setFocusPainted(false);
        instructionsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Guideline:\n" +
                "- Use arrows or awsd to move Pacman.\n" +
                "- Eat all the yellow dots to win.\n" +
                "- Avoid the ghosts or you will lose the game.\n" +
                "- Eat big energy balls to eat ghosts when ghosts are blue color.\n" +
                "- Press R to reset when game over\n."+
                "- Press P to pause game and press one more for continue.\n"+
                "-  ",
                "Pacman Game Guide", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton exitButton = new JButton("EXIT");
        exitButton.setFont(new Font("Arial", Font.PLAIN, 24));
        exitButton.setBackground(Color.RED);
        exitButton.setForeground(Color.WHITE);
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(e -> System.exit(0));

        add(titleLabel, gbc);
        add(startButton, gbc);
        add(instructionsButton, gbc);
        add(exitButton, gbc);
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}