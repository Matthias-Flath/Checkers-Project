package checkers_project;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
 // git staging please
/*
Renders the GUI for the checkers game
*/

public class CheckersGui extends Application {

	public static final int SQUARE_SIZE = 80;// tiles are 100 pixels wide
	public static final int SQUARES_WIDE = 8;// 8 tiles wide
	public static final int SQUARES_HIGH = 8;// 8 tiles high
	public static String movement = "";
	public static boolean canMove = true;
	private Group squareGroup = new Group();// keep track of all our squares
	private Group checkerPieceGroup = new Group();// keep track of all our pieces

	private Square[][] gameBoard = new Square[SQUARES_WIDE][SQUARES_HIGH];// keep track of where all our squares are

	// creates the game board
	private Parent createBoard() {
		Pane board = new Pane();
		board.setPrefSize(SQUARES_WIDE * SQUARE_SIZE + 200, SQUARES_HIGH * SQUARE_SIZE);// 8*8 squares 100 size each
		board.getChildren().addAll(squareGroup, checkerPieceGroup);
		GridPane labelHolder = new GridPane();
		Label moveLabel = new Label("Movement Label");
		Button moveButton = new Button("Movement Button");
		labelHolder.add(moveButton, 0, 0);
		labelHolder.add(moveLabel, 0, 1);
		board.getChildren().add(labelHolder);
		labelHolder.relocate(700, 0);
		moveButton.setOnAction(value -> {
			moveLabel.setText(CheckersGui.movement);
			CheckersGui.movement = "";
		});

		for (int y = 0; y < SQUARES_HIGH; y++) {// creating the squares and pieces
			for (int x = 0; x < SQUARES_WIDE; x++) {
				String chessLocation = (8-y) + CheckersGui.convertNumberToString(x);
				Square square = new Square((x + y) % 2 == 0, x, y, chessLocation);// only alternating squares matter
				gameBoard[x][y] = square;// add them to our board
				squareGroup.getChildren().add(square);

				CheckerPiece piece = null;

				if (y <= 2 && (x + y) % 2 != 0) {// the top ones are black the bottom ones are red
					piece = createPiece(PieceColor.Black, x, y);
				}
				if (y >= 5 && (x + y) % 2 != 0) {
					piece = createPiece(PieceColor.Red, x, y);
				}

				if (piece != null) {
					square.setPiece(piece);
					checkerPieceGroup.getChildren().add(piece);
				}
			}
		}

		return board;
	}

	private static String convertNumberToString(int y) {
		int check = y;
		switch (check) {
		case 0:
			return "a";
		case 1:
			return "b";
		case 2:
			return "c";
		case 3:
			return "d";
		case 4:
			return "e";
		case 5:
			return "f";
		case 6:
			return "g";
		case 7:
			return "h";
		default:
			return null;

		}
	}

	/*
	 * x0, x1, y0, y1 are math terms for calculating differences usually
	 */
	private MoveResult tryMove(CheckerPiece piece, int newX, int newY) {
		if (gameBoard[newX][newY].hasPiece() || (newX + newY) % 2 == 0) {// if the square you are moving to is occupied
																			// illegal
			return new MoveResult(MoveType.Illegal);
		}

		int x0 = toBoardCoordinates(piece.getOldXCoordinate());// starting positions
		int y0 = toBoardCoordinates(piece.getOldYCoordinate());

		if (Math.abs(newX - x0) == 1 && newY - y0 == piece.getColor().moveDir) {// if it is the right direction for the
																				// piece normal move
			return new MoveResult(MoveType.Normal);
		} else if (Math.abs(newX - x0) == 2 && newY - y0 == piece.getColor().moveDir * 2) {// if it is a double move
			int x1 = x0 + (newX - x0) / 2;
			int y1 = y0 + (newY - y0) / 2;

			if (gameBoard[x1][y1].hasPiece() && gameBoard[x1][y1].getPiece().getColor() != piece.getColor()) {// check
																												// if it
																												// is
																												// jumping
																												// over
																												// a
																												// piece
				return new MoveResult(MoveType.Jump, gameBoard[x1][y1].getPiece());// move and delete
			}
		}

		return new MoveResult(MoveType.Illegal);// otherwise illegal move
	}

	private int toBoardCoordinates(double pixel) {// helps translate pixel coordinates into our 8x8 grid
		return (int) (pixel + SQUARE_SIZE / 2) / SQUARE_SIZE;
	}

