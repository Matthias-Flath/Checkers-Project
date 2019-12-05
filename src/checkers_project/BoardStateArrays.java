package checkers_project;

import java.util.ArrayList;
import java.util.Arrays;

import checkers_project.helpers.Game;
import checkers_project.helpers.TextConversions;

public class BoardStateArrays {
	
	
	
	/**
	 * Return an array of all the possible single depth move strings at the current BoardState.
	 * @param current
	 * @return
	 */
	public static String[] allLegalMovesArray(BoardState current) {
		ArrayList<String> allLegalMoves = allLegalMovesList(current);
		String[] allLegalMovesArray = allLegalMoves.toArray(new String[allLegalMoves.size()]);

		// Show me if there is a bug where this method returns an illegal move.
		printIllegalMoves(current, allLegalMovesArray);
		
		return allLegalMovesArray;
	}
	
	public static void printIllegalMoves(BoardState current, String[] array) {
		for (int a = 0; a < array.length; a++) {
			if (!BoardStateMove.isLegalMove(current, array[a])) {
				
				System.out.print("Illegal Move: ");
				System.out.println(array[a]);
			}
		}
	}
	
	/**
	 * Return an ArrayList of all the possible single depth moves at the current BoardState.
	 * For single depth moves only
	 * @param current
	 * @return
	 */
	public static ArrayList<String> allLegalMovesList(BoardState current) {
		
		// Second move checking is left to the getJumpMovesStringList method.
		
		if (BoardStateJumps.canJump(current)) {
			// System.out.println("Can jump");
			return getJumpMovesStringList(current);
		} else {
			// System.out.println("Can't jump");
			return getNonJumpMovesStringList(current);
		}
	}
	
	/**
	 * Return an ArrayList of all the possible jump moves at the current BoardState.
	 * @precondition
	 * 		Assumes that at least 1 jump is possible
	 * @param current
	 * @return
	 */
	public static ArrayList<String> getJumpMovesStringList(BoardState current) {

		// Needs to check if this is the second move and if so limit the jumping to only one location. 

		ArrayList<String> legalMoves = new ArrayList();

		// Iterate through the entire board
		for (int y = 0; y < Game.ROWS; y++) {
			for (int x = 0; x < Game.COLUMNS; x++) {

				// If the piece is the current player's and not a king
				if (current.positions[y][x] == current.getTurn()) {
					
					// Player 1
					if (current.positions[y][x] == 1) {
						legalMoves.addAll(jumpStringsAtLocation(current, y, x, 1));
					// Player 2
					} else {
						legalMoves.addAll(jumpStringsAtLocation(current, y, x, -1));
					}
					
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
		
		if (direction != 1 && direction != -1) {
			System.out.println("jumpStringsAtLocation was called with an invalid direction argument.");
			System.exit(0);
		}
		
		ArrayList<String> legalMoves = new ArrayList();
		
		// False means left
		String leftString = jumpStringInSpecificDirectionAtLocation(current, y, x, direction, false);
		if (leftString != null) legalMoves.add(leftString);
		
		// True means right
		String rightString = jumpStringInSpecificDirectionAtLocation(current, y, x, direction, true);
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
	 * 		True means right
	 * 		False means left
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
		System.out.println(Arrays.toString(allMoves));
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
	 * Used to find all legal second moves for the checkers engine
	 * @return
	 */
	public static String[] allLegalMovesAtLocation(BoardState current, String previousMove) {
		
		ArrayList<String> legalMovesAtLocation = new ArrayList();
		
		// Convert the previous move string to an int array
		int[] previousMoveArray = TextConversions.convertMoveStringToIntArray(previousMove);
		
		// Extract the destination coordinates
		int dy = previousMoveArray[2];
		int dx = previousMoveArray[3];
		
		
		if (current.positions[dy][dx] == 1) {
			legalMovesAtLocation.addAll(jumpStringsAtLocation(current, dy, dx, 1));
		} else if (current.positions[dy][dx] == 2) {
			// There is a bug in this line
			legalMovesAtLocation.addAll(jumpStringsAtLocation(current, dy, dx, -1));

		} else if ((current.positions[dy][dx] == 3) || (current.positions[dy][dx] == 4)) {
			legalMovesAtLocation.addAll(jumpStringsAtLocation(current, dy, dx, 1));
			legalMovesAtLocation.addAll(jumpStringsAtLocation(current, dy, dx, -1));
		}
		
		String[] allLegalMovesAtLocationArray = legalMovesAtLocation.toArray(new String[legalMovesAtLocation.size()]);
		return allLegalMovesAtLocationArray;
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

		int range = allMovesOfMultipleLevels.length;
		for (int i = 0; i < range; i++) {
			if (allMovesOfMultipleLevels == null) {
				System.out.println("There is a null value in the childStateArray.");
				System.exit(0);
			}
		}
				
		return allMovesOfMultipleLevels;
		
	}
	
 	// Bug in this and the below method
	/**
	 * Return an ArrayList of all possible end BoardStates after the current turn.  (Full depth moves) 
	 * @param current
	 * @return
	 */
	public static ArrayList<BoardState> possibleChildStates(BoardState current) {
		
		
		// Iterate through all the possible first moves
		ArrayList<BoardState> childStates = new ArrayList();
		String[] legalFirstMoves = allLegalMovesArray(current);
		
		// System.out.println(Arrays.toString(legalFirstMoves));
		
		int firstMoves = legalFirstMoves.length;
		
		// BoardState[] firstBoardStates = new BoardState[firstMoves];
		
		for (int i = 0; i < firstMoves; i++) {
			String firstMove = legalFirstMoves[i];
			BoardState temp =  new BoardState(current, legalFirstMoves[i]);
			
			if (temp.isSecondMovePossible() == false) {
				childStates.add(temp);
			} else {
			
				// Need to properly test this line
				childStates.addAll(possibleGrandchildStates(temp, firstMove));
			}
		}
	
		return childStates;
	}
	
	public static ArrayList<BoardState> possibleGrandchildStates(BoardState current, String previousMove) {
		
		// Iterate through all the possible first moves
		ArrayList<BoardState> childStates = new ArrayList<BoardState>();
		
		// List of legal second moves
		
		String[] legalSecondMoves = allLegalMovesAtLocation(current, previousMove);

		int moveNum = legalSecondMoves.length;

		for (int i = 0; i < moveNum; i++) {
			String secondMove = legalSecondMoves[i];

			BoardState temp =  new BoardState(current, secondMove);
			
			if (temp.isSecondMovePossible() == false) {
				childStates.add(temp);
			} else {

				childStates.addAll(possibleGrandchildStates(temp, secondMove));
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
