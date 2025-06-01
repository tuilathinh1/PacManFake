package Test1;

public class Pacman {
    public int x, y;
    private int direction = 0; // 0: right, 1: down, 2: left, 3: up
    private int nextDirection = 0;
    private int startX, startY;
    private int[][] gameMap;
    private int[] DX;
    private int[] DY;
    private int COLS;
    private int ROWS;

    public Pacman(int x, int y, int[][] gameMap, int[] DX, int[] DY, int COLS, int ROWS) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.gameMap = gameMap;
        this.DX = DX;
        this.DY = DY;
        this.COLS = COLS;
        this.ROWS = ROWS;
    }

    public void setNextDirection(int dir) {
        this.nextDirection = dir;
    }

    public int update() {
        int pointsEaten = 0;

        // Try to change direction
        int newX = x + DX[nextDirection];
        int newY = y + DY[nextDirection];
        if (isValidMove(newX, newY)) {
            direction = nextDirection;
        }

        // Move in current direction
        newX = x + DX[direction];
        newY = y + DY[direction];
        if (isValidMove(newX, newY)) {
            x = newX;
            y = newY;

            // Eat dots or power pellets
            if (gameMap[y][x] == 0) {
                gameMap[y][x] = 2; // Mark as eaten
                pointsEaten = 10;
            } else if (gameMap[y][x] == 3) {
                gameMap[y][x] = 2; // Mark as eaten
                pointsEaten = 50;
            }
        }

        // Tunnel effect
        if (x < 0) x = COLS - 1;
        if (x >= COLS) x = 0;
        if (y < 0) y = ROWS - 1;
        if (y >= ROWS) y = 0;

        return pointsEaten;
    }

    public void reset() {
        x = startX;
        y = startY;
        direction = 0;
        nextDirection = 0;
    }

    private boolean isValidMove(int x, int y) {
        if (x < 0 || x >= COLS || y < 0 || y >= ROWS) {
            return false;
        }
        return gameMap[y][x] != 1;
    }

    public int getDirection() {
        return direction;
    }
}