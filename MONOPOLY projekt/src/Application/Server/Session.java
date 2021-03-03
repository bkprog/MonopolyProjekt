package Application.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Session extends Thread {
    private ArrayList<Socket> socketPlayers;
    private ArrayList playersList = new ArrayList();
    private Socket client;
    int playerNumber;

    public Session(Socket client, ArrayList<Socket> socketArray,int np){
        this.client = client;
        this.socketPlayers = socketArray;
        playerNumber = np;
    }

    public void run(){
        try{
            DataInputStream socketIn = new DataInputStream(client.getInputStream());
            String playerName = socketIn.readUTF();
            System.out.println(playerNumber + " Player nickname is " + playerName);
            if(playerNumber>1){
                System.out.println("Server is waiting for clients connection :)");
            }
            String information = "Player " + playerName + " joined to your session!";
            Broadcast(socketPlayers,information);
            while(true){

            }
        }
        catch(IOException ex){
            System.out.println("Session error: " + ex.getMessage());
        }
    }

    public void Broadcast(ArrayList<Socket> socketPlayers,String info){
        try{
            for(int i=0;i<socketPlayers.size();i++){
                DataOutputStream socketOut = new DataOutputStream(socketPlayers.get(i).getOutputStream());
                if(socketPlayers.get(i)==client){
                    socketOut.writeUTF("You have joined to the server!");
                }
                else{
                    socketOut.writeUTF(info);
                }
            }
//            for(Socket s : socketPlayers){
//                DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
//                socketOut.writeUTF(info);
//            }
        }
        catch(Exception e){
            System.out.println("Error broadcast info: " + e.getMessage());
        }
    }
}
