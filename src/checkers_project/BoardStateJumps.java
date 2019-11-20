package checkers_project;

public class BoardStateJumps {

	/**
	 * Check if there is at least 1 jump available at the current BoardState.
	 * @precondition
	 * 		Assumes the BoardState is valid
	 * @param current
	 * @return
	 * 		true if at least 1 jump is found
	 * 		false if no jumps were found
	 */
	public static boolean canJump(BoardState current) {
		if (numJumps(current) > 0) return true;
		return false;
	}
	
	/**
	 * Return the number of jumps possible at a specific point on the board.  This method does not consider turns. 
	 * @param current
	 * @param y
	 * @param x
	 * @return
	 */
	private static byte numJumpsAtLocation(BoardState current, int py, int px) {
		
		if (current.positions[py][px] == 1) {
			// System.out.println("Check black jumps.");
			return numBlackJumps(current, py, px);
		} else if (current.positions[py][px] == 2)  {
			return numRedJumps(current, py, px);
		} else if (current.positions[py][px] == 3 || current.positions[py][px] == 4) {
			// System.out.println("Check king jumps.");
			return numKingJumps(current, py, px);
		}
		
		return 0;
	}
	
	/**
	 * Determine how many jumps are possible at a given location on the board in a specific direction.
	 * This method does not consider turns. 
	 * @precondition
	 * 		The position must be a valid position for the board
	 * @param current
	 * @param y
	 * @param x
	 * @param direction
	 * @return
	 * 		byte of the number of jumps possible at that board location in a specific direction
	 */
	private static byte numJumpsAtLocation(BoardState current, int y, int x, int direction) {
		
		// Do I need any checks as to whether positions[y][x] is used.
		
		byte jumpCount = 0;

		int player = current.whosePieceIsThis(y, x);

		int leftXToJumpOver;
		int rightXToJumpOver;

		int leftXToLandOn;
		int rightXToLandOn;

		// Check if the potential jump y value is on the board.
		boolean isYJumpLegal = TextConversions.isLegalY(y+ (2 * direction));

		if (isYJumpLegal == false) return 0;
		
		// Right leaning rows
		if (y%2 == 1) {
			// 0 and +1 for the next row
			// - 1 and + 1 for the second row

			leftXToJumpOver = x;
			rightXToJumpOver = x + 1;

			// Consider refactoring the below 7 lines into 1 method call
			
			if (TextConversions.isLegalX(leftXToJumpOver - 1)) {	
				if (isOpposingPieceAt(current, player, y + direction, leftXToJumpOver)) {
					if (current.positions[y+(2 * direction)][x-1] == 0) {
						jumpCount++;
					}
				}
			}

			if (TextConversions.isLegalX(rightXToJumpOver)) {	
				if (isOpposingPieceAt(current, player, y + direction, rightXToJumpOver)) {

					if (TextConversions.isLegalX(rightXToJumpOver + 1)) {
						if (current.positions[y+(2 * direction)][x + 1] == 0) {
							jumpCount++;
						}
					}
				}
			}

		// Left leaning rows
		} else {
			// -1 and 0 for the next row
			// -1 and positive one

			leftXToJumpOver = x - 1;
			rightXToJumpOver = x;

			if (TextConversions.isLegalX(leftXToJumpOver)) {

				if (isOpposingPieceAt(current, player, y+ direction, leftXToJumpOver)) {
					if (current.positions[y+(2 * direction)][x-1] == 0) {
						jumpCount++;
					}
				}
			}

			if (TextConversions.isLegalX(rightXToJumpOver)) {
				if (isOpposingPieceAt(current, player, y+ direction, rightXToJumpOver)) {
					if (TextConversions.isLegalX(x+1)) {
						if (current.positions[y+(2 * direction)][x + 1] == 0) {
							jumpCount++;
						}
					}
				}
			}	
		}						

		return jumpCount;
	}
	
	/**
	 * Determine the number of jumps possible in a forward direction
	 * @param current
	 * @param y
	 * @param x
	 * @return
	 * 		byte of the number of jumps moving in a forward direction.
	 */
	public static byte numBlackJumps(BoardState current, int y, int x) {
		// System.out.println("Check black jump.");
		return numJumpsAtLocation(current, y, x, 1);	
	}

