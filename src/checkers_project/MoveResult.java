package checkers_project;

public class MoveResult {

	// a result is what piece and how did it move
	// did this piece try an illegal move, a normal move, or a jump
	private MoveType type;
	private CheckerPiece piece;

	public MoveResult(MoveType type, CheckerPiece piece) {
		this.type = type;
		this.piece = piece;
	}

	public MoveResult(MoveType type) {
		this.type = type;
		this.piece = null;
	}

	public CheckerPiece getPiece() {
		return piece;
	}

	public MoveType getType() {
		return type;
	}
}
