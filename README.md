# Snake Game

Welcome to the Snake Game! This project is a simple 2-level snake game where the player controls a snake to defeat an enemy snake, unlock doors, and navigate through obstacles. Below is an overview of the project structure and key functionalities.

## Features

1. **Playable Snake and Enemy NPC:**
   - Control a playable snake and face off against an enemy NPC snake.
   - The enemy snake pathfinds towards food, eats it, and cannot grow.

2. **Map and Obstacles:**
   - Navigate through a map with obstacles and a locked door.
   - The door opens when the playable snake defeats the enemy snake.

3. **Levels:**
   - The game consists of 2 levels, each defined as a subfolder in the assets folder with graphics, obstacle mapping, and game data.

4. **Combat:**
   - Each snake has a strength level.
   - When two snakes collide, the one with higher strength kills the other.

5. **Level Progression:**
   - When the enemy snake is defeated, the door opens, allowing the player to proceed to the next level.

6. **Save and Leaderboard:**
   - Save the game's progress, including the current level and score.
   - Enter your name in the leaderboard when the player dies.

## Class Diagram

The class diagram provided below was generated using IntelliJ:

![class diagram](https://i.imgur.com/kkVKH3H.png)

## Controls

1. **Controls:**
   - Use arrow keys or WASD to control the snake.
   - Use space to sprint, but mind your stamina level.
   - Navigate through the map, defeat the enemy snake, and unlock the door to progress.

2. **Scoring:**
   - Gain points by defeating the enemy snake and progressing through levels.

3. **Saving and Leaderboard:**
   - Save your game progress to resume later.
   - Enter your name in the leaderboard when your snake meets its end.

## Development Setup

1. **Requirements:**
   - Java Development Kit (JDK)
   - LibGDX Game Development Framework

2. **Build from Source:**
   ```bash
   git clone https://github.com/domnu-raul/simple-snake-game.git
   cd simple-snake-game
   ./gradlew desktop:dist
   ```
   Use ```java -jar ./desktop/build/libs/desktop-1-0.jar``` to play the game.
