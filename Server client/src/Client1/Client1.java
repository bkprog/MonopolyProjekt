package Client1;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client1 {
    public static void main(String[] args){
        Socket connectionSocket = null;
        BufferedWriter serverOutput;
        BufferedReader serverInput;
        String addresServer = "localhost",myUserName,opUserName,userInput,serverReply;
        int port = 2556;
        Scanner keyboard;
        boolean isFirst = false;
        keyboard = new Scanner(System.in);
        try{
            while(true){
                connectionSocket = new Socket(addresServer,port);
                serverOutput = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
                serverInput = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                System.out.println("Connection made.");
                System.out.print("Please enter a name you wished to be addressed as: ");
                myUserName = keyboard.nextLine();
                userInput = "HELLO " + myUserName + "\n";
                serverOutput.write(userInput);
                serverOutput.flush();
                serverReply = serverInput.readLine();
                System.out.println(serverReply);
                if(serverReply.equals("100")){
                    isFirst =true;
                    System.out.println("waiting for opponent...");
                    serverReply = serverInput.readLine();
                }
                opUserName = serverReply.substring(4);
                System.out.println("You have been matched with " + opUserName);
                break;
            }
        }
        catch (IOException ex){
            System.out.println("Error: " + ex.getMessage());
        }

    }
}
