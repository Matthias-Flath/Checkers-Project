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
		
		switch(x) {
			case(0): 
				xLetter = "a";
				break;
			case(1): 
				xLetter = "b";
				break;
			case(2): 
				xLetter = "c";
				break;
			case(3):
				xLetter = "d";
				break;
			case(4): 
				xLetter = "e";
				break;
			case(5):
				xLetter = "f";
				break;
			case(6): 
				xLetter = "g";
				break;
			case(7):
				xLetter = "h";
				break;
			default:
				xLetter = "Error: this should never be reached.";
		}
		
		switch(y) {
		case(0): 
			yString = "1";
			break;
		case(1): 
			yString = "2";
			break;
		case(2): 
			yString = "3";
			break;
		case(3):
			yString = "4";
			break;
		case(4): 
			yString = "5";
			break;
		case(5):
			yString = "6";
			break;
		case(6): 
			yString = "7";
			break;
		case(7):
			yString = "8";
			break;
		default:
			yString = "Error:";
		}
		
		
		switch(y) {
		
		}
		
		
		if (((x+y)%2) == 0) {
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
		return yString + xLetter;
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
