package checkers_project;

import java.util.ArrayList;

public class BoardState {
	
	private byte turn;
	private byte[][] positions = new byte[Game2.ROWS][Game2.COLUMNS];
	ArrayList<String> legalMoves = new ArrayList();
	
	public BoardState() {
		// Create a board state for the beginning of the game.
		
		// positions
		// 1 is a black piece
		// 2 is a red piece
		// 3 is a black king
		// 4 is a red king
	
		// turns
		// 1 black
		// 2 is red
	
		for (byte i = 0; i < Game2.ROWS; i++) {
			for (byte j = 0; j < Game2.COLUMNS; j++) {
				if (i < 3) {
					positions[i][j] = 1;
				} else if (i > 4) {
					positions[i][j] = 2;
				} else {
					positions[i][j] = 0;
				}
			}
		}	
		this.turn = 1;	
	}
	
	public BoardState(BoardState current, String move) {
		this.positions = current.positions.clone();
		
		this.turn = current.turn;
		this.nextTurn();
		
		// I need to make sure this doesn't change my turn again. 
		this.preCheckedMove(move);
	}
	
	// Create another BoardState constructor that takes an int array (might be a lot faster)
	
	public void preCheckedMove(String move) {
		int[] fromAndTo = TextConversions.convertMoveStringToIntArray(move);	
		preCheckedMove(fromAndTo[0], fromAndTo[1], fromAndTo[2], fromAndTo[3]);
	}
	
	public void preCheckedMove(int py, int px, int dy, int dx) {
		if ((dy - py) == 1 || (dy - py) == -1) {
			this.positions[dy][dx] = this.positions[py][px];
			this.positions[py][px] = 0;
		} else {
			// Rules for jumps
			
			int direction = (dy - py) / 2;
			
			if ((py % 2) == 1) {
				// Right leaning rows
				
				if (direction == 1) {
					// Move the piece to the new location
					positions[dy][dx] = positions[py][px];
					positions[py][px] = 0;
					
					// Eliminate the piece jumped over
					if ((dx - px) == direction) {
						positions[py+direction][px + direction] = 0;
					} else {
						// Eliminate the piece jumped over
						positions[py+direction][px] = 0;
					}
				} else {
					// Move the piece to the new location
					positions[dy][dx] = positions[py][px];
					positions[py][px] = 0;
					
					// Eliminate the piece jumped over
					if ((dx - px) == direction) {
						positions[py+direction][px] = 0;
					} else {
						// Eliminate the piece jumped over
						positions[py+direction][px + direction] = 0;
					}
				}
				
				
			} else {
				// left leaning rows
				
				if (direction == 1) {
					// Move the piece to the new location
					positions[dy][dx] = positions[py][px];
					positions[py][px] = 0;
					
					// Eliminate the piece jumped over
					if ((dx - px) == direction) {
						positions[py+direction][px] = 0;
					} else {
						// Eliminate the piece jumped over
						positions[py+direction][px + direction] = 0;
					}
				} else {
					// Move the piece to the new location
					positions[dy][dx] = positions[py][px];
					positions[py][px] = 0;
					
					// Eliminate the piece jumped over
					if ((dx - px) == direction) {
						positions[py+direction][px + direction] = 0;
					} else {
						// Eliminate the piece jumped over
						positions[py+direction][px] = 0;
					}
				}
			}
		}
		
		if (dy == 0 || dy == 7) {
			king(dy, dx);
		}
		
	}
	
	public void king(int dy, int dx) {
		if (positions[dy][dx] == 1) {
			positions[dy][dx] = 3;
		} else {
			positions[dy][dx] = 4;
		}
	}
	
	public boolean isLegalMove(String move) {
		// This line calls the print message again. 
		
		if (canJump() > 0) {
			// Enforcing the jump rule.
			// System.out.println("Enforcing the jump rule.");
			if (TextConversions.isJump(move) != true) {
				System.out.println("Failed the jump rule.");
				return false;
			}
		}
		
		String[] fromAndTo = TextConversions.convertMoveStringToStringArray(move);
		int[] coordinates = TextConversions.convertStringArrayToIntArray(fromAndTo[0], fromAndTo[1]);
		// System.out.println(isLegalMove(coordinates[0], coordinates[1], coordinates[2], coordinates[3]));
		
		return isLegalMove(coordinates[0], coordinates[1], coordinates[2], coordinates[3]);
	}
		
