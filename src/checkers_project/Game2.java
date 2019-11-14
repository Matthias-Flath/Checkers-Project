package checkers_project;

import java.util.Scanner;

public class Game2 {
	
	public static final byte ROWS = 8;
	public static final byte COLUMNS = 4;
	
	private BoardState board;
	private byte gameTurn;
	boolean win;
	
	public static void main(String[] args) {
		Game2 demo = new Game2();
		
	}

	// turn of 1 means player 1
	// turn of 2 means player 2
	public Game2() {
		
		// Create a game state with the proper starting pieces
		board = new BoardState();
		gameTurn = board.getTurn();
		
		// board.printState();
		
		win = false;
		
		/*
		 * while (win == false) { turn(); }
		 */
	}
	
	public void checkVictory() {
		if (this.gameTurn == 1) {
			if (board.checkBlackVictory()) {
				this.win = true;
				player1Victory();
			}
		} else {
			if (board.checkRedVictory()) {
				this.win = true;
				player2Victory();
			}
		} 
		
		if (board.checkDraw()) {
			this.draw();
		}
	}
	
	public void draw() {
		System.out.println("Draw!");
	}
	
	public void player1Victory() {
		System.out.println("Player 1 Wins!");
	}
	
	public void player2Victory() {
		System.out.println("Player 2 Wins!");
	}
	
	public void turn() {
		// Show the user the current state of the board
		System.out.println();
		board.printState();
		// System.out.println("Player " + this.gameTurn + "'s turn.");
		// System.out.println("Board State turn " + board.getTurn() + " turn");
		
		// Ask for the move of the player whose turn it is
		String moveString = getUserMove();
			
		// Check to make sure the move is legal
		boolean result = board.isLegalMove(moveString);
			
		if (result) {
			board.preCheckedMove(moveString);
			
			// I need to check if I already jumped before
			if (board.canJumpAtDestination(moveString)) {
				turn();
			} else {
				board.nextTurn();
				this.nextGameTurn();
			}
			
			
			
		} else {
			// If the move ins't legal, ask for a new move.
			turn();
		}
		
		// System.out.println(result);
		
		
		
		
	}
	
	public String getUserMove() {
		Scanner in = new Scanner(System.in);
		
		board.canJump();
		
		if (gameTurn == 1) {
			System.out.print("Black Turn. Please enter the start and end coordinates of your move: ");
		} else {
			System.out.print("Red Turn. Please enter the start and end coordinates of your move: ");
		}
		
		String s = in.nextLine();
		s.strip();

		return s;
	}
	
	public void nextGameTurn() {
		// System.out.println("We reached the next turn method.");
		if (this.gameTurn == 1) this.gameTurn = 2;
		else this.gameTurn = 1;
	}

	public void testMove(String moveString) {
		if (board.isLegalMove(moveString)) {
			board.preCheckedMove(moveString);
		}
		board.printState();
		System.out.println();
	}
	
}
