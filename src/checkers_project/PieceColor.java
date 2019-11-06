package checkers_project;

/*
 * what color the piece is determines which direction it can move
 */
public enum PieceColor {

	Red(-1),
	Black(1);
	
	final int moveDir;
	
	PieceColor(int moveDir){
		this.moveDir = moveDir;
	}
}
