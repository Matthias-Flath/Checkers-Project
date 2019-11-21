package checkers_project;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * positions
 * 		1 is a black piece
 * 		2 is a red piece
 * 		3 is a black king
 * 		4 is a red king
 * turns
 * 		1 black
 * 		2 is red * 
 * @author James Lanska
 *
 */
public class BoardState {
	
	private byte turn;
	private boolean secondMove = false;
	byte[][] positions = new byte[Game.ROWS][Game.COLUMNS];
	
	/**
	 * Create a board state for the beginning of the game.
	 * @precondition
	 * 		The game has not started.
	 * @postcondition
	 * 		The game has started with black starting first. 
	 */
	public BoardState() {

		for (byte i = 0; i < Game.ROWS; i++) {
			for (byte j = 0; j < Game.COLUMNS; j++) {
				if (i < 3) {
					positions[i][j] = 1;
				} else if (i > 4) {
					positions[i][j] = 2;
				} else {
					positions[i][j] = 0;
				}
			}
		}	
		this.turn = 1;	
	}
	
	/**
	 * Create a new boardState object
	 * @param current
	 * @param move
	 * @postcondition
	 * 		If a second move is possible, this.SecondMove is true
	 * 		Else, the nextTurn method is called
	 * @throws IllegalArgumentException
	 * 		If move is illegal for BoardState current
	 */
	public BoardState(BoardState current, String move) {
				
		this.positions = current.positions.clone();
		this.turn = current.turn;
		
		if (BoardStateJumps.canJumpAtDestination(current, move)) {
			this.secondMove = true;
		} else {
			this.nextTurn();
		}
		
		if (!current.isLegalMove(move)) {
			System.out.println("BoardState object cannot be created because this move is illegal.");
			throw new IllegalArgumentException();
		}
		
		// This line doesn't do anything with turns
		this.preCheckedMove(move);
	}
	
	/**
	 * Create a new BoardState object from a previous object and a move. 
	 * Allows the user to determine whether or not to move to the next turn after the move. 
	 * This method is used by the canJumpAtDestination methods.
	 * @param current
	 * @param move
	 * @param nextTurn
	 */
	public BoardState(BoardState current, String move, boolean nextTurn) {
		this.positions = current.positions.clone();
		this.turn = current.turn;
		this.preCheckedMove(move);
		
		if (nextTurn) {
			this.nextTurn();
		}
	}
	
	/**
	 * Return the current turn of the boardstate.
	 * @return
	 */
	public byte getTurn() {
		return this.turn;
	}
	
	// Add checks to this method
	/**
	 * Turn off the secondMove value.
	 * Change the turn of the calling BoardState.
	 * @precondition
	 * 		No additional moves are possible for the current player
	 * @postcondition
	 * 		secondMove is false
	 * 		turn has switched to the other user.
	 */
	public void nextTurn() {
		
		this.secondMove = false;
		
		if (this.turn == 1) {
			
			this.turn = 2;
		} else {
			this.turn = 1;
		}
	}

	/**
	 * Get the value of the secondMove instance variable.
	 * This method doesn't actually do any computation, merely fetch the value.
	 * @return
	 */
	public boolean isSecondMovePossible() {
		return this.secondMove;
	}
	
	public void setSecondMove(boolean secondMove) {
		
		
		this.secondMove = secondMove;
	}
	
	/**
	 * Complete the move described in the string on the calling BoardState.
	 * Doesn't actually change the turn, simply moves the piece
	 * @param move
	 * @throws IllegalArgumentException
	 * 		If move is not legal for the calling BoardState
	 */
	public void preCheckedMove(String move) {
		
		// For debugging
		if (this.isLegalMove(move) == false) {
			System.out.println("This move is illegal!");
			System.out.println(move);
			this.printState();
			throw new IllegalArgumentException();
		}
		
		int[] fromAndTo = TextConversions.convertMoveStringToIntArray(move);	
		System.out.println(Arrays.toString(fromAndTo));
		preCheckedMove(fromAndTo[0], fromAndTo[1], fromAndTo[2], fromAndTo[3]);
	}
	
	/**
	 * Complete the move described in the string on the calling BoardState.
	 * Doesn't actually change the turn, simply moves the piece.
	 * This method does not check for move legality.  It assumes that you are calling this method from a method that already has.
	 * @param py
	 * @param px
	 * @param dy
	 * @param dx
	 * @postcondition
	 * 		The move is complete, turn has not been changed.
	 */
	public void preCheckedMove(int py, int px, int dy, int dx) {
		
		// Move the piece
		this.positions[dy][dx] = this.positions[py][px];
		this.positions[py][px] = 0;
		
		// Call the isJump method
		if (TextConversions.isJump(py, px, dy, dx)) {
			int middleX = BoardStateJumps.getXToJumpOver(py, px, dy, dx);
			int direction = (dy - py) / 2;
			
			// Delete the jumped over piece
			positions[py+direction][middleX] = 0;
		}
		
		// King pieces that reach the end
		if (dy == 0 || dy == 7) {
			
			// Checks to make sure the pieces aren't already kings.
			if (positions[dy][dx] == 1 || positions[dy][dx] == 2) {
				king(dy, dx);
			}
		}
	}
	
	
	/**
	 * Convert a piece to a king
	 * This method doesn't check the precondition.  It assumes that it is called from a method that did.
	 * @precondition
	 * 		Piece reached the appropriate end of the board.
	 * @param dy
	 * @param dx
	 */
	public void king(int dy, int dx) {
		if (positions[dy][dx] == 1) {
			positions[dy][dx] = 3;
		} else if (positions[dy][dx] == 2) {
			positions[dy][dx] = 4;
		}
	}
	