	public boolean isLegalMove(int py, int px, int dy, int dx) {
		
		// Check to make sure all py and x are actually in the domain. 
		
		if (!TextConversions.isLegalY(py)) return false;
		if (!TextConversions.isLegalY(dy)) return false;
		if (!TextConversions.isLegalX(px)) return false;
		if (!TextConversions.isLegalX(dx)) return false;
		
		
		// System.out.println("Got to isLegalMove with coordinates.");
		
		// System.out.println(positions[py][px]);
		// System.out.println("turn: " + this.turn);
		
		// Check to ensure a piece is actually there
		if (positions[py][px] == 0) return false;
 		// Check to make sure it is the appropriate turn (or turn with kings)
		if ((positions[py][px] != turn) && (positions[py][px] != turn + 2)) return false;
		
		// The piece is black
		if (positions[py][px] == 1) {
			// System.out.println("Check black move legality");
			return blackMoveLegality(py, px, dy, dx);
		}
		
		// The piece is red
		if (positions[py][px] == 2) {
			// System.out.println("Check red move legality");
			return redMoveLegality(py, px, dy, dx);
		}
		
		// The piece is a king
		if (positions[py][px] == 3 || positions[py][px] == 4) {
			return kingMoveLegality(py, px, dy, dx);
		}
		
		return false;
	}
	
	public boolean checkSecondMoveStartingPoints(String move, int dy, int dx) {
		
		int[] intArray = TextConversions.convertStringToIntArray(move);
		
		if (intArray[0] == dy && intArray[1] == dx) {
			return true;
		}
		
		return false;
	}
	
	private boolean blackMoveLegality(int py, int px, int dy, int dx)	{
		
		if ((dy - py) == 2) {
			return jumpLegality(py, px, dy, dx);
		}
		
		if ((dy - py) == 1) {
			// System.out.println("We reached here");
			
			if (py%2 == 0) {
				// Check -1 and 0
				// This makes sure the move matches where a piece can legally move (not considering obstructions)
				if ((dx - px) == -1 || (dx - px) == 0) {
					// Check for obstructions
					if (positions[dy][dx] == 0) return true;
				} else {
					return false;
				}
			} else {
				// Check 0 and 1
				if ((dx - px) == 1 || (dx - px) == 0) {
					// Check for obstructions
					if (positions[dy][dx] == 0) return true;
				} else {
					return false;
				}
			}			
		}
		return false;
	}
		
	private boolean redMoveLegality(int py, int px, int dy, int dx) {
		if ((dy - py) == -2) {
			// System.out.println("Got to jump legality.");
			return jumpLegality(py, px, dy, dx);
		}
		
		if ((dy - py) == -1) {
			// Even rows of the board
			if (py%2 == 0) {
				// Check -1 and 0
				// This makes sure the move matches where a piece can legally move (not considering obstructions)
				if ((dx - px) == -1 || (dx - px) == 0) {
					// Check for obstructions
					if (positions[dy][dx] == 0) return true;
				} else return false;
			// Odd rows of the board
			} else {
				// Check 0 and 1
				if ((dx - px) == 1 || (dx - px) == 0) {
					// Check for obstructions
					if (positions[dy][dx] == 0) return true;
				} else return false;
			}			
		}
		
		return false;
	}
	
	private boolean kingMoveLegality(int py, int px, int dy, int dx) {
		
		// System.out.println(" Got to King move legality ");
		
		boolean pieceLocation = TextConversions.checkOnBoard(py, px);
		boolean destinationLocation = TextConversions.checkOnBoard(dy, dx);
		
		// This method can simply call Black and Red move legality depending on direction (not on actual ownership of the piece. 
		
		// I do need to be careful about jumps that go multiple directions. 
		
		if (pieceLocation && destinationLocation) {
			if ((dy - py) == -1 || (dy - py) == -2) {
				// System.out.println("Got to red move legality for kings");
				return redMoveLegality(py, px, dy, dx);
			} else {
				return blackMoveLegality(py, px, dy, dx);
			}
			
		} else {
			System.out.println("The coordinates given aren't actually on the board.");
		}
		
		return false;
	}
	
