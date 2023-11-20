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

## Class Diagram Overview

The class diagram provides an overview of the project structure. Here are some key classes and interfaces:

- **Background:** Represents the background of the game.
- **Door:** Manages the door functionality, including opening and transitioning to the next level.
- **EnemySnake:** Represents the NPC enemy snake, including pathfinding and movement.
- **Food:** Represents the in-game food that both snakes can consume.
- **GridMap:** Handles the grid-based map and obstacles.
- **Leaderboard:** Manages the game's leaderboard, allowing players to enter their names.
- **PlayableSnake:** Represents the player-controlled snake.
- **Player:** Represents the player entity.
- **SaveHandler:** Manages game saves and loading.
- **SnakeMap:** Represents the map for the snakes to navigate.
- **SnakePlayer:** Handles player input and score tracking.
- **SnakeScreen:** Manages the game screen, including rendering and transitioning between states.

## How to Play

1. **Controls:**
   - Use arrow keys or a similar input method to control the playable snake.
   - Navigate through the map, defeat the enemy snake, and unlock the door to progress.

2. **Scoring:**
   - Gain points by defeating the enemy snake and progressing through levels.
   - Save your progress to track your achievements in the leaderboard.

3. **Level Progression:**
   - Each level introduces new challenges, obstacles, and enemy behaviors.
   - Reach the end of Level 2 to complete the game.

4. **Saving and Leaderboard:**
   - Save your game progress to resume later.
   - Enter your name in the leaderboard when your snake meets its end.

## Development Setup

1. **Requirements:**
   - Java Development Kit (JDK)
   - LibGDX Game Development Framework

2. **Clone the Repository:**
   ```bash
   git clone https://github.com/your_username/snake-game.git
