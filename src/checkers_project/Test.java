package checkers_project;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * This class should only be used for console based input
 * Create a subclass (or something of that variety to change userTurn() for the GUI.
 * @author computer
 *
 */
public class Test {
	
	public static final byte ROWS = 8;
	public static final byte COLUMNS = 4;
	
	private BoardState board;
	private byte gameTurn;
	boolean end;
	boolean multiplayer;
	
	private LinkedList<String> moveStringHistory = new LinkedList();
	private LinkedList<BoardState> boardStateHistory = new LinkedList();
	
	// Eliminate this after testing is complete.
	int index = 0;
	
	// This array is used to test both sides of the game (without an engine)
	String moveArray[] = {"3a 4b", "6d 5c", "4b 6d", "7c 5e", "3e 4f", "7e 6d",
			"2b 3a", "6f 5g", "3g 4h", "5g 3e", "2d 4f", "5e 3g", "2h 4f", "6b 5a",
			"2f 3e", "8d 7c", "1c 2b", "8f 7e", "1e 2f", "6h 5g", "4h 6f", "6f 8d",
			"7g 6h", "8d 6b", "7a 5c", "3c 4b", "5a 3c", "2b 4d", "4d 6b", "8h 7g",
			"3a 4b", "7g 6f", "2f 3g", "6f 5e", "1g 2f", "8b 7a", "6b 7c", "7a 6b",
			"7c 8d", "6b 5c", "8d 7e", "5c 3a", "7e 5c", "5e 4d", "5c 4b", "6h 5g",
			"4f 6h", "3a 2b", "1a 3c", "3c 5e"};
	
	/*
	 * Invariant of the Test ADT
	 * turn of 1 means player 1
	 * turn of 2 means player 2
	 * 
	 */
	
	/**
	 * Start the game
	 * @param args
	 */
	public static void main(String[] args) {
		Test demo = new Test();
	}
 
	/**
	 * Create a game object (for testing)
	 */
	public Test() {
		
		// Create a game state with the proper starting pieces
		this.board = new BoardState();
		this.gameTurn = board.getTurn();
		this.multiplayer = true;
		this.end = false;
		
		// Add the starting position to the boardStateHistory
		
		// Run the game
		while (end == false) { 
			
			// Add some stuff about no move win conditions here.
			
			oneGameTurn(); 
			checkVictory();
		} 		 
	}
	
	public String getPreviousMove() {
		// Do I need to do any checking to make sure it isn't null?
		return this.moveStringHistory.peek();
	}
	
	// Win conditions
	
	/**
	 * 
	 */
	public void checkVictory() {
		// Add checking for no possible moves.
		
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
	
	/**
	 * 
	 */
	public void draw() {
		System.out.println("Draw!");
	}
	
	/**
	 * 
	 */
	public void player1Victory() {
		System.out.println("Player 1 Wins!");
	}
	
	/**
	 * 
	 */
	public void player2Victory() {
		System.out.println("Player 2 Wins!");
	}
	
	// Game turns
	
	/**
	 * 
	 */
	public void oneGameTurn() {
		
		if (this.gameTurn == 1) {
			userTurn();
	
			// potentially add win conditions here
			
		} else {
			if (multiplayer) userTurn();
			else computerTurn();
			
			// And here
		}
	}
	
	/**
	 * 
	 */
	public void userTurn() {
		
		// Show the user the current state of the board
		System.out.println();
		board.printState();

		// List all legal moves for the user
		// BoardStateArrays.displayAllLegalMoves(board);
		
		// Tell the user if they need to jump
		// BoardStateJumps.printJumpRequirementString(board);
		
		// Get a move string from the user
		String moveString = getUserMove();
		
		// Check to make sure the move is legal
		boolean result;
		
		if (board.isSecondMovePossible()) {
			System.out.println("Possible second move");
			result = board.isLegalMove(moveString, this.getPreviousMove());
		} else {
			// System.out.println("Current turn:" + this.gameTurn);
			result = board.isLegalMove(moveString);
			// System.out.println("Current board turn" + this.board.getTurn());
		
		}
		
		System.out.println(result);
		
		if (result) {
			// Add the pre-move state to the boardHistory
			this.boardStateHistory.add(board);
			
			// Add the moveString to the history
			this.moveStringHistory.add(moveString);
			
			// Set the second turn value
			board.setSecondMove(BoardStateJumps.canJumpAtDestination(board, moveString));
						
			// Actually move the piece
			board.preCheckedMove(moveString);
			
			// Move to the next single depth turn
			this.nextGameTurn();
			
		} else {
			// If the move ins't legal, ask for a new move.
			userTurn();
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUserMove() {
		// The while loop is only here for integration testing
		while (index < 0) { // moveArray.length
			index++;

			if (gameTurn == 1) { 
				System.out.print("Black Turn. Please enter the start and end coordinates of your move: "); 
			} else { 
				System.out.print("Red Turn. Please enter the start and end coordinates of your move: ");
			}

			System.out.print(moveArray[index -1]); 
			System.out.println(); 
			
			return moveArray[index - 1];

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
	
	
	/**
	 * 
	 */
	public void computerTurn() {
		BoardState nextState = getOpponentMove();
		this.board = nextState;
		this.gameTurn = nextState.getTurn();
		board.printState();
	}


	/**
	 * 
	 * @return
	 */
	public BoardState getOpponentMove() {
		
		BoardState move = null;
		
		if (!multiplayer) {
			// Random Move
			// move = CheckersEngine.getRandomMoveString(getStringArrayOfPossibleMoves());
			
			// System.out.println(this.gameTurn);

			// Minmax move
			move = CheckersEngine.minMaxMove(this.board);
			
		} else {
			// Call the methods from the sockets classes
		}
		
		return move;
		
	}
	
	/**
	 * Checks if a secondTurn is possible.  If so, calls oneGameTurn() again.
	 * If not, switches the turn. 
	 */
	public void nextGameTurn() {
		// Deal with the secondMove problem here
		
		if (board.isSecondMovePossible()) {
			this.oneGameTurn();
		} else {
			// Move to the next turn
			this.gameTurn = (byte) ((this.gameTurn == 1) ? 2 : 1); 
		}
	}

}


