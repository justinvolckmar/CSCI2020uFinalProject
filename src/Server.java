
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.*;
import java.net.InetAddress;
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
	protected ObservableList<Socket> sockets;
	protected int clientNum;

	public static void main(String[] args) { launch(args); }

	@Override public void start(Stage window) throws EOFException { 
		new Thread(() ->  {
			System.out.println("Minesweeper Server Initiated at " + new Date());
			try { // Create server socket
				server = new ServerSocket(8000);
				sockets = FXCollections.observableArrayList();
				//Listen for a connection request
				//Create data input and output streams
				while (true) {
					//create a list to store scores
					scores = FXCollections.observableArrayList();
					try {
						//read in client data
						Socket localSocket = server.accept();
						sockets.add(localSocket);
						new Thread(new HandleClient(sockets.get(clientNum), clientNum)).start();
						System.out.println("-- Connection Established -- client number: " + ++clientNum + " --");
						System.out.println("Local Time: " + new Date());
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

	protected class HandleClient implements Runnable {
		protected Socket socket;
		protected int socketNum;
		protected DataInputStream fromClient;
		protected DataOutputStream toClient;
		public HandleClient(Socket socket, int socketNum) {
			this.socket = socket;
			this.socketNum = socketNum;
		}
		@Override public void run() {
			new Thread(() -> {
				try {
					fromClient = new DataInputStream(socket.getInputStream());
					toClient = new DataOutputStream(socket.getOutputStream());
					InetAddress inetAddress = socket.getInetAddress();
					String hostname = inetAddress.getHostName();
					String ip = inetAddress.getHostAddress();
					System.out.println("Communications begin at " + new Date());
					long minutes = fromClient.readLong();
					long seconds = fromClient.readLong();
					String date = minutes + ":" + seconds;
					GameScore client = new GameScore(hostname, ip, clientNum, fromClient.readInt(), fromClient.readInt(), fromClient.readInt(), fromClient.readInt(), date);
					System.out.println("Client data: " + client.toString());
					scores.add(client);
					System.out.println("Read in data from client");
					//read in top scores
					reader = new BufferedReader(new FileReader("src/Scoreboard.csv"));
					//read file into observable list
					String current = "";
					while ((current = reader.readLine()) != null) {
						if (current != "") {
							String[] data = current.split(",");
							GameScore score = new GameScore(data[0], data[1], Integer.parseInt(data[2]), Integer.parseInt(data[3]), Integer.parseInt(data[4]), Integer.parseInt(data[5]), Integer.parseInt(data[6]), data[7]);
							scores.add(score);
						}
					}
					System.out.println("Read in data from scoreboard");
					//sort scores
					Collections.sort(scores, Collections.reverseOrder());
					System.out.println("Sorted data");
					//write new top 10 and return to client
					toClient.writeInt(scores.size());
					write(scores.size());
					System.out.println("Written to file and returned top 10 to client");
				} catch (IOException e) { }
			}).start();
		}

		public void write(int num) throws IOException {
			if (num > 10) { num = 10; }
			writer = new FileWriter("src/Scoreboard.csv", false);
			for (int i = 0 ; i < num ; i++) {
				GameScore s = scores.get(i);
				String current = s.hostname + "," + s.address + "," + s.clientNum + "," + s.score + "," + s.moves + "," + s.mines + "," + s.flags + "," + s.time;
				writer.write(current + "\n");
				String[] times = s.time.split(":");
				toClient.writeLong(Long.valueOf(times[0]));//minutes
				toClient.writeLong(Long.valueOf(times[1]));//seconds
				toClient.writeUTF(s.hostname);
				toClient.writeUTF(s.address);
				toClient.writeInt(s.clientNum);
				toClient.writeInt(s.score);
				toClient.writeInt(s.moves);
				toClient.writeInt(s.mines);
				toClient.writeInt(s.flags);
				toClient.flush();
			}
			writer.close();
		}
	}
} 

