package checkers_project;

public class TreeNode {

	BoardState current;
	TreeNode[] children;
	int depth;
	int boardPositionValue;
	
	
	public TreeNode(BoardState current, int numChildren) {
		this.current = current;
		this.children = new TreeNode[numChildren];
	}
	
	public TreeNode getChildAtIndex() {
		// if index is valid, return TreeNode
		
		return null;
		
	}
	
	public BoardState getTreeNodeData() {
		return this.current;
	}
	
	public void setChild(int index, TreeNode newNode) {
		children[index] = newNode;
	}
	
	
}
