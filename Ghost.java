import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ghost {
    public int x, y;
    public int dx, dy;
    public Color color;
    private int startX, startY;
    private int[][] gameMap;
    private int[] DX;
    private int[] DY;
    private int COLS;
    private int ROWS;
    private Random random = new Random();

    public Ghost(int x, int y, Color color, int[][] gameMap, int[] DX, int[] DY, int COLS, int ROWS) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.color = color;
        this.gameMap = gameMap;
        this.DX = DX;
        this.DY = DY;
        this.COLS = COLS;
        this.ROWS = ROWS;
        do {
            this.dx = random.nextInt(3) - 1;
            this.dy = random.nextInt(3) - 1;
        } while (dx == 0 && dy == 0); // Ensure ghost moves
    }

    public void update() {
        // Simple AI - random movement with some targeting
        if (random.nextDouble() < 0.3) {
            List<Integer> validDirections = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                int newX = x + DX[i];
                int newY = y + DY[i];
                if (isValidMove(newX, newY)) {
                    validDirections.add(i);
                }
            }
            if (!validDirections.isEmpty()) {
                int dir = validDirections.get(random.nextInt(validDirections.size()));
                dx = DX[dir];
                dy = DY[dir];
            }
        }

        int newX = x + dx;
        int newY = y + dy;
        if (isValidMove(newX, newY)) {
            x = newX;
            y = newY;
        } else {
            List<Integer> validDirections = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                newX = x + DX[i];
                newY = y + DY[i];
                if (isValidMove(newX, newY)) {
                    validDirections.add(i);
                }
            }
            if (!validDirections.isEmpty()) {
                int dir = validDirections.get(random.nextInt(validDirections.size()));
                dx = DX[dir];
                dy = DY[dir];
                x += dx;
                y += dy;
            }
        }

        // Tunnel effect
        if (x < 0) x = COLS - 1;
        if (x >= COLS) x = 0;
        if (y < 0) y = ROWS - 1;
        if (y >= ROWS) y = 0;
    }

    public void reset() {
        x = startX;
        y = startY;
        do {
            this.dx = random.nextInt(3) - 1;
            this.dy = random.nextInt(3) - 1;
        } while (dx == 0 && dy == 0); // Ensure ghost moves
    }

    private boolean isValidMove(int x, int y) {
        if (x < 0 || x >= COLS || y < 0 || y >= ROWS) {
            return false;
        }
        return gameMap[y][x] != 1;
    }
}