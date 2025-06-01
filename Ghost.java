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
    private enum State { EXIT_CAGE, WANDER, CHASE }
    private State state;
    private double cageTimer;
    private double moveTimer;
    private int lastDir = -1; // Track last direction to avoid oscillation
    private int stuckCounter = 0; // Count consecutive stuck states
    private static final double CAGE_EXIT_TIME = 2.0;
    private static final double SIGHT_RANGE_ENTER = 8.0;
    private static final double SIGHT_RANGE_EXIT = 9.0; // Hysteresis for state transition
    private static final double MOVE_INTERVAL = 0.18; // Slower than Pacman (0.15s)
    private static final int MAX_STUCK_COUNT = 10; // Reset after 10 stuck updates

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

    public void update(double deltaTime) {
        cageTimer += deltaTime;
        moveTimer += deltaTime;

        // Get valid directions
        List<Integer> validDirections = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int newX = x + DX[i];
            int newY = y + DY[i];
            if (isValidMove(newX, newY)) {
                validDirections.add(i);
            }
        }

        // Handle no valid directions
        if (validDirections.isEmpty()) {
            stuckCounter++;
            if (dx != 0 || dy != 0) {
                int newDx = -dx;
                int newDy = -dy;
                if (isValidMove(x + newDx, y + newDy)) {
                    dx = newDx;
                    dy = newDy;
                    stuckCounter = 0;
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
                        System.out.println("Ghost stuck at (" + x + "," + y + "), state: " + state + ", resetting to WANDER");
                    }
                }
            }
            // Reset to start if stuck too long
            if (stuckCounter >= MAX_STUCK_COUNT) {
                reset();
                System.out.println("Ghost reset to start (" + x + "," + y + ") due to prolonged stuck state");
            }
            System.out.println("Ghost at (" + x + "," + y + "), state: " + state + ", dx: " + dx + ", dy: " + dy + ", validDirections: " + validDirections);
            return;
        } else {
            stuckCounter = 0;
        }

        // Only update direction and move when moveTimer reaches interval
        if (moveTimer >= MOVE_INTERVAL) {
            if (state == State.EXIT_CAGE) {
                if (cageTimer >= CAGE_EXIT_TIME || y <= 10) {
                    state = State.WANDER;
                }
                // Prioritize moving up
                int dir = validDirections.contains(3) ? 3 : validDirections.get(random.nextInt(validDirections.size()));
                dx = DX[dir];
                dy = DY[dir];
                lastDir = dir;
            } else {
                // Check if Pacman is in sight with hysteresis
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
                        if (lastDir != -1 && i == (lastDir + 2) % 4) continue; // Avoid reversing
                        int newX = x + DX[i];
                        int newY = y + DY[i];
                        double distance = Math.sqrt(Math.pow(newX - pacman.x, 2) + Math.pow(newY - pacman.y, 2));
                        if (distance < minDistance) {
                            minDistance = distance;
                            dir = i;
                        }
                    }
                } else {
                    dir = (random.nextDouble() < 0.9) ? findDirectionToNearestDot(validDirections) : validDirections.get(random.nextInt(validDirections.size()));
                    if (lastDir != -1 && validDirections.contains(lastDir) && dir == (lastDir + 2) % 4) {
                        dir = validDirections.get(random.nextInt(validDirections.size())); // Avoid reversing
                    }
                }
                dx = DX[dir];
                dy = DY[dir];
                lastDir = dir;
            }

            x += dx;
            y += dy;

            // Tunnel effect
            if (x < 0) x = COLS - 1;
            if (x >= COLS) x = 0;
            if (y < 0) y = ROWS - 1;
            if (y >= ROWS) y = 0;

            moveTimer = 0;
            System.out.println("Ghost moved to (" + x + "," + y + "), state: " + state + ", dx: " + dx + ", dy: " + dy + ", validDirections: " + validDirections);
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
                        if (lastDir != -1 && dir == (lastDir + 2) % 4) continue; // Avoid reversing
                        int newX = x + DX[dir];
                        int newY = y + DY[dir];
                        double distance = Math.sqrt(Math.pow(newX - j, 2) + Math.pow(newY - i, 2));
                        // Score based on distance and 2-step lookahead
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
        dy = -1;
        lastDir = -1;
        stuckCounter = 0;
    }

    private boolean isValidMove(int x, int y) {
        if (x < 0 || x >= COLS || y < 0 || y >= ROWS) {
            return false;
        }
        boolean valid = gameMap[y][x] != 1;
        if (!valid) {
            System.out.println("Invalid move for Ghost at (" + x + "," + y + "), map value: " + gameMap[y][x]);
        }
        return valid;
    }
}