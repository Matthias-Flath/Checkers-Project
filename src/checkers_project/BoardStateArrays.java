package checkers_project;

import java.util.ArrayList;
import java.util.Arrays;

public class BoardStateArrays {
	
	/**
	 * Return an array of all the possible single depth moves at the current BoardState.
	 * @param current
	 * @return
	 */
	public static String[] allLegalMovesArray(BoardState current) {
		ArrayList<String> allLegalMoves = allLegalMovesList(current);
		String[] allLegalMovesArray = allLegalMoves.toArray(new String[allLegalMoves.size()]);
		return allLegalMovesArray;
	}
	
	/**
	 * Return an ArrayList of all the possible single depth moves at the current BoardState.
	 * For single depth moves only
	 * @param current
	 * @return
	 */
	public static ArrayList<String> allLegalMovesList(BoardState current) {
		
		// Needs to check if this is the second move and if so limit the jumping to only one location. 
		
		if (BoardStateJumps.canJump(current)) {
			return getJumpMovesStringList(current);
		} else {
			return getNonJumpMovesStringList(current);
		}
	}
	
	/**
	 * Return an ArrayList of all the possible non-jump moves at the current BoardState.
	 * @precondition
	 * 		Assumes that no jumps are possible.
	 * @param current
	 * @return
	 */
	public static ArrayList<String> getJumpMovesStringList(BoardState current) {

		ArrayList<String> legalMoves = new ArrayList();

		// Iterate through the entire board
		for (int y = 0; y < Game.ROWS; y++) {
			for (int x = 0; x < Game.COLUMNS; x++) {

				// If the piece is the current player's and not a king
				if (current.positions[y][x] == current.getTurn()) {
					// Code to figure out which direction they are going.
					
				// If the king is the current player's
				} else if (current.positions[y][x] == current.getTurn() + 2) {
					
					// Moving up the board
					legalMoves.addAll(jumpStringsAtLocation(current, y, x, 1));
					
					// Moving down the board
					legalMoves.addAll(jumpStringsAtLocation(current, y, x, -1));


					
				}
			}
		}
					
		
		return null; //legalMoves;
	}
	
	/**
	 * 
	 * @param current
	 * @param y
	 * @param x
	 * @param direction
	 * 		1 or -1 to represent which way we are moving rows on the board
	 * @return
	 */
	private static ArrayList<String> jumpStringsAtLocation(BoardState current, int y, int x, int direction) {
		
		// Do I need any checks as to whether positions[y][x] is used.
		
		ArrayList<String> legalMoves = new ArrayList();
		
		int player = current.whosePieceIsThis(y, x);

		int leftXToJumpOver;
		int rightXToJumpOver;

		// Check if the potential jump y value is on the board.
		boolean isYJumpLegal = TextConversions.isLegalY(y+ (2 * direction));
		if (isYJumpLegal == false) return legalMoves;
		
		// Right leaning rows
		if (y%2 == 1) {
			// 0 and +1 for the next row
			// - 1 and + 1 for the second row

			leftXToJumpOver = x;
			rightXToJumpOver = x + 1;

			if (TextConversions.isLegalX(leftXToJumpOver - 1)) {	
				if (BoardStateJumps.isOpposingPieceAt(current, player, y + direction, leftXToJumpOver)) {
					if (current.positions[y+(2 * direction)][x-1] == 0) {
						int[] array = {y, x, y+(2 * direction), x - 1 };
						String jump = TextConversions.convertIntArrayToString(array);
						legalMoves.add(jump);
					}
				}
			}

			if (TextConversions.isLegalX(rightXToJumpOver + 1)) {	
				if (BoardStateJumps.isOpposingPieceAt(current, player, y + direction, rightXToJumpOver)) {
					if (current.positions[y+(2 * direction)][x + 1] == 0) {
						int[] array = {y, x, y+(2 * direction), x + 1 };
						String jump = TextConversions.convertIntArrayToString(array);
						legalMoves.add(jump);
					}
				}
			}

		// Left leaning rows
		} else {
			// -1 and 0 for the next row
			// -1 and positive one

			leftXToJumpOver = x - 1;
			rightXToJumpOver = x;

			if (TextConversions.isLegalX(leftXToJumpOver)) {
				if (BoardStateJumps.isOpposingPieceAt(current, player, y+ direction, leftXToJumpOver)) {
					if (current.positions[y+(2 * direction)][x-1] == 0) {
						int[] array = {y, x, y+(2 * direction), x - 1 };
						String jump = TextConversions.convertIntArrayToString(array);
						legalMoves.add(jump);
					}
				}
			}

			if (TextConversions.isLegalX(rightXToJumpOver + 1)) {
				if (BoardStateJumps.isOpposingPieceAt(current, player, y+ direction, rightXToJumpOver)) {
					if (current.positions[y+(2 * direction)][x + 1] == 0) {
						int[] array = {y, x, y+(2 * direction), x + 1 };
						String jump = TextConversions.convertIntArrayToString(array);
						legalMoves.add(jump);
					}
				}
			}	
		}						

		return legalMoves;
	}
	
