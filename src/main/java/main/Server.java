package main.java.main;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Server extends Application {
	
	protected BufferedReader reader;
	protected FileWriter writer;
	protected ObservableList<GameScore> scores;

	protected ServerSocket server;
	protected Socket socket;
	protected DataInputStream fromClient;
	protected DataOutputStream toClient;
	
	public static void main(String[] args) { launch(args); }
	
	@Override public void start(Stage window) throws IOException { 
		try { // Create server socket
			System.out.println("Minesweeper Server Initiated");
			server = new ServerSocket(8000);
			//Listen for a connection request
			socket = server.accept();
			//Create data input and output streams
			fromClient = new DataInputStream(socket.getInputStream());
			toClient = new DataOutputStream(socket.getOutputStream());
			while (true) {
				//create a list to store scores
				scores = FXCollections.observableArrayList();
				try {
					//read in top scores
					reader = new BufferedReader(new FileReader("src/files/Scoreboard.csv"));
					//read file into observable list
					String current = "";
					while ((current = reader.readLine()) != null) {
						String[] data = current.split(",");
						GameScore score = new GameScore(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]), Integer.parseInt(data[3]), data[4]);
						scores.add(score);
					}
					System.out.println("Read in data from scoreboard");
					//read in client data
					int minutes = fromClient.readInt();
					int seconds = fromClient.readInt();
					String date = minutes + ":" + seconds;
					GameScore score = new GameScore(fromClient.readInt(), fromClient.readInt(), fromClient.readInt(), fromClient.readInt(), date);
					scores.add(score);
					System.out.println("Read in data from client");
					//sort scores
					Collections.sort(scores, Collections.reverseOrder());
					System.out.println("Sorted data");
					//write new top 10 and return to client
					toClient.writeInt(scores.size());
					write(scores.size());
					System.out.println("Written to file and returned top 10 to client");
					return;
					//this will catch the first run and create a file
				} catch (FileNotFoundException f) {
					//if the file does not exist create one
					System.out.println("File did not exist");
					return;
				}
			}
		} catch (IOException e) { e.printStackTrace(); }
		toClient.close();
		fromClient.close();
		System.out.println("Closed ports at " + new Date());
		Group root = new Group();
		Scene scene = new Scene(root,600,400);
		window.setScene(scene);
		window.setTitle("Minesweeper Server");
		window.show();
	}

	public void write(int num) throws IOException {
		if (num > 10) { num = 10; }
		writer = new FileWriter("src/files/Scoreboard.csv", false);
		for (int i = 0 ; i < num ; i++) {
			GameScore s = scores.get(i);
			String current = s.score + "," + s.moves + "," + s.mines + "," + s.flags + "," + s.time;
			writer.write(current);
			String[] times = s.time.split(":");
			toClient.writeInt(Integer.parseInt(times[0]));//minutes
			toClient.writeInt(Integer.parseInt(times[1]));//seconds
			toClient.writeInt(s.score);
			toClient.writeInt(s.moves);
			toClient.writeInt(s.mines);
			toClient.writeInt(s.flags);
			toClient.flush();
		}
 		writer.close();
	}
} 
//        //end of server side code



//        //I have put the client code here so that it can be easily seen while coding
//        //the server backend to see the connections (It will be moved to the minesweep file later)
//        //----------------------------------------------------------------------------------------
//        //Start of the client side code (this code will need to be added to main set of code)
//        try {
//            //declare the array that will hold the name and score
//            ObservableList<ScoreBoard> scoreArraylocal = FXCollections.observableArrayList();
//            //read contents of the array on the server to a local array
//            for (int i = 0; i< toServer.scoreArray.length; i++)
//                scoreArraylocal.get(i) = toServer.scoreArray.get(i);
//
//            //end of this code
//        for (int i = 0; i< toServer.scoreArray.length; i++) {
//        //checks to see if the list is value is null
//        if (scoreArray.get(i) == null) {
//            scoreArray.get(i) == totalScore;
//            break;
//        //checks to see if the value is in the right position
//        }else if(totalScore > scoreArray[i] && totalScore < scoreArray.get(i+1)){
//            scoreArray.get(i) == totalScore;
//        }else{
//            continue;
//            }
//        //sorts the newly updated list
//        scoreArray.sort();
//        //write the updated array to the servers version
//        for (int i = 0; i< toServer.scoreArray.length; i++)
//            toServer.scoreArray.get(i) = scoreArraylocal.get(i);
//       toServer.WriteToFile();
//        //take the array find if it is greater than the specified value
//        //if it is trim the last entry
//        if (scoreArray.length >= 12)
//            scoreArray.remove(scoreArray.length - 1);
//        }
//            } catch (IOException ex) {
//                System.err.println(ex);
//            }
//        try {
//            //Create a socket to connect to the server
//            Socket socket = new Socket("localhost" , 8000);
//
//            //Create an input stream to receive data to the server
//            fromServer = new DataInputStream(socket.getInputStream());
//
//            //Create an output stream to send data to the server
//            toServer = new DataOutputStream(socket.getOutputStream());
//        }catch (IOException ex){
//            ta.appendText(ex.toString() + '\n');
//        }
//        //end of the client code
//    }

