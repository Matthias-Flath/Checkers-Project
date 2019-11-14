package checkers_project;

import java.util.Scanner;

public class Test {
	
	public static final byte ROWS = 8;
	public static final byte COLUMNS = 4;
	
	private BoardState board;
	private byte gameTurn;
	boolean end;
	
	int index = 0;
	
	String moveArray[] = {"3a 4b", "6d 5c", "4b 6d", "7c 5e", "3e 4f", "7e 6d", "2b 3a", "6f 5g", 
			"3g 4h", "5g 3e", "2d 4f", "5e 3g", "2h 4f", "6b 5a", "2f 3e", "8d 7c", "1c 2b", "8f 7e",
			"1e 2f", "6h 5g", "4h 6f", "6f 8d", "7g 6h", "8d 6b", "7a 5c", "3c 4b", "5a 3c", "2b 4d", 
			"4d 6b", "8h 7g", "3a 4b", "7g 6f", "2f 3g", "6f 5e", "1g 2f", "8b 7a", "6b 7c", "7a 6b",
			"7c 8d", "6b 5c", "8d 7e", "5c 3a", "7e 5c", "5e 4d", "5c 4b", "6h 5g", "4f 6h", "3a 2b",
			"1a 3c", "3c 5e"};
	
	
	
	public static void main(String[] args) {
		Test demo = new Test();
		
	}

	// turn of 1 means player 1
	// turn of 2 means player 2
	public Test() {
		
		// Create a game state with the proper starting pieces
		board = new BoardState();
		gameTurn = board.getTurn();
		
		// board.printState();
		
		end = false;
		
		while (end == false) { 
			turn(); 
			checkVictory();
		}
		 
	}
	
	public void checkVictory() {
		if (board.checkBlackVictory()) {
			this.end = true;
			player1Victory();
		}
	
		if (board.checkRedVictory()) {
			this.end = true;
			player2Victory();
		}
	 
		if (board.checkDraw()) {
			this.end = true;
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
		
		// I need to add the text for the can jump here
		board.printJumpRequirementString();
		String moveString = getUserMove();
			
		// Check to make sure the move is legal
		boolean result = board.isLegalMove(moveString);
		
		// System.out.println(result);
		
		if (result) {
			boolean kingAtStartingPoint = board.isKingAtStartingPoint(moveString);
			boolean isJump = TextConversions.isJump(moveString);
			
			board.preCheckedMove(moveString);
			// System.out.println("Successfully completed the move.");
			// I need to check if I already jumped before
			
			if (kingAtStartingPoint && isJump) {
				// System.out.println("Checking the king");
				
				
				if (board.canJumpAtDestinationForAKing(moveString)) {
					System.out.println("Can jump at destination for a King");
					secondTurn(moveString);
				} else {
					board.nextTurn();
					this.nextGameTurn();
				}
			
			} else if (!kingAtStartingPoint && isJump){
				// System.out.println("Is jump.");
				if (board.canJumpAtDestination(moveString)) {
					// System.out.println("Right before calling turn again.");
					secondTurn(moveString);
				} else {
					board.nextTurn();
					this.nextGameTurn();
				}
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
	
	public void secondTurn(String moveString) {
		// Show the user the current state of the board
		System.out.println();
		board.printState();

		int[] intArray = TextConversions.convertStringToIntArray(moveString);
		
		byte secondJump = board.canJump(intArray[2], intArray[3]);
		
		if (secondJump == 1) {
			System.out.println("There is only 1 jump available.");
		} else {
			System.out.println("There are  " + secondJump + " jumps available.  Choose your jump");
		}

		int dy = intArray[2];
		int dx = intArray[3];
		
		// Check to make sure it starts from the previous endpoint.

		
		moveString = getUserMove();
		
		if (board.checkSecondMoveStartingPoints(moveString, dy, dx) == false) {
			moveString = getUserMove();
		}
		
		
		// Check to make sure the move is legal
		boolean result = board.isLegalMove(moveString);
		
		// System.out.println(result);
		
		if (result) {
			boolean kingAtStartingPoint = board.isKingAtStartingPoint(moveString);
			boolean isJump = TextConversions.isJump(moveString);
			
			board.preCheckedMove(moveString);
			// System.out.println("Successfully completed the move.");
			// I need to check if I already jumped before
			
			if (kingAtStartingPoint && isJump) {
				// System.out.println("Checking the king");
				
				
				if (board.canJumpAtDestinationForAKing(moveString)) {
					System.out.println("Can jump at destination for a King");
					secondTurn(moveString);
				} else {
					board.nextTurn();
					this.nextGameTurn();
				}
			
			} else if (!kingAtStartingPoint && isJump){
				// System.out.println("Is jump.");
				if (board.canJumpAtDestination(moveString)) {
					// System.out.println("Right before calling turn again.");
					secondTurn(moveString);
				} else {
					board.nextTurn();
					this.nextGameTurn();
				}
			} else {
				board.nextTurn();
				this.nextGameTurn();
			}
			
		} else {
			// If the move ins't legal, ask for a new move.
			secondTurn(moveString);
		}
		
		
		
		
	}
	
	
	
	public String getUserMove() {
		while (index < moveArray.length) {
			index++;
			
			if (gameTurn == 1) {
				System.out.print("Black Turn. Please enter the start and end coordinates of your move: ");
			} else {
				System.out.print("Red Turn. Please enter the start and end coordinates of your move: ");
			}
			
			System.out.print(moveArray[index -1]);
			System.out.println();
			return moveArray[index -1];
			
			
		}
		
		Scanner in = new Scanner(System.in);
		
		// board.canJump();
		
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

}
