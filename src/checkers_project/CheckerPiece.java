package checkers_project;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;


/*
Abstract Data Type for the Checkers Piece.
Descrives the set of actions that a checkers piece can make, and all of the
various information it can hold. 
*/

public class CheckerPiece extends StackPane {

	// is it a red or black piece
	private PieceColor color;
	private double mouseX, mouseY;// mouse coordinates for when you pick things up
	private double oldXCoordinate, oldYCoordinate;// so it can remember where it used to be
	private Ellipse pieceSprite;

	// a piece needs a color which also determines which direction it can move
	// it also needs starting coordinates
	public CheckerPiece(PieceColor color, int xCoordinate, int yCoordinate) {
		this.color = color;// set whether it is a black or red piece

		move(xCoordinate, yCoordinate);// move the piece to the starting coordinates

		this.pieceSprite = new Ellipse(CheckersGui.SQUARE_SIZE * 0.35, CheckersGui.SQUARE_SIZE * 0.35);// need to draw
																										// the piece

		if (color == PieceColor.Red) {// depending on if it is red or black fill it with that color
			pieceSprite.setFill(Color.RED);
		} else {
			pieceSprite.setFill(Color.BLACK);
		}

		pieceSprite.setStroke(Color.BLACK);
		pieceSprite.setStrokeWidth(CheckersGui.SQUARE_SIZE * 0.03);

		pieceSprite.setTranslateX((CheckersGui.SQUARE_SIZE - (CheckersGui.SQUARE_SIZE * 0.35 * 2)) / 2);
		pieceSprite.setTranslateY((CheckersGui.SQUARE_SIZE - (CheckersGui.SQUARE_SIZE * 0.35 * 2)) / 2);// make sure it
																										// is in middle
																										// of square

		getChildren().add(pieceSprite);

//		setOnMousePressed(e -> {//need to know where the mouse is when clicked
//			mouseX = e.getSceneX();
//			mouseY = e.getSceneY();
//		});
//		
//		setOnMouseDragged(e -> {//move the piece with the mouse
//			relocate(e.getSceneX() - mouseX + oldXCoordinate, e.getSceneY() - mouseY + oldYCoordinate);
//		});
	}

	public Ellipse getPieceSprite() {
		return pieceSprite;
	}

	public void setPieceSprite(Ellipse pieceSprite) {
		this.pieceSprite = pieceSprite;
	}

	public double getMouseX() {
		return mouseX;
	}

	public void setMouseX(double mouseX) {
		this.mouseX = mouseX;
	}

	public double getMouseY() {
		return mouseY;
	}

	public void setMouseY(double mouseY) {
		this.mouseY = mouseY;
	}

	public void setColor(PieceColor color) {
		this.color = color;
	}

	public void move(int x, int y) {// make sure you remember the old coordinates then move the piece to the new
									// ones
		oldXCoordinate = x * CheckersGui.SQUARE_SIZE;
		oldYCoordinate = y * CheckersGui.SQUARE_SIZE;
		relocate(oldXCoordinate, oldYCoordinate);
	}

	public void cancelMove() {// returns the piece back to it's last known location
		relocate(oldXCoordinate, oldYCoordinate);
	}

	public PieceColor getColor() {
		return color;
	}

	public double getOldXCoordinate() {
		return oldXCoordinate;
	}

	public void setOldXCoordinate(double oldXCoordinate) {
		this.oldXCoordinate = oldXCoordinate;
	}

	public double getOldYCoordinate() {
		return oldYCoordinate;
	}

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