	public static ArrayList<String> getNonJumpMovesStringList(BoardState current) {
		ArrayList<String> legalMoves = new ArrayList();
		
		// Iterate through the entire board
		for (int y = 0; y < Game.ROWS; y++) {
			for (int x = 0; x < Game.COLUMNS; x++) {
				
				// If the piece is the current player's
				if (current.positions[y][x] == current.getTurn() || current.positions[y][x] == current.getTurn() + 2) {
					
					// Check for a single move by brute force.  (i = -1 & 1)
					for (int i = -1; i < 2; i+= 2) {
						if (BoardStateMove.isLegalMove(current, y, x, y + i, x + 1)) {
							
							// Create an int array representing the move.
							int[] legalMoveToAdd = {y, x, y + i, x + 1};
							
							// Convert the int array to a String.
							String legalMoveString = TextConversions.convertIntArrayToString(legalMoveToAdd);
							
							// Add the string to the ArrayList.
							legalMoves.add(legalMoveString);
							
						}
						
						if (BoardStateMove.isLegalMove(current, y, x, y + i, x)) {
							int[] legalMoveToAdd = {y, x, y + i, x};
							String legalMoveString = TextConversions.convertIntArrayToString(legalMoveToAdd);
							legalMoves.add(legalMoveString);
								
						}
						
						if (BoardStateMove.isLegalMove(current, y, x, y + i, x - 1)) {
							int[] legalMoveToAdd = {y, x, y + i, x - 1};
							String legalMoveString = TextConversions.convertIntArrayToString(legalMoveToAdd);
							legalMoves.add(legalMoveString);
						}
					}
				}
			}
		}
		
		return legalMoves;
	}
	
	
	/**
	 * Print out the string array of all legal single depth moves at this BoardState.
	 * @param current
	 */
	public static void displayAllLegalMoves(BoardState current) {
		// Get an array of all possible string moves
		
		// Print the entire array
	}

	

	
	
	/**
	 * Return the number of possible single depth moves at the current board state
	 * @param current
	 * @return
	 */
	public static int numLegalMoves(BoardState current) {
		return allLegalMovesArray(current).length;
	}
	
	/**
	 * Return the number of possible moves of full depth
	 * For use by the checkers engine
	 * @param current
	 * @return
	 */
	public static BoardState[] possibleChildStatesArray(BoardState current) {
		
		ArrayList<BoardState> childStates = possibleChildStates(current);
		// System.out.println(childStates);
		
		// System.out.println("finished created the arraylist");
		BoardState[] allMovesOfMultipleLevels =  childStates.toArray(new BoardState[childStates.size()]);
		
		// System.out.println(Arrays.toString(allMovesOfMultipleLevels));
		// System.out.println(allMovesOfMultipleLevels.length + " possibleChildStatesArray length");
		
		return allMovesOfMultipleLevels;
	}
	
	/**
	 * 
	 * @param current
	 * @return
	 */
	public static ArrayList<BoardState> possibleChildStates(BoardState current) {
		// Iterate through all the possible first moves
		ArrayList<BoardState> childStates = new ArrayList();
		String[] legalFirstMoves = allLegalMovesArray(current);
		
		// System.out.println(Arrays.toString(legalFirstMoves));
		
		int firstMoves = numLegalMoves(current);
		BoardState[] firstBoardStates = new BoardState[firstMoves];
		
		for (int i = 0; i < firstMoves; i++) {
			firstBoardStates[i] = new BoardState(current, legalFirstMoves[i]);
		}
		
		for (int j = 0; j < firstMoves; j++) {

			// If the new board state and the current board state are different, then the move has finished
			if (firstBoardStates[j].getTurn() != current.getTurn()) {
				childStates.add(firstBoardStates[j]);
			// Otherwise recursively find the endpoints of that move.
			} else {
				System.out.println("Found a jump turn");
				// System.exit(0);
				ArrayList<BoardState> nextLevel = possibleChildStates(firstBoardStates[j]);
				childStates.addAll(nextLevel);
			}
			
			 
			// check if move is the same a "this"
			
			// If not recursively call possibleChildStates
			
		}
	
		// Iterate through the array
		// Check if there are possible next jumps
		// If so call another method and add it to board state.
		
		
		
		
		return childStates;
	}
	
	/**
	 * 
	 * @param current
	 * @return
	 */
	public static int numPossibleChildren(BoardState current) {
		int numChildren = possibleChildStatesArray(current).length;
		
		return numChildren;
	}
	
	/**
	 * 
	 * @param firstMove
	 * @return
	 */
	public static ArrayList<BoardState> possibleSecondMoves(BoardState firstMove) {
		ArrayList<BoardState> childStates = new ArrayList();
		
		
		return null;
	}
	
	// has second jump method
	
	/**
	 * 
	 * @param current
	 * @return
	 */
	public static int[][] availableDestinations(BoardState current) {
		// This should be used to highlight legal moves in the GUI.
		// Then when the user clicks a specific piece, it shrinks to show only the destinations for that tile. 
		
		return null;
	}
	
	/**
	 * 
	 * @param current
	 * @return
	 */
	public static String[] getStringArrayOfPossibleMoves(BoardState current) {
		
		String[] allLegalMovesArray = allLegalMovesArray(current);
		
		System.out.println(Arrays.toString(allLegalMovesArray));
		
		return allLegalMovesArray;
	}
	
}
