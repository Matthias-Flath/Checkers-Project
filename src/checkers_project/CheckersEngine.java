package checkers_project;
/*
 * Takes a BoardState object and determines the optimal next move using the 
 * minmax search algorithm. 
*/

import java.util.Random;
import java.util.Arrays;

public class CheckersEngine {

	public static final int depth = 4;
	
	/**
	 * 
	 * @param bound
	 * @return
	 */
	public static int getRandomMove(int bound) {
		Random random = new Random();
		int move = random.nextInt(bound);
		return move;
		
	}
	
	/**
	 * 
	 * @param allPossibleMoves
	 * @return
	 */
	public static String getRandomMoveString(String[] allPossibleMoves) {
		
		int length = allPossibleMoves.length;  
		int randomIndex = getRandomMove(length);
		return allPossibleMoves[randomIndex];
	}

	/**
	 * 
	 * @param currentState
	 * @return
	 */
	public static BoardState minMaxMove(BoardState currentState) {
		
		System.out.println("Reached minMaxMove");
		
		BoardState[] allMoves = BoardStateArrays.possibleChildStatesArray(currentState);
		int numChildren = allMoves.length;
		
		TreeNode root = new TreeNode(currentState, numChildren);
		
		// Fill the tree
		fillTreeNode(root, depth);
		
		evaluatePositionLeaves(root);
		
		evaluateFilledTree(root);
		
		// Index with the best move
		int maxIndex = findBestMove(root);
		
		BoardState bestMove = allMoves[maxIndex];
		
		return bestMove;
		
	}
	
	/**
	 * Recursively fill the search tree
	 * @param root
	 * @param depth
	 */
	public static void fillTreeNode(TreeNode root, int depth) {
				
		BoardState[] allMoves = BoardStateArrays.possibleChildStatesArray(root.getTreeNodeData());
		
		// System.out.println(allMoves);
		System.out.println("Depth " + depth);
		
		int numChildren = allMoves.length;
		System.out.println("Number of children" + numChildren);

		if (depth > 0 ) {
			for (int i = 0; i < numChildren; i++) {
				int numSecondLevelChildren = BoardStateArrays.numPossibleChildren(allMoves[i]);
				
				TreeNode newNode;
				
				if (depth > 1) {
					newNode = new TreeNode(allMoves[i], numSecondLevelChildren);
				} else {
					newNode = new TreeNode(allMoves[i], 0);
				}

				// Assign the new node to the parent's children array at the appropriate position.
				root.children[i] = newNode;
				
				// Recursively add the next levels down
				if (depth > 1) {
					fillTreeNode(newNode, depth - 1); 
				}
			}
		} 
	}
	
	/**
	 * 
	 * @param root
	 */
	public static void evaluatePositionLeaves(TreeNode root) {
		
		System.out.println(root);
		
		if (root == null) {
			System.out.println("The calling root is equal to null");
		} else {
			if (root.children == null) {
				double evaluationScore = root.getTreeNodeData().evaluate();
				root.setPositionValue(evaluationScore);
			} else {
				
				for (int i = 0; i < root.getNumChildren(); i++) {
					evaluatePositionLeaves(root.children[i]);
				}
			}
		}
		
		
	}
	
	// This method needs to be recursive.
	public static void evaluateFilledTree(TreeNode root) {
		// All the leaves should be properly evaluated by now. 

		BoardState temp = root.getTreeNodeData();
		// temp.printState();
		
		System.out.println("Evaluate filled tree.");
		
		// This line has a bug in it
		if (root.boardPositionValue == 0) {
			// Iterate through all of the children
			
			int numChildren = root.getNumChildren();
			
			for (int i = 0; i < numChildren; i++) {
				evaluateFilledTree(root.children[i]);
			}
			
		}
		
		if (temp.getTurn() == 1) {
			int maxIndex = maxValueIndex(root);
			
			double value = root.children[maxIndex].boardPositionValue;
			System.out.println("Value " + value);
			root.setPositionValue(value);
		}

		if (root.getTreeNodeData().getTurn() == 2) {
			int minIndex = minValueIndex(root);
			root.setPositionValue(root.children[minIndex].boardPositionValue);
		}
	}

	
	
	/**
	 * 
	 * @param root
	 * @return
	 */
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
	
	/**
	 * 
	 * @param root
	 * @return
	 */
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
	
	/**
	 * 
	 * @param root
	 * @return
	 */
	public static int findBestMove(TreeNode root) {
		return maxValueIndex(root);
	}
	
}
