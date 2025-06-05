# PacManFake

Overview
This is a Java-based implementation of the classic Pacman game, built using Swing for the graphical user interface. The game features a maze where Pacman navigates to eat dots and power pellets while avoiding or chasing ghosts. It includes a start menu, pause menu, background music, and a scoring system.
Features
Gameplay Mechanics: Move Pacman through the maze, eat dots and power pellets, and avoid or chase ghosts.
Ghost AI: Four ghosts with distinct behaviors (wandering, chasing, and frightened states).
Visuals: Smooth animations for Pacman and ghosts, with gradient effects and a progress bar.
Audio: Background music (nhacgame.wav) and sound effects (nhaccgame.wav).
Menus: Start menu with options to start the game, view guidelines, or exit; pause menu with resume, main menu, and exit options.
Controls: Use arrow keys or WASD to move Pacman, 'P' to pause/resume, and 'R' to reset after game over.
Setup Instructions
Prerequisites:
Java Development Kit (JDK) 8 or higher.
An IDE like IntelliJ IDEA or Eclipse, or a simple Java compiler.
Project Structure: Ensure all files are placed in the same directory as listed in the file structure below. The resources (BackGround.jpg, nhacgame.wav, nhaccgame.wav) should be accessible in the project root.
Running the Game:
Compile and run PacmanGame.java as the main class.
The game window will open, displaying the start menu.
Click "Start" to begin playing, or "Guideline" for instructions.
Gameplay Guide
Objective: Eat all the yellow dots in the maze to win the game.
Controls:
Use arrow keys or WASD to move Pacman.
Press 'P' to pause/resume the game.
Press 'R' to reset the game after a game over.
Power Pellets: Eating large energy balls (power pellets) turns ghosts blue, allowing Pacman to eat them for bonus points.
Lives: Pacman starts with 3 lives. Colliding with a ghost (when not frightened) costs a life. The game ends when all lives are lost.
Scoring:
Small dots: 10 points each.
Power pellets: 50 points each.
Eating a ghost: 200 points.
File Structure
PacmanGame.java: Main game class, handles game loop, rendering, and user input.
Pacman.java: Manages Pacman's movement, direction, and interactions.
Ghost.java: Controls ghost behavior, including AI states (wandering, chasing, frightened).
StartMenu.java: Displays the start menu with options to start, view guidelines, or exit.
PauseMenu.java: Displays the pause menu with options to resume, return to the main menu, or exit.
BackGround.jpg: Background image for the start menu.
nhacgame.wav: Background music played during gameplay.
nhaccgame.wav: Additional sound effect (not currently used in the code).

README.md: This documentation file.
Known Issues

The game may lose focus after displaying dialog messages (e.g., game over or win). Click the game window to regain focus.
Ensure the audio files (nhacgame.wav, nhaccgame.wav) and background image (BackGround.jpg) are in the correct directory, or the game may fail to load them.
Credits

Developer: Huynh Trinh Phuc Thinh , Truong Huy Hoang
