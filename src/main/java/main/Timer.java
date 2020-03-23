package main.java.main;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import java.time.Duration;
import java.time.LocalTime;

import static main.java.main.Minesweeper.timerLabel;

public class Timer implements Runnable {
	public AnimationTimer gameTimer;
	protected long minutes, seconds;
	@Override public void run() {
		gameTimer = new AnimationTimer() {
			private LocalTime startTime;
			BooleanProperty running = new SimpleBooleanProperty(false);
			
			@Override public void handle(long now) {
				long elapsedSeconds = Duration.between(startTime, LocalTime.now()).getSeconds();
				minutes = elapsedSeconds / 60 ;
				seconds = elapsedSeconds % 60 ;
				timerLabel.setText("Elapsed Time: [" + minutes + ":" + seconds + "]");
			}
			
			@Override public void start() {
				running.set(true);
				startTime = LocalTime.now();
				super.start();
			}
			
			@Override public void stop() {
				running.set(false);
				super.stop();
			}
		};
		gameTimer.start();
	}
}
