package checkers_project.gui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/*
ADT for the checkers board.   Holds an 8x8 matrix of board positions and what is
on each position. 

*/
//a tile on our board


/**
 * @Description A Square is a space on a checker board. It has a location both in  x,y coordinate space and in chess notation
 * on an 8x8 grid(1a is the bottom left square, 8h is the top right). It has a color, and it can hold a CheckerPiece though it 
 * can be empty. You can click on it to collect the chessLocation in a static string in the ChecekersGui class
 */
public class Square extends StackPane {

	private CheckerPiece piece;// tiles can have pieces
	private String chessLocation;

	/**
	 * @description A square on a checkerboard. 
	 * @param color - a boolean that determines whether the space on the board is colored or not
	 * @param xCoordinate - the x-coordinate of the square in a scene
	 * @param yCoordinate - the y-coordinate of the square in a scene
	 * @param chessLocation - the String description of the location in an 8x8 grid using chess notation. 1a is bottom left, 8h is top right.
	 * @precondition chessLocation.length() == 2
	 * @postcondition a square is created
	 * @throws IllegalArgumentException if chessLocation.length != 2
	 */
	
	public Square(boolean color, int xCoordinate, int yCoordinate, String chessLocation) {// creates our squares
		if(chessLocation.length() != 2){
			throw new IllegalArgumentException("Chess notation is a 2 character string");
		}
		this.setChessLocation(chessLocation);
		relocate(xCoordinate * CheckersGui.SQUARE_SIZE, yCoordinate * CheckersGui.SQUARE_SIZE);
		Rectangle squareSprite = new Rectangle(CheckersGui.SQUARE_SIZE, CheckersGui.SQUARE_SIZE);

		if (color == false) {
			squareSprite.setFill(Color.LIGHTYELLOW);
		} else {
			squareSprite.setFill(Color.GREEN);
		}

		getChildren().add(squareSprite);

		setOnMouseClicked(e -> {
			if(this.hasPiece()) {
				CheckersGui.movement += this.chessLocation + " has a piece";
			} else {
				CheckersGui.movement += this.chessLocation + " does not have a piece";
			}
		});

	}

	/**
	 * @description determines if the square has a piece
	 * @return true if the square has a CheckerPiece or false if the square does not have a piece
	 */
	
	public boolean hasPiece() {// does this tile have a piece?
		if (piece != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @description returns the CheckerPiece the square object has a reference to.
	 * @return the CheckerPiece object the square has
	 */
	
	public CheckerPiece getPiece() {// returns the piece if it has one
		return piece;
	}

	/**
	 * @description A square can hold a CheckerPiece, This changes that piece to a different piece
	 * @param piece - A CheckerPiece object
	 * @precondition piece is either a CheckerPiece object or null
	 * @postcondition The CheckerPiece this square has a reference to is changed
	 */
	
	public void setPiece(CheckerPiece piece) {// sets the piece if it has one
		this.piece = piece;
	}
	
	/**
	 * @description Returns the current position of the square in chess notation
	 * @param 
	 * @precondition 
	 * @postcondition 
	 * @return String chessLocation
	 * @throws NullPointerException if the square does not have a chess location
	 */

	public String getChessLocation() {
		if(this.chessLocation!=null) {
		return chessLocation;
		}
		else throw new NullPointerException("No chess Location");
	}

	/**
	 * @description sets the chessLocation instance variable to a string
	 * @param  chessLocation - A string description of a board position used most commonly in chess. For example 1a is the bottom left corner
	 * @precondition chessLocation should be a String in chess format I.E. 1a, 3c, 4h etc...
	 * @postcondition chessLocation is set to the new Location
	 * @return nothing
	 * @throws IllegalArgumentException chessLocation.length() != 2
	 * 
	 */
	public void setChessLocation(String chessLocation) {
		if(chessLocation.length() != 2) {
			throw new IllegalArgumentException("A location on the board in chess notation is only 2 characters.");
		}
		this.chessLocation = chessLocation;
	}

}
