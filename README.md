# PacManFake

## 🕹 Overview

This is a Java-based implementation of the classic Pacman game, built using **Swing** for the graphical user interface.  
The game features a maze where Pacman navigates to eat dots and power pellets while avoiding or chasing ghosts.  
It includes a start menu, pause menu, background music, and a scoring system.

---

##  Features

- **Gameplay Mechanics**: Move Pacman through the maze, eat dots and power pellets, and avoid or chase ghosts.  
- **Ghost AI**: Four ghosts with distinct behaviors (wandering, chasing, and frightened states).  
- **Visuals**: Smooth animations for Pacman and ghosts, with gradient effects and a progress bar.  
- **Audio**: Background music (`nhacgame.wav`) and sound effects (`nhaccgame.wav`).  
- **Menus**:
  - Start menu with options to start the game, view guidelines, or exit  
  - Pause menu with resume, main menu, and exit options  
- **Controls**: Use `Arrow keys` or `WASD` to move Pacman, `P` to pause/resume, and `R` to reset after game over.

---

##  Setup Instructions

### Prerequisites
- Java Development Kit (JDK) 8 or higher  
- An IDE like IntelliJ IDEA, Eclipse, or any Java compiler

### Project Structure
- Ensure all files are in the same directory as listed below  
- Resources like `BackGround.jpg`, `nhacgame.wav`, and `nhaccgame.wav` must be accessible in the project root

### Running the Game
1. Compile and run `PacmanGame.java` as the main class  
2. The game window will open with the start menu  
3. Click **Start** to play or **Guideline** for instructions

---

## 🎮 Gameplay Guide

### Objective
Eat all the yellow dots in the maze to win the game.

### Controls
- Move: `Arrow keys` or `WASD`  
- Pause/Resume: `P`  
- Reset: `R` (after game over)

### Power Pellets
Eating large energy balls (power pellets) turns ghosts blue, allowing Pacman to eat them for bonus points.

### Lives
- Pacman starts with 3 lives  
- Colliding with a ghost (when not frightened) loses one life  
- The game ends when all lives are lost

### Scoring
- Small dot: `+10` points  
- Power pellet: `+50` points  
- Eating a ghost: `+200` points  

---

##  File Structure

- `PacmanGame.java`: Main game class; handles game loop, rendering, and input  
- `Pacman.java`: Handles Pacman's movement and interactions  
- `Ghost.java`: Controls ghost behaviors and AI states  
- `StartMenu.java`: Displays the main menu  
- `PauseMenu.java`: Displays the pause menu  
- `BackGround.jpg`: Image used in the start menu  
- `nhacgame.wav`: Background music during gameplay  
- `nhaccgame.wav`: Additional sound effect (currently unused)  
- `README.md`: This documentation file

---

##  Known Issues

- The game may lose focus after showing dialog messages (e.g., "Game Over" or "You Win").  
  ➤ Click the game window to regain control.  
- Ensure `nhacgame.wav`, `nhaccgame.wav`, and `BackGround.jpg` are in the correct directory.  
  ➤ Missing files will prevent the game from loading properly.

---

##  Credits

**Developers**:  
- Huynh Trinh Phuc Thinh  
- Truong Huy Hoang
