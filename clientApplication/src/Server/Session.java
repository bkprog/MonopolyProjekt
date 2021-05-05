package Server;

import Source.Player;
import Source.Properties;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Session extends Thread {
    private ArrayList<Socket> socketPlayers;
    private ArrayList<Player> playersList;
    private ArrayList<Properties> propertiesList;
    private Socket client;
    int playerNumber;
    int numberOfPlayersInGame;
    int playersReady = 0;
    int playersTourReady = 0;

    public Session(Socket client, ArrayList<Socket> socketArray,int np,int numberOfPlayers,ArrayList<Player> playersList,ArrayList<Properties> propertiesList){
        this.client = client;
        this.socketPlayers = socketArray;
        playerNumber = np;
        numberOfPlayersInGame = numberOfPlayers;
        this.playersList = playersList;
        this.propertiesList = propertiesList;
    }

    public void run(){
        try{
            DataInputStream socketIn = new DataInputStream(client.getInputStream());
            do {

                //czyta nickname gracza
                String playerName = socketIn.readUTF();
                System.out.println(playerNumber + " Player nickname is " + playerName);
                Player newPlayer = new Player();
                newPlayer.setPlayerName(playerName);
                newPlayer.setPlayerNumber(playerNumber);
                newPlayer.setCash(3000);
                newPlayer.setPropertyId(0);
                playersList.add(newPlayer);


                if(playerNumber>0 && playerNumber<numberOfPlayersInGame){
                    System.out.println("Server is waiting for clients connection :)");
                }
                //gui working to this moment
                else
                    System.out.println("All clients connected great!!!");

                String information = "Players " + playerName + " joined to your session!";

                //pisze do klientów informacje jeśli klient się polaczyl
                Broadcast(socketPlayers,information);

                //sprawdza czy wszyscy gracze sa gotowi do gry
                BroadcastReady(socketPlayers,numberOfPlayersInGame);

                propertiesList = setAllProperties(propertiesList);
                sendSettingPropertiesArray(socketPlayers,propertiesList);

//                //wysyła informacje o iliści graczy w grze
                sendInformationOfNumberOfPlayers(socketPlayers);
//
                String readyClient = socketIn.readUTF();
                if(readyClient.startsWith("ReadyCheck")){
                    //wysyla informacje o tym ze gracz jest gotowy do gry
                    BroadcastReadyToOtherClients(socketPlayers,readyClient);
                }


//                setStartGame(socketPlayers,propertiesList,playersList);
                sendSettingsPlayerArray(socketPlayers,playersList);


//                while(true){
//
//                }
            }while(false);

            boolean firstTour = true;
            int playerTourIndex = 1;
            int i = 1;

            while(true){
                System.out.println("Tura numer: " + i);
                if(firstTour){
                    sendStartGame(socketPlayers,playerTourIndex);
                    firstTour = false;
                }
                String clientResponse = socketIn.readUTF();
                if(clientResponse.startsWith("PlayerTourReady")){
                    if(clientResponse.length() > 15){
                        int prisionDecion = Character.getNumericValue(clientResponse.charAt(22));
                        int prisionBuy = Character.getNumericValue(clientResponse.charAt(24));
                        int propBuy = Character.getNumericValue(clientResponse.charAt(26));
                        char cardId = clientResponse.charAt(20);
                        int passedStrat = Character.getNumericValue(clientResponse.charAt(18));
                        int boughtHouses = Character.getNumericValue(clientResponse.charAt(16));
                        System.out.println("Player Bought " + boughtHouses + " Houses!");
                        if(prisionDecion != 0){
                            prisonDecisionUpdate(socketPlayers,playerTourIndex);
                            System.out.println("Player is in jail!");
                        }
                        if(prisionBuy != 0){
                            prisonPayFine(socketPlayers,playerTourIndex);
                        }
                        if(cardId != '0'){
                            System.out.println("Card " + cardId);
                            CardMessage(socketPlayers,cardId,passedStrat);
                        }
//                        char houseBought = clientResponse.charAt(16);
                        String playerMove = clientResponse.substring(30);
                        if (propBuy != 0){
                            System.out.println("Player bought this property");
                            BuyPorpertyMessage(socketPlayers,playerMove);
                        }
//                        char cardId = clientResponse.charAt(18);

//                        System.out.println(houseBought);
//                        System.out.println("prison decision: " + prisionDecion);
//                        System.out.println("prison buy: " + prisionDecion);
//                        System.out.println("cardId : " + cardId);
//                        houseUpdate(socketPlayers,houseBought,playerTourIndex);
//                        cardMoveSend(socketPlayers,cardId,playerTourIndex);
                        updateMove(socketPlayers,playerMove);
                        if(boughtHouses != 0)
                            BuyHouseMessage(socketPlayers,boughtHouses);
                    }
                    System.out.println(clientResponse);
                    BroadcastReadyToOtherClientsTour(socketPlayers);
                }
                playerTourIndex++;
                if(playerTourIndex > numberOfPlayersInGame){
                    playerTourIndex = 1;
                }
                String clientResponse1 = socketIn.readUTF();
                if(clientResponse1.equals("allPlayersTourReady")){
                    System.out.println(clientResponse1);
                    sendStartGame(socketPlayers,playerTourIndex);
//                    sendStartGameToClient(socketPlayers,playerTourIndex);
                }
                i++;
            }
        }
        catch(IOException ex){
            System.out.println("Session error: " + ex.getMessage());
        }
        catch(Exception ex){
            System.out.println("Session error: " + ex.getMessage());
        }
    }

//    public void setStartGame(ArrayList<Socket> socketPlayers,ArrayList<Properties> propertiesList,ArrayList<Player> playersList){
//        sendSettingsPlayerArray(socketPlayers,playersList);
//        sendSettingPropertiesArray(socketPlayers,propertiesList);
//    }

    public void BroadcastReadyToOtherClientsTour(ArrayList<Socket> socketPlayers){
        playersTourReady++;
        try{
            for (Socket s : socketPlayers){
                DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
//                socketOut.writeUTF("PlayerTourReady");
                if (s != client){
                    socketOut.writeUTF("PlayerTourReady");
                }
            }
        }
        catch(Exception e){
            System.out.println("Error Ready to others: " + e.getMessage());
        }
    }

    public void houseUpdate(ArrayList<Socket> socketPlayers,char numberHouses,int playerindex){
        try{
            for(Socket s : socketPlayers){
                if(s != client){
                    DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
                    socketOut.writeUTF("houseBuy " + numberHouses + " " + playerindex);
                }
            }
        }
        catch(Exception ex){
            System.out.println("Error sending info about start game: " + ex.getMessage());
        }
    }

    public void cardMoveSend(ArrayList<Socket> socketPlayers,char cardID,int playerindex){
        try{
            for(Socket s : socketPlayers){
                if(s != client){
                    DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
                    socketOut.writeUTF("CardMove " + cardID + " " + playerindex);
                }
            }
        }
        catch(Exception ex){
            System.out.println("Error sending info about start game: " + ex.getMessage());
        }
    }

    public void prisonPayFine(ArrayList<Socket> socketPlayers,int playerindex){
        try{
            for(Socket s : socketPlayers){
                if(s != client){
                    DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
                    socketOut.writeUTF("PrisonPayFine " + playerindex);
                }
            }
        }
        catch(Exception ex){
            System.out.println("Error sending info about start game: " + ex.getMessage());
        }
    }

    public void prisonDecisionUpdate(ArrayList<Socket> socketPlayers,int playerindex){
        try{
            for(Socket s : socketPlayers){
                if(s != client){
                    DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
                    socketOut.writeUTF("InPrison " + playerindex );
                }
            }
        }
        catch(Exception ex){
            System.out.println("Error sending info about start game: " + ex.getMessage());
        }
    }

    public void CardMessage(ArrayList<Socket> socketPlayers,char cardId,int passedStrat){
        try{
            for(Socket s : socketPlayers){
                if(s != client){
                    DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
                    socketOut.writeUTF("BlueRedCard " + cardId + " " + passedStrat);
                }
            }
        }
        catch(Exception ex){
            System.out.println("Error sending info about start game: " + ex.getMessage());
        }
    }

    public void BuyPorpertyMessage(ArrayList<Socket> socketPlayers,String propertyID){
        try{
            for(Socket s : socketPlayers){
                if(s != client){
                    DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
                    socketOut.writeUTF("Purchase Property " + propertyID);
                }
            }
        }
        catch(Exception ex){
            System.out.println("Error sending info about start game: " + ex.getMessage());
        }
    }

    public void BuyHouseMessage(ArrayList<Socket> socketPlayers,int numberOfHouses){
        try{
            for(Socket s : socketPlayers){
                if(s != client){
                    DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
                    socketOut.writeUTF("HouseBought " + numberOfHouses);
                }
            }
        }
        catch(Exception ex){
            System.out.println("Error sending info about HousesBought: " + ex.getMessage());
        }
    }

    public void updateMove(ArrayList<Socket> socketPlayers,String move){
        try{
            for(Socket s : socketPlayers){
                if(s != client){
                    DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
                    socketOut.writeUTF("UpdateMove " + move);
                }
            }
        }
        catch(Exception ex){
            System.out.println("Error sending info about start game: " + ex.getMessage());
        }
    }

    public void sendStartGameToClient(ArrayList<Socket> socketPlayers,int playerTourIndex){
        try{
            for(Socket s : socketPlayers){
//                DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
//                socketOut.writeUTF("StartGame " + playerTourIndex);
                if(s == client){
                    DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
                    socketOut.writeUTF("StartGame " + playerTourIndex);
                }
            }
        }
        catch(Exception ex){
            System.out.println("Error sending info about start game: " + ex.getMessage());
        }
    }

    public void sendStartGame(ArrayList<Socket> socketPlayers,int playerTourIndex){
        try{
            for(Socket s : socketPlayers){
                DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
                socketOut.writeUTF("StartGame " + playerTourIndex);
//                if(s != client){
//                    DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
//                    socketOut.writeUTF("StartGame " + playerTourIndex);
//                }
            }
        }
        catch(Exception ex){
            System.out.println("Error sending info about start game: " + ex.getMessage());
        }
    }

    public void sendSettingPropertiesArray(ArrayList<Socket> socketPlayers,ArrayList<Properties> propertiesList){
        try{
            DataOutputStream socketOut = new DataOutputStream(client.getOutputStream());
            socketOut.writeUTF("PropertiesSettings ");
            for(int i = 0; i< propertiesList.size();i++){
                socketOut.writeUTF("PropertiesSettings Name" + propertiesList.get(i).getNameProperty());
                socketOut.writeUTF("PropertiesSettings payment4Stay" + String.valueOf(propertiesList.get(i).getPaymentForStay()));
                socketOut.writeUTF("PropertiesSettings id" + String.valueOf(propertiesList.get(i).getIDproperty()));
                socketOut.writeUTF("PropertiesSettings buyCost" + String.valueOf(propertiesList.get(i).getBuyCost()));
                socketOut.writeUTF("PropertiesSettings Country" + propertiesList.get(i).getCountryName());
                socketOut.writeUTF("PropertiesSettings Owner" + String.valueOf(propertiesList.get(i).getOwnerID()));
                socketOut.writeUTF("PropertiesSettings lvl1" + String.valueOf(propertiesList.get(i).getLvl1()));
                socketOut.writeUTF("PropertiesSettings lvl2" + String.valueOf(propertiesList.get(i).getLvl2()));
                socketOut.writeUTF("PropertiesSettings lvl3" + String.valueOf(propertiesList.get(i).getLvl3()));
                socketOut.writeUTF("PropertiesSettings lvl4" + String.valueOf(propertiesList.get(i).getLvl4()));
            }
//            for(Socket s : socketPlayers){
//                if(s != client){
//                    DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
//                    socketOut.writeUTF("PropertiesSettings ");
//                    for(int i = 0; i< propertiesList.size();i++){
//                        socketOut.writeUTF("PropertiesSettings Name" + propertiesList.get(i).getNameProperty());
//                        socketOut.writeUTF("PropertiesSettings payment4Stay" + String.valueOf(propertiesList.get(i).getPaymentForStay()));
//                        socketOut.writeUTF("PropertiesSettings id" + String.valueOf(propertiesList.get(i).getIDproperty()));
//                        socketOut.writeUTF("PropertiesSettings buyCost" + String.valueOf(propertiesList.get(i).getBuyCost()));
//                        socketOut.writeUTF("PropertiesSettings Country" + propertiesList.get(i).getCountryName());
//                        socketOut.writeUTF("PropertiesSettings Owner" + String.valueOf(propertiesList.get(i).getOwnerID()));
//                        socketOut.writeUTF("PropertiesSettings lvl1" + String.valueOf(propertiesList.get(i).getLvl1()));
//                        socketOut.writeUTF("PropertiesSettings lvl2" + String.valueOf(propertiesList.get(i).getLvl2()));
//                        socketOut.writeUTF("PropertiesSettings lvl3" + String.valueOf(propertiesList.get(i).getLvl3()));
//                        socketOut.writeUTF("PropertiesSettings lvl4" + String.valueOf(propertiesList.get(i).getLvl4()));
//                    }
//                }
//            }
        }
        catch(Exception e){
            System.out.println("Error sendingPropertiesArray: " + e.getMessage());
        }
    }

    public void sendSettingsPlayerArray(ArrayList<Socket> socketPlayers,ArrayList<Player> playersList){
        try{
            for(int i = 0 ; i<socketPlayers.size();i++){
                DataOutputStream socketOut = new DataOutputStream(socketPlayers.get(i).getOutputStream());
                socketOut.writeUTF("GameSettings");
                for( int j = 0 ; j<playersList.size();j++){
                    socketOut.writeUTF("Nickname" + playersList.get(j).getPlayerName());
                    socketOut.writeUTF("NumberPlayer" + String.valueOf(playersList.get(j).getPlayerNumber()));
                    socketOut.writeUTF("CashPlayer" + String.valueOf(playersList.get(j).getCash()));
                    socketOut.writeUTF("PropertyId" + String.valueOf(playersList.get(j).getPropertyId()));
                }

            }
        }
        catch(Exception ex){
            System.out.println("Error sendingPlayersArray: " + ex.getMessage());
        }
    }

    public void catchPlayerMove(ArrayList<Socket> socketPlayers){
        try{
            for(Socket s : socketPlayers){
                if(s != client){
                    DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
                    socketOut.writeUTF("HaHa");
                }
                // socketOut.writeUTF(String.valueOf(index));
//                ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
//                out.writeObject(playersList);
            }
        }
        catch(Exception ex){
            System.out.println("Error sending info about start game: " + ex.getMessage());
        }
    }



    public void sendInformationOfNumberOfPlayers(ArrayList<Socket> socketPlayers){
        try{
            for(Socket s : socketPlayers){
                DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
                socketOut.writeUTF("PlayersInGame " + numberOfPlayersInGame);
//                socketOut.writeUTF("PlayersInGame");
//                socketOut.writeInt(numberOfPlayersInGame);
            }
        }
        catch(Exception ex){
            System.out.println("Error sending info about number of players in game: " + ex.getMessage());
        }
    }

    public void Broadcast(ArrayList<Socket> socketPlayers,String info){
        try{
            for(int i=0;i<socketPlayers.size();i++){
                DataOutputStream socketOut = new DataOutputStream(socketPlayers.get(i).getOutputStream());
                if(socketPlayers.get(i) == client){
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

    public void BroadcastReady(ArrayList<Socket> socketPlayers,int numberOfPlayers){
        try{
            int numberOfPlayersConnected = 0;
            for(Socket s : socketPlayers){
                numberOfPlayersConnected++;
            }
            if(numberOfPlayers == numberOfPlayersConnected){
                for(Socket s : socketPlayers){
                    DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
                    socketOut.writeUTF("ReadyCheck");
                }
            }
        }
        catch (Exception e){
            System.out.println("Error broadcastReady: " + e.getMessage());
        }
    }

    public void BroadcastReadyToOtherClients(ArrayList<Socket> socketPlayers,String text){
        playersReady++;
        try{
            for (Socket s : socketPlayers){
                DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
                if (!(s == client)){
                    socketOut.writeUTF("PlayerReady" + text.substring(10));
                }
            }
        }
        catch(Exception e){
            System.out.println("Error Ready to others: " + e.getMessage());
        }
    }

    public ArrayList<Properties> setAllProperties(ArrayList<Properties> propertiesList){

        Properties property1 = new Properties(1,0,"Go!","NONE",0,0,0,0,0);
        Properties property2 = new Properties(2,10,"Saloniki","Grecja",120,40,120,360,640);
        Properties property3 = new Properties(3,0,"BlueQuestionMark","NONE",0,0,0,0,0);
        Properties property4 = new Properties(4,10,"Ateny","Grecja",120,40,120,360,640);
        Properties property5 = new Properties(5,400,"Parking","NONE",0,0,0,0,0);
        Properties property6 = new Properties(6,50,"Linie Kolejowe Południowe","NONE",400,100,200,400,0);
        Properties property7 = new Properties(7,15,"Neapol","Włochy",200,60,180,540,800);
        Properties property8 = new Properties(8,0,"RedQuestionMark","NONE",0,0,0,0,0);
        Properties property9 = new Properties(9,15,"Mediolan","Włochy",200,60,180,540,800);
        Properties property10 = new Properties(10,20,"Rzym","Włochy",240,80,200,600,900);
        Properties property11 = new Properties(11,0,"Wiezienie","NONE",0,0,0,0,0);
        Properties property12 = new Properties(12,20,"Barcelona","Hiszpania",280,100,300,900,1250);
        Properties property13 = new Properties(13,10,"Elektrownia","NONE",300,0,0,0,0);
        Properties property14 = new Properties(14,20,"Sewilla","Hiszpania",280,100,300,900,1250);
        Properties property15 = new Properties(15,25,"Madryt","Hiszpania",320,120,360,1000,1400);
        Properties property16 = new Properties(16,50,"Linie Kolejowe Wschodnie","NONE",400,100,200,400,0);
        Properties property17 = new Properties(17,30,"Liverpool","Anglia",360,140,400,1100,1500);
        Properties property18 = new Properties(18,0,"BlueQuestionMark","NONE",0,0,0,0,0);
        Properties property19 = new Properties(19,30,"Glasgow","Anglia",360,140,400,1100,1500);
        Properties property20 = new Properties(20,35,"Londyn","Anglia",360,160,440,1200,1600);
        Properties property21 = new Properties(21,0,"Darmowy Parking","NONE",0,0,0,0,0);
        Properties property22 = new Properties(22,35,"Rotterdam","Benelux",440,180,500,1400,1750);
        Properties property23 = new Properties(23,0,"RedQuestionMark","NONE",0,0,0,0,0);
        Properties property24 = new Properties(24,35,"Bruksela","Benelux",440,180,500,1400,1750);
        Properties property25 = new Properties(25,40,"Amsterdam","Benelux",480,200,600,1500,1850);
        Properties property26 = new Properties(26,50,"Linie Kolejowe Północne","NONE",400,100,200,400,0);
        Properties property27 = new Properties(27,45,"Malmo","Szwecja",520,220,660,1600,1950);
        Properties property28 = new Properties(28,45,"Goteborg","Szwecja",520,220,660,1600,1950);
        Properties property29 = new Properties(29,10,"Wodociągi","NONE",300,0,0,0,0);
        Properties property30 = new Properties(30,50,"Sztokholm","Szwecja",560,240,720,1700,2050);
        Properties property31 = new Properties(31,0,"Idziesz do Wiezienia","NONE",0,0,0,0,0);
        Properties property32 = new Properties(32,55,"Frankfurt","RFN",600,260,780,1900,2200);
        Properties property33 = new Properties(33,55,"Kolonia","RFN",600,260,780,1900,2200);
        Properties property34 = new Properties(34,0,"BlueQuestionMark","NONE",0,0,0,0,0);
        Properties property35 = new Properties(35,60,"Bonn","RFN",640,300,900,2000,2400);
        Properties property36 = new Properties(36,50,"Linie Kolejowe Zachodnie","NONE",400,100,200,400,0);
        Properties property37 = new Properties(37,0,"RedQuestionMark","NONE",0,0,0,0,0);
        Properties property38 = new Properties(38,70,"Insbruck","Austria",700,350,1000,2200,2600);
        Properties property39 = new Properties(39,200,"Podatek","NONE",0,0,0,0,0);
        Properties property40 = new Properties(40,100,"Wieden","Austria",800,400,1200,2800,3400);

        propertiesList.add(property1);
        propertiesList.add(property2);
        propertiesList.add(property3);
        propertiesList.add(property4);
        propertiesList.add(property5);
        propertiesList.add(property6);
        propertiesList.add(property7);
        propertiesList.add(property8);
        propertiesList.add(property9);
        propertiesList.add(property10);
        propertiesList.add(property11);
        propertiesList.add(property12);
        propertiesList.add(property13);
        propertiesList.add(property14);
        propertiesList.add(property15);
        propertiesList.add(property16);
        propertiesList.add(property17);
        propertiesList.add(property18);
        propertiesList.add(property19);
        propertiesList.add(property20);
        propertiesList.add(property21);
        propertiesList.add(property22);
        propertiesList.add(property23);
        propertiesList.add(property24);
        propertiesList.add(property25);
        propertiesList.add(property26);
        propertiesList.add(property27);
        propertiesList.add(property28);
        propertiesList.add(property29);
        propertiesList.add(property30);
        propertiesList.add(property31);
        propertiesList.add(property32);
        propertiesList.add(property33);
        propertiesList.add(property34);
        propertiesList.add(property35);
        propertiesList.add(property36);
        propertiesList.add(property37);
        propertiesList.add(property38);
        propertiesList.add(property39);
        propertiesList.add(property40);

        return propertiesList;
    }

}

