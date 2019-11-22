package checkers_project;

public class TreeNode {

	BoardState current;
	TreeNode[] children;
	double boardPositionValue;
	
	/**
	 * 
	 * @param current
	 * @param numChildren
	 */
	public TreeNode(BoardState current, int numChildren) {
		this.current = current;
		this.children = new TreeNode[numChildren];
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public TreeNode getChildAtIndex(int index) {
		// if index is valid, return TreeNode
		if (children == null) {
			System.out.println("There is no child at this index.");
			System.exit(0);
		}
		return children[index];
	}
	
	/**
	 * 
	 * @return
	 */
	public BoardState getTreeNodeData() {
		return this.current;
	}
	
	/**
	 * 
	 * @param index
	 * @param newNode
	 */
	public void setChild(int index, TreeNode newNode) {
		this.children[index] = newNode;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getNumChildren() {
		if (this.children == null) {
			return 0;
		}
		
		return children.length;
	}
	
	/**
	 * 
	 * @param value
	 */
	public void setPositionValue(double value) {
		this.boardPositionValue = value;
	}
	
	/**
	 * 
	 * @return
	 */
	public double getPositionValue() {
		return this.boardPositionValue;
	}
	
	public String toString() {
		if (this.current == null) {
			System.out.println("This node's BoardState is equal to null");
			System.exit(0);
		}
		
		if (this.children == null) {
			System.out.println("This node has no children");
		} else {
			System.out.println("Board position value" + this.boardPositionValue);
		}
		
		
		
		return "TreeNode toString";
	}
}