	/**
	 * Determine the number of jumps possible in a backward direction
	 * @param current
	 * @param y
	 * @param x
	 * @return
	 * 		byte of the number of jumps moving in a forward direction.
	 */
	public static byte numRedJumps(BoardState current, int y, int x) {
		// System.out.println("Check red jump.");
		return numJumpsAtLocation(current, y, x, -1);
	}
	
	/**
	 * Determine the number of jumps possible in either direction
	 * @param current
	 * @param y
	 * @param x
	 * @return
	 * 		byte of the number of jumps moving in either direction.
	 */
	public static byte numKingJumps(BoardState current, int y, int x) {
		// Make sure this method still works after the refactor to the byte cast
		return (byte) (numBlackJumps(current, y, x) + numRedJumps(current, y, x));
	}
	
	/**
	 * Determine the total number of possible jumps at a current board state.
	 * @precondition
	 * 		The board state must be valid. 
	 * @param current
	 * @return
	 * 		byte of the number of possible jumps
	 */
	public static byte numJumps(BoardState current) {
		byte jumpCount = 0;
		
		for (int y = 0; y < Game.ROWS; y++) {
			for (int x = 0; x < Game.COLUMNS; x++) {
				if (current.positions[y][x] == current.getTurn() || current.positions[y][x] == current.getTurn() + 2) {
					jumpCount += numJumpsAtLocation(current, y, x);
				}
			}
		}
		return jumpCount;
	}
	
	/**
	 * Check if there is a possible jump at the provided BoardState and position.
	 * @param current
	 * @param py
	 * @param px
	 * @return
	 */
	public static boolean canJumpAtPosition(BoardState current, int py, int px) {
		
		if (numJumpsAtLocation(current, py, px) > 0) return true;
		return false;
	}
	
	/**
	 * Determine if a second jump is possible after the given move.
	 * @param current
	 * @param moveString
	 * @return
	 */
	public static boolean canJumpAtDestination(BoardState current, String moveString) {
		
		// Convert the moveString to an int array
		int[] intArray = TextConversions.convertMoveStringToIntArray(moveString);

		int dy = intArray[2];
		int dx = intArray[3];

		// Check if the piece is a king.
		if (!isKingAtStartingPoint(current, moveString)) {
			// Check if the piece is kinged at its destination 
			if (dy == 7 || dy == 0) {
				// A piece cannot jump after being kinged.
				return false;
			}
		}
		
		// This general BoardState(BoardState current, String move) constructor calls this exact method to 
		// determine whether or not to move to the next turn in the game, so we can't use that in the normal way. 
		BoardState destinationState = new BoardState(current, moveString, false);
		
		if (canJumpAtPosition(destinationState, dy, dx)) {
			return true;
		}
		
		return false;
	}
	
	/** Check if the piece is a king before the move
	 * This method is used to ensure that pieces can't continue jumping after they are kinged.
	 * @param current
	 * @param moveString
	 * @return
	 */
	public static boolean isKingAtStartingPoint(BoardState current, String moveString) {
		
		int intArray[] = TextConversions.convertMoveStringToIntArray(moveString);
		
		int py = intArray[0];
		int px = intArray[1];
		
		if (current.positions[py][px] == 3 || current.positions[py][px] == 4) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Check if there is an opposing piece at a specific position.
	 * @param current
	 * @param player
	 * @param dy
	 * @param dx
	 * @return
	 */
	public static boolean isOpposingPieceAt(BoardState current, int player, int dy, int dx) {
		if (player == 1) {
			if (current.positions[dy][dx] == 2 || current.positions[dy][dx] == 4) {
				return true;
			}
		} else {
			if (current.positions[dy][dx] == 1 || current.positions[dy][dx] == 3) {
				return true;
			}
		}
		
		// System.out.println("That is not a valid player number.");
		return false;
	}
	
	/**
	 * 
	 * @param current
	 */
	public static void printJumpRequirementString(BoardState current) {
		// Make sure this still works for the second jump.
		
		byte jumpCount = numJumps(current);
		
		if (jumpCount > 0) {
			if (jumpCount == 1) {
				System.out.println("There is only 1 jump available.");
			} else {
				System.out.println("There are " + jumpCount + " jumps available.");
				System.out.println("Choose your jump.");
			}
		}
	}
}