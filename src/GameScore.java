

public class GameScore implements Comparable<GameScore> {
	
	protected int score, moves, mines, flags;
	protected String time;
	
	public GameScore(int score, int moves, int mines, int flags, String time) {
		this.score = score;
		this.moves = moves;
		this.mines = mines;
		this.flags = flags;
		this.time = time;
	}
	
	public int getScore() { return score; }
	public int getMoves() { return moves; }
	public int getMines() { return mines; }
	public int getFlags() { return flags; }
	public String getTime() { return time; }

	@Override public int compareTo(GameScore other) {
		if (this.score > other.score) { return 1; }
		else if (this.score == other.score) {
			if (this.moves < other.moves) { return 1; } 
			else { return 0; }
		}
		else { return 0; }
	}
	
	@Override public String toString() {
		return this.score + "," + this.moves + "," + this.mines + "," + this.flags + "," + this.time;
	}
	
}
