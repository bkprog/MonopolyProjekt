package Server;

import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    public static void main(String[] args){
        Socket player1 = null, player2 = null,player3 = null,player4 =null;

        String firstPlayer = "";
        String secoundPlayer = "";
        String thirdPlayer = "";
        String fourthPlayer = "";
        String input = "";

        ClientHandler handler = null;

        BufferedReader clientInput1 = null;
        BufferedReader clientInput2 = null;
        BufferedReader clientInput3 = null;
        BufferedReader clientInput4 = null;

        BufferedWriter clientOutput1 = null;
        BufferedWriter clientOutput2 = null;
        BufferedWriter clientOutput3 = null;
        BufferedWriter clientOutput4 = null;

        int serverPort = 2556;

        try{
            ServerSocket serverSocket = new ServerSocket(serverPort);
            System.out.println("Server is listening on port: " + serverPort);
            while(true){
                player1 = serverSocket.accept();
                clientInput1 = new BufferedReader(new InputStreamReader(player1.getInputStream()));
                clientOutput1 = new BufferedWriter(new OutputStreamWriter(player1.getOutputStream()));
                input = clientInput1.readLine();
                if(input.startsWith("HELLO ")){
                    clientOutput1.write("100\n");
                    clientOutput1.flush();
                    firstPlayer = input.substring(6);

                    System.out.println(firstPlayer + " is now connected to server!");
                    //2 klient
                    player2 = serverSocket.accept();
                    clientInput2 = new BufferedReader(new InputStreamReader(player2.getInputStream()));
                    clientOutput2 = new BufferedWriter(new OutputStreamWriter(player2.getOutputStream()));

                    input = clientInput2.readLine();
                    secoundPlayer = input.substring(6);
                    System.out.println(secoundPlayer + " is now connected to server!");
                    clientOutput1.write("200 "+secoundPlayer+"\n");
                    clientOutput1.flush();
                    clientOutput2.write("200 "+firstPlayer+"\n");
                    clientOutput2.flush();

                    handler = new ClientHandler(player1,player2);
                    Thread theThread = new Thread(handler);
                    theThread.start();

                }
            }
        }
        catch(IOException e){
            System.out.println(e.getMessage());
        }
    }


}
