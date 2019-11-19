package checkers_project;

public class TreeNode {

	BoardState current;
	TreeNode[] children;
	int depth;
	double boardPositionValue;
	
	
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
		this.children[index] = newNode;
	}
	
	public int getNumChildren() {
		return children.length;
	}
	
	public void setPositionValue(double value) {
		this.boardPositionValue = value;
	}
	
	public double getPositionValue() {
		return this.boardPositionValue;
	}
	
	
}
