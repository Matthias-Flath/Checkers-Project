package checkers_project;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/*
ADT for the checkers board.   Holds an 8x8 matrix of board positions and what is
on each position. 

*/
//a tile on our board
public class Square extends Rectangle {

	private CheckerPiece piece;//tiles can have pieces
	
	public Square(boolean color, int xCoordinate, int yCoordinate) {//creates our squares
		setWidth(CheckersGui.SQUARE_SIZE);
		setHeight(CheckersGui.SQUARE_SIZE);

		relocate(xCoordinate * CheckersGui.SQUARE_SIZE, yCoordinate * CheckersGui.SQUARE_SIZE);
		if(color == false) {
			setFill(Color.LIGHTYELLOW);
		} else {
			setFill(Color.GREEN);
		}
	}
	
	public boolean hasPiece() {// does this tile have a piece?
		if(piece != null) {
			return true;
		} else {
			return false;
		}
	}
	
	public CheckerPiece getPiece() {//returns the piece if it has one
		return piece;
	}
	
	public void setPiece(CheckerPiece piece) {//sets the piece if it has one
		this.piece = piece;
	}
}
