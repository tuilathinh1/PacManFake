import java.util.List;

import javax.swing.SwingUtilities;

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
        private boolean isMoving = false;
        private List<Ghost> ghosts; // Thêm trường ghosts

    public Pacman(int x, int y, int[][] gameMap, int[] DX, int[] DY, int COLS, int ROWS, List<Ghost> ghosts) {
        this.x = x;
        this.y = y;
        this.startX = x;
        this.startY = y;
        this.gameMap = gameMap;
        this.DX = DX;
        this.DY = DY;
        this.COLS = COLS;
        this.ROWS = ROWS;
        this.ghosts = ghosts; // Khởi tạo ghosts
    }

    public void setNextDirection(int dir) {
        this.nextDirection = dir;
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    public int update() {
        if (!isMoving) return 0;
        int pointsEaten = 0;

        // Thử đổi hướng
        int newX = x + DX[nextDirection];
        int newY = y + DY[nextDirection];
        if (isValidMove(newX, newY)) {
            direction = nextDirection;
        }

        // Di chuyển theo hướng hiện tại
        newX = x + DX[direction];
        newY = y + DY[direction];
        if (isValidMove(newX, newY)) {
            x = newX;
            y = newY;

            // Ăn chấm hoặc viên năng lượng
            if (gameMap[y][x] == 0) {
                gameMap[y][x] = 2; // Đánh dấu đã ăn
                pointsEaten = 10;
            } else if (gameMap[y][x] == 3) {
                gameMap[y][x] = 2; // Đánh dấu đã ăn
                pointsEaten = 50;
                // Kích hoạt trạng thái FRIGHTENED cho tất cả ma
                for (Ghost ghost : ghosts) {
                    ghost.setFrightened(true);
                }
            }
        }

        // Hiệu ứng đường hầm
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
        isMoving = false;
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