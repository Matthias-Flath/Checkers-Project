package checkers_project.gui;

import checkers_project.BoardState;
import checkers_project.BoardStateJumps;
import checkers_project.engine.CheckersEngine;
import checkers_project.helpers.Controller;
import checkers_project.helpers.Game;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/*
Renders the GUI for the checkers game
*/

/**
 * 
 * @description
 * This builds the different stages and scenes used in the checkers gui. It also creates the event handlers responsible for 
 * picking up pieces and moving them around and ties that to the BoardState and CheckersEngine
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

	/**
	 * @description
	 * 	This method creates two panes. one to hold the entire window and one to hold the refresh and back buttons.
	 * 	Additionally it attaches the drop shadow effect to the buttons and sets what they actually do. Finally it creates
	 * 	and places the squares and starting positions of the checkers pieces.
	 * @return
	 * 		the game gui fully created and initialized to a starting BoardState.
	 */	
	private Parent createBoard() {
		this.boardState = new BoardState();//start a new game of checkers
		Controller.boardStateHistory.push(boardState);
		this.gameTurn = boardState.getTurn();
		Pane board = new Pane();
		// Set preferred size
		board.setPrefSize(SQUARES_WIDE * SQUARE_SIZE + 200, SQUARES_HIGH * SQUARE_SIZE);// 8*8 squares 100 size each

		board.getChildren().addAll(squareGroup, checkerPieceGroup);//make sure the squares and pieces can be added and removed from the screen
		// A panel that holds the refresh and back buttons
		GridPane labelHolder = new GridPane();
		Button refreshButton = new Button("Refresh");
		Button backButton = new Button("Take back turn");
		labelHolder.add(refreshButton, 0, 0);
		labelHolder.add(backButton, 0, 3);
		board.getChildren().add(labelHolder);
		labelHolder.relocate(700, 0);

		DropShadow shadow = new DropShadow();//a cool effect when you mouse over a button

		//adds the event handlers neccessary to make the cool effect work
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
		
		//set what the buttons actually do when clicked
		refreshButton.setOnAction(value -> {
			this.refresh(boardState);
			movement = "";
		});
		backButton.setOnAction(value -> {
			this.boardState = Controller.boardStateHistory.pop();
			this.gameTurn = this.boardState.getTurn();
			this.refresh(boardState);
		});
		//a for loop creating our initial squares and pieces
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

		return board;//return the gui
	}

	
	/**
	 * @description
	 * 		a static method used to convert the y coordinate of a square into chess notation
	 * @param y
	 * 		the y coordinate of a square on an 8x8 grid
	 * @return
	 * 		the chess notation of the column
	 */
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
	 * @description
	 * 		This is used to convert a pixel coordinate on the screen into a grid coordinate so that we can easily 
	 * 		translate from a like 800x800 grid into 8x8. calling it twice gets you an x and y coordinate
	 * @param pixel
	 * 		the pixel coordinate we want to convert 
	 * @return
	 * 		a translated int value
	 */
	private int toBoardCoordinates(double pixel) {
		// helps translate pixel coordinates into our 8x8 grid
		return (int) (pixel + SQUARE_SIZE / 2) / SQUARE_SIZE;
	}

	// creates a checkers piece
	
	/**
	 * @description 
	 * 		this method first creates a CheckersPiece object. Then it attaches three event handlers to that object.
	 * 		The first sets what happens when you click a piece, in this case it picks up the chess location of the square 
	 * it currently resides in.
	 * 		The Second makes the piece follow your mouse as you drag the cursor around.
	 * 		The third picks up the chess location you are dropping the piece then uses the current BoardState to determine 
	 * 		if it is a legal move. If not legal it returns the piece to the original position. If it is legal it moves the
	 * 			piece, checks victory then runs the computer turn then checks victory then passes the turn. Unless you can 
	 * 			double jump. Then it lets you jump again.
	 * @param color - the color the piece will be
	 * @param xCoordinate - the XCoordinate it will be placed at
	 * @param yCoordinate - the YCoordinate it will be placed at
	 * @return
	 * 		the newly created CheckerPiece with game logic and event handlers attached
	 */
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
			if (!boardState.isLegalMove(movement)) {//is this a legal move? if not then move it back.
				piece.cancelMove();
				CheckersGui.movement = "";
			}
			if (boardState.isSecondMovePossible()) {//is a double jump possible?
				String previousMove = Controller.getPreviousMove();
				result = boardState.isLegalMove(movement, previousMove);
			} else {
				result = boardState.isLegalMove(movement);
			}

			if (result) {//if a jump or double jump is possible
				Controller.boardStateHistory.push(boardState);//save the current boardstate so we can go back
				Controller.moveStringHistory.push(movement);//and save current movement
				boardState.setSecondMove(BoardStateJumps.canJumpAtDestination(boardState, movement));
				boardState.preCheckedMove(movement);//make the move now
				this.refresh(boardState);//redraw the board at the new boardstate
				if(Controller.checkVictory(boardState)) {//did anybody win? if so show the end screen
					Scene endScreen = new Scene(endScreen());
					Stage endStage = new Stage();
					endStage.setScene(endScreen);
					endStage.show();
				}
				computerTurn = true;//time to do the computers turn
				if (boardState.isSecondMovePossible()) {//unless we can double jump
					computerTurn = false;
				} else {
					// Move to the next turn
					this.gameTurn = (byte) ((this.gameTurn == 1) ? 2 : 1);
					this.boardState.nextTurn();
				}
				if (computerTurn) {//if it is the computers turn
					this.boardState = Controller.getOpponentMove(this.boardState);//use the current boardstate to get a move
					this.gameTurn = boardState.getTurn();//move the turn
					Controller.checkVictory(boardState);//did the computer win
					this.refresh(boardState);//redraw the board
					Controller.boardStateHistory.push(boardState);//save the board
					if(Controller.checkVictory(boardState)) {//if victory show the end screen
						Scene endScreen = new Scene(endScreen());
						Stage endStage = new Stage();
						endStage.setScene(endScreen);
						endStage.show();
					}
				} else {
					CheckersGui.movement = "";
					this.refresh(boardState);
				}
			}
		});
		return piece;
	}

	// used to launch
	/**
	 * 
	 * @description
	 * 	launch runs start. it's a javafx thing that shows the starting stage
	 * @postcondition
	 * 		the startscreen is created and shown
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {//starts the game
		Scene startScreen = new Scene(createStartScreen());//create a start screen
		primaryStage.setTitle("Checkers");
		primaryStage.setScene(startScreen);
		primaryStage.show();//show it

	}
	
	/**
	 * @description
	 * 		Creates the buttons and labels used in the start screen and attaches all the event hnadlers for those buttons
	 * 		and labels.
	 * @return
	 * 		the start screen of the game
	 * 
	 */

	private Parent createStartScreen() {
		GridPane startScreen = new GridPane();
		startScreen.setPrefSize(500, 500);//create a screen and some buttons and labels
		Label title = new Label("\t\t    Checkers \n By James Lanska and Matthias Flath");
		Label difficultyLabel = new Label("Choose your difficulty! Harder difficulties take longer to compute.\nAlso legendary and higher might crash my laptop because it is so bad.");
		Button easyButton = new Button("Easy");
		Button mediumButton = new Button("Medium");
		Button hardButton = new Button("Hard");
		Button legendaryButton = new Button("Legendary");
		Button superLegendaryButton = new Button("Super Legendary!");

		DropShadow shadow = new DropShadow();
		
		//add mouse event handlers including effects and button clicking
		easyButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				easyButton.setEffect(shadow);
			}
		});

		// Removing the shadow when the mouse cursor is off
		easyButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				easyButton.setEffect(null);
			}
		});

	
		mediumButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				mediumButton.setEffect(shadow);
			}
		});
		
		hardButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				hardButton.setEffect(shadow);
			}
		});

		// Removing the shadow when the mouse cursor is off
		hardButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				hardButton.setEffect(null);
			}
		});

		legendaryButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				legendaryButton.setEffect(shadow);
			}
		});

		// Removing the shadow when the mouse cursor is off
		legendaryButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				legendaryButton.setEffect(null);
			}
		});
		
		superLegendaryButton.addEventHandler(MouseEvent.MOUSE_ENTERED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				superLegendaryButton.setEffect(shadow);
			}
		});

		// Removing the shadow when the mouse cursor is off
		superLegendaryButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				superLegendaryButton.setEffect(null);
			}
		});
		
		
		
		// Removing the shadow when the mouse cursor is off
		mediumButton.addEventHandler(MouseEvent.MOUSE_EXITED, new EventHandler<MouseEvent>() {
			public void handle(MouseEvent e) {
				mediumButton.setEffect(null);
			}
		});

		//when clicked the button stars the game with a different difficulty based which button clicked
		easyButton.setOnAction(value -> {
			CheckersEngine.depth = 2;
			Stage game = new Stage();
			Scene board = new Scene(createBoard());
			game.setTitle("Checkers");
			game.setScene(board);
			game.show();
		});

		mediumButton.setOnAction(value -> {
			CheckersEngine.depth = 4;
			Stage game = new Stage();
			Scene board = new Scene(createBoard());
			game.setTitle("Checkers");
			game.setScene(board);
			game.show();
		});
		
		hardButton.setOnAction(value -> {
			CheckersEngine.depth = 6;
			Stage game = new Stage();
			Scene board = new Scene(createBoard());
			game.setTitle("Checkers");
			game.setScene(board);
			game.show();
		});
		
		legendaryButton.setOnAction(value -> {
			CheckersEngine.depth = 8;
			Stage game = new Stage();
			Scene board = new Scene(createBoard());
			game.setTitle("Checkers");
			game.setScene(board);
			game.show();
		});
		
		superLegendaryButton.setOnAction(value -> {
			CheckersEngine.depth = 15;
			Stage game = new Stage();
			Scene board = new Scene(createBoard());
			game.setTitle("Checkers");
			game.setScene(board);
			game.show();
		});
		//adds all the buttons and labels to the startscreen
		startScreen.setVgap(15);
		startScreen.add(title, 1, 1);
		startScreen.add(easyButton, 1, 2);
		startScreen.add(mediumButton, 2, 2);
		startScreen.add(hardButton, 1, 3);
		startScreen.add(legendaryButton, 2, 3);
		startScreen.add(superLegendaryButton, 1, 4);
		startScreen.add(difficultyLabel, 1, 5);
		return startScreen;
	}
	
