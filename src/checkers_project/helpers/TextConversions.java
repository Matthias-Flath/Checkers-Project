package checkers_project.helpers;

import java.util.Arrays;

public class TextConversions {
	
	/**
	 * Check whether the x value is valid.
	 * @param x
	 * @return
	 * 		true if x is on the board
	 * 		false if x is not on the board
	 */
	public static boolean isLegalX(int x) {
		if (x >= 0 && x < Game.COLUMNS) {
			return true;
		}
		return false;
	}

	/**
	 * Check whether the y value is valid.
	 * @param y
	 * @return
	 * 		true if y is on the board
	 * 		false if y is not on the board
	 */
	public static boolean isLegalY(int y) {
		if (y < Game.ROWS && y >= 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * Check both x and y
	 * @param y
	 * @param x
	 * @return
	 * 		true if x and y are both valid
	 */
	public static boolean checkOnBoard(int y, int x) {
		return (isLegalX(x) && isLegalY(y));
	}
	
	/**
	 * Convert a letter to a column number
	 * @precondition
	 * 		letter is between "a" and "h".
	 * @param letter
	 * @return
	 * 		The corresponding column number out of 8 possibilities.  This number has not been
	 * 		pruned to the 4 column approach yet. 
	 * 		-1 if an invalid input was given.
	 * @throws IllegalArgumentException
	 * 		if the letter was not within the range or was not of length 1.
	 */
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
			System.err.println("You gave an invalid input to convertLetterToColumnNumber");
			throw new IllegalArgumentException();
		}		
	}
	
	
	/**
	 * Find the column letter at a given position
	 * @precondition
	 * 		The column number given is of shortened form (of 4 choices)
	 * 		The position given is valid.
	 * @param y
	 * 		The row
	 * @param x
	 * 		The column
	 * @return
	 * 		The column letter for the position given.
	 */
	public static String convertColumnNumberToLetter(int y, int x) {
		
		// Right leaning rows
		if (y%2 == 1) { 
			return convertNumberToLetter(x * 2 + 1);
		
		// Left leaning rows
		} else {
			return convertNumberToLetter(x * 2);
		}
	}
	
	/**
	 * Convert a long form column number (8 choices) to its column letter.
	 * @precondition
	 * 		number integer is of long form
	 * 		number integer is valid (between 0 and 7)
	 * @param number
	 * @return
	 * 		letter corresponding to a specific 
	 * @throws IllegalArgumentException
	 * 		If the number is not valid.
	 */
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
			System.err.println("You gave an invalid input to convertNumberToLetter");
			throw new IllegalArgumentException();
		}	
	}
	
	/**
	 * Convert a piece and destination string to an integer array
	 * @precondition
	 * 		piece and destination are both valid
	 * 		They can be either chess notation or long form number notation (8 x 8 coordinates)
	 * @param piece
	 * @param destination
	 * @return
	 * 		integer array [pieceY, pieceX, destinationY, destinationX]
	 * 		short form integers
	 */
	public static int[] convertStringsToIntArray(String piece, String destination) {		
		
		int[] pieceInts = convertPieceString(piece);
		int[] destinationInts = convertPieceString(destination);
		
		int[] returnList = {pieceInts[0], pieceInts[1], destinationInts[0], destinationInts[1]};
		return returnList;
	}
	
	/**
	 * Convert a piece string to an integer array
	 * @precondition
	 * 		piece and destination are both valid
	 * 		They can be either chess notation or long form number notation (8 x 8 coordinates)
	 * @param piece
	 * @return
	 * 		integer array of length 2 [y, x]
	 */
	public static int[] convertPieceString(String piece) {
		
		int y = -1;
		int x = -1;
		
		// If piece string is in chess notation
		try {
			int[] returnArray = convertChessNotation(piece);
			return returnArray;
			
		// If piece string is in 8x8 array notation.
		} catch(Exception e) {
			String yString = piece.substring(0, 1);
			String xString = piece.substring(1, 2);
			
			y = Integer.parseInt(yString);
			// Convert the number to short form (8x4) notation.
			x = Integer.parseInt(xString) / 2;
		}
		
		int[] returnArray = {y, x};
		return returnArray;
	}
	
	/**
	 * Convert a piece string to an integer array
	 * @precondition
	 * 		pieceString is in chess notation
	 * @param piece
	 * @return
	 * 		int array [y, x]
	 */
	public static int[] convertChessNotation(String piece) {
		// Subtract 1 to convert it from start_1 to start_0
		int y = Integer.parseInt(piece.substring(0, 1)) - 1;
		int x = convertLetterToColumnNumber(piece.substring(1, 2));
		// Divide by 2 to convert from 8x8 array notation to 8x4.
		x = x / 2;
		
		int[] returnArray = {y, x};
		return returnArray;
	}

	/**
	 * Convert a move String to a final int array
	 * @precondition
	 * 		moveString must be valid (piece and destination are on the board)
	 * 		The move itself does not need to be valid.
	 * @param move
	 * 		String representing the move (either 8x8 array notation or chess notation)
	 * @return
	 * 		int array [pieceY, pieceX, destinationY, destinationX]
	 */
	public static int[] convertMoveStringToIntArray(String move) {
		String[] stringArray = convertMoveStringToStringArray(move);
		int[] returnArray = convertStringsToIntArray(stringArray[0], stringArray[1]);
		return returnArray;
	}
	
	/**
	 * Split a move string into an array of 2 strings.
	 * @precondition
	 * 		The piece and destination must be split by a single " ".
	 * @param move
	 * @return
	 * 		Array of strings [piece, destination]
	 */
	public static String[] convertMoveStringToStringArray(String move) {
		move = move.strip();
		String[] returnArray = move.split(" ");
		return returnArray;
	}
	
	/**
	 * Convert an int array back to a string.
	 * @precondition
	 * 		The int array must be valid [py, px, dy, dx]
	 * @param intArray
	 * @return
	 * 		String representing the move in Chess notation.
	 */
	public static String convertIntArrayToString(int[] intArray) {
		// Seperate the array
		int py = intArray[0];
		int px = intArray[1];
		int dy = intArray[2];
		int dx = intArray[3];
			
		// convert ints to Strings
		String pyString = String.valueOf(py + 1);
		String dyString = String.valueOf(dy + 1);
		
		String pxString = convertColumnNumberToLetter(py, px);
		String dxString = convertColumnNumberToLetter(dy, dx);
		
		// Concatenate the string
		return pyString + pxString + " " + dyString + dxString;
	}
	
	/**
	 * Determine if a move is a jump
	 * @precondition
	 * 		The move must be valid.
	 * @param moveString
	 * @return
	 * 		true if the move is a jump.
	 * 		false if not.
	 */
	public static boolean isJump(String moveString) {
		int[] intArray = convertMoveStringToIntArray(moveString); 
		return isJump(intArray[0], intArray[1], intArray[2], intArray[3]);
	}
	
	/**
	 * Determine if a move is a jump
	 * @precondition
	 * 		The move must be valid
	 * @param py
	 * @param px
	 * @param dy
	 * @param dx
	 * @return
	 * 		true if the move is a jump.
	 * 		false if not.
	 */
	public static boolean isJump(int py, int px, int dy, int dx) {
		if (((dy - py) == 2) || ((dy - py) == -2)) {
			return true;
		}
		return false;
	}
	
}
