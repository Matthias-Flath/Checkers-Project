package checkers_project;
/*
 * Takes a BoardState object and determines the optimal next move using the 
 * minmax search algorithm. 
*/

import java.util.Random;
import java.util.Arrays;

public class CheckersEngine {

	public static final int depth = 4;
	
	
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

	public static BoardState minMaxMove(BoardState currentState) {
		
		BoardState[] allMoves = currentState.possibleChildStatesArray();
		
		int numChildren = allMoves.length;
		
		TreeNode root = new TreeNode(currentState, numChildren);
		
		// Fill the tree
		fillTreeNode(root, depth);
		
		evaluatePositions(root);
		
		// Index with the best move
		int maxIndex = maxValueIndex(root);
		
		
		BoardState bestMove = allMoves[maxIndex];
		
		return bestMove;
		
	}
	// Recursively fill the search tree
	public static void fillTreeNode(TreeNode root, int depth) {
		
		BoardState[] allMoves = root.getTreeNodeData().possibleChildStatesArray();
		
		int numChildren = allMoves.length;
		
		if (depth > 0 ) {
			for (int i = 0; i < numChildren; i++) {
				int numSecondLevelChildren = BoardState.numPossibleChildren(allMoves[i]);
				
				TreeNode newNode = new TreeNode(allMoves[i], numSecondLevelChildren);

				if (depth > 1) {
					fillTreeNode(newNode, depth - 1); 
				}
			}
		} 
	}
	
	public static void evaluatePositions(TreeNode root) {
		// fill in the boardPositionValue of each TreeNode object.
		// byte turn = root.getTreeNodeData().getTurn();
		// System.out.println(turn);
		
		
		
		
		
		System.out.println(root);
		
		// Why would root ever be null
		
		// Oh I have to add functionality to deal with automatic second jumps.
		
		// I also have to deal with the change in moves during minmax search layers
		
		if (root.getNumChildren() == 0) {
			double evaluationScore = root.getTreeNodeData().evaluate();
			root.setPositionValue(evaluationScore);
		} else {
			
			for (int i = 0; i < root.getNumChildren(); i++) {
				evaluatePositions(root.children[i]);
			}
		}
		
		// All the leaves should be properly evaluated by now. 
		
		if (root.getTreeNodeData().getTurn() == 1) {
			int maxIndex = maxValueIndex(root);
			
			root.setPositionValue(root.children[maxIndex].boardPositionValue);
		}
		
		if (root.getTreeNodeData().getTurn() == 2) {
			int minIndex = minValueIndex(root);
			root.setPositionValue(root.children[minIndex].boardPositionValue);
		}
		
	}
	
	public static int minValueIndex(TreeNode root) {
		double minimum = 100;
		int minIndex = -1;
		
		for (int i = 0; i < root.getNumChildren(); i++) {
			if (root.children[i].boardPositionValue < minimum) {
				minimum = root.children[i].boardPositionValue;
				minIndex = i;
			}
		}
		
		return minIndex;
	}
	
	public static int maxValueIndex(TreeNode root) {
		double max = -100;
		int maxIndex = -1;
		
		for (int i = 0; i < root.getNumChildren(); i++) {
			if (root.children[i].boardPositionValue > max) {
				max = root.children[i].boardPositionValue;
				maxIndex = i;
			}
		}
		return maxIndex;
	}
	
	public static int findBestMove(TreeNode root) {
		return maxValueIndex(root);
	}
	
}