	// creates a checkers piece
	private CheckerPiece createPiece(PieceColor color, int xCoordinate, int yCoordinate) {
		CheckerPiece piece = new CheckerPiece(color, xCoordinate, yCoordinate);

		piece.setOnMousePressed(e -> {// need to know where the mouse is when clicked
			piece.setMouseX(e.getSceneX());
			piece.setMouseY(e.getSceneY());
			int xLocation = toBoardCoordinates(piece.getLayoutX());
			int yLocation = toBoardCoordinates(piece.getLayoutY());
			CheckersGui.movement = gameBoard[xLocation][yLocation].getChessLocation();

		});

		piece.setOnMouseDragged(e -> {// move the piece with the mouse
			piece.relocate(e.getSceneX() - piece.getMouseX() + piece.getOldXCoordinate(),
					e.getSceneY() - piece.getMouseY() + piece.getOldYCoordinate());
		});

		piece.setOnMouseReleased(e -> {// back in the piece we can pick up the piece by clicking now we need to set
										// them down
			int newX = toBoardCoordinates(piece.getLayoutX());
			int newY = toBoardCoordinates(piece.getLayoutY());

			CheckersGui.movement += " " + gameBoard[newX][newY].getChessLocation();

			MoveResult result = tryMove(piece, newX, newY);// find out what kind of move we are attempting to do
			int x0 = toBoardCoordinates(piece.getOldXCoordinate());
			int y0 = toBoardCoordinates(piece.getOldYCoordinate());

			switch (result.getType()) {// moves piece based on what we are attempting to do
			case Illegal:
				piece.cancelMove();// returns piece to old coordinates
				break;
			case Normal:
				piece.move(newX, newY);// moves piece
				gameBoard[x0][y0].setPiece(null);// changes its location in our grid
				gameBoard[newX][newY].setPiece(piece);
				break;
			case Jump:
				piece.move(newX, newY);// moves piece
				gameBoard[x0][y0].setPiece(null);// changes its location in our grid
				gameBoard[newX][newY].setPiece(piece);

				CheckerPiece otherPiece = result.getPiece();// need to delete the piece we jumped over
				gameBoard[toBoardCoordinates(otherPiece.getOldXCoordinate())][toBoardCoordinates(
						otherPiece.getOldYCoordinate())].setPiece(null);
				checkerPieceGroup.getChildren().remove(otherPiece);
				break;

			default:
				break;

			}
		});

		return piece;

	}

	// used to launch
	@Override
	public void start(Stage primaryStage) throws Exception {

		Scene startScreen = new Scene(createStartScreen());
		primaryStage.setTitle("Checkers");
		primaryStage.setScene(startScreen);
		primaryStage.show();

	}

	private Parent createStartScreen() {
		GridPane startScreen = new GridPane();
		startScreen.setPrefSize(500, 500);
		Label title = new Label("\t\t    Checkers \n By James Lanska and Matthias Flath");
		Button onePlayerButton = new Button("One Player V.S. AI");
		Button twoPlayerButton = new Button("Two Player");
		Label ipLabel = new Label("IP Address");
		TextField ipTextField = new TextField();

		DropShadow shadow = new DropShadow();

		// Adding the shadow when the mouse cursor is on
		onePlayerButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				onePlayerButton.setEffect(shadow);
			}
		});

		twoPlayerButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				twoPlayerButton.setEffect(shadow);
			}
		});

		// Removing the shadow when the mouse cursor is off
		onePlayerButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				onePlayerButton.setEffect(null);
			}
		});

		twoPlayerButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				twoPlayerButton.setEffect(null);
			}
		});

		onePlayerButton.setOnAction(value -> {
			ipTextField.clear();
			ipTextField.setText("You clicked one player");
			;
			Stage game = new Stage();
			Scene board = new Scene(createBoard());
			game.setTitle("Checkers");
			game.setScene(board);
			game.show();
		});

		twoPlayerButton.setOnAction(value -> {
			ipTextField.clear();
			ipTextField.setText("You clicked two players but right now it's the same as one player");
			Stage game = new Stage();
			Scene board = new Scene(createBoard());
			game.setTitle("Checkers");
			game.setScene(board);
			game.show();
//	           
			/*
			 * Multiplayer.setIpAdress(ipTextField.getText(); createGameBoard(twoPlayers);
			 * show GameBoard hide this
			 */
		});
		startScreen.setVgap(15);
		startScreen.add(title, 2, 1);
		startScreen.add(onePlayerButton, 1, 2);
		startScreen.add(twoPlayerButton, 3, 2);
		startScreen.add(ipLabel, 1, 3);
		startScreen.add(ipTextField, 2, 3);
		return startScreen;
	}

	public static void main(String[] args) {
		launch(args);
	}

}

/*
 * 8*4 array to represent board states if the board states have an instance of
 * checkers Gui but it is declared null does it take space? When turn changes
 * check current board state and redraw Maybe Expand Board size to display whose
 * turn and a button to end turn
 * 
 * Button endTurnButton = new Button(End Turn)
 * 
 * endTurnButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new
 * EventHandler<MouseEvent>() {
 * 
 * @Override public void handle(MouseEvent e) { endTurnButton.setEffect(shadow);
 * } });
 *
 * // Removing the shadow when the mouse cursor is off
 * endTurnButton.addEventHandler(MouseEvent.MOUSE_EXITED, new
 * EventHandler<MouseEvent>() { public void handle(MouseEvent e) {
 * endTurnButton.setEffect(null); } });
 * 
 * endTurnButton.setOnAction(value -> { if(movesLeft ==0){ computerTurn(); } });
 * 
 * Move Logic needs an update if it is not your turn it is illegal move. a
 * moveCounter variable that will also prevent you from ending the turn when you
 * have not moved Label turnLabel = new Label(); turnLabel.setText(); use this
 * to tell you whose turn it is or a victory notification\ Gonna need an update
 * method that checks the current board state possibly attached to a button but
 * hopefully not;
 * 
 */

/*
 * Receipt Paper Notes Note 1 give each square a string mouse click and release
 * adds together then passes new combo string to james version addendum: if
 * square has piece make sure to clear string afterwards Note 2
 * Square2.setPiece(Square1.getPiece()); Square1.getPiece().move(Square2);
 * Square1.setPiece(null); Note 3 After Piece moves if it is king redraw it as
 * one Note 4 Will we have to Thread? Regardless need to make sure it updates
 * when Computer Moves as well Note 5 Rather than a loop use signal and response
 * Means we may not have to thread hopefully
 */

//--module-path "C:\Users\Matthias Laptop\Desktop\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml