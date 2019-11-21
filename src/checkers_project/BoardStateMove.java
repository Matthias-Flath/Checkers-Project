package checkers_project;

/*
 * Check the legality of moves
 */
public class BoardStateMove {

	/**
	 * Determines whether the moveString is valid for the current BoardState
	 * @precondition
	 * 		The BoardState is a legal BoardState.  
	 * 		Move object is expressed in chess notation (y first) or numbers.
	 * @param current
	 * @param move
	 * @return
	 * 		true if the move is legal
	 * 		false if the move is not legal
	 */
	public static boolean isLegalMove(BoardState current, String move) {
		// Create a check to make sure this is the first move.
		
		System.out.println("isLegalMove method");
		System.out.println(move);
		
		int[] coordinates = TextConversions.convertMoveStringToIntArray(move);
		return isLegalMove(current, coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
	}
	
	public static boolean isLegalMove(BoardState current, String move, String previousMove) {
		
		// Check to make sure the start and end points match, then call the regular isLegalMove method.
		if (isLegalStartingPoint(move, previousMove) == false) return false;
		
		// Check to make sure the move is a jump
		if (!TextConversions.isJump(move)) return false;
		
		return isLegalMove(current, move);
	}
	
	/**
	 * 
	 * @param move
	 * @param previousMove
	 * @return
	 */
	public static boolean isLegalStartingPoint(String move, String previousMove) {
		
		int[] previousMoveArray = TextConversions.convertMoveStringToIntArray(previousMove);
		int[] currentMoveArray = TextConversions.convertMoveStringToIntArray(move);
		
		int startY = previousMoveArray[2];
		int startX = previousMoveArray[3];
		
		if (currentMoveArray[2] != startY) return false;
		if (currentMoveArray[3] != startX) return false;
		
		return true;
	}
	
	/**
	 * Determines whether the move ints are valid for the current BoardState
	 * @precondition
	 * 		The BoardState is a legal BoardState.  
	 * 		The move is expressed in 8x4 notation
	 * @param current
	 * @param py
	 * @param px
	 * @param dy
	 * @param dx
	 * @return
	 * 		true if the move is legal
	 * 		false if the move is not legal
	 */
	public static boolean isLegalMove(BoardState current, int py, int px, int dy, int dx) {
		
		System.out.println("We got here");
		System.out.println(py);
		System.out.println(px);
		System.out.println(dy);
		System.out.println(dx);
		
		
		// System.out.println(current.positions[py][px]);
		
		
		// Check to ensure the jump rule is followed.
		if (BoardStateJumps.canJump(current)) {
			
			System.out.println("Can jump");
			
			// Enforcing the jump rule.
			// System.out.println("Enforcing the jump rule.");
			if (TextConversions.isJump(py, px, dy, dx) != true) {
				System.out.println("Failed the jump rule.");
				return false;
			}
		}
		
		// Check to make sure all py and x are actually in the domain. 
		if (!TextConversions.checkOnBoard(py, px)) return false;
		if (!TextConversions.checkOnBoard(dy, dx)) return false;
		
		
		// Check to ensure a piece is actually there
		if (current.positions[py][px] == 0) return false;
 		
		// Check to make sure the destination is empty
		if (current.positions[dy][dx] != 0) return false;
		
		// Check to make sure it is the appropriate turn (or turn with kings)
		if ((current.positions[py][px] != current.getTurn()) && (current.positions[py][px] != current.getTurn() + 2)) return false;
		
		
		
		// Black piece
		if (current.positions[py][px] == 1) {
			// System.out.println("Check black move legality");
			// System.out.println("Black move legality");
			return blackMoveLegality(current, py, px, dy, dx);
		}
		
		// Red piece
		if (current.positions[py][px] == 2) {
			// System.out.println("Check red move legality");
			return redMoveLegality(current, py, px, dy, dx);
		}
		
		// King
		if (current.positions[py][px] == 3 || current.positions[py][px] == 4) {
			return kingMoveLegality(current, py, px, dy, dx);
		}
		
		return false;
	}
	
	/**
	 * Check the legality of non-king black piece moves
	 * This method is private to ensure that it isn't called directly (only from isLegalMove.
	 * @precondition
	 * 		isLegalMove has already been called and therefore all those checks have been passed.
	 * @param current
	 * @param py
	 * @param px
	 * @param dy
	 * @param dx
	 * @return
	 * 		true if legal
	 * 		false if not
	 */
	private static boolean blackMoveLegality(BoardState current, int py, int px, int dy, int dx)	{
		
		// If it is a jump, check jump legality
		if ((dy - py) == 2) {
			return jumpLegality(current, py, px, dy, dx);
		}
		
		// If not jump
		if ((dy - py) == 1) {
			
			// Left leaning rows
			if (py%2 == 0) {
				// This makes sure the move matches where a piece can legally move (not considering obstructions)
				if ((dx - px) == -1 || (dx - px) == 0) return true;
				
				return false;
				
			// Right leaning rows
			} else {
				if ((dx - px) == 1 || (dx - px) == 0) return true; 
				return false;
			}			
		}
		return false;
	}
		
	/**
	 * Check the legality of non-king red piece moves
	 * This method is private to ensure that it isn't called directly (only from isLegalMove.
	 * @precondition
	 * 		isLegalMove has already been called and therefore all those checks have been passed.
	 * @param current
	 * @param py
	 * @param px
	 * @param dy
	 * @param dx
	 * @return
	 * 		true if legal
	 * 		false if not
	 */
	private static boolean redMoveLegality(BoardState current, int py, int px, int dy, int dx) {
		
		if ((dy - py) == -2) {
			return jumpLegality(current, py, px, dy, dx);
		}
		
		if ((dy - py) == -1) {
			// left leaning rows
			if (py%2 == 0) {
				// Check -1 and 0
				// This makes sure the move matches where a piece can legally move (not considering obstructions)
				if ((dx - px) == -1 || (dx - px) == 0) return true;
				
				return false;
			
				// Right leaning rows
			} else {
				// Check 0 and 1
				if ((dx - px) == 1 || (dx - px) == 0) return true; 
				
				return false;
			}			
		}
		
		return false;
	}
	
	/**
	 * Check the legality of king piece moves.
	 * This method is private to ensure that it isn't called directly (only from isLegalMove.
	 * @precondition
	 * 		isLegalMove has already been called and therefore all those checks have been passed.
	 * @param current
	 * @param py
	 * @param px
	 * @param dy
	 * @param dx
	 * @return
	 * 		true if legal
	 * 		false if not	 */
	private static boolean kingMoveLegality(BoardState current, int py, int px, int dy, int dx) {
		
		// I do need to be careful about jumps that go multiple directions. 
		if ((dy - py) == -1 || (dy - py) == -2) {
			return redMoveLegality(current, py, px, dy, dx);
		} else {
			return blackMoveLegality(current, py, px, dy, dx);
		}
	}
	
	/**
	 * Check the legality of a jump move
	 * This method is private to ensure that it isn't called directly (only from isLegalMove.
	 * @precondition
	 * 		This method is called from the isLegalMove (indirectly at least).  Therefore all of those 
	 * 		tests are passed.
	 * @param current
	 * @param py
	 * @param px
	 * @param dy
	 * @param dx
	 * @return
	 * 		true if legal
	 * 		false if not
	 */
	private static boolean jumpLegality(BoardState current, int py, int px, int dy, int dx) {
		
		if ((dy - py) == 2) {
			return positiveJumpLegality(current, py, px, dy, dx);
		} else if ((dy - py) == - 2) {
			return negativeJumpLegality(current, py, px, dy, dx);
		}
		
		System.out.println("This isn't actually a jump.");
		
		return false;
	}
	
	/**
	 * Check the legality of jump moves that move up the board.
	 * @param current
	 * @precondition
	 * 		This method is called from the isLegalMove (indirectly at least).  Therefore all of those 
	 * 		tests are passed.	 
	 * @param py
	 * @param px
	 * @param dy
	 * @param dx
	 * @return
	 * 		true if legal
	 * 		false if not
	 */
	private static boolean positiveJumpLegality(BoardState current, int py, int px, int dy, int dx) {
				
		int player = current.whosePieceIsThis(py, px);
		
		// Right leaning rows
		if ((py%2) == 1) {

			// Jump right
			if ((dx - px) == 1) {
				// Make sure there is an opponent piece to jump over.
				if (BoardStateJumps.isOpposingPieceAt(current, player, py + 1, px + 1)) {
					return true;
				}
			
			// Jump left	
			} else if ((dx - px) == -1) {
				
				if (BoardStateJumps.isOpposingPieceAt(current, player, py + 1, px)) {
					return true;
				}
			}

		// left leaning rows
		} else {
			// Jump right
			if ((dx - px) == 1) {
				
				if (BoardStateJumps.isOpposingPieceAt(current, player, py + 1, px)) {
					return true;
				}
			
			// Jump left	
			} else if ((dx - px) == -1) {
				
				if (BoardStateJumps.isOpposingPieceAt(current, player, py + 1, px - 1)) {
					return true;
				}
			
			}
		}
		
		return false;

	}
	
	/**
	 * Check the legality of jump moves that move down the board.
	 * @precondition
	 * 		This method is called from the isLegalMove (indirectly at least).  Therefore all of those 
	 * 		tests are passed.	 * 		
	 * @param current
	 * @param py
	 * @param px
	 * @param dy
	 * @param dx
	 * @return
	 * 		true if legal
	 * 		false if not
	 */
	private static boolean negativeJumpLegality(BoardState current, int py, int px, int dy, int dx) {
		
		int player = current.whosePieceIsThis(py, px);
		
		// Right leaning rows
		if ((py%2) == 1) {
			
			// Jump right
			if ((dx - px) == 1) {
				
				if (BoardStateJumps.isOpposingPieceAt(current, player, py - 1, px + 1)) {
					return true;
				}
			
			// Jump left
			} else if ((dx - px) == -1) {
				if (BoardStateJumps.isOpposingPieceAt(current, player, py - 1, px)) {
					return true;
				}
			}
		
		// left leaning rows
		} else {
			
			// Jump right
			if ((dx - px) == 1) {
				
				if (BoardStateJumps.isOpposingPieceAt(current, player, py - 1, px)) {
					return true;
				}
			
			// Jump left	
			} else if ((dx - px) == -1) {
				
				if (BoardStateJumps.isOpposingPieceAt(current, player, py - 1, px - 1)) {
					return true;
				}
			}
		}
		return false;
	}

	
}
