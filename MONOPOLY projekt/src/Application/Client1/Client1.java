package Application.Client1;

import Application.Sources.Player;
import Application.Sources.Properties;
import Application.Sources.Dice;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client1 {
    private static Socket client;

    public static void main(String[] args){
        int readyPlayers = 0;
        int playersInGame = 0;
        int numbertour = 1;
        int readyTour = 0;
        boolean firstRound = true;
        boolean gameSettingsReady = false;
        Dice dice = new Dice();
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
//                    for(int i = 0; i< playersList.size();i++){
//                        System.out.println("Name: " + playersList.get(i).getPlayerName());
//                        System.out.println("Player ID: " + playersList.get(i).getPlayerNumber());
//                        System.out.println("His cash: " + playersList.get(i).getCash());
//                        System.out.println("Stand on: " + playersList.get(i).getPropertyId());
//                    }
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
//                    for(int i=0;i<propertiesList.size();i++){
//                        System.out.println("\n");
//                        System.out.println("Field number " + propertiesList.get(i).getIDproperty());
//                        System.out.println("Country name: " + propertiesList.get(i).getCountryName());
//                        System.out.println("Property name: " + propertiesList.get(i).getNameProperty());
//                        System.out.println("Buy cost: " + propertiesList.get(i).getBuyCost());
//                        System.out.println("Payment for stay: " + propertiesList.get(i).getPaymentForStay());
//                        System.out.println("\n");
//                    }

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
                    if(!gameSettingsReady){
                        System.out.println("All Players are ready! Let's go!!!");
                        System.out.println("Game is starting...");
                        gameSettingsReady = true;
                    }
                    numbertour = Integer.parseInt(dIn.readUTF());
                    System.out.println("tura numer: " + numbertour);
                    System.out.println("nick: " + nickname);
                    System.out.println("Player is standing on: ");

                    Properties property = new Properties();
                    property = propertiesList.get(0);

                    System.out.println("Cityname: " + property.getNameProperty());
                    System.out.println("Country name: " + property.getCountryName());
                    System.out.println("Buy cost: " + property.getBuyCost());
                    System.out.println("Payment for stay: " + property.getPaymentForStay());


                    if(checkTourIndexPlayer(playersList,numbertour,nickname)){
                        int dice1 = dice.throwfunction();
                        int dice2 = dice.throwfunction();
                        System.out.println("First dice: " + dice1);
                        System.out.println("Secound dice: " + dice2);
                        System.out.println("Sum of dices: " + (dice2 + dice1));
                        System.out.println("Players are waiting for you enter some text...");
                        scanner.nextLine();
                        dOut.writeUTF("readyTour");
                        readyTour++;
                    }
                    else{
                        System.out.println("Oponents move wait for your turn!");
                        System.out.println("[Press Enter]");
                        scanner.nextLine();
                        dOut.writeUTF("readyTour");
                        readyTour++;
                    }
                }
                else if((info.equals("Game")) && (playersInGame == readyPlayers) && (gameSettingsReady)){
                    String serverinfo = dIn.readUTF();
                    System.out.println(serverinfo);
                    int serverResponse = Integer.parseInt(dIn.readUTF());
                    System.out.println(serverResponse);
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
    public static boolean checkTourIndexPlayer(ArrayList<Player> players,int index,String nickname){
        Player myProfile = new Player();
        for(Player p : players){
            if(p.getPlayerName().equals(nickname))
                myProfile = p;
        }
        if(myProfile.getPlayerNumber() == index)
            return true;
        else
            return false;
    }
}
