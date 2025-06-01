import javax.swing.*;
import java.awt.*;

public class StartMenu extends JPanel {
    public StartMenu(PacmanGame game) {
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
                "- Use arrows to move Pacman.\n" +
                "- Eat all the yellow dots to win.\n" +
                "- Avoid the ghosts or you will lose the game.\n" +
                "- Eat big energy balls to gain points.\n" +
                "- Press R to reset when game over.",
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
}