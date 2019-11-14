package checkers_project;

public class TreeNode {

	BoardState current;
	TreeNode parent;
	TreeNode rightSibling;
	
	
	public TreeNode(BoardState current) {
		this.current = current;
		this.rightSibling = null;
	}
}
