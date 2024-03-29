package checkers_project.helpers;

import java.util.LinkedList;

import checkers_project.BoardState;
import checkers_project.engine.CheckersEngine;

public class Controller {

	public static LinkedList<String> moveStringHistory = new LinkedList();
	public static LinkedList<BoardState> boardStateHistory = new LinkedList();
	private BoardState board;

	public static String getPreviousMove() {
		// Do I need to do any checking to make sure it isn't null?
		return Controller.moveStringHistory.peek();
	}

	public static void computerTurn(BoardState boardState) {
		BoardState nextState = getOpponentMove(boardState);
		boardState = nextState;
	}

	public static BoardState getOpponentMove(BoardState boardState) {
		BoardState move = null;
		move = CheckersEngine.minMaxMove(boardState);
		return move;

	}

	public static boolean checkVictory(BoardState board) {
		// Add checking for no possible moves.
		boolean end = false;
		if (board.checkBlackVictory()) {
			end = true;
			
		}
	
		if (board.checkRedVictory()) {
			end = true;
			
		}
	 
		if (board.checkDraw()) {
			end = true;
			

		}
		return end;
	}
	
	/**
	 * 
	 */
	public static void draw() {
	//	System.out.println("Draw!");
	}
	
	/**
	 * 
	 */
	public static void player1Victory() {
	//	System.out.println("Player 1 Wins!");
		
	}
	
	/**
	 * 
	 */
	public static void player2Victory() {
	//	System.out.println("Player 2 Wins!");
	}
	
}
