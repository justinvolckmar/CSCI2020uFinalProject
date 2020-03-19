import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import com.opencsv.CSVWriter;
import main.Main;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server extends Main {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //start of server side code
//        new Thread(() ->{
//           // Create server socket
//            try {
//                ServerSocket serverSocket = new ServerSocket(8000);
//                //Listen for a connection request
//                Socket socket = serverSocket.accept();
//                //Create data input and output streams
//                DataInputStream inputFromClient = new DataInputStream(
//                        socket.getInputStream());
//                DataOutputStream outputToClient = new DataOutputStream(
//                        socket.getOutputStream());
//                while (true){
//                    //declare csv variables
//                    String scoreCSV = "/src/main/ScoreBoard.csv";
//                    BufferedReader br = null;
//                    String line = "";
//                    String Splitoperator = ",";
//                    //declare an arraylist of type Scoreboard
//                    ObservableList<ScoreBoard> scoreArray = FXCollections.observableArrayList();
//                    try{
//                        br = new BufferedReader((New FileReader(scoreCSV)));
//                        //read file into observable list
//                        while ((line = br.readLine()) != null) {
//                            scoreArray.add(line.split(Splitoperator));
//                        }
//                    //this will catch the first run and create a file
//                    }catch (FileNotFoundException){
//                        //if the file does not exist create one
//                        CSVWriter csvWriter = new CSVWriter(new FileWriter("ScoreBoard.csv"));
//                        csvWriter.close();
//                    }
//                }
//                }
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }).start();
//        //end of server side code
//        //I have put the client code here so that it can be easily seen while coding
//        //the server backend to see the connections (It will be moved to the minesweep file later)
//        //----------------------------------------------------------------------------------------
//        //Start of the client side code
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
//    //this internal class will hold the name of the player and the score they got
//    //in the minesweeper class once the game has ended it will ask them to type their name and then
//    //the total score will be added to the class and the name form the textbox
//    class ScoreBoard{
//    String name;
//    int score;
//    public totalScore(String name, int score){
//        this.name = name;
//        this.score = score;
//    }
//    //getters
//    public String getName(){return name;}
//    public int getScore(){return score;}
//}
//}