	/**
	 * Check if a move is legal for the calling BoardState.
	 * @param move
	 * @return
	 */
	public boolean isLegalMove(String move) {
		return BoardStateMove.isLegalMove(this, move);
	}

	/**
	 * Check if a second move is legal for the calling BoardState.
	 * @param move
	 * @param previousMove
	 * @return
	 */
	public boolean isLegalMove(String move, String previousMove) {
		return BoardStateMove.isLegalMove(this, move, previousMove);
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public boolean checkBlackVictory() {
		// Add a condition for no possible moves.
		
		
		for (int y = 0; y < Game.ROWS; y++) {
			for (int x = 0; x < Game.COLUMNS; x++) {
				if ((positions[y][x] == 2) || (positions[y][x] == 4)) {
					
					// System.out.println(positions[y][x]);
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean checkRedVictory() {
		for (int i = 0; i < Game.ROWS; i++) {
			for (int j = 0; j < Game.COLUMNS; j++) {
				if (positions[i][j] == 1 || positions[i][j] == 3) return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean checkVictory() {
		boolean black = checkBlackVictory();
		boolean red = checkRedVictory();
		
		return black || red;
	}
	
	/**
	 * Check if each side has only 1 piece.  This isn't the official definition of a draw for checkers, but it works well.
	 * @return
	 */
	public boolean checkDraw() {
		// I need to look up the official definitions of draws in the rules
		// For now, we will simply use each side has only 1 piece. 
		
		int player1Count = 0;
		int player2Count = 0;
		
		for (int y = 0; y < Game.ROWS; y++) {
			for (int x = 0; x < Game.COLUMNS; x++) {
				if ((this.positions[y][x] == 1) || (this.positions[y][x] == 3)) {
					player1Count++;
				} 

				if ((this.positions[y][x] == 2) || (this.positions[y][x] == 4)) {
					player2Count++;
				}
			}
		}
		
		if (player1Count > 1 || player2Count > 1) return false;
		else return true;
			
	}
	
	/**
	 * 
	 */
	public String toString() {
		String returnString = "";
		
		for (byte i = Game.ROWS - 1; i >= 0; i--) {
			for (byte j = 0; j < Game.COLUMNS; j++) {
				
				int charStartingPoint = 97;
				int charInt = charStartingPoint + (j * 2);

				char first = (char) charInt;
				char second = (char) (charInt + 1);

				String rowNum = String.valueOf(i + 1);

				String firstChar = String.valueOf(first);
				String secondChar = String.valueOf(second);

				// Logic to add the piece at this position.
				String pieceString = " " + String.valueOf(this.positions[i][j]) + " ";

				if (i%2 == 0) {
					returnString += (rowNum + firstChar + pieceString + rowNum + secondChar + "   " );
				} else {
					returnString += (rowNum + firstChar + "   " + rowNum + secondChar + pieceString);
				} 
			} 
			returnString += "\n";
		}
		return returnString;
	}
	
	/**
	 * 
	 */
	public void printState() {
		System.out.println(this.toString());
	}

	/**
	 * 
	 * @param py
	 * @param px
	 * @return
	 */
	public int whosePieceIsThis(int py, int px) {
		if (positions[py][px] == 2 || positions[py][px] == 4) {
			return 2;
		} else {
			return 1;
		}
	}

	/**
	 * 
	 * @return
	 */
	public double evaluate() {
		
		int player1Pieces = 0;
		int player1Kings = 0;
		int player2Pieces = 0;
		int player2Kings = 0;
		
		int pieceDifferential = 0;
		
		for (int y = 0; y < Test.ROWS; y++) {
			for (int x = 0; x < Test.COLUMNS; y++) {
				if (this.positions[y][x] == 1) {
					player1Pieces++;	
				}	
				
				else if (this.positions[y][x] == 2) {
					player2Pieces++;
				} else if (this.positions[y][x] == 3) {
					player1Kings++;
					
				} else if (this.positions[y][x] == 4){
					player2Kings++;	
				}
			}
		}
		
		double returnValue = 0;
		
		returnValue += (player1Pieces - player2Pieces);
		returnValue += (1.5) * (player1Kings - player2Kings);
		
		double jumpNum = BoardStateJumps.numJumps(this);
		
		returnValue += (jumpNum * 0.2);
		
		return returnValue;
		
		// Add a random small number addition to make sure that no two numbers are equal.
		
	}
	
}