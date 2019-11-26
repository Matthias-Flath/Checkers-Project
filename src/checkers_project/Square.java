package checkers_project;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/*
ADT for the checkers board.   Holds an 8x8 matrix of board positions and what is
on each position. 

*/
//a tile on our board
public class Square extends StackPane {

	private CheckerPiece piece;//tiles can have pieces
	private String chessLocation;
	
	public Square(boolean color, int xCoordinate, int yCoordinate, String chessLocation) {//creates our squares
//		setWidth(CheckersGui.SQUARE_SIZE);
//		setHeight(CheckersGui.SQUARE_SIZE);
		this.setChessLocation(chessLocation);
		relocate(xCoordinate * CheckersGui.SQUARE_SIZE, yCoordinate * CheckersGui.SQUARE_SIZE);
		Rectangle squareSprite = new Rectangle(CheckersGui.SQUARE_SIZE, CheckersGui.SQUARE_SIZE);
		
		if(color == false) {
			squareSprite.setFill(Color.LIGHTYELLOW);
		} else {
			squareSprite.setFill(Color.GREEN);
		}
		
		getChildren().add(squareSprite);
		
		setOnMouseClicked(e -> {
			if(CheckersGui.movement.length() > 5) {
				CheckersGui.movement = "";
			}
			CheckersGui.movement += this.getChessLocation() + " ";
		});
		
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

	public String getChessLocation() {
		return chessLocation;
	}

	public void setChessLocation(String chessLocation) {
		this.chessLocation = chessLocation;
	}
	
	
}
