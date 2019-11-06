package checkers_project;

public class BoardPosition {
	private final int xIndex;
	private final int yIndex;
	private final boolean playableSquare;
	private final String color;
	private final String xLetter;
	private final String yString;
	
	
	public BoardPosition(int x, int y) {
		this.xIndex = x;
		this.yIndex = y;
		
		this.xLetter = xString(x);
		this.yString = yString(y);
		
		if (checkPositionPlayability(x, y)) {
			playableSquare = true;
			color = "black";
		} else {
			playableSquare = false;
			color = "white";
		}
		
	}
	
	public String getBoardIndex() {
		String xString = String.valueOf(this.xIndex);
		String yString = String.valueOf(this.yIndex);
		
		return xString + yString;
	}
	
	public String getBoardPosition() {
		return  xLetter + yString;
	}
	
	public static String xString(int x) {
		
		switch(x) {
		case(0): 
			return "a";
		case(1): 
			return "b";
		case(2): 
			return "c";
		case(3):
			return "d";
		case(4): 
			return "e";
		case(5):
			return "f";
		case(6): 
			return "g";
		case(7):
			return "h";
		default:
			return "Error:";
		}	
	}
	
	/*
	 * Add a check here to make sure that the value of y is valid.
	 */
	public static String yString(int y) {
		return String.valueOf(y + 1);
	}
	
	public static String getBoardPositionString(int y, int x) {
		
		return yString(x) + xString(y);
		
	}
	
	public boolean getPlayability() {
		return this.playableSquare;
		
	}
	
	public static boolean checkPositionPlayability(int x, int y) {
		if (((x+y)%2) == 0) {
			return true;
		}
		return false;
	}

	
}
