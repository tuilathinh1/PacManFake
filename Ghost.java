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
    private Pacman pacman;
    private enum State { EXIT_CAGE, WANDER, CHASE, FRIGHTENED }
    private State state;
    private double cageTimer;
    private double moveTimer;
    private int lastDir = -1; // Theo dõi hướng trước đó
    private int stuckCounter = 0; // Đếm số lần kẹt
    private static final double CAGE_EXIT_TIME = 2.0;
    private static final double SIGHT_RANGE_ENTER = 8.0;
    private static final double SIGHT_RANGE_EXIT = 9.0; // Trễ để ổn định trạng thái
    private static final double MOVE_INTERVAL = 0.18; // Chậm hơn Pacman
    private static final int MAX_STUCK_COUNT = 10; // Đặt lại sau 10 lần kẹt
    private boolean frightened = false;
    private double frightenedTimer = 0;
    private static final double FRIGHTENED_DURATION = 7.0; // Thời gian trạng thái FRIGHTENED (7 giây)

    public Ghost(int x, int y, Color color, int[][] gameMap, int[] DX, int[] DY, int COLS, int ROWS, Pacman pacman) {
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
        this.pacman = pacman;
        this.state = State.EXIT_CAGE;
        this.cageTimer = 0;
        this.moveTimer = 0;
        this.dx = 0;
        this.dy = -1;
    }

    public void setFrightened(boolean frightened) {
        this.frightened = frightened;
        if (frightened) {
            state = State.FRIGHTENED;
            frightenedTimer = 0;
            // Đảo ngược hướng
            dx = -dx;
            dy = -dy;
            lastDir = (lastDir + 2) % 4; // Cập nhật lastDir để tránh đi lùi
        } else {
            state = State.WANDER;
        }
    }

    public void update(double deltaTime) {
        cageTimer += deltaTime;
        moveTimer += deltaTime;

        if (frightened) {
            frightenedTimer += deltaTime;
            if (frightenedTimer >= FRIGHTENED_DURATION) {
                setFrightened(false); // Hết thời gian FRIGHTENED
            }
        }

// Lấy các hướng hợp lệ
    List<Integer> validDirections = new ArrayList<>();
    for (int i = 0; i < 4; i++) {
        int newX = x + DX[i];
        int newY = y + DY[i];
        if (isValidMove(newX, newY)) {
            validDirections.add(i);
        }
    }

        // Xử lý khi không có hướng hợp lệ
        if (validDirections.isEmpty()) {
            stuckCounter++;
            if (dx != 0 || dy != 0) {
                int newDx = -dx;
                int newDy = -dy;
                if (isValidMove(x + newDx, y + newDy)) {
                    dx = newDx;
                    dy = newDy;
                    stuckCounter = 0;
                    return;
                } else {
                    for (int i = 0; i < 4; i++) {
                        if (isValidMove(x + DX[i], y + DY[i])) {
                            dx = DX[i];
                            dy = DY[i];
                            lastDir = i;
                            stuckCounter = 0;
                            break;
                        }
                    }
                    if (dx == 0 && dy == 0) {
                        dx = 0;
                        dy = 0;
                        state = State.WANDER;
                    }
                }
            }
            if (stuckCounter >= MAX_STUCK_COUNT) {
                reset();
            }
            return;
        } else {
            stuckCounter = 0;
        }

    if (moveTimer >= MOVE_INTERVAL) {
            if (state == State.EXIT_CAGE) {
                // Thoát lồng: ưu tiên di chuyển lên
                if (validDirections.contains(3)) { // UP
                    dx = DX[3];
                    dy = DY[3];
                    lastDir = 3;
                } else {
                    int dir = validDirections.get(random.nextInt(validDirections.size()));
                    dx = DX[dir];
                    dy = DY[dir];
                    lastDir = dir;
                }
                // Chuyển sang WANDER khi thoát lồng
                if (y <= 10 || cageTimer >= CAGE_EXIT_TIME) {
                    state = State.WANDER;
                    System.out.println("Ghost at (" + x + ", " + y + ") exited cage, switching to WANDER");
                }
            } else if (state == State.FRIGHTENED) {
                // Di chuyển ngẫu nhiên, tránh hướng ngược lại
                List<Integer> candidates = new ArrayList<>();
                for (int d : validDirections) {
                    if (lastDir == -1 || d != (lastDir + 2) % 4) {
                        candidates.add(d);
                    }
                }
                if (candidates.isEmpty()) {
                    candidates = validDirections;
                }
                int chosenDir = candidates.get(random.nextInt(candidates.size()));
                dx = DX[chosenDir];
                dy = DY[chosenDir];
                lastDir = chosenDir;
            } else {
                // Chuyển đổi trạng thái với trễ
                double distanceToPacman = Math.sqrt(Math.pow(x - pacman.x, 2) + Math.pow(y - pacman.y, 2));
                if (state == State.CHASE && distanceToPacman > SIGHT_RANGE_EXIT) {
                    state = State.WANDER;
                } else if (state == State.WANDER && distanceToPacman <= SIGHT_RANGE_ENTER) {
                    state = State.CHASE;
                }

                int dir;
                if (state == State.CHASE) {
                    double minDistance = Double.MAX_VALUE;
                    dir = validDirections.get(0);
                    for (int i : validDirections) {
                        if (lastDir != -1 && i == (lastDir + 2) % 4) continue;
                        int newX = x + DX[i];
                        int newY = y + DY[i];
                        double distance = Math.sqrt(Math.pow(newX - pacman.x, 2) + Math.pow(newY - pacman.y, 2));
                        if (distance < minDistance) {
                            minDistance = distance;
                            dir = i;
                        }
                    }
                    dx = DX[dir];
                    dy = DY[dir];
                    lastDir = dir;
                } else if (state == State.WANDER) {
                    int chosenDir = findDirectionToNearestDot(validDirections); // Tìm chấm gần nhất
                    dx = DX[chosenDir];
                    dy = DY[chosenDir];
                    lastDir = chosenDir;
                }
            }

            x += dx;
            y += dy;

            // Hiệu ứng đường hầm
            if (x < 0) x = COLS - 1;
            if (x >= COLS) x = 0;
            if (y < 0) y = ROWS - 1;
            if (y >= ROWS) y = 0;

            moveTimer = 0; // Đặt lại sau khi di chuyển
        }
    }

    private int findDirectionToNearestDot(List<Integer> validDirections) {
        int bestDir = validDirections.get(0);
        double minDistance = Double.MAX_VALUE;
        int maxScore = -1;

        for (int i = Math.max(0, y - 10); i < Math.min(ROWS, y + 11); i++) {
            for (int j = Math.max(0, x - 10); j < Math.min(COLS, x + 11); j++) {
                if (gameMap[i][j] == 0 || gameMap[i][j] == 3) {
                    for (int dir : validDirections) {
                        if (lastDir != -1 && dir == (lastDir + 2) % 4) continue;
                        int newX = x + DX[dir];
                        int newY = y + DY[dir];
                        double distance = Math.sqrt(Math.pow(newX - j, 2) + Math.pow(newY - i, 2));
                        int score = countValidNeighbors(newX, newY);
                        for (int k = 0; k < 4; k++) {
                            int nextX = newX + DX[k];
                            int nextY = newY + DY[k];
                            if (isValidMove(nextX, nextY)) {
                                score += countValidNeighbors(nextX, nextY);
                            }
                        }
                        if (distance < minDistance || (distance == minDistance && score > maxScore)) {
                            minDistance = distance;
                            bestDir = dir;
                            maxScore = score;
                        }
                    }
                }
            }
        }
        return bestDir;
    }

    private int countValidNeighbors(int x, int y) {
        int count = 0;
        for (int i = 0; i < 4; i++) {
            int newX = x + DX[i];
            int newY = y + DY[i];
            if (isValidMove(newX, newY)) {
                count++;
            }
        }
        return count;
    }

public void reset() {
    x = startX;
    y = startY;
    state = State.EXIT_CAGE;
    cageTimer = 0;
    moveTimer = 0;
    dx = 0;
    dy = -1; // Hướng lên để thoát lồng
    lastDir = 3; // Hướng lên (UP = 3)
    stuckCounter = 0;
    frightened = false;
    frightenedTimer = 0;
    System.out.println("Ghost reset to (" + x + ", " + y + "), State: " + state);
}

    public boolean isFrightened() {
        return frightened;
    }

    public double getFrightenedTimer() {
    return frightenedTimer;
}

    private boolean isValidMove(int x, int y) {
        // Chuẩn hóa tọa độ cho hiệu ứng đường hầm
        int normalizedX = x;
        int normalizedY = y;
        if (x < 0) normalizedX = COLS - 1;
        if (x >= COLS) normalizedX = 0;
        if (y < 0) normalizedY = ROWS - 1;
        if (y >= ROWS) normalizedY = 0;

        boolean valid = gameMap[normalizedY][normalizedX] != 1;
        return valid;
    }
}