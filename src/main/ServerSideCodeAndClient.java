import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class Server extends Main {
    @Override
    public void start(Stage primaryStage) throws Exception {
        //start of server side code
        new Thread(() ->{
           // Create server socket
            try {
                ServerSocket serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> ta.appendText("Server started at " +
                        new Date() + '\n'));

                //Listen for a connection request
                Socket socket = serverSocket.accept();

                //Create data input and output streams
                DataInputStream inputFromClient = new DataInputStream(
                        socket.getInputStream());
                DataOutputStream outputToClient = new DataOutputStream(
                        socket.getOutputStream());

                while (true){
                    //this will be the array that holds the total Scores
                    ObservableList<ScoreBoard> scoreArray = FXCollections.observableArrayList();
                    //need to write this array to a csv file and then load at begining
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();
        //end of server side code
        //I have put the client code here so that it can be easily seen while coding
        //the server backend to see the connections (It will be moved to the minesweep file later)
        //----------------------------------------------------------------------------------------
        //Start of the client side code
        try {
            toServer.flush();
            for (int i = 0; i< toServer.scoreArray.length; i++) {
                //checks to see if the list is value is null
                 if (scoreArray[i] == null) {
                    scoreArray[i] == totalScore;
                    break;
                    //checks to see if the value is in the right position
                }else if(totalScore > scoreArray[i] && totalScore < scoreArray[i+1]){
                    scoreArray[i] == totalScore;
                }else{
                    continue;
                }
                //sorts the newly updated list
                scoreArray.sort();
                //take the array find if it is greater than the specified value
                //if it is trim the last entry
                if (scoreArray.length >= 12)
                    scoreArray.remove(scoreArray.length - 1);
                }
            } catch (IOException ex) {
                System.err.println(ex);
            }
        try {
            //Create a socket to connect to the server
            Socket socket = new Socket("localhost" , 8000);

            //Create an input stream to receive data to the server
            fromServer = new DataInputStream(socket.getInputStream());

            //Create an output stream to send data to the server
            toServer = new DataOutputStream(socket.getOutputStream());
        }catch (IOException ex){
            ta.appendText(ex.toString() + '\n');
        }
        //end of the client code
    }
    //this internal class will hold the name of the player and the score they got
    //in the minesweeper class once the game has ended it will ask them to type their name and then
    //the total score will be added to the class and the name form the textbox
    class ScoreBoard{
    String name;
    int score;
    public totalScore(String name, int score){
        this.name = name;
        this.score = score;
    }
    //getters
    public String getName(){return name;}
    public int getScore(){return score;}
}
}
