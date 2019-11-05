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
		 for (int x = 0; x < ROWS; x++) {
			 for (int y = 0; y < COLUMNS; y++) {
				 this.positions[x][y] = new BoardPosition(x, y);
			 }
		 }
	}
	
	public String printGameState() {
		
		// The 1X1 index is at the bottom left, so I need to start printing from the 
		// top left.
		
		for (int y = COLUMNS - 1; y >= 0; y--) {
			for (int x = 0; x < ROWS; x++) {
				 System.out.print(positions[x][y].getBoardPosition() + " ");
			}		
			System.out.println();
		}
		
		

		return "";
	}
	
	
}

