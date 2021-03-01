package Application.ServerApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket sender = null, reciver = null;
    ClientHandler(Socket sender, Socket reciver){
        this.reciver = reciver;
        this.sender = sender;
    }
    public void run(){
        String input = "";
        BufferedWriter outPutToSender = null,outPutToReciver = null;
        BufferedReader clientInput = null;
        try{
            while(true){
                outPutToSender = new BufferedWriter(new OutputStreamWriter(sender.getOutputStream()));
                outPutToReciver = new BufferedWriter(new OutputStreamWriter(reciver.getOutputStream()));
                clientInput = new BufferedReader(new InputStreamReader(sender.getInputStream()));
                //polacznie z wysylajacym
                System.out.println("Connection made with " + sender);
                input = clientInput.readLine();
                System.out.println("Recived: " + input);
            }
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }
}
