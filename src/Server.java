
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.Date;

public class Server extends Application {

	protected BufferedReader reader;
	protected FileWriter writer;
	protected ObservableList<GameScore> scores;

	protected ServerSocket server;
	protected Socket socket;
	protected DataInputStream fromClient;
	protected DataOutputStream toClient;

	public static void main(String[] args) { launch(args); }

	@Override public void start(Stage window) throws EOFException { 
		new Thread(() ->  {
			System.out.println("Minesweeper Server Initiated at " + new Date());
			try { // Create server socket
				server = new ServerSocket(8000);
				//Listen for a connection request
				socket = server.accept();
				fromClient = new DataInputStream(socket.getInputStream());
				toClient = new DataOutputStream(socket.getOutputStream());
				//Create data input and output streams
				System.out.println("Communications begin at " + new Date());
				while (true) {
					//create a list to store scores
					scores = FXCollections.observableArrayList();
					try {
						//read in client data
						long minutes = fromClient.readLong();
						long seconds = fromClient.readLong();
						String date = minutes + ":" + seconds;
						GameScore client = new GameScore(fromClient.readInt(), fromClient.readInt(), fromClient.readInt(), fromClient.readInt(), date);
						System.out.println("Client data: " + client.toString());
						scores.add(client);
						System.out.println("Read in data from client");
						//read in top scores
						reader = new BufferedReader(new FileReader("src/Scoreboard.csv"));
						//read file into observable list
						String current = "";
						while ((current = reader.readLine()) != null) {
							String[] data = current.split(",");
							GameScore score = new GameScore(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]), data[4]);
							scores.add(score);
						}
						System.out.println("Read in data from scoreboard");
						//sort scores
						Collections.sort(scores, Collections.reverseOrder());
						System.out.println("Sorted data");
						//write new top 10 and return to client
						toClient.writeInt(scores.size());
						write(scores.size());
						System.out.println("Written to file and returned top 10 to client");
					} catch (EOFException f) { return; }
				}
			} catch (IOException e) { e.printStackTrace(); }
			window.setOnCloseRequest(e -> {
				try {
					toClient.close();
					fromClient.close();
					System.out.println("Closed ports at " + new Date());
				} catch (IOException f) { f.printStackTrace(); }
			});
		}).start();
	}

	public void write(int num) throws IOException {
		if (num > 10) { num = 10; }
		writer = new FileWriter("src/Scoreboard.csv", false);
		for (int i = 0 ; i < num ; i++) {
			GameScore s = scores.get(i);
			String current = s.score + "," + s.moves + "," + s.mines + "," + s.flags + "," + s.time;
			writer.write(current + "\n");
			String[] times = s.time.split(":");
			toClient.writeLong(Long.valueOf(times[0]));//minutes
			toClient.writeLong(Long.valueOf(times[1]));//seconds
			toClient.writeInt(s.score);
			toClient.writeInt(s.moves);
			toClient.writeInt(s.mines);
			toClient.writeInt(s.flags);
			toClient.flush();
		}
		writer.close();
	}
} 

