package checkers_project;
/*
ADT for the checkers board.   Holds an 8x8 matrix of board positions and what is
on each position. 

*/

public class Board {

	public static final int ROWS = 8;
	public static final int COLUMNS = 8;
	
	BoardPosition positions[][] = new BoardPosition[8][8];
	
	public Board() {
		 for (int x = 0; x < COLUMNS; x++) {
			 for (int y = 0; y < ROWS; y++) {
				 this.positions[x][y] = new BoardPosition(x, y);
			 }
		 }
	}
	
	
	// For debugging
	public void printBoardPositions() {
		
		// The 1X1 index is at the bottom left, so I need to start printing from the 
		// top left.
		
		for (int y = COLUMNS - 1; y >= 0; y--) {
			for (int x = 0; x < ROWS; x++) {
				 System.out.print(positions[x][y].getBoardPosition() + " ");
			}		
			System.out.println();
		}
	}
	
	/*
	 * Check if the tile can have a checkers piece on it. 
	 * 
	 */
	public boolean checkPositionPlayability(int x, int y) {
		if (x < COLUMNS && x >= 0 && y < ROWS && y >= 0) {
			return positions[x][y].getPlayability();
		} else {
			throw new IllegalArgumentException("You must have proper indexes for the board"); 
		}
	}
	
	
}

