package checkers_project;

public class TextConversions {
	
	
	public static boolean isLegalX(int x) {
		if (x >= 0 && x < Game2.COLUMNS) {
			return true;
		}
		return false;
	}

	public static boolean isLegalY(int y) {
		if (y < Game2.ROWS && y >= 0) {
			return true;
		}
		return false;
	}
	
	public static boolean checkOnBoard(int y, int x) {
		// Y is listed first because a chess board is listed as 1a where 1 is the y axis and a is the x axis.
		
		boolean onBoard = true;
		if (y < 0 || y > 7 ) onBoard = false;
		if (x < 0 || x > 3) onBoard = false;
		return onBoard;
	}
	
	// This isn't a row
	public static int convertLetterToColumnNumber(String letter) {
		switch (letter) {
		case "a":
			return 0;
		case "b":
			return 1;
		case "c":
			return 2;
		case "d":
			return 3;
		case "e":
			return 4;
		case "f":
			return 5;
		case "g":
			return 6;
		case "h":
			return 7;
		default:
			return -1;
		}		
	}
	
	public static String convertColumnNumberToLetter(int y, int x) {
		
		if (y%2 == 1) { 
			// Right leaning rows
			return convertNumberToLetter(x * 2 + 1);
			
		} else {
			// Left leaning rows
			return convertNumberToLetter(x * 2);
		}
	}
	
	public static String convertNumberToLetter(int number) {
		switch (number) {
		case 0:
			return "a";
		case 1:
			return "b";
		case 2:
			return "c";
		case 3:
			return "d";
		case 4:
			return "e";
		case 5:
			return "f";
		case 6:
			return "g";
		case 7:
			return "h";
		default:
			System.exit(0); 
			return "";
		}	
	}
	

	public static int[] convertStringArrayToIntArray(String piece, String destination) {		
				
		String pieceXString = piece.substring(1, 2);
		// System.out.println(pieceXString);
		
		String pieceYString = piece.substring(0,1);
		// System.out.println(pieceYString);
		
		
		String destinationXString = destination.substring(1, 2);
		String destinationYString = destination.substring(0, 1);

		int pieceX = -1;
		int pieceY = -1;
		
		int destinationX = -1;
		int destinationY = -1;
		
		try {
			pieceX = Integer.parseInt(pieceXString) / 2;
			pieceY = Integer.parseInt(pieceYString);
			
			destinationX = Integer.parseInt(destinationXString) / 2;
			destinationY = Integer.parseInt(destinationYString);
			
		} catch (Exception e) {
			pieceX = convertLetterToColumnNumber(pieceXString);
			pieceY = Integer.parseInt(pieceYString);
			
			pieceX /= 2;
			pieceY -= 1;
			
			destinationX = convertLetterToColumnNumber(destinationXString);
			destinationY = Integer.parseInt(destinationYString);
			
			destinationX /= 2;
			destinationY -= 1;
		}
		
		// System.out.println(pieceY);
		// System.out.println(pieceX);
		// System.out.println(destinationY);
		
		int[] returnList = {pieceY, pieceX, destinationY, destinationX};
		return returnList;
	}
	
	public static int[] convertPieceString(String piece) {
		
		
		int y = -1;
		int x = -1;
		
		try {
			int[] returnArray = convertChessNotation(piece);
		} catch(Exception e) {
			String yString = piece.substring(0, 1);
			String xString = piece.substring(1, 2);
			
			y = Integer.parseInt(yString);
			x = Integer.parseInt(xString) / 2;
			
		}
		
		int[] returnArray = {y, x};
		
		return returnArray;
	}
	
	public static int[] convertChessNotation(String piece) {
		int y = Integer.parseInt(piece.substring(0, 1)) - 1;
		int x = convertLetterToColumnNumber(piece.substring(1, 2));
		x = x / 2;
		
		System.out.println(x);
		System.out.println(y);
		
		int[] returnArray = {y, x};
		
		return returnArray;
	}

	public static int[] convertMoveStringToIntArray(String move) {
		String[] stringArray = convertMoveStringToStringArray(move);
		
		int[] returnArray = convertStringArrayToIntArray(stringArray[0], stringArray[1]);
		
		return returnArray;
	}
	
	public static String[] convertMoveStringToStringArray(String move) {
		String[] returnArray = move.split(" ");
		return returnArray;
	}
	
	public static int[] convertStringToIntArray(String move) {
		String[] stringArray = move.split(" ");
		// System.out.println(stringArray[0]);
		// System.out.println(stringArray[1]);
		
		return convertStringArrayToIntArray(stringArray[0], stringArray[1]);
		
	}
	
	public static String convertIntArrayToString(int[] intArray) {
		int py = intArray[0];
		int px = intArray[1];
		int dy = intArray[2];
		int dx = intArray[3];
			
		String pyString = String.valueOf(py + 1);
		String dyString = String.valueOf(dy + 1);
		
		String pxString = convertColumnNumberToLetter(py, px);
		String dxString = convertColumnNumberToLetter(dy, dx);
		
		return pyString + pxString + " " + dyString + dxString;
	}
			
	public static boolean isJump(String moveString) {
		int[] intArray = convertStringToIntArray(moveString); 
		return isJump(intArray[0], intArray[1], intArray[2], intArray[3]);
		
	}
	
	public static boolean isJump(int py, int px, int dy, int dx) {
		if (((dy - py) == 2) || ((dy - py) == -2)) {
			return true;
		}
		return false;
	}

	
	
}
