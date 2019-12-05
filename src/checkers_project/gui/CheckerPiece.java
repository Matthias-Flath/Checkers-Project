package checkers_project.gui;
import checkers_project.BoardState;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;


/*
Abstract Data Type for the Checkers Piece.
Describes the set of actions that a checkers piece can make, and all of the
various information it can hold. 
*/


/**
 * @description The CheckerPiece object is a game piece used in checkers. It can either be red or black, and be a king in either 
 * of those colors. It has a location and Sprite that is an Ellipse object. It can remember where it used to be.
 */


public class CheckerPiece extends StackPane {

	// is it a red or black piece
	private BoardState currentBoardstate;
	private CheckersGui game;
	private PieceColor color;
	private double mouseX, mouseY;// mouse coordinates for when you pick things up
	private double oldXCoordinate, oldYCoordinate;// so it can remember where it used to be
	private Ellipse pieceSprite;
	private Rectangle kingSprite;
	// a piece needs a color which also determines which direction it can move
	// it also needs starting coordinates

	/**
	 * @description The constructor for a CheckerPiece Object. It sets the color and starting location of the piece as 
	 * 				well as a reference to the GUI and current boardstate
	 * @param color - the starting PieceColor enumeration of the piece.
 	 * @param xCoordinate - the starting x-coordinate location of the piece
	 * @param yCoordinate - the starting y-coordinate locatiopn of the piece
	 * @param game - a reference to a ChecersGui object that the piece resides in
	 * @postcondition a CheckerPiece object is created moved to a location and a given a sprite
	 */
	
	
	public CheckerPiece(PieceColor color, int xCoordinate, int yCoordinate, CheckersGui game) {
		this.color = color;// set whether it is a black or red piece

		move(xCoordinate, yCoordinate);// move the piece to the starting coordinates

		this.pieceSprite = new Ellipse(CheckersGui.SQUARE_SIZE * 0.35, CheckersGui.SQUARE_SIZE * 0.35);// need to draw
		this.kingSprite = new Rectangle(CheckersGui.SQUARE_SIZE *0.35, CheckersGui.SQUARE_SIZE * 0.35);																								// the piece

		if (color == PieceColor.Red) {// depending on if it is red or black fill it with that color
			pieceSprite.setFill(Color.RED);
		} else if(color == PieceColor.Black) {
			pieceSprite.setFill(Color.BLACK);
		} else if(color == PieceColor.RedKing) {
			this.kingSprite.setFill(Color.BLACK);	
		} else {
			this.kingSprite.setFill(Color.RED);	
		}
		

		this.kingSprite.setStroke(Color.BLACK);
		this.kingSprite.setStrokeWidth(CheckersGui.SQUARE_SIZE * 0.03);
		this.kingSprite.setTranslateX((CheckersGui.SQUARE_SIZE - (CheckersGui.SQUARE_SIZE * 0.35 * 2)) / 2);
		this.kingSprite.setTranslateY((CheckersGui.SQUARE_SIZE - (CheckersGui.SQUARE_SIZE * 0.35 * 2)) / 2);// make sure it
		
		pieceSprite.setStroke(Color.BLACK);
		pieceSprite.setStrokeWidth(CheckersGui.SQUARE_SIZE * 0.03);
		pieceSprite.setTranslateX((CheckersGui.SQUARE_SIZE - (CheckersGui.SQUARE_SIZE * 0.35 * 2)) / 2);
		pieceSprite.setTranslateY((CheckersGui.SQUARE_SIZE - (CheckersGui.SQUARE_SIZE * 0.35 * 2)) / 2);// make sure it
																										// is in middle
																										// of square
		getChildren().add(kingSprite);
		getChildren().add(pieceSprite);

		if(this.color == PieceColor.RedKing || this.color == PieceColor.BlackKing) {
			getChildren().remove(pieceSprite);
		}
	}

	/**
	 * @description returns the Ellipse pieceSprite instance Variable
	 * @return Ellipse pieceSprite
	 */
	public Ellipse getPieceSprite() {
		return pieceSprite;
	}

	/**
	 * @description changes the sprite into a new Sprite
	 * @param pieceSprite - an Ellipse object you want to set as the sprite for this object
	 * @postcondition the pieceSprite instance variable will be changed to the new Ellipse object
	 */
	public void setPieceSprite(Ellipse pieceSprite) {
		this.pieceSprite = pieceSprite;
	}

