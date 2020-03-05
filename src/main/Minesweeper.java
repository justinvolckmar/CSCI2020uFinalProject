package main;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Minesweeper {
	
	//nested layout placeholders
	protected Stage window;
	protected VBox root;
	protected HBox optionsBox, layout;
	
	//grid for buttons
	protected GridPane buttonBox;
	protected GridSlot[][] grid;
	
	//toggle for flags
	protected ToggleGroup group;
	protected RadioButton flag, select;
	
	//ui control
	protected Label gameOver, title;
	protected TextField flags, mine, score, moves;
	
	//size is (nxn) of grid, total # mines, buttonSize (based on buttons#), total score counter, total flag counter, and move tracker
	protected int size, mines, buttonSize, totalScore, totalFlags, numMoves;
	
	public class GridSlot extends Button {
		protected boolean hasMine, hasFlag;
		protected int x, y, score;
		public GridSlot(int x, int y) { hasMine = false; hasFlag = false; this.x = x; this.y = y; this.score = 0; }
	}
	
	public Minesweeper(Stage window, int size, int mines) throws Exception {
		//store default values
		this.window = window;
		this.size = size;
		this.mines = mines;
		//initialize root for minesweeper board
		start();
	}
	
	public void start() throws Exception {
		//create root box
		root = new VBox();
		root.setSpacing(20);
		root.setPadding(new Insets(20,20,20,20));
		root.setAlignment(Pos.CENTER);
		
		//set the scene to root
		Scene scene = new Scene(root,1920,1080);
		scene.getStylesheets().add(getClass().getResource("../main/Main.css").toExternalForm());
		
		//create side box for data
		layout = new HBox();
		layout.setSpacing(20);
		layout.setPadding(new Insets(20,20,20,20));
		layout.setAlignment(Pos.CENTER);
		
		//create options box for toggles
		optionsBox = new HBox();
		optionsBox.setSpacing(10);
		optionsBox.setAlignment(Pos.CENTER);
		
		//create toggle group and toggles
		group = new ToggleGroup();
		flag = new RadioButton("Flag");
		flag.setToggleGroup(group);
		select = new RadioButton("Select");
		select.setToggleGroup(group);
		select.setSelected(true);
		optionsBox.getChildren().addAll(select, flag);
		
		//initialize labels
		title = new Label("Minesweeper (" + size + "x" + size +")");
		gameOver = new Label();
		
		//create gridpane for GridSlot buttons
		buttonBox = new GridPane();
		buttonBox.setAlignment(Pos.CENTER);
		grid = new GridSlot[size][size];
		buttonSize = (int) (scene.getHeight()/(size+2));
		for (int i = 0 ; i < size ; i++) {//row index
			for (int j = 0 ; j < size ; j++) {//column index
				grid[i][j] = new GridSlot(i, j); //initialize local button
				grid[i][j].setPrefSize(buttonSize, buttonSize); //set size
				grid[i][j].setStyle("default-button"); //set style
				GridPane.setConstraints(grid[i][j], j, i); //set location
				grid[i][j].setFocusTraversable(false); //set not in focus loop
				buttonBox.getChildren().add(grid[i][j]); //add to gridpane
				grid[i][j].setOnAction(e -> { //set functionality
					//TODO add 1 to counter for numMoves, if numMoves == 0 : setMines(slot); where slot cannot have a mine.
					if (numMoves == 0) {
						setMines((GridSlot)e.getSource());
					}
					numMoves++;
					moves.setText("Moves: " + numMoves);//keep track of number of "moves"
					if (group.getSelectedToggle() == flag) { //if flagging mode: set flag on spot
						GridSlot slot = (GridSlot) e.getSource();
						if (!slot.hasFlag) {
							slot.setGraphic(new ImageView(new Image("images/flag.png", buttonSize/2, buttonSize/2, true, true)));
							slot.hasFlag = true;
							totalFlags++;
							flags.setText("Total Flags: " + totalFlags);
							if (!checkMines()) { gameOver(); }
						}
					} else { //if not flagging mode, handle action
						onAction((GridSlot) e.getSource());
					}
				});
			}
		}
		//initialize the mines randomly on the grid
		//setMines();
		layout.getChildren().addAll(buttonBox, showData()); // add to hbox linearly
		root.getChildren().addAll(title, layout, optionsBox); // add to vbox linearly
		//set the scene and launch the window
		window.setScene(scene);
		window.setTitle("Minesweeper");
		window.setX(0); window.setY(0);
		window.setMaximized(true);
		//window.setFullScreen(true);
		window.show();
	}//end of start()
	
	private void setMines(GridSlot slot) {
		int mineCount = 0;
		while (mineCount < mines) { //randomly place x amount of mines until they have all been placed
			int ranx = (int) (Math.random()*size);
			int rany = (int) (Math.random()*size);
			//mines are initialized on first move and cannot be placed on first move slot.
			if (!grid[ranx][rany].hasMine && !(ranx == slot.x && rany == slot.y)) {//check if a mine already exists before counting random placement
				grid[ranx][rany].hasMine = true;
				grid[ranx][rany].score = 9;
				mineCount++;
			}
		}
		findScores(); //calculate the scores on the grid based on their mine placement (scores stay hidden)
	}

	private VBox showData() {
		//create new vbox for all data outputs
		VBox data = new VBox();
		data.setSpacing(20);
		data.setAlignment(Pos.CENTER);
		
		//create score output
		score = new TextField("Score: " + totalScore);
		score.setEditable(false);
		score.setFocusTraversable(false);
		
		//create num moves output
		moves = new TextField("Moves: " + numMoves);
		moves.setEditable(false);
		moves.setFocusTraversable(false);
		
		//create num flags output
		flags = new TextField("Total Flags: " + totalFlags);
		flags.setEditable(false);
		flags.setFocusTraversable(false);
		
		//create num mines output
		mine = new TextField("Total Mines: " + mines);
		mine.setEditable(false);
		mine.setFocusTraversable(false);
		
		//add all outputs to data and return
		data.getChildren().addAll(score, moves, flags, mine);
		return data;
	}
	
	private void findScores() {
		//loop for all values in the grid
		for (int i = 0 ; i < size ; i++) {
			for (int j = 0 ; j < size ; j++) {
				if (grid[i][j].hasMine) { // if the index has a mine ; loop for all surrounding values
					for (int x = -1 ; x <= 1 ; x++) {
						for (int y = -1 ; y <= 1 ; y++) { //add 1 to any surrounding values score if it is not a mine.
							if (i+x >= 0 && i+x < size && j+y >= 0 && j+y < size && !(x == 0 && y == 0) && !grid[i+x][j+y].hasMine) {
								grid[i+x][j+y].score++;
							}
						}
					}
				}
			}
		}
	}

	private void gameOver() {
		optionsBox.getChildren().removeAll(select, flag);//remove radiobuttons
		boolean lose = checkMines(); //check for win
		if (lose) {
			gameOver.setText("You uncovered a mine! The game is now over. Restart?");
			gameOver.setTextFill(Color.RED);
		} else {
			gameOver.setText("You flagged all the mines! You win. Restart?");
			gameOver.setTextFill(Color.GREEN);
		}
		for (int i = 0 ; i < size ; i++) {
			for (int j = 0 ; j < size ; j++) { //if a gridslot has a flag and has a mine then add the score to the final
				if (grid[i][j].hasFlag && grid[i][j].score == 9) { totalScore += grid[i][j].score; }
			}
		}
		Button restart = new Button("New Game");
		restart.setOnAction(e -> { window.setScene(Main.scene); }); //start a new game
		optionsBox.getChildren().addAll(gameOver, restart);//add a new game button and win/loss message
		for (int i = 0 ; i < size ; i++) {
			for (int j = 0 ; j < size ; j++) {
				if (!grid[i][j].isDisabled()) {//if a grid slot is not disabled, disable it and check whether or not it's score
					grid[i][j].setDisable(true); //should be calculated
					if (!lose && grid[i][j].score < 9 && !grid[i][j].hasFlag) {
						totalScore += grid[i][j].score;
					} else if (grid[i][j].score > 0) { //if the score is not calculated, add a red border to the gridslot
						grid[i][j].getStyleClass().add("not-processed");
					}
				}
				if (grid[i][j].hasMine && grid[i][j].hasFlag) { //adjust the mines to be visible -- and flagged if hasFlag
					grid[i][j].setGraphic(new ImageView(new Image("images/mineflag.png", buttonSize/2, buttonSize/2, true, true)));
				} else if (grid[i][j].hasMine) {
					grid[i][j].setGraphic(new ImageView(new Image("images/mine.png", buttonSize/2, buttonSize/2, true, true)));
				}
			}
		}
		updateBoard(); //update the board to show values (board has been disabled at this point -- displays all values)
	}
	
	private boolean checkMines() {//returns true if any mine is still unflagged at end of game
		boolean mineUnflagged = false;//returns false if all mines are flagged at end of game
		for (int i = 0 ; i < size ; i++) {
			for (int j = 0 ; j < size ; j++) {
				if (grid[i][j].hasMine && !grid[i][j].hasFlag) {
					mineUnflagged = true;
				}
			}
		}
		return mineUnflagged;
	}

	
	public void onAction(GridSlot slot) {
		if (slot.hasMine || !checkMines()) {
			gameOver();
			//final score save implementation for a scores list savefile
			grid[slot.x][slot.y].getStyleClass().add("mine-clicked");//set the clicked mine to a red background
		} else {
			slot.setDisable(true);//disable the slot and compute the score
			if (slot.score > 0 && slot.score < 9 && !slot.hasFlag) { totalScore += slot.score; }
			for (int x = -1 ; x <= 1 ; x++) {
				for (int y = -1 ; y <= 1 ; y++) {//check if surrounding spots should be "clicked" too 
					if (slot.x+x >= 0 && slot.x+x < size && slot.y+y >= 0 && slot.y+y < size && !(x == 0 && y == 0) && !grid[slot.x+x][slot.y+y].hasMine) {
						if (!grid[slot.x+x][slot.y+y].isDisabled() && grid[slot.x][slot.y].score == 0) {
							onAction(grid[slot.x+x][slot.y+y]);
						}
					}
				}
			}
		}
		updateBoard();//update the board to the user
	}

	private void updateBoard() {
		for (int i = 0 ; i < size ; i++) {
			for (int j = 0 ; j < size ; j++) {//loop for all values, display correct score value if the grid slot is disabled
				if (grid[i][j].isDisabled() && grid[i][j].score > 0 && grid[i][j].score < 9) {
					if (grid[i][j].hasFlag) {
						grid[i][j].setGraphic(new ImageView(new Image("images/" + grid[i][j].score + "f.png", buttonSize/2, buttonSize/2, true, true)));
					} else {
						grid[i][j].setGraphic(new ImageView(new Image("images/" + grid[i][j].score + ".png", buttonSize/2, buttonSize/2, true, true)));
					}
				}
			}
		}
		score.setText("Score: " + totalScore);//update the score field
	}
}