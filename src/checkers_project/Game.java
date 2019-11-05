package checkers_project;
/*
Includes the main method.  Starts the game of checkers. 

*/
public class Game {

	public static final int PIECES_PER_PLAYER = 12;
	
	public static void main(String[] args) {
		Board checkers = new Board();
		
		ComputerPlayer computer = new ComputerPlayer();
		Player player1 = new Player();
		
		String a = checkers.printGameState();
	}
}
