# 2082-Final-Project | Checkers Game

### Features
 - GUI in JavaFX to play Checkers
 - set of methods to verify moves are allowed.
    - A piece can only move diagonally one space into an open space towards the opposing side.
    - If a piece reaches the opposite side it becomes a "King" and can now move diagonally forwards or backwards.
    - A piece can "Jump" over an opponents piece to an open square. Directional limitations still apply.
    - When you "Jump" over a piece remove the piece that was jumped over from the game.
 - Choose if you want to play against an AI or another player.
 - Includes an engine to play against the user.  
    - Evaluates games states, searches between them to find the best move
 - Allows two users running the same program to play against each other from different machines with the java sockets program.
