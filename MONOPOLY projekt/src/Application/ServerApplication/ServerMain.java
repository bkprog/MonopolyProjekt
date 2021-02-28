package Application.ServerApplication;


import java.net.*;
import java.io.*;


public class ServerMain {

    public static void main(String args[]) {

        int port = 2556;

        try(ServerSocket serverSocket = new ServerSocket(port)){
            System.out.println("Server listening on port: " + port);

            while(true){

                Socket socket = serverSocket.accept();

                System.out.println("Client connected!");

                OutputStream output = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(output,true);
                Dice dice = new Dice();

                int randomDiceFirst = dice.throwFunction();
                int randomDiceSecound = dice.throwFunction();


                writer.println(randomDiceFirst);
                writer.println(randomDiceSecound);

            }
        }

        catch(IOException ex){
            System.out.println("Server exeption: " + ex.getMessage());
            ex.printStackTrace();
        }

    }
}
