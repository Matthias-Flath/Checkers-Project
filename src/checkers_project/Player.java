package checkers_project;
/*
ADT for the human player.
Has a series of methods to validate  moves and actually move based on user input. 

*/
public class Player {
	
	
	CheckerPiece[] pieces = new CheckerPiece[Game.PIECES_PER_PLAYER];
	
	/*
	 * Initialize the pieces for the human player (White) (
	 */
	public Player() {
		int counter = 0;
		for (int y = 0; y < 3; y++) {
			for (int x = 0; x < Board.COLUMNS; x++) {
				if (BoardPosition.checkPositionPlayability(x, y) == true) {
					pieces[counter] = new CheckerPiece(1, false, x, y);
				}
			}
		}
	}
	
	
/*
 * static int whoseTurnIsIt
 * int playerNumber
 *
 *
 * public player(int playerNumber) {
 * 	this.playerNumber = playerNumber
 * whoseTurnIsIt = 1;
 * }
 *
 * public void attemptMove() {
 * 	if isTurn()
 * runs the selected pieces move() method
 * which determines if it is a legal move and if it is moves the piece
 * if playernumber is 1 whoseTurnIsit = 2
 * else if playernumber = 2 whose Turn Is it = 1
 * else
 * does nothing
 * }
 *
 * public boolean isTurn() {
 * is it your turn?
 * if yes return true
 * else return false
 *
 * }
 *
 *
 *
 *
 */
}
