package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) { launch(args); }

	public void start(Stage window) throws Exception {
		Pane root = new Pane();
		//Pane root = (Pane) FXMLLoader.load(getClass().getResource("../main/Main.fxml"));
		Scene scene = new Scene(root, 600, 400);
		scene.getStylesheets().add(getClass().getResource("../main/Main.css").toExternalForm());
		window.setScene(scene);
		window.setTitle("Final Project Application Window");
		window.show();
	}

}
