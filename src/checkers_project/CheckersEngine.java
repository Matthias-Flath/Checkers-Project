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

	public static String minMaxMove(BoardState currentState) {
		
		int numChildren = currentState.numLegalMoves();
		
		TreeNode root = new TreeNode(currentState, numChildren);
		
		// Fill the tree
		fillTreeNode(root, depth);
		
		
		
		
		
		
		
		
		return "";
		
		
		
	}
	
	// Recursively fill the search tree
	
	public static void fillTreeNode(TreeNode root, int depth) {
				
		int numChildren = root.getTreeNodeData().numLegalMoves();
		String[] moves = root.getTreeNodeData().allLegalMovesArray();
		
		for (int i = 0; i < numChildren; i++) {
			BoardState newState = new BoardState(root.getTreeNodeData(), moves[i]);
			
			// Add a depth check here
			int childrenOfChildNum = 0;
			
			if (depth > 0) {
				childrenOfChildNum = newState.numLegalMoves();
			}
			
			TreeNode newNode = new TreeNode(newState, childrenOfChildNum);
			root.setChild(i, newNode);

			if (depth > 0) {
				fillTreeNode(newNode, depth - 1);
			}
		}
		
	}
	
}