	public boolean jumpLegality(int py, int px, int dy, int dx) {
		
		if (positions[dy][dx] != 0) return false;
		
		// Check if it is in the arrayList of allowable jumps. 
		// System.out.println("Reached jump legality method.");
		
		if ((dy - py) == 2) {
			
			return positiveJumpLegality(py, px, dy, dx);
		} else if ((dy - py) == - 2) {
			return negativeJumpLegality(py, px, dy, dx);
		}
		
		System.out.println("This isn't actually a jump.");
		
		return false;
	}
	
	public boolean positiveJumpLegality(int py, int px, int dy, int dx) {
		
		// System.out.println("Reached Positive Jump legality.");
		
		int player = whosePieceIsThis(py, px);
		
		if ((py%2) == 1) {
			// Right leaning rows
			if ((dx - px) == 1) {
				// Jump right
				if (isOpposingPieceAt(player, py + 1, px + 1)) {
					return true;
				}
				
			} else if ((dx - px) == -1) {
				// Jump left
				if (isOpposingPieceAt(player, py + 1, px)) {
					return true;
				}
			}
		} else {
			// left leaning rows
			if ((dx - px) == 1) {
				// Jump right
				if (isOpposingPieceAt(player, py + 1, px)) {
					return true;
				}
				
			} else if ((dx - px) == -1) {
				// Jump left
				if (isOpposingPieceAt(player, py + 1, px - 1)) {
					return true;
				}
			
			}
		}
		
		return false;

	}
	
	public boolean negativeJumpLegality(int py, int px, int dy, int dx) {
		
		// System.out.println("Reached Negative Jump Legality Method.");
		int player = whosePieceIsThis(py, px);
		
		if ((py%2) == 1) {
			// Right leaning rows
			// System.out.println("Got to right leaning row");
			if ((dx - px) == 1) {
				
				// Jump right
				if (isOpposingPieceAt(player, py - 1, px + 1)) {
					return true;
				}
				
			} else if ((dx - px) == -1) {
				// System.out.println("Got to jump left.");
				// Jump left
				if (isOpposingPieceAt(player, py - 1, px)) {
					// System.out.println("Got to check opposing player");
					return true;
				}
			}
		} else {
			// left leaning rows
			if ((dx - px) == 1) {
				// Jump right
				if (isOpposingPieceAt(player, py - 1, px)) {
					return true;
				}
				
			} else if ((dx - px) == -1) {
				// Jump left
				if (isOpposingPieceAt(player, py - 1, px - 1)) {
					return true;
				}
			
			}
		}
		
		
		return false;
	}