	/**
	 * @description returns the double mouseX instance variable
	 * @return double mouseX instance variable
	 */
	public double getMouseX() {
		return mouseX;
	}


	/**
	 * @description changes the instance variable mouseX
	 * @param mouseX - double value
	 * @postcondition mouseX instance variable is changed
	 */
	public void setMouseX(double mouseX) {
		this.mouseX = mouseX;
	}

	/**
	 * @description returns the double mouseY instance variable
	 * @return double mouseY instance variable
	 */
	public double getMouseY() {
		return mouseY;
	}

	/**
	 * @description changes the instance variable mouseY
	 * @param mouseY - double value
	 * @postcondition mouseY instance variable is changed
	 */
	public void setMouseY(double mouseY) {
		this.mouseY = mouseY;
	}

	/**
	 * @description changes the enumeration color of the piece
	 * @param color - the enumeration color you want to change to
	 * @precondition color must be one of the types listed in the enumeration PieceColor
	 * @postcondition the color is changed to the new type
	 */
	public void setColor(PieceColor color) {
		this.color = color;
	}

	/**
	 * @description moves the CheckerPiece object to a new location while still remembering the old location
	 * @param x - an integer identifying the x-coordinate you want to move to.
	 * @param y - an integer identifying the y-coordinate you want to move to.
	 * @postcondition - the CheckerPiece is moved to the new location specified by x,y and it's old position is saved as 
	 * 					oldXCoordinate and oldYCoordinate.
	 */
	
	public void move(int x, int y) {// make sure you remember the old coordinates then move the piece to the new
									// ones
		oldXCoordinate = x * CheckersGui.SQUARE_SIZE;
		oldYCoordinate = y * CheckersGui.SQUARE_SIZE;
		relocate(oldXCoordinate, oldYCoordinate);
	}

	
	/**
	 * @description moves the CheckerPiece to the location specified by it's oldXCoordinate and oldYCoordinate values
	 * @postcondition the CheckerPiece is moved to the "old" location
	 */
	public void cancelMove() {// returns the piece back to it's last known location
		relocate(oldXCoordinate, oldYCoordinate);
	}
	
	/**
	 * @description returns the color of the piece
	 * @return the color of the piece
	 */

	public PieceColor getColor() {
		return color;
	}
	
	
	/**
	 * @description returns the oldXCoordinate instance variable
	 * @return double oldXCoordinate
	 */

	public double getOldXCoordinate() {
		return oldXCoordinate;
	}
	
	/**
	 * @description changes the oldXCoordinate instance variable to a new value
	 * @param oldXCoordinate - double
	 * @postcondition the instance variable oldXCoordinate is changed to the new value
	 */
	public void setOldXCoordinate(double oldXCoordinate) {
		this.oldXCoordinate = oldXCoordinate;
	}

	/**
	 * @description returns the oldYCoordinate variable
	 * @return the double instance variable oldYCoordinate
	 */
	public double getOldYCoordinate() {
		return oldYCoordinate;
	}
	
	
 /**
  * @description Changes the oldYCoordinate variable
  * @param oldYCoordinate - an double that describes a location in x,y terms in a window
  * @postcondition oldYCoordinate is changed to the new value
  */
	public void setOldYCoordinate(double oldYCoordinate) {
		this.oldYCoordinate = oldYCoordinate;
	}

	/*
	 * private boardPosition xCoordinate, yCoordinate private boolean king; private
	 * int controllingPlayer;
	 *
	 *
	 *
	 * public checker (boardPosition, controllingPlayer){ this.boardPosition =
	 * boardPosition this.controllingPlayer = controllingPlayer
	 *
	 *
	 *
	 * }
	 *
	 * public boolean legalMove(boardSpace){ is it a piece that belongs to you? is
	 * moving the selected piece to the boardSpace a legal move is the space empty
	 * is it adjacent diagonal and towards the opposite side? (or just adjacent if
	 * king) is it a jump? is it on the board?
	 *
	 * if yes return true if no return false }
	 *
	 * public void move(boardSpace){ if(legalMove(boardSpace){ moves the selected
	 * piece to the boardSpace need to check if it jumped a piece or hit the edge if
	 * it did remove the jumped piece or king the piece respectively }
	 *
	 * public void render() {
	 *
	 * draw the piece at its boardPosition if player equals 1 its red if player
	 * equals 2 its black if king == true draw it as a king else regular piece }
	 *
	 * public void remove() {
	 *
	 * remove the piece from the board
	 *
	 * }
	 *
	 * }
	 *
	 */
}
