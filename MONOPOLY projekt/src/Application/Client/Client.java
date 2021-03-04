package Application.Client;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
    private static Socket client;

    public static void main(String[] args){
        int readyPlayers = 0;
        int playersInGame = 0;
        ArrayList<Player> playersList = new ArrayList<>();
        String hostname = "localhost";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your nickname: ");
        String nickname = scanner.nextLine();
        try(Socket client = new Socket(hostname,2115)){
            DataInputStream dIn = new DataInputStream(client.getInputStream());
            DataOutputStream dOut = new DataOutputStream(client.getOutputStream());

            dOut.writeUTF(nickname);
            while(true){
                String info = dIn.readUTF();
                if(info.equals("ReadyCheck")){
                    System.out.println(playersInGame);
                    System.out.println("All players joined the game time to ready check!!!");
                    boolean readyCheck = true;
                    do{
                        System.out.println("Enter < ready > if you ready: ");
                        String readyString = scanner.nextLine();
                        if(readyString.equals("ready")){
                            readyCheck = false;
                            readyPlayers++;
                            dOut.writeUTF("ReadyCheck" + nickname);
                        }
                    }while(readyCheck);
                    System.out.println("Great!!! You are ready!");
                }
                else if(info.equals("GameSettings") && (playersInGame == readyPlayers)){
                    for(int i =0 ;i<playersInGame;i++){
                        Player player = new Player();
                        player.setPlayerName(dIn.readUTF());
                        player.setPlayerNumber(Integer.parseInt(dIn.readUTF()));
                        player.setCash(Integer.parseInt(dIn.readUTF()));
                        player.setPropertyId(Integer.parseInt(dIn.readUTF()));
                        playersList.add(player);
                    }
                    for(int i = 0; i< playersList.size();i++){
                        System.out.println("Name: " + playersList.get(i).getPlayerName());
                        System.out.println("Player ID: " + playersList.get(i).getPlayerNumber());
                        System.out.println("His cash: " + playersList.get(i).getCash());
                        System.out.println("Stand on: " + playersList.get(i).getPropertyId());
                    }
                }
                else if(info.equals("PlayersInGame")){
                    int number = dIn.readInt();
                    playersInGame = number;
                }
                else if(info.startsWith("PlayerReady")){
                    System.out.println("Player " + info.substring(11) +" is Ready!");
                    readyPlayers++;

                }
                else if((info.equals("StartGame")) && (playersInGame == readyPlayers)){
                    System.out.println("All Players are ready! Let's go!!!");
                    System.out.println("Game is starting...");

                    while(true){

                    }
                }
                else if(info.startsWith("You have") || info.startsWith("Player ")){
                    System.out.println(info);
                }
            }

        }
        catch(IOException ex){
            System.out.println("Error: " + ex.getMessage());
        }

    }
}