/**
 * @description
 * 		Removes all the current pieces then creates new ones based on the boardstate
 * @param boardState - the boardstate we want to create pieces based on.
 */
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
					piece = createPiece(PieceColor.BlackKing, myY, myX);
					this.gameBoard[myX][myY].setPiece(piece);
					checkerPieceGroup.getChildren().add(piece);

				} else {// red king
					piece = createPiece(PieceColor.RedKing, myY, myX);
					this.gameBoard[myX][myY].setPiece(piece);
					checkerPieceGroup.getChildren().add(piece);

				}
			}
		}
	}

	/**
	 * @description
	 * 		the ending screen with a custom message based on difficulty and whether you won, lost, or draw.
	 * @return
	 * 		the ending screen
	 */		
	public Parent endScreen() {
		BorderPane endScreen = new BorderPane();
		// Set preferred size
		endScreen.setPrefSize(SQUARES_WIDE * SQUARE_SIZE + 200, SQUARES_HIGH * SQUARE_SIZE);// 8*8 squares 100 size each
		Label endLabel = new Label();
		String difficulty;
		if(CheckersEngine.depth == 2) {
			difficulty = "easy";
		} else if(CheckersEngine.depth == 4) {
			difficulty = "medium";
		}else if(CheckersEngine.depth == 6) {
			difficulty = "hard";
		}else if(CheckersEngine.depth == 8) {
			difficulty = "legendary";
		}else {
			difficulty = "super legendary";
		}
		if(boardState.checkBlackVictory()) {
			endLabel.setText("Congratulations!\nYou have defeated the Computer on " + difficulty + " mode. \nWhat a champ!");
		} else if(boardState.checkRedVictory()) {
			endLabel.setText("I'm Sorry.\nBut the Computer has defeated you on " + difficulty + " mode.");
		} else {
			endLabel.setText("A draw! What a performance!");
		}
			
		endScreen.setCenter(endLabel);
		return endScreen;
	}
	//launch the game
	public static void main(String[] args) {
		launch(args);
	}

}

// --module-path "C:\Users\Matthias Laptop\Desktop\openjfx-13.0.1_windows-x64_bin-sdk\javafx-sdk-13.0.1\lib" --add-modules javafx.controls,javafx.fxml
//--module-path "C:\Users\Matthias Laptop\Desktop\javafx-sdk-11.0.2\lib" --add-modules javafx.controls,javafx.fxml