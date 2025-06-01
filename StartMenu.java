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

        JButton startButton = new JButton("Bắt đầu");
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

        JButton instructionsButton = new JButton("Hướng dẫn");
        instructionsButton.setFont(new Font("Arial", Font.PLAIN, 24));
        instructionsButton.setBackground(Color.CYAN);
        instructionsButton.setForeground(Color.BLACK);
        instructionsButton.setFocusPainted(false);
        instructionsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                "Hướng dẫn:\n" +
                "- Sử dụng phím mũi tên để di chuyển Pacman.\n" +
                "- Ăn hết các chấm vàng để thắng.\n" +
                "- Tránh các con ma, nếu không sẽ mất mạng.\n" +
                "- Ăn viên năng lượng lớn để tăng điểm.\n" +
                "- Nhấn R để reset khi game over.",
                "Hướng dẫn chơi Pacman", JOptionPane.INFORMATION_MESSAGE);
        });

        JButton exitButton = new JButton("Thoát");
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