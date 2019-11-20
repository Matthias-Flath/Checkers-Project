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
	 */
	public BoardState(BoardState current, String move) {
				
		// System.out.println("Original " + current);
		
		this.positions = current.positions.clone();
		this.turn = current.turn;
		
		if (BoardStateJumps.canJumpAtDestination(current, move)) {
			this.secondMove = true;
		} else {
			this.nextTurn();
		}
		
		// System.out.println("Cloned " + this);
		
		// Call a check method to ensure that it is indeed a safe move.
		
		this.preCheckedMove(move);
		
		// System.out.println(move);
		// System.out.println("Cloned and moved " + this);
		
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
	 * 
	 * @return
	 */
	public byte getTurn() {
		return this.turn;
	}
	
	/**
	 * 
	 */
	public void nextTurn() {
		
		this.secondMove = false;
		
		// System.out.println("We reached the next turn method.");
		if (this.turn == 1) {
			
			this.turn = 2;
		} else {
			this.turn = 1;
		}
	}

	public boolean isSecondMovePossible() {
		return this.secondMove;
	}
	
	
	// Create another BoardState constructor that takes an int array (might be a lot faster)
	
	// Doesn't actually change the turn, simply moves the piece
	/**
	 * 
	 * @param move
	 */
	public void preCheckedMove(String move) {
		// System.out.println(move);
		int[] fromAndTo = TextConversions.convertMoveStringToIntArray(move);	
		System.out.println(Arrays.toString(fromAndTo));
		preCheckedMove(fromAndTo[0], fromAndTo[1], fromAndTo[2], fromAndTo[3]);
	}
	
	/**
	 * 
	 * @param py
	 * @param px
	 * @param dy
	 * @param dx
	 */
	public void preCheckedMove(int py, int px, int dy, int dx) {
		
		// System.out.println(px);
		// System.out.println(dx);
		
		
		
		if ((dy - py) == 1 || (dy - py) == -1) {
			// System.out.println("Reached the single move area");
			
			this.positions[dy][dx] = this.positions[py][px];
			// System.out.println(dy);
			// System.out.println(dx);
			// System.out.println(positions[dy][dx]);
			
			this.positions[py][px] = 0;
			
			// System.out.println("After the move method");
			// System.out.println(this);
			
			
		} else {
			// Rules for jumps
			
			int direction = (dy - py) / 2;
			
			if ((py % 2) == 1) {
				// Right leaning rows
				
				if (direction == 1) {
					// Move the piece to the new location
					positions[dy][dx] = positions[py][px];
					positions[py][px] = 0;
					
					// Eliminate the piece jumped over
					if ((dx - px) == direction) {
						positions[py+direction][px + direction] = 0;
					} else {
						// Eliminate the piece jumped over
						positions[py+direction][px] = 0;
					}
				} else {
					// Move the piece to the new location
					positions[dy][dx] = positions[py][px];
					positions[py][px] = 0;
					
					// Eliminate the piece jumped over
					if ((dx - px) == direction) {
						positions[py+direction][px] = 0;
					} else {
						// Eliminate the piece jumped over
						positions[py+direction][px + direction] = 0;
					}
				}
				
				
			} else {
				// left leaning rows
				
				if (direction == 1) {
					// Move the piece to the new location
					positions[dy][dx] = positions[py][px];
					positions[py][px] = 0;
					
					// Eliminate the piece jumped over
					if ((dx - px) == direction) {
						positions[py+direction][px] = 0;
					} else {
						// Eliminate the piece jumped over
						positions[py+direction][px + direction] = 0;
					}
				} else {
					// Move the piece to the new location
					positions[dy][dx] = positions[py][px];
					positions[py][px] = 0;
					
					// Eliminate the piece jumped over
					if ((dx - px) == direction) {
						positions[py+direction][px + direction] = 0;
					} else {
						// Eliminate the piece jumped over
						positions[py+direction][px] = 0;
					}
				}
			}
		}
		
		
		
		if (dy == 0 || dy == 7) {
			if (positions[dy][dx] == 3 || positions[dy][dx] == 4) {
				king(dy, dx);
			}
		}
		
	}
	
	/**
	 * 
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
	 * 
	 * @param move
	 * @return
	 */
	public boolean isLegalMove(String move) {
		return BoardStateMove.isLegalMove(this, move);
	}
	

	/**
	 * 
	 * @return
	 */
	public boolean checkBlackVictory() {
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
	 * 
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

