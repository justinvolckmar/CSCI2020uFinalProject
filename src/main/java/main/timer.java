package main;
import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import java.time.Duration;
import java.time.LocalTime;

import static main.Minesweeper.timerLabel;

public class timer implements Runnable {
	public static AnimationTimer gameTimer;
	@Override
	public void run() {
		gameTimer = new AnimationTimer() {
			private LocalTime startTime ;
			BooleanProperty running = new SimpleBooleanProperty(false);

			@Override
			public void handle(long now) {
				long elapsedSeconds = Duration.between(startTime, LocalTime.now()).getSeconds();
				long minutes = elapsedSeconds / 60 ;
				long seconds = elapsedSeconds % 60 ;
				timerLabel.setText("Time Taken: ("+minutes +" : "+seconds+")");
			}
			@Override
			public void start() {
				running.set(true);
				startTime = LocalTime.now();
				super.start();
			}
			@Override
			public void stop() {
				running.set(false);
				super.stop();
			}
		};
		gameTimer.start();
	}
}
