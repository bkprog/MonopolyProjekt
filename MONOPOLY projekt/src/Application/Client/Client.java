package Application.Client;


import Application.Sources.Dice;
import Application.Sources.Player;
import Application.Sources.Properties;
import Application.Sources.BlueRedCards;

import java.net.*;
import java.io.*;
import java.util.*;

public class Client {
    private static Socket client;
    private static int playersInGameVar = 0;
    public static void main(String[] args){
        int readyPlayers = 0;
        int playersInGame = 0;
        int playerTour = 1;
        char blueRedCardID = '0';
        int PlayerTourReady = 0;
        int passStart = 0;
        int ifPlayerBoughtProperty = 0;
        boolean firstTour = true;
        boolean gameSettingsReady = false;
        Player myProfile = new Player();
        Properties property = new Properties();
        Dice dice = new Dice();
        ArrayList<BlueRedCards> blueRedCardsList = new ArrayList<>();
        blueRedCardsList = initializeRandomCards();
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
                        propertyNew.setLvl1(Integer.parseInt(dIn.readUTF()));
                        propertyNew.setLvl2(Integer.parseInt(dIn.readUTF()));
                        propertyNew.setLvl3(Integer.parseInt(dIn.readUTF()));
                        propertyNew.setLvl4(Integer.parseInt(dIn.readUTF()));
                        propertiesList.add(propertyNew);
                    }

                    for(int i=0;i<propertiesList.size();i++){
                        Properties prop = propertiesList.get(i);
                        System.out.println("\nPropertyname: " + prop.getNameProperty());
                        System.out.println("Payyment for stay with lvl " + prop.getActualLvlProperty() + " is: " + prop.getPaymentForStay());
                        prop.buildPropertyLvl1();
                        System.out.println("Payyment for stay with lvl " + prop.getActualLvlProperty() + " is: " + prop.getPaymentForStay());
                        prop.buildPropertyLvl2();
                        System.out.println("Payyment for stay with lvl " + prop.getActualLvlProperty() + " is: " + prop.getPaymentForStay());
                        prop.buildPropertyLvl3();
                        System.out.println("Payyment for stay with lvl " + prop.getActualLvlProperty() + " is: " + prop.getPaymentForStay());
                        prop.buildPropertyLvl4();
                        System.out.println("Payyment for stay with lvl " + prop.getActualLvlProperty() + " is: " + prop.getPaymentForStay());
                        prop.destroyHouses();
                        System.out.println("Payyment for stay with lvl " + prop.getActualLvlProperty() + " is: " + prop.getPaymentForStay());
                    }
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

                else if(info.startsWith("CardMove ")){
                    int cardID = card4ClientFromServer(info.charAt(9));
                    int playerID = Character.getNumericValue(info.charAt(11));
                    System.out.println("Player id: " + playerID);
                    System.out.println("Card id: " + cardID);
                    if(cardID != 0){
                        BlueRedCards card = new BlueRedCards();
                        card = blueRedCardsList.get(cardID-1);
                        System.out.println("Card Reward: " + card.getCashReward());
                        System.out.println("Card fine: " + card.getCashFine());
                        Player oponent = new Player();
                        oponent = playersList.get(playerID-1);
                        oponent.setCash(oponent.getCash() - card.getCashFine());
                        oponent.setCash(oponent.getCash() + card.getCashReward());
                        if(card.getDestinationField()>0){
                            if(oponent.getPropertyId() > propertiesList.get(card.getDestinationField()-1).getIDproperty() && cardID != 9){
                                oponent.setCash(oponent.getCash() + 400);
                                System.out.println("Oponent pass Start and get +400$");
                            }
                        }
                        updatePlayersCash(playersList,oponent.getPlayerNumber(),oponent.getCash());
                    }
                }

