package checkers_project;

import java.util.LinkedList;

public class Controller {

	static LinkedList<String> moveStringHistory = new LinkedList();
	static LinkedList<BoardState> boardStateHistory = new LinkedList();
	
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
	
}

