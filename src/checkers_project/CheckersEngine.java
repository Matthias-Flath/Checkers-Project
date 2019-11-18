package checkers_project;
/*
 * Takes a BoardState object and determines the optimal next move using the 
 * minmax search algorithm. 
*/

import java.util.Random;

public class CheckersEngine {

	public static final byte depth = 4;
	
	
	public static int getRandomMove(int bound) {
		Random random = new Random();
		int move = random.nextInt(bound);
		return move;
		
	}
	
	public static String getRandomMoveString(String[] allPossibleMoves) {
		
		int length = allPossibleMoves.length;  
		
		int randomIndex = getRandomMove(length);
		
		return allPossibleMoves[randomIndex];
	}

}
