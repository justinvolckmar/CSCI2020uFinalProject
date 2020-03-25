package main.java.main;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GraphData {

    //public static void main(String[] args) { launch(args); }

    protected Minesweeper game;
    protected ScheduledExecutorService scheduledExecutorService;
    
    public GraphData(Minesweeper game) throws Exception { 
    	this.game = game;
    	launch();
    }
    
    public void launch() throws Exception {
    	Stage primaryStage = new Stage();
        primaryStage.setTitle("Score over Time");
        
        //defining the axes
        final CategoryAxis xAxis = new CategoryAxis(); // we are gonna plot against time
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (s)");
        xAxis.setAnimated(false); // axis animations are removed
        yAxis.setLabel("Total Score");
        yAxis.setAnimated(false); // axis animations are removed

        //creating the line chart with two axis created above
        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Score over Time Graph");
        lineChart.setAnimated(false); // disable animations

        //defining a series to display data
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        //series.setName("Score");

        // add series to chart
        lineChart.getData().add(series);

        // this is used to display time in HH:mm:ss format
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        // setup a scheduled executor to periodically put data into the chart
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        // put data onto graph per second
        scheduledExecutorService.scheduleAtFixedRate(() -> {

        	//schedule the score at a fixed rate
        	int score = game.totalScore;
        	
            // Update the chart
            Platform.runLater(() -> {
                // get current time
                Date now = new Date();
                // put random number with current time
                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), score));
            });
        }, 0, 1, TimeUnit.SECONDS);

        // setup scene
        Scene scene = new Scene(lineChart, 800, 600);
        primaryStage.setX(1100);
        primaryStage.setY(100);
        primaryStage.setScene(scene);

        // show the stage
        primaryStage.show();
    }
    
    public void stop() {
    	scheduledExecutorService.shutdown();
    }
}