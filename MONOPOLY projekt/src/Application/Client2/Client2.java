package Application.Client2;

import Application.Sources.Dice;
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
        int playerTour = 1;
        int PlayerTourReady = 0;
        boolean firstTour = true;
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
                }
                else if(info.equals("PropertiesSettings") && (playersInGame == readyPlayers)){

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
                }
                else if(info.equals("PlayersInGame")){
                    int number = dIn.readInt();
                    playersInGame = number;
                }
                else if(info.startsWith("PlayerReady")){
                    System.out.println("Player " + info.substring(11) +" is Ready!");
                    readyPlayers++;
                }

                else if(info.equals("PlayerTourReady")){
                    PlayerTourReady += 1;
                    System.out.println("Players ready: " + PlayerTourReady);
                    if(PlayerTourReady == playersInGame){
                        System.out.println("Players before: " + PlayerTourReady);
                        PlayerTourReady = 0;
                        System.out.println("Players after: " + PlayerTourReady);
                        dOut.writeUTF("allPlayersTourReady");
                    }
                }

                else if((info.startsWith("StartGame")) && (playersInGame == readyPlayers)){
                    if(!gameSettingsReady){
                        System.out.println("All Players are ready! Let's go!!!");
                        System.out.println("Game is starting...");

                    }

                    if(checkTourIndexPlayer(playersList,Integer.parseInt(info.substring(10)),nickname)){
                        if(firstTour){
                            System.out.println("Its your turn press <Enter>");
                            scanner.nextLine();
                            System.out.println("Player ready before: " + PlayerTourReady);
                            dOut.writeUTF("PlayerTourReady");
                            PlayerTourReady += 1;
                            System.out.println("Player ready before: " + PlayerTourReady);
                            gameSettingsReady = true;
                            firstTour = false;
                        }
                        else{
                            System.out.println("Its your turn press <Enter>");
                            scanner.nextLine();
                            System.out.println("Player ready before: " + PlayerTourReady);
                            dOut.writeUTF("PlayerTourReady");
                            PlayerTourReady += 1;
                            System.out.println("Player ready before: " + PlayerTourReady);
                            gameSettingsReady = true;
                            firstTour = false;
                        }

                    }

                    if(!checkTourIndexPlayer(playersList,Integer.parseInt(info.substring(10)),nickname)){
                        if(firstTour){
                            System.out.println("Oponents move, wait for your turn! \nPress <Enter>");
                            scanner.nextLine();
                            System.out.println("Player ready before: " + PlayerTourReady);
                            dOut.writeUTF("PlayerTourReady");
                            PlayerTourReady += 1;
                            System.out.println("Player ready after: " + PlayerTourReady);
                            gameSettingsReady = true;
                            firstTour = false;
                        }
                        else{
                            System.out.println("Oponents move, wait for your turn! \nPress <Enter>");
                            scanner.nextLine();
                            System.out.println("Player ready before: " + PlayerTourReady);
                            dOut.writeUTF("PlayerTourReady");
                            PlayerTourReady += 1;
                            System.out.println("Player ready before: " + PlayerTourReady);
                            gameSettingsReady = true;
                            firstTour = false;
                        }
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

    public static void updatePlayerMove(ArrayList<Player> players,int idPlayer, int idProperty){
        for(Player p : players){
            if (p.getPlayerNumber() == idPlayer){
                p.setPropertyId(idProperty);
            }
        }
    }

    public static Player getPlayer(ArrayList<Player> players,String nickName){
        for(Player p : players){
            if (p.getPlayerName().equals(nickName)){
                return p;
            }
        }
        return null;
    }

    public static boolean checkTourIndexPlayer(ArrayList<Player> players,int index,String nickname){
        Player myProfile = new Player();
        for(Player p : players){
            if(p.getPlayerName().equals(nickname))
                myProfile = p;
        }
        int temp = myProfile.getPlayerNumber();
        if(temp == index)
            return true;
        else
            return false;
    }
}
