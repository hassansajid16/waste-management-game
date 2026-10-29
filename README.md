# Waste Management Game

A Java desktop board game about waste management and recycling, built as a team project for **CSC2058 (2024/25) at Queen's University Belfast** by Group 31.

Players log in, roll dice to move their tokens around a tile-based board, complete special tasks and objectives, and play a waste-sorting mini-game where items have to be put into the correct waste category.

## Features

- **Login system:** a login screen that players use before the game starts
- **Splash screen:** an opening screen with the game logo
- **Board game:** a tile-based game board where player tokens move according to dice rolls (dice images 1 to 6)
- **Special tasks:** tiles that trigger special tasks during play
- **Objectives panel:** shows the goals players are working towards
- **Player statistics:** a panel that tracks each player's progress
- **Waste sorting mini game:** sort different waste items into the right categories, launched from the main game

## Tech

- Java (9 or later, as the project uses a `module-info.java` module file)
- Eclipse project (`.project` and `.classpath` included)
- Java desktop GUI

## How to run

1. Clone the repository: `git clone https://github.com/hassansajid16/waste-management-game.git`
2. Open Eclipse and choose **File > Import > Existing Projects into Workspace**.
3. Select the `WasteManagementGame.java` folder inside the cloned repo.
4. Open `src/wastemanagement/Main.java` and choose **Run > Run As > Java Application**.

## Project structure

All source code is in `WasteManagementGame.java/src/wastemanagement/`:

| File | Purpose |
| --- | --- |
| `Main.java` | Entry point |
| `SplashScreen.java` | Opening screen |
| `Login.java` | Login screen |
| `WasteManagementGame.java` | Main game window and logic |
| `GameManager.java` | Controls the game flow |
| `GameBoardManager.java`, `Board.java`, `Tile.java`, `GameTilePanel.java` | Game board and its tiles |
| `Player.java`, `PlayerToken.java`, `PlayerStatisticsPanel.java` | Players, their tokens and their stats |
| `SpecialTask.java`, `ObjectivesPanel.java` | Tasks and objectives |
| `WasteSortingGame.java`, `WasteSortingLauncher.java` | Waste sorting mini game |

Images (the logo and the six dice faces) are in `src/Resources/`. Project planning documents are in `Project Problem and Solution/`.

## Branches

- `main`: the stable version
- `Development`: integration of ongoing work
- `Login-Page`, `UI/UX`, `Error-Handling`: feature branches for each area of the game

## Notes

This repository is a copy of the team's original project, which was hosted on Queen's University Belfast's GitLab.
