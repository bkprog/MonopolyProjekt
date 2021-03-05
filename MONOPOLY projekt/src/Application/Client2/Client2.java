package Application.Client2;

import Application.Sources.Player;
import Application.Sources.Properties;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client2 {
    private static Socket client;

    public static void main(String[] args){
        int readyPlayers = 0;
        int playersInGame = 0;
        ArrayList<Player> playersList = new ArrayList<>();
        ArrayList<Properties> propertiesList = new ArrayList<>();
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
                else if(info.equals("PropertiesSettings")&& (playersInGame == readyPlayers)){
                    for(int i =0 ;i<40;i++){
                        Properties property = new Properties();
                        property.setNameProperty(dIn.readUTF());
                        property.setPaymentForStay(Integer.parseInt(dIn.readUTF()));
                        property.setIDproperty(Integer.parseInt(dIn.readUTF()));
                        property.setBuyCost(Integer.parseInt(dIn.readUTF()));
                        property.setCountryName(dIn.readUTF());
                        property.setOwnerID(Integer.parseInt(dIn.readUTF()));
                        propertiesList.add(property);
                    }
                    for(int i=0;i<propertiesList.size();i++){
                        System.out.println("\n");
                        System.out.println("Field number " + propertiesList.get(i).getIDproperty());
                        System.out.println("Country name: " + propertiesList.get(i).getCountryName());
                        System.out.println("Property name: " + propertiesList.get(i).getNameProperty());
                        System.out.println("Buy cost: " + propertiesList.get(i).getBuyCost());
                        System.out.println("Payment for stay: " + propertiesList.get(i).getPaymentForStay());
                        System.out.println("\n");
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
                        if(dIn.readUTF().equals("YourTurn")){
                            System.out.println("Players are waiting for you enter some text... ");
                            String clientInput = scanner.nextLine();
                            dOut.writeUTF(clientInput);
                        }
                        else{
                            System.out.println("Oponents move wait for your turn!");
                        }
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
