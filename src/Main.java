
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) { launch(args); }
	
	public static Minesweeper game;
	public static GraphData scoreGraph;
	protected static Scene scene;
	
	public void start(Stage window) throws Exception, NumberFormatException {
		GridPane root = new GridPane();
		root.setAlignment(Pos.CENTER);
		root.setHgap(10); root.setVgap(10);
		scene = new Scene(root, 400, 300);
		Label minesweeper = new Label("Minesweeper Settings: ");
		Label size = new Label("Size (nxn): ");
		TextField sizeField = new TextField("10");
		Label mines = new Label("Mines: ");
		TextField minesField = new TextField("15");
		Button button = new Button("New Minesweeper");
		button.setMaxWidth(150);
		button.setOnAction(e -> { 
			try {
				game = new Minesweeper(window, Integer.parseInt(sizeField.getText()), Integer.parseInt(minesField.getText()));
				scoreGraph = new GraphData(game);
			} catch (NumberFormatException e1) { e1.printStackTrace(); } catch (Exception e1) { e1.printStackTrace(); } 
		});
		root.getChildren().addAll(minesweeper,size,sizeField,mines,minesField,button);
		GridPane.setConstraints(minesweeper,0,0);
		GridPane.setConstraints(size, 0, 1);
		GridPane.setConstraints(sizeField, 1, 1);
		GridPane.setConstraints(mines, 0, 2);
		GridPane.setConstraints(minesField, 1, 2);
		GridPane.setConstraints(button, 1, 3);
		window.setScene(scene);
		window.setTitle("Final Project Application Window");
		window.show();
	}

}
