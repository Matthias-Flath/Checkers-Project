package checkers_project;

import java.util.ArrayList;
import java.util.Arrays;

public class BoardStateArrays {
	
	// This entire thing needs to be rebuilt
	
	// elimintate the 
	
	public static ArrayList<String> getAvailableJumpMoves(BoardState current) {
		
		return null; //legalMoves;
	}
	
	// This method needs to be fixed
	public static void displayAllLegalMoves(BoardState current) {
		//this.canJump();

		// I need to add something here for the double jump or triple jump.
		// Add something here for the regular moves. 

		// Replace the for each with a simple print statement
	}


	public static void displayAllLegalMovesSecondJump(BoardState current, String moveString) {
		// This method uses the string from the previous jump
		// The destination must be the same as all the new starting points.
		
		String destinationString = moveString.substring(3, 5);
		
		// System.out.println(destinationString);
		
		// legalMoves.forEach((n) -> checkSecondLegalMoves(destinationString, n)); 
				
		// legalMoves.forEach((m) -> System.out.println(m));
		
	}
	

	public static void addAvailableNonJumpMoves(BoardState current) {
		ArrayList<String> legalMoves = new ArrayList();
		
		for (int y = 0; y < Game.ROWS; y++) {
			for (int x = 0; x < Game.COLUMNS; x++) {
				if (current.positions[y][x] == current.getTurn() || current.positions[y][x] == current.getTurn() + 2) {
					// Check for a single move by brute force.  
					
					for (int i = -1; i < 2; i+= 2) {
						if (BoardStateMove.isLegalMove(current, y, x, y + i, x + 1)) {
							// System.out.println("We found a success.");
							int[] legalMoveToAdd = {y, x, y + i, x + 1};
							String legalMoveString = TextConversions.convertIntArrayToString(legalMoveToAdd);
							legalMoves.add(legalMoveString);
							
						}
						
						if (BoardStateMove.isLegalMove(current, y, x, y + i, x)) {
							// System.out.println("We found a success.");

							int[] legalMoveToAdd = {y, x, y + i, x};
							String legalMoveString = TextConversions.convertIntArrayToString(legalMoveToAdd);
							legalMoves.add(legalMoveString);
								
						}
						
						if (BoardStateMove.isLegalMove(current, y, x, y + i, x - 1)) {
							// System.out.println("We found a success.");

							int[] legalMoveToAdd = {y, x, y + i, x - 1};
							String legalMoveString = TextConversions.convertIntArrayToString(legalMoveToAdd);
							legalMoves.add(legalMoveString);
							
						}
					}
				}
			}
		}
	}
	
	public static ArrayList<String> allLegalMoves(BoardState current) {
		
		/*
		 * legalMoves.clear();
		 * 
		 * if (canJump() != 0) { return getAvailableJumpMoves(); } else {
		 * addAvailableNonJumpMoves(); }
		 */
		
		
		// Should simply return this information 
		// return legalMoves;
		
		// iterate through the positions array and then run a method to determine each legal move for each piece of that player. 
		
		// Concatenate all of them and return the list. 
		
		// Use this list to create the tree. 
		
		return null;
		
	}

	public static String[] allLegalMovesArray(BoardState current) {
		ArrayList<String> allLegalMoves = allLegalMoves(current);
		
		String[] allLegalMovesArray = allLegalMoves.toArray(new String[allLegalMoves.size()]);
		
		return allLegalMovesArray;
	}
	
	public static int numLegalMoves(BoardState current) {
		return allLegalMovesArray(current).length;
	}
	
	public static BoardState[] possibleChildStatesArray(BoardState current) {
		
		ArrayList<BoardState> childStates = possibleChildStates(current);
		// System.out.println(childStates);
		
		// System.out.println("finished created the arraylist");
		BoardState[] allMovesOfMultipleLevels =  childStates.toArray(new BoardState[childStates.size()]);
		
		// System.out.println(Arrays.toString(allMovesOfMultipleLevels));
		// System.out.println(allMovesOfMultipleLevels.length + " possibleChildStatesArray length");
		
		return allMovesOfMultipleLevels;
	}
	
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
	
	public static int numPossibleChildren(BoardState current) {
		int numChildren = possibleChildStatesArray(current).length;
		
		return numChildren;
	}
	

	public static ArrayList<BoardState> possibleSecondMoves(BoardState firstMove) {
		ArrayList<BoardState> childStates = new ArrayList();
		
		
		return null;
	}
	
	// has second jump method
	

	public static int[][] availableDestinations(BoardState current) {
		// This should be used to highlight legal moves in the GUI.
		// Then when the user clicks a specific piece, it shrinks to show only the destinations for that tile. 
		
		return null;
	}
	
	public static String[] getStringArrayOfPossibleMoves(BoardState current) {
		
		String[] allLegalMovesArray = allLegalMovesArray(current);
		
		System.out.println(Arrays.toString(allLegalMovesArray));
		
		return allLegalMovesArray;
	}
	
}
