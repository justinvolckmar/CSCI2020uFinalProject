
import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import java.time.Duration;
import java.time.LocalTime;


public class Timer implements Runnable {
	public AnimationTimer gameTimer;
	protected static long minutes, seconds;
	@Override public void run() {
		gameTimer = new AnimationTimer() {
			//declare the startTime of type LocalTime
			private LocalTime startTime;
			BooleanProperty running = new SimpleBooleanProperty(false);
			
			@Override public void handle(long now) {
				long elapsedSeconds = Duration.between(startTime, LocalTime.now()).getSeconds();
				minutes = elapsedSeconds / 60 ;
				seconds = elapsedSeconds % 60 ;
				Minesweeper.timerLabel.setText("Elapsed Time: [" + minutes + ":" + seconds + "]");
			}
			//This method sets up the timer to start
			@Override public void start() {
				running.set(true);
				startTime = LocalTime.now();
				super.start();
			}
			//This method sets up the timer to stop
			@Override public void stop() {
				running.set(false);
				super.stop();
			}
		};
		gameTimer.start();
	}
}