	public boolean checkBlackVictory() {
		for (int y = 0; y < Game2.ROWS; y++) {
			for (int x = 0; x < Game2.COLUMNS; x++) {
				if ((positions[y][x] == 2) || (positions[y][x] == 4)) {
					
					// System.out.println(positions[y][x]);
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean checkRedVictory() {
		for (int i = 0; i < Game2.ROWS; i++) {
			for (int j = 0; j < Game2.COLUMNS; j++) {
				if (positions[i][j] == 1 || positions[i][j] == 3) return false;
			}
		}
		return true;
	}
	
	public boolean checkVictory() {
		boolean black = checkBlackVictory();
		boolean red = checkRedVictory();
		
		return black || red;
	}
	
	@SuppressWarnings("all")
	public boolean checkDraw() {
		// I need to look up the official definitions of draws in the rules
		// For now, we will simply use each side has only 1 piece. 
		
		int player1Count = 0;
		int player2Count = 0;
		
		for (int y = 0; y < Game2.ROWS; y++) {
			for (int x = 0; x < Game2.COLUMNS; x++) {
				if ((this.positions[y][x] == 1) || (this.positions[y][x] == 3)) {
					player1Count++;
				} 

				if ((this.positions[y][x] == 2) || (this.positions[y][x] == 4)) {
					player2Count++;
				}
			}
		}
		
		if (player1Count > 1 || player2Count > 1) return false;
		else return true;
			
	}
	
	public void printState() {
		for (byte i = Game2.ROWS - 1; i >= 0; i--) {
			for (byte j = 0; j < Game2.COLUMNS; j++) {
				
				int charStartingPoint = 97;
				int charInt = charStartingPoint + (j * 2);

				char first = (char) charInt;
				char second = (char) (charInt + 1);

				String rowNum = String.valueOf(i + 1);

				String firstChar = String.valueOf(first);
				String secondChar = String.valueOf(second);

				// Logic to add the piece at this position.
				String pieceString = " " + String.valueOf(this.positions[i][j]) + " ";

				if (i%2 == 0) {
					System.out.print(rowNum + firstChar + pieceString + rowNum + secondChar + "   " );
				} else {
					System.out.print (rowNum + firstChar + "   " + rowNum + secondChar + pieceString);
				} 
			} 
			System.out.println();
		}
	}

	public byte getTurn() {
		return this.turn;
	}
	
	public void nextTurn() {
		
		// System.out.println("We reached the next turn method.");
		if (this.turn == 1) {
			
			this.turn = 2;
		} else {
			this.turn = 1;
		}
		
		legalMoves.clear();
	}
	
	public byte canJump() {
		boolean jump = false;
		byte jumpCount = 0;
		
		for (int y = 0; y < Game2.ROWS; y++) {
			for (int x = 0; x < Game2.COLUMNS; x++) {
				if (this.positions[y][x] == this.turn || this.positions[y][x] == this.turn + 2) {
					jumpCount += canJump(y, x);
				}
			}
		}
		return jumpCount;
	}
	
	public byte canJump(int py, int px) {
		
		if (positions[py][px] == 1) {
			// System.out.println("Check black jumps.");
			return checkBlackJumps(py, px);
		} else if (positions[py][px] == 2)  {
			return checkRedJumps(py, px);
		} else if (positions[py][px] == 3 || positions[py][px] == 4) {
			// System.out.println("Check king jumps.");
			return checkKingJumps(py, px);
		}
		
		return 0;
	}
	
	public boolean canJumpAtDestination(String moveString) {
		
		int[] intArray = TextConversions.convertStringToIntArray(moveString);
		
		int dy = intArray[2];
		int dx = intArray[3];
		
		// How do I make sure that this is only newly kinged pieces. 
		if (dy == 7 || dy == 0) {
			// A piece cannot jump after being kinged.
			return false;
		}
		
		if (canJump(dy, dx) > 0) {
			return true;
		}
		
		return false;
		
	}
	
	public boolean canJumpAtDestinationForAKing(String moveString) {
		
		int[] intArray = TextConversions.convertStringToIntArray(moveString);
		
		int dy = intArray[2];
		int dx = intArray[3];
		
		// System.out.println(positions[dy][dx]);
		
		if (canJump(dy, dx) > 0) {
			return true;
		}
		
		return false;
		
		
		// Need something to allow for a multidirectional jumping check. 
		
		
	}
	
	public boolean isKingAtStartingPoint(String moveString) {
		
		int intArray[] = TextConversions.convertStringToIntArray(moveString);
		
		int py = intArray[0];
		int px = intArray[1];
		
		// System.out.println(py);
		// System.out.println(px);
		
		// System.out.println(positions[py][px]);
		
		if (positions[py][px] == 3 || positions[py][px] == 4) {
			return true;
		}
		
		return false;
	}
	
	public void printJumpRequirementString() {
		// Fix this for the second jump.
		
		byte jumpCount = canJump();
		
		if (jumpCount > 0) {
			if (jumpCount == 1) {
				System.out.println("There is only 1 jump available.");
			} else {
				System.out.println("There are " + jumpCount + " jumps available.");
				System.out.println("Choose your jump.");
			}
			
		}
		
		
	}

	public byte checkBlackJumps(int y, int x) {
		// System.out.println("Check black jump.");
		return checkJumps(y, x, 1);	
	}
	
	public byte checkRedJumps(int y, int x) {
		// System.out.println("Check red jump.");
		return checkJumps(y, x, -1);
	}
	
	public byte checkKingJumps(int y, int x) {
		
		// System.out.println("Check king jump.");
		
		byte jumpCount = 0;
		
		jumpCount += checkBlackJumps(y, x);
		jumpCount += checkRedJumps(y, x);
		
		return jumpCount;
	}
	
	public boolean isOpposingPieceAt(int player, int dy, int dx) {
		if (player == 1) {
			if (positions[dy][dx] == 2 || positions[dy][dx] == 4) {
				return true;
			}
		} else {
			if (positions[dy][dx] == 1 || positions[dy][dx] == 3) {
				return true;
			}
		}
		
		// System.out.println("That is not a valid player number.");
		return false;
	}
	
	public int whosePieceIsThis(int py, int px) {
		if (positions[py][px] == 2 || positions[py][px] == 4) {
			return 2;
		} else {
			return 1;
		}
	}
	
	public byte checkJumps(int y, int x, int direction) {
		
		// System.out.println(y + " " + x + " " + direction);
		// System.out.println("Check jumps");
		
		byte jumpCount = 0;
		
		int player = whosePieceIsThis(y, x);
		// System.out.println(player);
		
		int leftXToJumpOver;
		int rightXToJumpOver;
		
		int leftXToLandOn;
		int rightXToLandOn;
		
		// System.out.println(y);
		// System.out.println(y + (2 * direction));
		
		boolean isYJumpLegal = TextConversions.isLegalY(y+ (2 * direction));
		// System.out.println(isYJumpLegal);
		// System.out.println(isYJumpLegal);
		
		if ((y%2 == 1) && (isYJumpLegal)) {
			// Right leaning rows
			// 0 and +1 for the next row
			// - 1 and + 1 for the second row
			
			// System.out.println("Right leaning row");
			// System.out.println(direction);
			
			leftXToJumpOver = x;
			rightXToJumpOver = x + 1;
			
			if (TextConversions.isLegalX(leftXToJumpOver - 1)) {	
				// System.out.println("Check left");
				if (isOpposingPieceAt(player, y + direction, leftXToJumpOver)) {
					if (positions[y+(2 * direction)][x-1] == 0) {
						int[] legalMoveToAdd = {y, x, y+ (2 * direction), x-1};
						String legalMoveString = TextConversions.convertIntArrayToString(legalMoveToAdd);
						legalMoves.add(legalMoveString);
						jumpCount++;
					}
				}
			}
			
			if (TextConversions.isLegalX(rightXToJumpOver)) {	
				// System.out.println("Check right");
				if (isOpposingPieceAt(player, y + direction, rightXToJumpOver)) {

					if (TextConversions.isLegalX(rightXToJumpOver + 1)) {
						if (positions[y+(2 * direction)][x + 1] == 0) {
							int[] legalMoveToAdd = {y, x, y+ (2 * direction), x+1};
							String legalMoveString = TextConversions.convertIntArrayToString(legalMoveToAdd);
							legalMoves.add(legalMoveString);
							jumpCount++;
						}
					}
				}
			}
				
			
		} else if (isYJumpLegal) {
			// Left leaning rows
			// -1 and 0 for the next row
			// -1 and positive one
			
			leftXToJumpOver = x - 1;
			rightXToJumpOver = x;
			
			// System.out.println("Left leaning row");
			
			
			if (TextConversions.isLegalX(leftXToJumpOver)) {

				if (isOpposingPieceAt(player, y+ direction, leftXToJumpOver)) {
					if (positions[y+(2 * direction)][x-1] == 0) {
						
						int[] legalMoveToAdd = {y, x, y+ (2 * direction), x-1};
						String legalMoveString = TextConversions.convertIntArrayToString(legalMoveToAdd);
						legalMoves.add(legalMoveString);
						jumpCount++;
					}
				}
			}
			
			if (TextConversions.isLegalX(rightXToJumpOver)) {
				if (isOpposingPieceAt(player, y+ direction, rightXToJumpOver)) {
					if (TextConversions.isLegalX(x+1)) {
						if (positions[y+(2 * direction)][x + 1] == 0) {
							int[] legalMoveToAdd = {y, x, y+ (2 * direction), x+1};
							String legalMoveString = TextConversions.convertIntArrayToString(legalMoveToAdd);
							legalMoves.add(legalMoveString);
							jumpCount++;
						}
					}
				}
			}	
			
		}						
	
		return jumpCount;
	}
	
	public ArrayList<String> getAvailableJumpMoves() {
		
		return legalMoves;
	}
	
	public void displayAllLegalMoves() {
		this.canJump();
		
		// I need to add something here for the double jump or triple jump.
		// Add something here for the regular moves. 
		
		legalMoves.forEach((n) -> System.out.println(n)); 
	}
	
	public void clearlegalMoves() {
		this.legalMoves.clear();
	}
	
	public void displayAllLegalMovesSecondJump(String moveString) {
		// This method uses the string from the previous jump
		// The destination must be the same as all the new starting points.
		
		String destinationString = moveString.substring(3, 5);
		
		// System.out.println(destinationString);
		
		legalMoves.forEach((n) -> checkSecondLegalMoves(destinationString, n)); 
				
		legalMoves.forEach((m) -> System.out.println(m));
		
	}
		
	private void checkSecondLegalMoves(String destinationString, String n) {
		// System.out.println(n.substring(0, 2));
		if (n.substring(0, 2) == destinationString) {
			
		}
		
		// I need a way to remove the wrong ones from the arraylist without a for each loop. 
		
	}	
	
	public void addAvailableNonJumpMoves() {
		for (int y = 0; y < Game2.ROWS; y++) {
			for (int x = 0; x < Game2.COLUMNS; x++) {
				if (positions[y][x] == this.turn || positions[y][x] == this.turn + 2) {
					// Check for a single move by brute force.  
					
					for (int i = -1; i < 2; i+= 2) {
						if (isLegalMove(y, x, y + i, x + 1)) {
							// System.out.println("We found a success.");
							int[] legalMoveToAdd = {y, x, y + i, x + 1};
							String legalMoveString = TextConversions.convertIntArrayToString(legalMoveToAdd);
							legalMoves.add(legalMoveString);
							
						}
						
						if (isLegalMove(y, x, y + i, x)) {
							// System.out.println("We found a success.");

							int[] legalMoveToAdd = {y, x, y + i, x};
							String legalMoveString = TextConversions.convertIntArrayToString(legalMoveToAdd);
							legalMoves.add(legalMoveString);
								
						}
						
						if (isLegalMove(y, x, y + i, x - 1)) {
							// System.out.println("We found a success.");

							int[] legalMoveToAdd = {y, x, y + i, x - 1};
							String legalMoveString = TextConversions.convertIntArrayToString(legalMoveToAdd);
							legalMoves.add(legalMoveString);
							
						}
					}
				}
			}
		}
	}
	
	public ArrayList<String> allLegalMoves() {
		
		
		if (canJump() != 0) {
			return getAvailableJumpMoves();
		} else {
			addAvailableNonJumpMoves();
		}
		
		return legalMoves;
		
		// iterate through the positions array and then run a method to determine each legal move for each piece of that player. 
		
		// Concatenate all of them and return the list. 
		
		// Use this list to create the tree. 
		
	}

	public String[] allLegalMovesArray() {
		ArrayList<String> allLegalMoves = allLegalMoves();
		
		String[] allLegalMovesArray = allLegalMoves.toArray(new String[allLegalMoves.size()]);
		
		return allLegalMovesArray;
	}
	

	public int[][] availableDestinations() {
		// This should be used to highlight legal moves in the GUI.
		// Then when the user clicks a specific piece, it shrinks to show only the destinations for that tile. 
		
		return null;
	}
	
	
	
}

