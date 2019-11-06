package checkers_project;
/*
Abstract Data Type for the Checkers Piece.
Descrives the set of actions that a checkers piece can make, and all of the
various information it can hold. 
*/


public class CheckerPiece {

	private int team;
	private boolean king;
	private int xCoordinate;
	private int yCoordinate;
	
	public CheckerPiece(int team, boolean king, int x, int y) {
		this.team = team;
		this.king = king;
		xCoordinate = x;
		yCoordinate = y;
	}
	
	public int getX() {
		return this.xCoordinate;
	}
	
	public int getY() {
		return this.yCoordinate;
	}
	
	/*
	 * private boardPosition
	 * 			xCoordinate, yCoordinate
	 *  private boolean king;
	 *  private int controllingPlayer;
	 *
	 *
	 *
	 * 	public checker (boardPosition, controllingPlayer){
	 * 	this.boardPosition = boardPosition
	 * 	this.controllingPlayer = controllingPlayer
	 *
	 *
	 *
	 * }
	 *
	 * public boolean legalMove(boardSpace){
	 * is it a piece that belongs to you?
	 * is moving the selected piece to the boardSpace a legal move
	 * 		is the space empty
	 * 		is it adjacent diagonal and towards the opposite side? (or just adjacent if king)
	 * 		is it a jump?
	 * 		is it on the board?
	 *
	 * if yes
	 * 	return true
	 * if no
	 * 	return false
	 * }
	 *
	 * public void move(boardSpace){
	 *  if(legalMove(boardSpace){
	 * moves the selected piece to the boardSpace
	 * need to check if it jumped a piece or hit the edge
	 * 	if it did remove the jumped piece or king the piece respectively
	 * }
	 *
	 * public void render() {
	 *
	 * draw the piece at its boardPosition
	 * 	if player equals 1 its red
	 *  if player equals 2 its black
	 *  if king == true draw it as a king
	 *  else regular piece
	 * }
	 *
	 * public void remove() {
	 *
	 * remove the piece from the board
	 *
	 * }
	 *
	 * }
	 *
	 */
}
