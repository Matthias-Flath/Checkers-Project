package checkers_project;

/*
Uses the checkers engine to actually make the computer's move in the game.
*/
public class ComputerPlayer extends Player {

	private CheckerPiece[] pieces = new CheckerPiece[Game.PIECES_PER_PLAYER];
	
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
	
	public boolean pieceAtLocation(int x, int y) {
		
		for (CheckerPiece i : this.pieces) {
			if (i != null) {
				if (i.getX() == x && i.getY() == y) {
					return true;
				}
			}
		}
		
		return false;
		
	}
	
	/*
	 * public CheckerPiece getPiece(int x, int y) {
	 * 
	 * 
	 * 
	 * return null;
	 * 
	 * }
	 */
	
	
	
/*
 *
 * use engine to determine best move
 * then do that move
 *
 *
 *
 */

}
