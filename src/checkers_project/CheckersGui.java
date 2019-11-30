package checkers_project;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import javafx.application.Application;
import javafx.event.EventHandler;
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
import javafx.stage.Stage;

/*
Renders the GUI for the checkers game
*/
public class CheckersGui extends Application {

	public BoardState boardState;
	public static final int SQUARE_SIZE = 80;// tiles are 100 pixels wide
	public static final int SQUARES_WIDE = 8;// 8 tiles wide
	public static final int SQUARES_HIGH = 8;// 8 tiles high
	public static String movement = "";
	public static boolean canMove = true;
	private Group squareGroup = new Group();// keep track of all our squares
	private Group checkerPieceGroup = new Group();// keep track of all our pieces
	private byte gameTurn;
	private Square[][] gameBoard = new Square[SQUARES_WIDE][SQUARES_HIGH];// keep track of where all our squares are
	// creates the game board

	private Parent createBoard() {
		Pane board = new Pane();
		// Set preferred size
		board.setPrefSize(SQUARES_WIDE * SQUARE_SIZE + 200, SQUARES_HIGH * SQUARE_SIZE);// 8*8 squares 100 size each

		board.getChildren().addAll(squareGroup, checkerPieceGroup);
		// A panel that holds some testing equipment
		GridPane labelHolder = new GridPane();

		Button refreshButton = new Button("Refresh");
		Button backButton = new Button("Take back turn");
		labelHolder.add(refreshButton, 0, 0);
		labelHolder.add(backButton, 0, 3);
		board.getChildren().add(labelHolder);
		labelHolder.relocate(700, 0);

		DropShadow shadow = new DropShadow();

		refreshButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				refreshButton.setEffect(shadow);
			}
		});

		refreshButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				refreshButton.setEffect(null);
			}
		});

		backButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				backButton.setEffect(shadow);
			}
		});

		backButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				backButton.setEffect(null);
			}
		});

		refreshButton.setOnAction(value -> {
			this.refresh(boardState);
			movement = "";
		});
		backButton.setOnAction(value -> {
			this.boardState = Controller.boardStateHistory.pop();
			Controller.moveStringHistory.pop();
			this.refresh(boardState);
		});

		for (int y = 0; y < SQUARES_HIGH; y++) {// creating the squares and pieces
			for (int x = 0; x < SQUARES_WIDE; x++) {
				String chessLocation = (8 - y) + CheckersGui.convertNumberToString(x);
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

	/**
	 * 
	 * @param pixel
	 * @return
	 */
	private int toBoardCoordinates(double pixel) {
		// helps translate pixel coordinates into our 8x8 grid
		return (int) (pixel + SQUARE_SIZE / 2) / SQUARE_SIZE;
	}

	// creates a checkers piece
	private CheckerPiece createPiece(PieceColor color, int xCoordinate, int yCoordinate) {
		CheckerPiece piece = new CheckerPiece(color, xCoordinate, yCoordinate, this);

		piece.setOnMousePressed(e -> {// need to know where the mouse is when clicked
			piece.setMouseX(e.getSceneX());
			piece.setMouseY(e.getSceneY());
			int xLocation = toBoardCoordinates(piece.getLayoutX());
			int yLocation = toBoardCoordinates(piece.getLayoutY());
			CheckersGui.movement = gameBoard[xLocation][yLocation].getChessLocation();

		});

		piece.setOnMouseDragged(e -> {
			// move the piece with the mouse
			piece.relocate(e.getSceneX() - piece.getMouseX() + piece.getOldXCoordinate(),
					e.getSceneY() - piece.getMouseY() + piece.getOldYCoordinate());
		});

		piece.setOnMouseReleased(e -> {
			// back in the piece we can pick up the piece by clicking now we need to set
			// them down
			int newX = toBoardCoordinates(piece.getLayoutX());
			int newY = toBoardCoordinates(piece.getLayoutY());
			CheckersGui.movement += " " + gameBoard[newX][newY].getChessLocation();
			boolean result;
			boolean computerTurn = false;
			if (!boardState.isLegalMove(movement)) {
				piece.cancelMove();
				CheckersGui.movement = "";
			}
			if (boardState.isSecondMovePossible()) {
				String previousMove = Controller.getPreviousMove();
				result = boardState.isLegalMove(movement, previousMove);
			} else {
				result = boardState.isLegalMove(movement);
			}

			if (result) {
				Controller.boardStateHistory.push(boardState);
				Controller.moveStringHistory.push(movement);
				boardState.setSecondMove(BoardStateJumps.canJumpAtDestination(boardState, movement));
				boardState.preCheckedMove(movement);
				this.refresh(boardState);
				Controller.checkVictory(boardState);
				computerTurn = true;
				if (boardState.isSecondMovePossible()) {
					computerTurn = false;
				} else {
					// Move to the next turn
					this.gameTurn = (byte) ((this.gameTurn == 1) ? 2 : 1);
					this.boardState.nextTurn();
				}
				if (computerTurn) {
					this.boardState = Controller.getOpponentMove(this.boardState);
					this.gameTurn = boardState.getTurn();
					Controller.checkVictory(boardState);
					this.refresh(boardState);
					Controller.boardStateHistory.push(boardState);
					Controller.checkVictory(boardState);
				} else {
					CheckersGui.movement = "";
					this.refresh(boardState);
				}
			}
		});
		return piece;
	}

	// used to launch
	@Override
	public void start(Stage primaryStage) throws Exception {
		boardState = new BoardState();
		Controller.boardStateHistory.push(boardState);
		this.gameTurn = boardState.getTurn();
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

	public void refresh(BoardState boardState) {

		for (int x = 0; x < CheckersGui.SQUARES_WIDE; x++) {
			for (int y = 0; y < CheckersGui.SQUARES_HIGH; y++) {
				checkerPieceGroup.getChildren().remove(this.gameBoard[x][y].getPiece());
				this.gameBoard[x][y].setPiece(null);
			}
		}

		for (int y = 0; y < Game.ROWS; y++) {
			for (int x = 0; x < Game.COLUMNS; x++) {
				int myX = 7 - y;
				int myY;
				if (myX % 2 == 1) {
					myY = 2 * x;
				} else {
					myY = 2 * x + 1;
				}

				CheckerPiece piece = null;
				if (this.boardState.positions[y][x] == 0) {

				} else if (this.boardState.positions[y][x] == 1) {// black
					piece = createPiece(PieceColor.Red, myY, myX);
					this.gameBoard[myX][myY].setPiece(piece);
					checkerPieceGroup.getChildren().add(piece);

				} else if (this.boardState.positions[y][x] == 2) {// red
					piece = createPiece(PieceColor.Black, myY, myX);
					this.gameBoard[myX][myY].setPiece(piece);
					checkerPieceGroup.getChildren().add(piece);

				} else if (this.boardState.positions[y][x] == 3) {// black king
					piece = createPiece(PieceColor.Red, myY, myX);
					this.gameBoard[myX][myY].setPiece(piece);
					checkerPieceGroup.getChildren().add(piece);

				} else {// red king
					piece = createPiece(PieceColor.Black, myY, myX);
					this.gameBoard[myX][myY].setPiece(piece);
					checkerPieceGroup.getChildren().add(piece);

				}
			}
		}
	}

	public static void main(String[] args) {
		launch(args);
	}

}

// --module-path "C:\Users\Matthias Laptop\Desktop\openjfx-13.0.1_windows-x64_bin-sdk\javafx-sdk-13.0.1\lib" --add-modules javafx.controls,javafx.fxml
//--module-path "C:\Users\Matthias Laptop\Desktop\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml