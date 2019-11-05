package checkers_project;
/*
Uses the checkers engine to actually make the computer's move in the game.


*/
public class ComputerPlayer extends Player {

	public ComputerPlayer() {
		int counter = 0;
		for (int y = 6; y <= 8; y++) {
			for (int x = 0; x < Board.COLUMNS; x++) {
				if (BoardPosition.checkPositionPlayability(x, y) == true) {
					pieces[counter] = new CheckerPiece(2, false, x, y);
				}
			}
		}
	}
/*
 *
 * use engine to determine best move
 * then do that move
 *
 *
 *
 */

}
