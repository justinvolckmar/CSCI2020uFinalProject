

public class GameScore implements Comparable<GameScore> {
	
	protected int clientNum, score, moves, mines, flags;
	protected String hostname, address, time;
	
	//Initialize a gamescore object with all known information about a client and their game result.
	public GameScore(String hostname, String ip, int clientNum, int score, int moves, int mines, int flags, String time) {
		this.hostname = hostname;
		address = ip;
		this.clientNum = clientNum;
		this.score = score;
		this.moves = moves;
		this.mines = mines;
		this.flags = flags;
		this.time = time;
	}
	
	//Getters used to fetch values to place in columns
	public String getHostname() { return hostname; }
	public String getAddress() { return address; }
	public int getClientNum() { return clientNum; }
	public int getScore() { return score; }
	public int getMoves() { return moves; }
	public int getMines() { return mines; }
	public int getFlags() { return flags; }
	public String getTime() { return time; }

	//Compare to function to handle sorting with default list sort comparisons
	@Override public int compareTo(GameScore other) {
		if (this.score > other.score) { return 1; }
		else if (this.score == other.score) {
			if (this.moves < other.moves) { return 1; } 
			else { return 0; }
		}
		else { return 0; }
	}
	
	//to String function to output basic info about a game
	@Override public String toString() {
		return this.score + "," + this.moves + "," + this.mines + "," + this.flags + "," + this.time;
	}
	
}