                else if(info.startsWith("PrisonDecision ")){
                    String clienres = info.substring(14);
                    System.out.println(clienres);
                    int prisonDecision = Character.getNumericValue(info.charAt(15));
                    int playerIndex = Character.getNumericValue(info.charAt(17));
                    int prisonBuy = Character.getNumericValue(info.charAt(19));
//                    System.out.println("Prison decision: " + prisonDecision);
//                    System.out.println("Player index: " + playerIndex);
//                    System.out.println("Prison buy: " + prisonBuy);

                    if(prisonDecision == 1){
                        System.out.println("\nPlayer " + playersList.get(playerIndex-1).getPlayerName() + " goes to prison\n");
                        playersList.get(playerIndex-1).setInJail(true);
                    }
                    else{
                        if(prisonBuy == 1){
                            playersList.get(playerIndex-1).setCash(playersList.get(playerIndex-1).getCash() - 200);
                        }
                        System.out.println("\nPlayer " + playersList.get(playerIndex-1).getPlayerName() + " is free\n");
                        playersList.get(playerIndex-1).setInJail(false);
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
                        if(propertyOwner.getIsInJail()){
                            System.out.println(propertyOwner.getPlayerName() + " is in jail! ");
                        }
                        else{
                            propertyOwner.setCash(propertyOwner.getCash() + propertyOponent.getPaymentForStay());
                            oponent.setCash(oponent.getCash() - propertyOponent.getPaymentForStay());
                            System.out.println("Player " + oponent.getPlayerName() + " pay to " + propertyOwner.getPlayerName() + " " + propertyOponent.getPaymentForStay() + "$");
                            updatePlayersCash(playersList,propertyOwner.getPlayerNumber(),propertyOwner.getCash());
                            updatePlayersCash(playersList,oponent.getPlayerNumber(),oponent.getCash());
                        }

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
                            int isInPrison = 0;
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
                                            System.out.println("You can't afford it!\n");
                                        }
                                    }
                                }
                                else if(idQuestionMark(property)){
                                    Dice randomQuestionMarkDice = new Dice();
                                    BlueRedCards card = new BlueRedCards();
                                    int cardID = randomQuestionMarkDice.throwQuestionMarkCard();
                                    blueRedCardID = cardId4Server(cardID);
                                    card = blueRedCardsList.get(cardID - 1);
                                    System.out.println("\nYou stand on Question mark field!\n");
                                    System.out.println("Card id: " + card.getCardId());
                                    System.out.println("Card text: " + card.getCardText());

                                    System.out.println("Reward: " + card.getCashReward() + "$");
                                    if(card.getCashReward() != 0){
                                        System.out.println("You account was " + myProfile.getCash() + "$ now: " + (myProfile.getCash() + card.getCashReward()));
                                        myProfile.setCash(myProfile.getCash() + card.getCashReward());
                                        updatePlayersCash(playersList,myProfile.getPlayerNumber(),myProfile.getCash());
                                    }

                                    System.out.println("Fine: " + card.getCashFine() + "$");
                                    if(card.getCashFine() != 0){
                                        System.out.println("You account was " + myProfile.getCash() + "$ now: " + (myProfile.getCash() - card.getCashFine()));
                                        myProfile.setCash(myProfile.getCash() - card.getCashFine());
                                        updatePlayersCash(playersList,myProfile.getPlayerNumber(),myProfile.getCash());
                                    }

                                    System.out.println("Destination: " + card.getDestinationField());
                                    if(card.getDestinationField() !=  0){
                                        if(card.getDestinationField() == 31){
                                            System.out.println("You go to jail!");
                                            isInPrison = 1;
                                            playersList.get(Integer.parseInt(info.substring(10)) - 1).setInJail(true);
                                            myProfile.setPropertyId(11);
                                            updatePlayerMove(playersList,myProfile.getPlayerNumber(),myProfile.getPropertyId());
                                            property = propertiesList.get(myProfile.getPropertyId()-1);
                                        }
                                        else{
                                            int actualPropety = myProfile.getPropertyId();
                                            int destinationProperty = card.getDestinationField();
                                            if(actualPropety >= destinationProperty){
                                                myProfile.setCash(myProfile.getCash() + 400);
                                                System.out.println("You pass Start ang you recive 400$!");
                                            }
                                            System.out.println("Now u standing at : " + propertiesList.get(card.getDestinationField()-1).getNameProperty());
                                            myProfile.setPropertyId(card.getDestinationField());
                                            updatePlayerMove(playersList,myProfile.getPlayerNumber(),myProfile.getPropertyId());
                                            property = propertiesList.get(myProfile.getPropertyId()-1);
                                            if(propertyBuyable(property)){
                                                if(property.getOwnerID() == myProfile.getPlayerNumber()){
                                                    System.out.println("You are at home take a bear!");
                                                }
                                                else if(property.getOwnerID() == 0){
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
                                                    Player oponent = new Player();
                                                    oponent = getPlayer(playersList,property.getOwnerID());
                                                    if(oponent.getIsInJail()){
                                                        System.out.println(oponent.getPlayerName() + " is in jail go on!");
                                                    }
                                                    else{
                                                        System.out.println("This property have owner u have to pay " + property.getPaymentForStay() + "$ to " + oponent.getPlayerName());
                                                        myProfile.setCash(myProfile.getCash() - property.getPaymentForStay());
                                                        oponent.setCash(oponent.getCash() + property.getPaymentForStay());
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    System.out.println("Go forward: " + card.getFieldsForward());
                                    if(card.getFieldsForward() != 0){
                                        System.out.println("You do " + card.getFieldsForward() + " more!");
                                        myProfile.setPropertyId(myProfile.getPropertyId() + card.getFieldsForward());
                                        System.out.println("Now you are standing on: " + propertiesList.get(myProfile.getPropertyId()).getNameProperty());
                                        updatePlayerMove(playersList,myProfile.getPlayerNumber(),myProfile.getPropertyId());
                                        property = propertiesList.get(myProfile.getPropertyId());
                                        System.out.println("Property : " + property.getIDproperty() + " " + property.getNameProperty());
                                        if(propertyBuyable(property)){
                                            if(property.getOwnerID() == myProfile.getPlayerNumber()){
                                                System.out.println("You are at home take a bear!");
                                            }
                                            else if(property.getOwnerID() == 0){
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
                                                Player oponent = new Player();
                                                oponent = getPlayer(playersList,property.getOwnerID());
                                                if(oponent.getIsInJail()){
                                                    System.out.println(oponent.getPlayerName() + " is in jail go on!");
                                                }
                                                else{
                                                    System.out.println("This property have owner u have to pay " + property.getPaymentForStay() + "$ to " + oponent.getPlayerName());
                                                    myProfile.setCash(myProfile.getCash() - property.getPaymentForStay());
                                                    oponent.setCash(oponent.getCash() + property.getPaymentForStay());
                                                }
                                            }
                                        }
                                    }

                                    System.out.println("Go backward: " + card.getFieldsBackward());
                                    if(card.getFieldsBackward() != 0){
                                        System.out.println("You do " + card.getFieldsBackward() + " less!");
                                        myProfile.setPropertyId(myProfile.getPropertyId() - card.getFieldsBackward());
                                        if(myProfile.getPropertyId() < 0){
                                            myProfile.setPropertyId(40 + myProfile.getPropertyId());
                                        }
                                        property = propertiesList.get(myProfile.getPropertyId());
                                        System.out.println("Now u statnding at: " + property.getNameProperty());
                                        if(propertyBuyable(property)){
                                            if(property.getOwnerID() == myProfile.getPlayerNumber()){
                                                System.out.println("You are at home take a bear!");
                                            }
                                            else if(property.getOwnerID() == 0){
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
                                                Player oponent = new Player();
                                                oponent = getPlayer(playersList,property.getOwnerID());
                                                if(oponent.getIsInJail()){
                                                    System.out.println(oponent.getPlayerName() + " is in jail go on!");
                                                }
                                                else{
                                                    System.out.println("This property have owner u have to pay " + property.getPaymentForStay() + "$ to " + oponent.getPlayerName());
                                                    myProfile.setCash(myProfile.getCash() - property.getPaymentForStay());
                                                    oponent.setCash(oponent.getCash() + property.getPaymentForStay());
                                                }
                                            }
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
                                if(oponent.getIsInJail()){
                                    System.out.println(oponent.getPlayerName() + " is in jail go on!");
                                }
                                else{
                                    System.out.println("This property have owner u have to pay " + property.getPaymentForStay() + "$ to " + oponent.getPlayerName());
                                    myProfile.setCash(myProfile.getCash() - property.getPaymentForStay());
                                    oponent.setCash(oponent.getCash() + property.getPaymentForStay());
                                }
                            }
                            getCashAllPlayers(playersList);
                            System.out.println("playersSize: " + playersList.size());

                            System.out.println("\nPress < enter > to end your tour: ");
                            scanner.nextLine();
                            System.out.println("Player ready before: " + PlayerTourReady);
                            dOut.writeUTF("PlayerTourReady " + blueRedCardID + " " +  0 + " " + isInPrison + " " +passStart + " " + ifPlayerBoughtProperty + " " +Integer.parseInt(info.substring(10)) +  " " + property.getIDproperty());
                            ifPlayerBoughtProperty = 0;
                            blueRedCardID = '0';
                            PlayerTourReady += 1;
                            System.out.println("Player ready before: " + PlayerTourReady);
                            gameSettingsReady = true;
                            firstTour = false;
                        }
                        else{
                            //funkcja regulujaca pieniadze graczy
                            //checkPositionPlayers(playersList,propertiesList,Integer.parseInt(info.substring(10)));
                            if(playersList.get(Integer.parseInt(info.substring(10)) - 1).getIsInJail()){
                                System.out.println("You are in prison!\n");
                                System.out.println("[1] Try thow double in dice\n");
                                if(playersList.get(Integer.parseInt(info.substring(10)) - 1).getCash() >= 200)
                                    System.out.println("[2] Pay 200$ to get out!\n");
                                int prisonDecision = Integer.parseInt(scanner.nextLine());
                                int isInPrison = 1;
                                if(prisonDecision == 1) {
                                    int dicePrison1 = dice.throwfunction();
                                    int dicePrison2 = dice.throwfunction();
                                    System.out.println("Your first Dice: " + dicePrison1);
                                    System.out.println("Your Secound Dice: " + dicePrison2);
                                    if(dicePrison1 == dicePrison2){
                                        playersList.get(Integer.parseInt(info.substring(10)) - 1).setInJail(false);
                                        isInPrison = 0;
                                        System.out.println("Great you are no longer in prison!");
                                    }
                                    else{
                                        System.out.println("Sorry u are still in prison. Good luck next time!");
                                    }
                                    System.out.println("Press < enter > : ");
                                    scanner.nextLine();

                                    dOut.writeUTF("PlayerTourReady " + blueRedCardID + " " + 0 + " " + isInPrison + " " + 0 + " " + 0 + " " + Integer.parseInt(info.substring(10)) +  " " + 11);
                                    blueRedCardID = '0';
                                    PlayerTourReady += 1;
                                }
                                if(prisonDecision == 2 && (playersList.get(Integer.parseInt(info.substring(10)) - 1).getCash() >= 200)){
                                    playersList.get(Integer.parseInt(info.substring(10)) - 1).setCash(playersList.get(Integer.parseInt(info.substring(10)) - 1).getCash() - 200);
                                    playersList.get(Integer.parseInt(info.substring(10)) - 1).setInJail(false);
                                    System.out.println("Great you are no longer in prison!");
                                    System.out.println("Press < enter > : ");
                                    scanner.nextLine();
                                    dOut.writeUTF("PlayerTourReady "  + blueRedCardID + " " + 1 + " " +  0 + " " + 0 + " " + 0 + " " + Integer.parseInt(info.substring(10)) +  " " + 11);
                                    blueRedCardID = '0';
                                    PlayerTourReady += 1;
                                }
                            }
                            else {
                                myProfile = getPlayer(playersList,nickname);


                                System.out.println("Its your turn press <Enter> to Dice!");
                                scanner.nextLine();
                                int isInPrison = 0;
                                int dice1 = dice.throwfunction();
                                int dice2 = dice.throwfunction();
                                System.out.println("Your Dice info:\n");
                                System.out.println("First dice is: " + dice1);
                                System.out.println("Secound dice is: " + dice2);
                                System.out.println("Sum of dices is: " + (dice2 + dice1) + "\n");



                                int newPosition = myProfile.getPropertyId() + dice1 + dice2;
                                System.out.println(newPosition);
                                if(newPosition + 1 == 31){
                                    newPosition = 10;
                                    System.out.println("\nYou go to jail!!!\n");
                                    isInPrison = 1;
                                    playersList.get(Integer.parseInt(info.substring(10)) - 1).setInJail(true);
                                }

                                if(newPosition > 39){
                                    newPosition = newPosition - 40;
                                    passStart = 1;
                                    System.out.println("Nice your acconut get 400$");
                                    playersList.get(Integer.parseInt(info.substring(10)) - 1).setCash(playersList.get(Integer.parseInt(info.substring(10)) - 1).getCash() + 400);
                                }
                                myProfile.setPropertyId(newPosition);
                                updatePlayerMove(playersList,myProfile.getPlayerNumber(),newPosition);

                                property = propertiesList.get(newPosition);
                                System.out.println("Property Info: \n");


                                System.out.println("Id property: " + property.getIDproperty());
                                System.out.println("Owner id: " + property.getOwnerID());
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
                                    else if(idQuestionMark(property)){
                                        Dice randomQuestionMarkDice = new Dice();
                                        BlueRedCards card = new BlueRedCards();
                                        int cardID = randomQuestionMarkDice.throwQuestionMarkCard();
                                        blueRedCardID = cardId4Server(cardID);
                                        card = blueRedCardsList.get(cardID - 1);
                                        System.out.println("\nYou stand on Question mark field!\n");
                                        System.out.println("Card id: " + card.getCardId());
                                        System.out.println("Card text: " + card.getCardText());

                                        System.out.println("Reward: " + card.getCashReward() + "$");
                                        if(card.getCashReward() != 0){
                                            System.out.println("You account was " + myProfile.getCash() + "$ now: " + (myProfile.getCash() + card.getCashReward()));
                                            myProfile.setCash(myProfile.getCash() + card.getCashReward());
                                            updatePlayersCash(playersList,myProfile.getPlayerNumber(),myProfile.getCash());
                                        }

                                        System.out.println("Fine: " + card.getCashFine() + "$");
                                        if(card.getCashFine() != 0){
                                            System.out.println("You account was " + myProfile.getCash() + "$ now: " + (myProfile.getCash() - card.getCashFine()));
                                            myProfile.setCash(myProfile.getCash() - card.getCashFine());
                                            updatePlayersCash(playersList,myProfile.getPlayerNumber(),myProfile.getCash());
                                        }

                                        System.out.println("Destination: " + card.getDestinationField());
                                        if(card.getDestinationField() !=  0){
                                            if(card.getDestinationField() == 31){
                                                System.out.println("You go to jail!");
                                                isInPrison = 1;
                                                playersList.get(Integer.parseInt(info.substring(10)) - 1).setInJail(true);
                                                myProfile.setPropertyId(11);
                                                updatePlayerMove(playersList,myProfile.getPlayerNumber(),myProfile.getPropertyId());
                                                property = propertiesList.get(myProfile.getPropertyId()-1);
                                            }
                                            else{
                                                int actualPropety = myProfile.getPropertyId();
                                                int destinationProperty = card.getDestinationField();
                                                if(actualPropety >= destinationProperty){
                                                    myProfile.setCash(myProfile.getCash() + 400);
                                                    System.out.println("You pass Start ang you recive 400$!");
                                                }
                                                System.out.println("Now u standing at : " + propertiesList.get(card.getDestinationField()-1).getNameProperty());
                                                myProfile.setPropertyId(card.getDestinationField());
                                                updatePlayerMove(playersList,myProfile.getPlayerNumber(),myProfile.getPropertyId());
                                                property = propertiesList.get(myProfile.getPropertyId()-1);
                                                if(propertyBuyable(property)){
                                                    if(property.getOwnerID() == myProfile.getPlayerNumber()){
                                                        System.out.println("You are at home take a bear!");
                                                    }
                                                    else if(property.getOwnerID() == 0){
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
                                                        Player oponent = new Player();
                                                        oponent = getPlayer(playersList,property.getOwnerID());
                                                        if(oponent.getIsInJail()){
                                                            System.out.println(oponent.getPlayerName() + " is in jail go on!");
                                                        }
                                                        else{
                                                            System.out.println("This property have owner u have to pay " + property.getPaymentForStay() + "$ to " + oponent.getPlayerName());
                                                            myProfile.setCash(myProfile.getCash() - property.getPaymentForStay());
                                                            oponent.setCash(oponent.getCash() + property.getPaymentForStay());
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        System.out.println("Go forward: " + card.getFieldsForward());
                                        if(card.getFieldsForward() != 0){
                                            System.out.println("You do " + card.getFieldsForward() + " more!");
                                            myProfile.setPropertyId(myProfile.getPropertyId() + card.getFieldsForward());
                                            System.out.println("Now you are standing on: " + propertiesList.get(myProfile.getPropertyId()).getNameProperty());
                                            updatePlayerMove(playersList,myProfile.getPlayerNumber(),myProfile.getPropertyId());
                                            property = propertiesList.get(myProfile.getPropertyId());
                                            System.out.println("Property : " + property.getIDproperty() + " " + property.getNameProperty());
                                            if(propertyBuyable(property)){
                                                if(property.getOwnerID() == myProfile.getPlayerNumber()){
                                                    System.out.println("You are at home take a bear!");
                                                }
                                                else if(property.getOwnerID() == 0){
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
                                                    Player oponent = new Player();
                                                    oponent = getPlayer(playersList,property.getOwnerID());
                                                    if(oponent.getIsInJail()){
                                                        System.out.println(oponent.getPlayerName() + " is in jail go on!");
                                                    }
                                                    else{
                                                        System.out.println("This property have owner u have to pay " + property.getPaymentForStay() + "$ to " + oponent.getPlayerName());
                                                        myProfile.setCash(myProfile.getCash() - property.getPaymentForStay());
                                                        oponent.setCash(oponent.getCash() + property.getPaymentForStay());
                                                    }
                                                }
                                            }
                                        }

                                        System.out.println("Go backward: " + card.getFieldsBackward());
                                        if(card.getFieldsBackward() != 0){
                                            System.out.println("You do " + card.getFieldsBackward() + " less!");
                                            myProfile.setPropertyId(myProfile.getPropertyId() - card.getFieldsBackward());
                                            if(myProfile.getPropertyId() < 0){
                                                myProfile.setPropertyId(40 + myProfile.getPropertyId());
                                            }
                                            property = propertiesList.get(myProfile.getPropertyId());
                                            System.out.println("Now u statnding at: " + property.getNameProperty());
                                            if(propertyBuyable(property)){
                                                if(property.getOwnerID() == myProfile.getPlayerNumber()){
                                                    System.out.println("You are at home take a bear!");
                                                }
                                                else if(property.getOwnerID() == 0){
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
                                                    Player oponent = new Player();
                                                    oponent = getPlayer(playersList,property.getOwnerID());
                                                    if(oponent.getIsInJail()){
                                                        System.out.println(oponent.getPlayerName() + " is in jail go on!");
                                                    }
                                                    else{
                                                        System.out.println("This property have owner u have to pay " + property.getPaymentForStay() + "$ to " + oponent.getPlayerName());
                                                        myProfile.setCash(myProfile.getCash() - property.getPaymentForStay());
                                                        oponent.setCash(oponent.getCash() + property.getPaymentForStay());
                                                    }
                                                }
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
                                    if(oponent.getIsInJail()){
                                        System.out.println(oponent.getPlayerName() + " is in jail go on!");
                                    }
                                    else{
                                        System.out.println("This property have owner u have to pay " + property.getPaymentForStay() + "$ to " + oponent.getPlayerName());
                                        myProfile.setCash(myProfile.getCash() - property.getPaymentForStay());
                                        oponent.setCash(oponent.getCash() + property.getPaymentForStay());
                                    }
                                }
                                getCashAllPlayers(playersList);
                                System.out.println("playersSize: " + playersList.size());

                                System.out.println("\nPress < enter > to end your tour: ");
                                scanner.nextLine();
                                System.out.println("Player ready before: " + PlayerTourReady);
                                dOut.writeUTF("PlayerTourReady " + blueRedCardID + " " + 0 + " " + isInPrison + " " +passStart + " " + ifPlayerBoughtProperty + " " +Integer.parseInt(info.substring(10)) +  " " + property.getIDproperty());
                                PlayerTourReady += 1;
                                blueRedCardID = 0;
                                ifPlayerBoughtProperty = 0;
                                System.out.println("Player ready before: " + PlayerTourReady);
                                passStart = 0;
                                gameSettingsReady = true;
                                firstTour = false;
                            }
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




    public static int card4ClientFromServer(char cardID){
        switch (cardID){
            case '1': return 1;
            case '2': return 2;
            case '3': return 3;
            case '4': return 4;
            case '5': return 5;
            case '6': return 6;
            case '7': return 7;
            case '8': return 8;
            case '9': return 9;
            case 'a': return 10;
            case 'b': return 11;
            case 'c': return 12;
            case 'd': return 13;
            case 'e': return 14;
            case 'f': return 15;
            case 'g': return 16;
        }
        return 0;
    }

    public static char cardId4Server(int cardID){
        switch (cardID){
            case 1: return '1';
            case 2: return '2';
            case 3: return '3';
            case 4: return '4';
            case 5: return '5';
            case 6: return '6';
            case 7: return '7';
            case 8: return '8';
            case 9: return '9';
            case 10: return 'a';
            case 11: return 'b';
            case 12: return 'c';
            case 13: return 'd';
            case 14: return 'e';
            case 15: return 'f';
            case 16: return 'g';
        }
        return '0';
    }

    public static boolean idQuestionMark(Properties property){
        if(property.getNameProperty().equals("BlueQuestionMark") || property.getNameProperty().equals("RedQuestionMark")){
            return true;
        }
        else
            return false;
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
        if(property.getNameProperty().startsWith("Blue")
                || property.getNameProperty().startsWith("Red") || property.getNameProperty().startsWith("Podatek") ||
                property.getNameProperty().startsWith("Parking") || property.getNameProperty().startsWith("Wiezienie") ||
                property.getNameProperty().startsWith("Go") || property.getNameProperty().startsWith("Idziesz") ||
                property.getNameProperty().startsWith("Darmowy"))
            return false;
        else
            return true;
//        switch (property.getIDproperty()){
//            case 1:{return false;}
//            case 3:{return false;}
//            case 5:{return false;}
//            case 8:{return false;}
//            case 11:{return false;}
//            case 18:{return false;}
//            case 21:{return false;}
//            case 23:{return false;}
//            case 31:{return false;}
//            case 34:{return false;}
//            case 37:{return false;}
//            case 39:{return false;}
//            default: return true;
//        }
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

    public static ArrayList<BlueRedCards> initializeRandomCards(){
        ArrayList<BlueRedCards> blueRedCardsInitial = new ArrayList<>();

        BlueRedCards card1 = new BlueRedCards(1,"Wracasz do Mardrytu",0,0,0,0,15);
        BlueRedCards card2 = new BlueRedCards(2,"Bank wyplaca ci procenty w wysokosci 100$",100,0,0,0,0);
        BlueRedCards card3 = new BlueRedCards(3,"Idziesz do Kolei Wschodnich, jezeli przechodzisz przez start otrymujesz 400$!",0,0,0,0,16);
        BlueRedCards card4 = new BlueRedCards(4,"Bank wyplaca ci nalezne odsetki w wyskości 300$",300,0,0,0,0);
        BlueRedCards card5 = new BlueRedCards(5,"Idziesz do Brukseli, jezeli przechodzisz przez start otrzymujesz 400$",0,0,0,0,24);
        BlueRedCards card6 = new BlueRedCards(6,"Piles w czasie pracy placisz 40$",0,40,0,0,0);
        BlueRedCards card7 = new BlueRedCards(7,"Idziesz do Neapolu, jezeli przechodzisz przez start otrzymujesz 400$",0,0,0,0,7);
        BlueRedCards card8 = new BlueRedCards(8,"Placisz oplate za szkole 300$",0,300,0,0,0);
        BlueRedCards card9 = new BlueRedCards(9,"Idziesz do wiezienia!",0,0,0,0,31);
        BlueRedCards card10 = new BlueRedCards(10,"Rozwiazales dobrze krzyzowke otrzymujesz 200$",200,0,0,0,0);
        BlueRedCards card11 = new BlueRedCards(11,"Wracasz na Start",0,0,0,0,1);
        BlueRedCards card12 = new BlueRedCards(12,"Cofasz sie o 3 pola.",0,0,0,3,0);
        BlueRedCards card13 = new BlueRedCards(13,"Idziesz 3 pola dalej",0,0,3,0,0);
        BlueRedCards card14 = new BlueRedCards(14,"Mandat za szybka jazde 30$.",0,30,0,0,0);
        BlueRedCards card15 = new BlueRedCards(15,"Bank pomylil sie na twoja korzysc otrzymujesz 400$.",400,0,0,0,0);
        BlueRedCards card16 = new BlueRedCards(16,"Wracasz do Wiednia",0,0,0,0,40);

        blueRedCardsInitial.add(card1);
        blueRedCardsInitial.add(card2);
        blueRedCardsInitial.add(card3);
        blueRedCardsInitial.add(card4);
        blueRedCardsInitial.add(card5);
        blueRedCardsInitial.add(card6);
        blueRedCardsInitial.add(card7);
        blueRedCardsInitial.add(card8);
        blueRedCardsInitial.add(card9);
        blueRedCardsInitial.add(card10);
        blueRedCardsInitial.add(card11);
        blueRedCardsInitial.add(card12);
        blueRedCardsInitial.add(card13);
        blueRedCardsInitial.add(card14);
        blueRedCardsInitial.add(card15);
        blueRedCardsInitial.add(card16);

        return blueRedCardsInitial;
    }
}
