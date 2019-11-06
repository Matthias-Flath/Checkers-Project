package checkers_project;
/*
Includes the main method.  Starts the game of checkers. 

*/
public class Game {

	public static final int PIECES_PER_PLAYER = 12;
	private int turn;
	private Board checkers;
	private ComputerPlayer computer;
	Player player1;
	
	
	public static void main(String[] args) {
		
		Game demo = new Game();
			
		demo.printBoardPositions();
	}
	
	public Game() {
		// The user goes first.
		this.turn = 1;
		
		// Create the objects to play the game
		this.checkers = new Board();
		this.computer = new ComputerPlayer();
		this.player1 = new Player();
	}
		
	public int getTurn() {
		return turn;
	}
	
	public void printBoardPositions() {
		
		// The 1X1 index is at the bottom left, so I need to start printing from the 
		// top left.
		
		for (int y = Board.COLUMNS - 1; y >= 0; y--) {
			for (int x = 0; x < Board.ROWS; x++) {
				 System.out.print(BoardPosition.getBoardPositionString(y, x) + " ");
			}		
			System.out.println();
		}
	}
	

	public String toString() {
		// The 1X1 index is at the bottom left, so I need to start printing from the 
				// top left.
				
				for (int y = Board.COLUMNS - 1; y >= 0; y--) {
					for (int x = 0; x < Board.ROWS; x++) {
						 System.out.print(BoardPosition.getBoardPositionString(y, x) + " ");
					}		
					System.out.println();
				}
		return "";
	}
	
}
