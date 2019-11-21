package checkers_project;

import java.util.ArrayList;
import java.util.Arrays;

public class BoardStateArrays {
	
	/**
	 * Return an array of all the possible single depth move strings at the current BoardState.
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
	 * 		Assumes that at least 1 jump is possible
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
		return legalMoves;
	}
	
	/**
	 * Returns a list of strings of all possible jumps at that location.
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
		
		String leftString = jumpStringInSpecificDirectionAtLocation(current, y, x, direction, false);
		if (leftString != null) legalMoves.add(leftString);
		
		String rightString = jumpStringInSpecificDirectionAtLocation(current, y, x, direction, false);
		if (rightString != null) legalMoves.add(rightString);
		
		return legalMoves;
	}
	
	/**
	 * Determine if a specific jump is possible. 
	 * Given a starting point, vertical and horizontal direction to jump
	 * @param current
	 * @param y
	 * @param x
	 * @param direction
	 * @param right
	 * 		This is the same regardless of which vertical direction the piece is moving on the board.
	 * @return
	 * 		String describing the possible jump move (chess notation)
	 */
	public static String jumpStringInSpecificDirectionAtLocation(BoardState current, int y, int x, int direction, boolean right) {
		
		boolean jumpLegality = BoardStateJumps.canJumpInSpecificDirectionAtLocation(current, y, x, direction, right);
		if (!jumpLegality) return null;
		
		int xToLandOn =  (right) ? x + 1 : x - 1;
		int[] array = {y, x, y + (2 * direction), xToLandOn };
		String jump = TextConversions.convertIntArrayToString(array);
		
		return jump;
	}
	
	/**
	 * Return an ArrayList of all the possible non-jump moves at the current BoardState.
	 * Assumes that no jump moves are possible.
	 * @param current
	 * @return
	 */
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
		String[] allMoves = allLegalMovesArray(current);
		System.out.println(Arrays.deepToString(allMoves));
	}
	
	/**
	 * Return the number of possible single depth moves at the current board state
	 * @param current
	 * @return
	 */
	public static int numLegalMoves(BoardState current) {
		return allLegalMovesArray(current).length;
	}
	
	
	// Possible Child State
	// The below methods are for the checkers engine. 
	
	/**
	 * Returns an array of all possible end BoardStates after the current turn.  (Full depth moves)
	 * For use by the checkers engine
	 * @param current
	 * @return
	 */
	public static BoardState[] possibleChildStatesArray(BoardState current) {
		
		ArrayList<BoardState> childStates = possibleChildStates(current);
		BoardState[] allMovesOfMultipleLevels =  childStates.toArray(new BoardState[childStates.size()]);

		return allMovesOfMultipleLevels;
	}
	
	/**
	 * Return an ArrayList of all possible end BoardStates after the current turn.  (Full depth moves) 
	 * @param current
	 * @return
	 */
	public static ArrayList<BoardState> possibleChildStates(BoardState current) {
		
		// Iterate through all the possible first moves
		ArrayList<BoardState> childStates = new ArrayList();
		String[] legalFirstMoves = allLegalMovesArray(current);
		
		int firstMoves = numLegalMoves(current);
		
		// BoardState[] firstBoardStates = new BoardState[firstMoves];
		
		for (int i = 0; i < firstMoves; i++) {
			BoardState temp =  new BoardState(current, legalFirstMoves[i]);
			
			if (temp.isSecondMovePossible() == false) {
				childStates.add(temp);
			} else {
				
				childStates.addAll(possibleChildStates(temp));
			}
		}
	
		return childStates;
	}
	
	/**
	 * Return the number possible BoardStates after this turn. 
	 * This will be used to determine the number of children in a TreeNode.
	 * @param current
	 * @return
	 */
	public static int numPossibleChildren(BoardState current) {
		int numChildren = possibleChildStatesArray(current).length;
		return numChildren;
	}
	
}
