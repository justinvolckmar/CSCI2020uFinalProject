
import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import java.time.Duration;
import java.time.LocalTime;


public class Timer implements Runnable {
	// initiate a new animationTimer object
	public AnimationTimer gameTimer;
	// declare minutes and seconds variables
	protected static long minutes, seconds;
	@Override public void run() {
		gameTimer = new AnimationTimer() {
			//declare the startTime of type LocalTime
			private LocalTime startTime;
			// boolean to check if timer is started or stopped
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
		// starts the timer
		gameTimer.start();
	}
}
