import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;


public class PacmanGame extends JPanel implements KeyListener, Runnable {
    private static final int BOARD_WIDTH = 900;
    private static final int BOARD_HEIGHT = 720;
    private static final int UI_HEIGHT = 50;
    private static final int CELL_SIZE = 30;
    private static final int ROWS = 24;
    private static final int COLS = 30;

    private boolean gameRunning = true;
    private boolean gameStarted = false;
    private boolean gameWon = false;
    private int score = 0;
    private int lives = 3;
    private int totalDots = 0;
    private int dotsEaten = 0;

    private long lastTime = System.nanoTime();
    private double animationTime = 0;
    private double mouthAnimation = 0;
    private double ghostAnimation = 0;

    private Pacman pacman;
    private List<Ghost> ghosts;
    private Random random = new Random();
    private StartMenu startMenu;
    private boolean paused = false;
    private PauseMenu pauseMenu;

    private boolean gameOverDisplayed = false;

    private int[][] gameMap = {
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,1,1,1,0,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,0,1,1,1,1,0,1},
        {1,3,1,1,1,1,0,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,0,1,1,1,1,3,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,0,1},
        {1,0,0,0,0,0,0,1,1,0,0,0,0,1,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,0,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,0,1,1,1,1,1,1},
        {2,2,2,2,2,1,0,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,0,1,2,2,2,2,2},
        {1,1,1,1,1,1,0,1,1,2,2,2,2,2,2,2,2,2,2,2,2,1,1,0,1,1,1,1,1,1},
        {2,2,2,2,2,2,0,2,2,2,1,1,1,1,2,2,1,1,1,1,2,2,2,0,2,2,2,2,2,2},
        {1,1,1,1,1,1,0,1,1,2,1,2,2,2,2,2,2,2,2,1,2,1,1,0,1,1,1,1,1,1},
        {2,2,2,2,2,1,0,1,1,2,1,2,2,2,2,2,2,2,2,1,2,1,1,0,1,2,2,2,2,2},
        {1,1,1,1,1,1,0,1,1,2,1,1,1,1,1,1,1,1,1,1,2,1,1,0,1,1,1,1,1,1},
        {2,2,2,2,2,2,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,2,2,2,2,2,2},
        {1,1,1,1,1,1,0,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,0,1,1,1,1,1,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,0,1,1,1,1,0,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,0,1,1,1,1,0,1},
        {1,3,0,0,1,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1,1,0,0,3,1},
        {1,1,1,0,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,0,1,1,1},
        {1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1},
        {1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1},
        {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
        {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
    };

    private static final int RIGHT = 0, DOWN = 1, LEFT = 2, UP = 3;
    private static final int[] DX = {1, 0, -1, 0};
    private static final int[] DY = {0, 1, 0, -1};

    public PacmanGame() {
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT + UI_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                System.out.println("Game panel gained focus");
            }

            @Override
            public void focusLost(FocusEvent e) {
                System.out.println("Game panel lost focus");
            }
        });

        setLayout(new BorderLayout());
        startMenu = new StartMenu(this);
        add(startMenu, BorderLayout.CENTER);
        initializeGame();
        pauseMenu = new PauseMenu(this);

        Thread gameThread = new Thread(this);
        gameThread.start();

        requestFocusInWindow();
    }

    private void initializeGame() {
        ghosts = new ArrayList<>();
        pacman = new Pacman(14, 20, gameMap, DX, DY, COLS, ROWS, ghosts);
        ghosts.add(new Ghost(14, 11, new Color(255, 0, 0), gameMap, DX, DY, COLS, ROWS, pacman));
        ghosts.add(new Ghost(15, 11, new Color(255, 184, 255), gameMap, DX, DY, COLS, ROWS, pacman));
        ghosts.add(new Ghost(14, 12, new Color(0, 255, 255), gameMap, DX, DY, COLS, ROWS, pacman));
        ghosts.add(new Ghost(15, 12, new Color(255, 184, 82), gameMap, DX, DY, COLS, ROWS, pacman));

        totalDots = 0;
        for (int i = 0; i < gameMap.length; i++) {
            for (int j = 0; j < gameMap[i].length; j++) {
                if (gameMap[i][j] == 0 || gameMap[i][j] == 3) {
                    totalDots++;
                }
            }
        }
        System.out.println("Total dots calculated: " + totalDots);
        dotsEaten = 0;
        score = 0;
        lives = 3;
        gameRunning = true;
        gameWon = false;
    }

    @Override
    public void run() {
        while (true) {
            try {
                long currentTime = System.nanoTime();
                double deltaTime = (currentTime - lastTime) / 1_000_000_000.0;
                lastTime = currentTime;

                if (gameRunning && gameStarted && !paused) {
                    update(deltaTime);
                }
                repaint();

                Thread.sleep(16);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void update(double deltaTime) {
        animationTime += deltaTime;
        mouthAnimation += deltaTime * 8;
        ghostAnimation += deltaTime * 3;

        if (animationTime >= 0.15) {
            // Cập nhật Pacman và kiểm tra va chạm
            int points = pacman.update();
            score += points;
            if (points > 0) {
                dotsEaten++;
            }
            System.out.println("Pacman updated to (" + pacman.x + ", " + pacman.y + ")");
            checkCollisions(); // Kiểm tra va chạm ngay sau khi Pacman di chuyển

            // Cập nhật từng ghost và kiểm tra va chạm
            for (Ghost ghost : ghosts) {
                ghost.update(0.15); // Logic trong Ghost xử lý MOVE_INTERVAL = 0.18
                System.out.println("Ghost updated to (" + ghost.x + ", " + ghost.y + "), Frightened: " + ghost.isFrightened());
            }
            checkCollisions(); // Kiểm tra va chạm ngay sau khi ghost di chuyển

            animationTime = 0;
        }

        if (!gameWon && dotsEaten >= totalDots) {
            gameWon = true;
            gameRunning = false;
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(this, "You win! Your score: " + score);
                resetGame();
                requestFocusInWindow();
            });
        }
    }

    private void checkCollisions() {
        if (ghosts != null && !gameOverDisplayed) {
            for (Ghost ghost : ghosts) {
                // Kiểm tra va chạm dựa trên ô hiện tại
                if (pacman.x == ghost.x && pacman.y == ghost.y) {
                    System.out.println("Collision detected at Pacman: (" + pacman.x + ", " + pacman.y + "), Ghost: (" + ghost.x + ", " + ghost.y + "), Frightened: " + ghost.isFrightened());
                    if (ghost.isFrightened()) {
                        score += 200; // Điểm thưởng khi ăn ma
                        ghost.reset();
                    } else {
                        lives--;
                        System.out.println("Lives remaining: " + lives);
                        if (lives <= 0) {
                            gameRunning = false;
                            gameOverDisplayed = true; //stop announcement repeat when lose 
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(this, "Game Over!\nYour score: " + score);
                                resetGame();
                                requestFocusInWindow();
                            });
                            break;
                        } else {
                            pacman.reset();
                            for (Ghost g : ghosts) {
                                g.reset();
                            }
                        }
                    }
                    break; // thoát vòng lặp sau khi xử lý 1 va chạmchạm
                }
            }
        }
    }

    public void resetGame() {
        gameStarted = false;
        gameWon = false;
        paused = false;
        gameOverDisplayed = false; // đặt lại cờ khi reset game
        removeAll();
        startMenu = new StartMenu(this);
        add(startMenu, BorderLayout.CENTER);

        gameMap = new int[][] {
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,1,0,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,0,1,1,1,1,0,1},
            {1,3,1,1,1,1,0,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,0,1,1,1,1,3,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,1,1,0,1},
            {1,0,0,0,0,0,0,1,1,0,0,0,0,1,1,1,1,0,0,0,0,1,1,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,0,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,0,1,1,1,1,1,1},
            {2,2,2,2,2,1,0,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,0,1,2,2,2,2,2},
            {1,1,1,1,1,1,0,1,1,2,2,2,2,2,2,2,2,2,2,2,2,1,1,0,1,1,1,1,1,1},
            {2,2,2,2,2,2,0,2,2,2,1,1,1,1,2,2,1,1,1,1,2,2,2,0,2,2,2,2,2,2},
            {1,1,1,1,1,1,0,1,1,2,1,2,2,2,2,2,2,2,2,1,2,1,1,0,1,1,1,1,1,1},
            {2,2,2,2,2,1,0,1,1,2,1,2,2,2,2,2,2,2,2,1,2,1,1,0,1,2,2,2,2,2},
            {1,1,1,1,1,1,0,1,1,2,1,1,1,1,1,1,1,1,1,1,2,1,1,0,1,1,1,1,1,1},
            {2,2,2,2,2,2,0,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,2,0,2,2,2,2,2,2},
            {1,1,1,1,1,1,0,1,1,1,1,1,2,1,1,1,1,2,1,1,1,1,1,0,1,1,1,1,1,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,0,1,1,1,1,0,1,1,1,1,1,1,0,1,1,0,1,1,1,1,1,1,0,1,1,1,1,0,1},
            {1,3,0,0,1,1,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,1,1,0,0,3,1},
            {1,1,1,0,1,1,0,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,0,1,1,0,1,1,1},
            {1,0,0,0,0,0,0,1,1,0,0,0,0,0,0,0,0,0,0,0,0,1,1,0,0,0,0,0,0,1},
            {1,0,1,1,1,1,1,1,1,1,1,1,0,1,1,1,1,0,1,1,1,1,1,1,1,1,1,1,0,1},
            {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
            {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };

        ghosts = new ArrayList<>();
        pacman = new Pacman(14, 20, gameMap, DX, DY, COLS, ROWS, ghosts);
        ghosts.add(new Ghost(14, 11, new Color(255, 0, 0), gameMap, DX, DY, COLS, ROWS, pacman));
        ghosts.add(new Ghost(15, 11, new Color(255, 184, 255), gameMap, DX, DY, COLS, ROWS, pacman));
        ghosts.add(new Ghost(14, 12, new Color(0, 255, 255), gameMap, DX, DY, COLS, ROWS, pacman));
        ghosts.add(new Ghost(15, 12, new Color(255, 184, 82), gameMap, DX, DY, COLS, ROWS, pacman));

        totalDots = 0;
        for (int i = 0; i < gameMap.length; i++) {
            for (int j = 0; j < gameMap[i].length; j++) {
                if (gameMap[i][j] == 0 || gameMap[i][j] == 3) {
                    totalDots++;
                }
            }
        }
        score = 0;
        dotsEaten = 0;
        lives = 3;
        gameRunning = true;
        gameWon = false;
        animationTime = 0;
        mouthAnimation = 0;
        ghostAnimation = 0;

        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        if (!gameStarted) {
            return;
        }

        drawMap(g2d);
        drawPacman(g2d);
        drawGhosts(g2d);
        drawUI(g2d);
    }

    private void drawMap(Graphics2D g2d) {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE + UI_HEIGHT;

                if (gameMap[i][j] == 1) {
                    GradientPaint gradient = new GradientPaint(
                        x, y, new Color(0, 0, 150),
                        x + CELL_SIZE, y + CELL_SIZE, new Color(0, 0, 255)
                    );
                    g2d.setPaint(gradient);
                    g2d.fill(new Rectangle2D.Float(x, y, CELL_SIZE, CELL_SIZE));

                    g2d.setColor(new Color(100, 100, 255));
                    g2d.setStroke(new BasicStroke(1));
                    g2d.draw(new Rectangle2D.Float(x, y, CELL_SIZE, CELL_SIZE));
                } else if (gameMap[i][j] == 0) {
                    g2d.setColor(new Color(255, 255, 0, 100));
                    g2d.fill(new Ellipse2D.Float(x + CELL_SIZE/2 - 5, y + CELL_SIZE/2 - 5, 10, 10));
                    g2d.setColor(Color.YELLOW);
                    g2d.fill(new Ellipse2D.Float(x + CELL_SIZE/2 - 2, y + CELL_SIZE/2 - 2, 4, 4));
                } else if (gameMap[i][j] == 3) {
                    double pulse = Math.sin(animationTime * 5) * 0.3 + 0.7;
                    int size = (int)(12 * pulse);
                    g2d.setColor(new Color(255, 255, 0, (int)(150 * pulse)));
                    g2d.fill(new Ellipse2D.Float(x + CELL_SIZE/2 - size, y + CELL_SIZE/2 - size, size * 2, size * 2));
                    g2d.setColor(Color.YELLOW);
                    g2d.fill(new Ellipse2D.Float(x + CELL_SIZE/2 - 6, y + CELL_SIZE/2 - 6, 12, 12));
                }
            }
        }
    }

    private void drawPacman(Graphics2D g2d) {
        int x = pacman.x * CELL_SIZE + CELL_SIZE / 2;
        int y = pacman.y * CELL_SIZE + CELL_SIZE / 2 + UI_HEIGHT;
        int radius = CELL_SIZE / 2 - 2;

        RadialGradientPaint glowPaint = new RadialGradientPaint(
            x, y, radius + 5,
            new float[]{0f, 1f},
            new Color[]{new Color(255, 255, 0, 100), new Color(255, 255, 0, 0)}
        );
        g2d.setPaint(glowPaint);
        g2d.fill(new Ellipse2D.Float(x - radius - 5, y - radius - 5, (radius + 5) * 2, (radius + 5) * 2));

        g2d.setColor(Color.YELLOW);
        double mouthAngle = Math.abs(Math.sin(mouthAnimation)) * 60;
        double startAngle = pacman.getDirection() * 90 - mouthAngle / 2;
        double arcExtent = 360 - mouthAngle;

        Arc2D.Float pacmanArc = new Arc2D.Float(
            (float)(x - radius), (float)(y - radius), (float)(radius * 2), (float)(radius * 2),
            (float)startAngle, (float)arcExtent, Arc2D.PIE
        );
        g2d.fill(pacmanArc);

        g2d.setColor(Color.BLACK);
        int eyeSize = 4;
        double eyeAngle = Math.toRadians(pacman.getDirection() * 90 - 45);
        int eyeX = x + (int)(radius * 0.3 * Math.cos(eyeAngle));
        int eyeY = y + (int)(radius * 0.3 * Math.sin(eyeAngle));
        g2d.fill(new Ellipse2D.Float(eyeX - eyeSize/2, eyeY - eyeSize/2, eyeSize, eyeSize));
    }

    private void drawGhosts(Graphics2D g2d) {
        System.out.println("Drawing " + ghosts.size() + " ghosts");
        for (Ghost ghost : ghosts) {
            System.out.println("Drawing ghost at (" + ghost.x + ", " + ghost.y + ")");
            int x = ghost.x * CELL_SIZE + CELL_SIZE / 2;
            int y = ghost.y * CELL_SIZE + CELL_SIZE / 2 + UI_HEIGHT;
            int radius = CELL_SIZE / 2 - 2;

            Color ghostColor;
            if (ghost.isFrightened()) {
                // Nhấp nháy khi còn dưới 2 giây
                double frightenedTimeLeft = 7.0 - ghost.getFrightenedTimer();
                if (frightenedTimeLeft < 2.0) {
                    ghostColor = (Math.sin(ghostAnimation * 5) > 0) ? Color.BLUE : Color.WHITE;
                } else {
                    ghostColor = Color.BLUE;
                }
            } else {
                ghostColor = ghost.color;
            }

            GradientPaint bodyGradient = new GradientPaint(
                x, y - radius, ghostColor.brighter(),
                x, y + radius, ghostColor.darker()
            );
            g2d.setPaint(bodyGradient);
            g2d.fill(new Arc2D.Float(x - radius, y - radius, radius * 2, radius * 2, 0, 180, Arc2D.PIE));
            g2d.fill(new Rectangle2D.Float(x - radius, y, radius * 2, radius));

            int zigzagHeight = 6;
            int[] xPoints = new int[8];
            int[] yPoints = new int[8];
            double bounce = Math.sin(ghostAnimation + ghost.x) * 2;

            for (int i = 0; i < 8; i++) {
                xPoints[i] = x - radius + (i * radius / 3);
                if (i % 2 == 0) {
                    yPoints[i] = y + radius + (int)bounce;
                } else {
                    yPoints[i] = y + radius - zigzagHeight + (int)bounce;
                }
            }
            g2d.fillPolygon(xPoints, yPoints, 8);

            g2d.setColor(Color.WHITE);
            int eyeSize = 6;
            g2d.fill(new Ellipse2D.Float(x - radius/2 - eyeSize/2, y - radius/2, eyeSize, eyeSize));
            g2d.fill(new Ellipse2D.Float(x + radius/2 - eyeSize/2, y - radius/2, eyeSize, eyeSize));

            g2d.setColor(Color.BLACK);
            int pupilSize = 3;
            int pupilOffset = 1;
            g2d.fill(new Ellipse2D.Float(x - radius/2 - pupilSize/2 + ghost.dx * pupilOffset,
                                       y - radius/2 + ghost.dy * pupilOffset, pupilSize, pupilSize));
            g2d.fill(new Ellipse2D.Float(x + radius/2 - pupilSize/2 + ghost.dx * pupilOffset,
                                       y - radius/2 + ghost.dy * pupilOffset, pupilSize, pupilSize));
        }
    }

    private void drawUI(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        g2d.setFont(new Font("Arial", Font.BOLD, 24));
        g2d.drawString("Score: " + score, 20, 30);

        g2d.setColor(Color.RED);
        g2d.drawString("Lives: " + lives, 200, 30);

        g2d.setColor(Color.WHITE);
        g2d.drawString("Progression: " + dotsEaten + "/" + totalDots, 350, 30);

        int progressWidth = 200;
        int progressHeight = 10;
        g2d.setColor(Color.GRAY);
        g2d.fill(new Rectangle2D.Float(600, 20, progressWidth, progressHeight));

        g2d.setColor(Color.GREEN);
        int fillWidth = (int)((double)dotsEaten / totalDots * progressWidth);
        g2d.fill(new Rectangle2D.Float(600, 20, fillWidth, progressHeight));
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameStarted) {
            return;
        }
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP:
             case KeyEvent.VK_W:
                pacman.setNextDirection(UP);
                pacman.setMoving(true);
                break;
            case KeyEvent.VK_DOWN:
              case KeyEvent.VK_S:
                pacman.setNextDirection(DOWN);
                pacman.setMoving(true);
                break;
            case KeyEvent.VK_LEFT:
             case KeyEvent.VK_A:
                pacman.setNextDirection(LEFT);
                pacman.setMoving(true);
                break;
            case KeyEvent.VK_RIGHT:
             case KeyEvent.VK_D:
                pacman.setNextDirection(RIGHT);
                pacman.setMoving(true);
                break;
            case KeyEvent.VK_R:
                if (!gameRunning) resetGame();
                break;
            case KeyEvent.VK_P: // Xử lý phím P
            if (gameStarted) {
                paused = !paused; // Đảo ngược trạng thái tạm dừng
                if (paused) {
                    // Khi tạm dừng, thêm PauseMenu
                    add(pauseMenu, BorderLayout.CENTER);
                } else {
                    // Khi bỏ tạm dừng, loại bỏ PauseMenu
                    remove(pauseMenu);
                    requestFocusInWindow(); // Đảm bảo game panel lấy lại focus để nhận input
                }
                revalidate(); // Cập nhật lại bố cục container
                repaint();    // Vẽ lại giao diện
            }
            break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}

    public void setGameStarted(boolean started) {
        gameStarted = started;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
        if (paused) {
            add(pauseMenu, BorderLayout.CENTER);
        } else {
            remove(pauseMenu);
            requestFocusInWindow();
        }
        revalidate();
        repaint();
    }

    public List<Ghost> getGhosts() {
        return ghosts;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Pacman Game - Java Edition");
            PacmanGame game = new PacmanGame();

            frame.add(game);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            game.requestFocusInWindow();
        });
    }
}