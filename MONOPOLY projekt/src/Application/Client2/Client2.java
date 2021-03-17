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
    private static int playersInGameVar = 0;
    public static void main(String[] args){
        int readyPlayers = 0;
        int playersInGame = 0;
        int playerTour = 1;
        int PlayerTourReady = 0;
        int passStart = 0;
        int ifPlayerBoughtProperty = 0;
        boolean firstTour = true;
        boolean gameSettingsReady = false;
        Player myProfile = new Player();
        Properties property = new Properties();
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
                        Properties propertyNew = new Properties();
                        propertyNew.setNameProperty(dIn.readUTF());
                        propertyNew.setPaymentForStay(Integer.parseInt(dIn.readUTF()));
                        propertyNew.setIDproperty(Integer.parseInt(dIn.readUTF()));
                        propertyNew.setBuyCost(Integer.parseInt(dIn.readUTF()));
                        propertyNew.setCountryName(dIn.readUTF());
                        propertyNew.setOwnerID(Integer.parseInt(dIn.readUTF()));
                        propertiesList.add(propertyNew);
                    }

//                    for(int i=0;i<propertiesList.size();i++){
//
//                    }
                }
                else if(info.equals("PlayersInGame")){
                    int number = dIn.readInt();
                    playersInGame = number;
                    playersInGameVar = number;
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

                else if(info.startsWith("UpdateMove ")){
                    String move = info.substring(11);
                    int ifPlayerPassStart = Character.getNumericValue(move.charAt(0));
                    int ifPlayerBought = Character.getNumericValue(move.charAt(2));
                    int playerMovedId = Character.getNumericValue(move.charAt(4));
                    System.out.println(ifPlayerBought + " " + ifPlayerPassStart + " " + playerMovedId);
                    Player oponent = new Player();
                    oponent = getPlayer(playersList,playerMovedId);
                    if(ifPlayerPassStart == 1){
                        oponent.setCash(oponent.getCash() + 400);
                        updatePlayersCash(playersList,playerMovedId,oponent.getCash());
                        System.out.println("Oponent pass the start!\n");
                    }
                    move = info.substring(17);
                    System.out.println(move);
                    int propertyId = Integer.parseInt(move);
                    Properties propertyOponent = new Properties();
                    propertyOponent = propertiesList.get(propertyId-1);
                    if(ifPlayerBought == 1){
                        propertyOponent.setOwnerID(playerMovedId);
                        oponent.setCash(oponent.getCash() - propertyOponent.getBuyCost());
                        updatePlayersCash(playersList,playerMovedId,oponent.getCash());
                        updatePropertiesList(propertiesList,propertyOponent);
                        System.out.println("Oponent bought city! " + propertyOponent.getIDproperty() +"\n");
                    }
                    System.out.println("Playermoved id: " + playerMovedId + " propertyId: " + propertyId);
                    updatePlayerMove(playersList,playerMovedId,propertyId);

                    if(propertyId == 5){
                        oponent.setCash(oponent.getCash() - propertiesList.get(propertyId-1).getPaymentForStay());
                        System.out.println(oponent.getPlayerName() + " have to pay fee " + propertiesList.get(propertyId-1).getPaymentForStay());
                    }

                    if(propertyId == 39){
                        oponent.setCash(oponent.getCash() - propertiesList.get(propertyId-1).getPaymentForStay());
                        System.out.println(oponent.getPlayerName() + " have to pay fee " + propertiesList.get(propertyId-1).getPaymentForStay());
                    }

                    if(playerMovedId != propertyOponent.getOwnerID() && propertyOponent.getOwnerID() != 0){
                        Player propertyOwner = new Player();
                        propertyOwner = playersList.get(propertyOponent.getOwnerID()-1);
                        propertyOwner.setCash(propertyOwner.getCash() + propertyOponent.getPaymentForStay());
                        oponent.setCash(oponent.getCash() - propertyOponent.getPaymentForStay());
                        System.out.println("Player " + oponent.getPlayerName() + " pay to " + propertyOwner.getPlayerName() + " " + propertyOponent.getPaymentForStay() + "$");
                        updatePlayersCash(playersList,propertyOwner.getPlayerNumber(),propertyOwner.getCash());
                        updatePlayersCash(playersList,oponent.getPlayerNumber(),oponent.getCash());
                    }
                }

                else if((info.startsWith("StartGame")) && (playersInGame == readyPlayers)){
                    if(!gameSettingsReady){
                        System.out.println("All Players are ready! Let's go!!!");
                        System.out.println("Game is starting...");

                    }

                    if(checkTourIndexPlayer(playersList,Integer.parseInt(info.substring(10)),nickname)){
                        if(firstTour){
                            System.out.println("Its your turn press <Enter> to Dice!");
                            scanner.nextLine();
                            int dice1 = dice.throwfunction();
                            int dice2 = dice.throwfunction();
                            System.out.println("Your Dice info:\n");
                            System.out.println("First dice is: " + dice1);
                            System.out.println("Secound dice is: " + dice2);
                            System.out.println("Sum of dices is: " + (dice2 + dice1) + "\n");

                            myProfile = getPlayer(playersList,nickname);
                            int newPosition = myProfile.getPropertyId() + dice1 + dice2;
                            myProfile.setPropertyId(newPosition);
                            updatePlayerMove(playersList,myProfile.getPlayerNumber(),newPosition);
                            //myProfile = getPlayer(playersList,nickname);

                            System.out.println("Property Info: \n");
                            property = propertiesList.get(myProfile.getPropertyId());
                            System.out.println("Id property: " + property.getIDproperty());
                            System.out.println("Country name: " + property.getCountryName());
                            System.out.println("City name: " + property.getNameProperty());
                            System.out.println("Buy cost: " + property.getBuyCost());
                            System.out.println("Stand cost: " + property.getPaymentForStay());

                            if(property.getOwnerID() == 0){
                                if(propertyBuyable(property)){
                                    System.out.println("Your cash: " + myProfile.getCash());
                                    System.out.println("Cost buy this property is: " + property.getBuyCost() + "$");
                                    System.out.println("Do you want to buy this property? (0-No OR 1-Yes): ");
                                    int answear = Integer.parseInt(scanner.nextLine());
                                    if(answear == 1){
                                        if(myProfile.getCash() >= property.getBuyCost()){
                                            ifPlayerBoughtProperty = 1;
                                            myProfile.setCash(myProfile.getCash() - property.getBuyCost());
                                            property.setOwnerID(myProfile.getPlayerNumber());
                                            System.out.println("Your cash after transaction: " + myProfile.getCash());
                                            updatePropertiesList(propertiesList,property);
                                        }
                                        else{
                                            System.out.println("You cant aford it!\n");
                                        }
                                    }
                                }
                                else{
                                    System.out.println("Your cash: " + myProfile.getCash());
                                    System.out.println("Property fee is: " + property.getPaymentForStay());
                                    myProfile.setCash(myProfile.getCash() - property.getPaymentForStay());
                                    System.out.println("Your cash after transaction: " + myProfile.getCash());
                                }
                            }
                            else if(property.getOwnerID() == myProfile.getPlayerNumber()){
                                System.out.println("its yours property take bear!");
                            }
                            else{
                                Player oponent = new Player();
                                oponent = getPlayer(playersList,property.getOwnerID());
                                System.out.println("This property have owner u have to pay " + property.getPaymentForStay() + "$ to " + oponent.getPlayerName());
                                myProfile.setCash(myProfile.getCash() - property.getPaymentForStay());
                                oponent.setCash(oponent.getCash() + property.getPaymentForStay());
                            }
                            getCashAllPlayers(playersList);
                            System.out.println("playersSize: " + playersList.size());

                            System.out.println("\nPress < enter > to end your tour: ");
                            scanner.nextLine();
                            System.out.println("Player ready before: " + PlayerTourReady);
                            dOut.writeUTF("PlayerTourReady " + passStart + " " + ifPlayerBoughtProperty + " " +Integer.parseInt(info.substring(10)) +  " " + property.getIDproperty());
                            ifPlayerBoughtProperty = 0;
                            PlayerTourReady += 1;
                            System.out.println("Player ready before: " + PlayerTourReady);
                            gameSettingsReady = true;
                            firstTour = false;
                        }
                        else{
                            //funkcja regulujaca pieniadze graczy
                            //checkPositionPlayers(playersList,propertiesList,Integer.parseInt(info.substring(10)));

                            System.out.println("Its your turn press <Enter> to Dice!");
                            scanner.nextLine();
                            int dice1 = dice.throwfunction();
                            int dice2 = dice.throwfunction();
                            System.out.println("Your Dice info:\n");
                            System.out.println("First dice is: " + dice1);
                            System.out.println("Secound dice is: " + dice2);
                            System.out.println("Sum of dices is: " + (dice2 + dice1) + "\n");


                            myProfile = getPlayer(playersList,nickname);
                            int newPosition = myProfile.getPropertyId() + dice1 + dice2;
                            System.out.println(newPosition);
                            if(newPosition + 1 == 31){
                                newPosition = 10;
                                System.out.println("\nYou go to jail!!!\n");
                            }

                            if(newPosition > 39){
                                newPosition = newPosition - 40;
                                passStart = 1;
                                System.out.println("Nice your acconut get 400$");
                            }
                            myProfile.setPropertyId(newPosition);
                            updatePlayerMove(playersList,myProfile.getPlayerNumber(),newPosition);
                            //myProfile = getPlayer(playersList,nickname);


                            System.out.println("Property Info: \n");
                            property = propertiesList.get(myProfile.getPropertyId());
                            System.out.println("Id property: " + property.getIDproperty());
                            System.out.println("Country name: " + property.getCountryName());
                            System.out.println("City name: " + property.getNameProperty());
                            System.out.println("Buy cost: " + property.getBuyCost());
                            System.out.println("Stand cost: " + property.getPaymentForStay());

                            if(property.getOwnerID() == 0){
                                if(propertyBuyable(property)){
                                    System.out.println("Your cash: " + myProfile.getCash());
                                    System.out.println("Cost buy this property is: " + property.getBuyCost() + "$");
                                    System.out.println("Do you want to buy this property? (0-No OR 1-Yes): ");
                                    int answear = Integer.parseInt(scanner.nextLine());
                                    if(answear == 1){
                                        if(myProfile.getCash() >= property.getBuyCost()){
                                            ifPlayerBoughtProperty = 1;
                                            myProfile.setCash(myProfile.getCash() - property.getBuyCost());
                                            property.setOwnerID(myProfile.getPlayerNumber());
                                            System.out.println("Your cash after transaction: " + myProfile.getCash());
                                            updatePropertiesList(propertiesList,property);
                                        }
                                        else{
                                            System.out.println("You cant aford it!\n");
                                        }
                                    }
                                }
                                else{
                                    System.out.println("Your cash: " + myProfile.getCash());
                                    System.out.println("Property fee is: " + property.getPaymentForStay());
                                    myProfile.setCash(myProfile.getCash() - property.getPaymentForStay());
                                    System.out.println("Your cash after transaction: " + myProfile.getCash());
                                }
                            }
                            else if(property.getOwnerID() == myProfile.getPlayerNumber()){
                                System.out.println("its yours property take bear!");
                            }
                            else{
                                Player oponent = new Player();
                                oponent = getPlayer(playersList,property.getOwnerID());
                                System.out.println("This property have owner u have to pay " + property.getPaymentForStay() + "$ to " + oponent.getPlayerName());
                                myProfile.setCash(myProfile.getCash() - property.getPaymentForStay());
                                oponent.setCash(oponent.getCash() + property.getPaymentForStay());
                            }
                            getCashAllPlayers(playersList);
                            System.out.println("playersSize: " + playersList.size());

                            System.out.println("\nPress < enter > to end your tour: ");
                            scanner.nextLine();
                            System.out.println("Player ready before: " + PlayerTourReady);
                            dOut.writeUTF("PlayerTourReady " + passStart + " " + ifPlayerBoughtProperty + " " +Integer.parseInt(info.substring(10)) +  " " + property.getIDproperty());
                            PlayerTourReady += 1;
                            ifPlayerBoughtProperty = 0;
                            System.out.println("Player ready before: " + PlayerTourReady);
                            passStart = 0;
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

    public static void checkPositionPlayers(ArrayList<Player> playersList,ArrayList<Properties> propertiesList,int skipIndex){
        for(int i =0;i<playersInGameVar;i++){
            if(i != skipIndex){
                Player player = new Player();
                player = playersList.get(i);
                Properties property = new Properties();
                property = propertiesList.get(player.getPropertyId());
                if(player.getPropertyId() != 0){
                    if(property.getOwnerID() != player.getPlayerNumber()){
                        Player oponent = new Player();
                        oponent = playersList.get(property.getOwnerID());
                        oponent.setCash(oponent.getCash() + property.getPaymentForStay());
                        player.setCash(player.getCash() - property.getPaymentForStay());
                        updatePlayersCash(playersList,oponent.getPlayerNumber(),oponent.getCash());
                        updatePlayersCash(playersList,player.getPlayerNumber(),player.getCash());
                    }
                }
            }
        }
    }

    public static void getCashAllPlayers(ArrayList<Player> playersList){
        for(Player p : playersList){
            System.out.println("Players " + p.getCash() + "$ " + p.getPlayerName());
        }
    }

    public static void updatePlayersCash(ArrayList<Player> playersList,int playerId,int newCash){
        playersList.get(playerId-1).setCash(newCash);
    }

    public static void updatePropertiesList(ArrayList<Properties> PropertiesList,Properties updatedProperty){
        PropertiesList.get(updatedProperty.getIDproperty()-1).setOwnerID(updatedProperty.getOwnerID());
    }

    public static boolean propertyBuyable(Properties property){
        switch (property.getIDproperty()){
            case 1:{return false;}
            case 3:{return false;}
            case 5:{return false;}
            case 8:{return false;}
            case 11:{return false;}
            case 18:{return false;}
            case 21:{return false;}
            case 23:{return false;}
            case 31:{return false;}
            case 34:{return false;}
            case 37:{return false;}
            case 39:{return false;}
            default: return true;
        }
    }

    public static void updatePlayerMove(ArrayList<Player> players,int idPlayer, int idProperty){
        for(Player p : players){
            if (p.getPlayerNumber() == idPlayer){
                p.setPropertyId(idProperty);
            }
        }
    }

    public static Player getPlayer(ArrayList<Player> players,int playerIndex){
        for(Player p : players){
            if (p.getPlayerNumber() == playerIndex){
                return p;
            }
        }
        return null;
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
