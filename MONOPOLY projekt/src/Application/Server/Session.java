package Application.Server;

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
            //czyta nickname gracza
            String playerName = socketIn.readUTF();
            System.out.println(playerNumber + " Player nickname is " + playerName);
            Player newPlayer = new Player();
            newPlayer.setPlayerName(playerName);
            newPlayer.setPlayerNumber(playerNumber);
            newPlayer.setCash(2000);
            newPlayer.setPropertyId(0);
            playersList.add(newPlayer);


            if(playerNumber>0 && playerNumber<numberOfPlayersInGame){
                System.out.println("Server is waiting for clients connection :)");
            }

            String information = "Player " + playerName + " joined to your session!";

            //pisze do klientów informacje jeśli klient się polaczyl
            Broadcast(socketPlayers,information);

            //sprawdza czy wszyscy gracze sa gotowi do gry
            BroadcastReady(socketPlayers,numberOfPlayersInGame);

            //wysyła informacje o iliści graczy w grze
            sendInformationOfNumberOfPlayers(socketPlayers);
            String readyClient = socketIn.readUTF();
            if(readyClient.startsWith("ReadyCheck")){

                //wysyla informacje o tym ze gracz jest gotowy do gry
                BroadcastReadyToOtherClients(socketPlayers,readyClient);
            }

            sendSettingsPlayerArray(socketPlayers,playersList);
            //wysyła do klientów informacje o rozpoczeciu gry
            sendStartGame(socketPlayers);
            while(true){

            }
        }
        catch(IOException ex){
            System.out.println("Session error: " + ex.getMessage());
        }
    }

    public void sendSettingPropertiesArray(ArrayList<Socket> socketPlayers,ArrayList<Properties> propertiesList){

    }

    public void sendSettingsPlayerArray(ArrayList<Socket> socketPlayers,ArrayList<Player> playersList){
        try{
            for(int i = 0 ; i<socketPlayers.size();i++){
                DataOutputStream socketOut = new DataOutputStream(socketPlayers.get(i).getOutputStream());
                socketOut.writeUTF("GameSettings");
                for( int j = 0 ; j<playersList.size();j++){
                    socketOut.writeUTF(playersList.get(j).getPlayerName());
                    socketOut.writeUTF(String.valueOf(playersList.get(j).getPlayerNumber()));
                    socketOut.writeUTF(String.valueOf(playersList.get(j).getCash()));
                    socketOut.writeUTF(String.valueOf(playersList.get(j).getPropertyId()));
                }
            }
        }
        catch(Exception ex){
            System.out.println("Error sendingPlayersArray: " + ex.getMessage());
        }
    }

    public void sendStartGame(ArrayList<Socket> socketPlayers){
        try{
            for(Socket s : socketPlayers){
                DataOutputStream socketOut = new DataOutputStream(s.getOutputStream());
                socketOut.writeUTF("StartGame");
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
                socketOut.writeUTF("PlayersInGame");
                socketOut.writeInt(numberOfPlayersInGame);
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
        Properties property1 = new Properties(1,0,"Go","NONE",0);
        Properties property2 = new Properties(2,10,"Saloniki","Grecja",120);
        Properties property3 = new Properties(3,0,"BlueQuestionMark","NONE",0);
        Properties property4 = new Properties(4,10,"Ateny","Grecja",120);
        Properties property5 = new Properties(5,400,"Parking","NONE",0);
        Properties property6 = new Properties(6,50,"Linie Kolejowe Południowe","NONE",400);
        Properties property7 = new Properties(7,15,"Neapol","Włochy",200);
        Properties property8 = new Properties(8,0,"RedQuestionMark","NONE",0);
        Properties property9 = new Properties(9,15,"Mediolan","Włochy",200);
        Properties property10 = new Properties(10,20,"Rzym","Włochy",240);
        Properties property11 = new Properties(11,0,"Wiezienie","NONE",0);
        Properties property12 = new Properties(12,20,"Barcelona","Hiszpania",280);
        Properties property13 = new Properties(13,10,"Elektrownia","NONE",300);
        Properties property14 = new Properties(14,20,"Sewilla","Hiszpania",280);
        Properties property15 = new Properties(15,25,"Madryt","Hiszpania",320);
        Properties property16 = new Properties(16,50,"Linie Kolejowe Wschodnie","NONE",400);
        Properties property17 = new Properties(17,30,"Liverpool","Anglia",360);
        Properties property18 = new Properties(18,0,"BlueQuestionMark","NONE",0);
        Properties property19 = new Properties(19,30,"Glasgow","Anglia",360);
        Properties property20 = new Properties(20,35,"Londyn","Anglia",360);
        Properties property21 = new Properties(21,0,"Darmowy Parking","NONE",0);
        Properties property22 = new Properties(22,35,"Rotterdam","Benelux",440);
        Properties property23 = new Properties(23,0,"RedQuestionMark","NONE",0);
        Properties property24 = new Properties(24,35,"Bruksela","Benelux",440);
        Properties property25 = new Properties(25,40,"Amsterdam","Benelux",480);
        Properties property26 = new Properties(26,50,"Linie Kolejowe Wschodnie","NONE",400);
        Properties property27 = new Properties(27,45,"Malmo","Szwecja",520);
        Properties property28 = new Properties(28,45,"Goteborg","Szwecja",520);
        Properties property29 = new Properties(29,10,"Wodociągi","NONE",300);
        Properties property30 = new Properties(30,50,"Sztokholm","Szwecja",560);
        Properties property31 = new Properties(31,0,"Idziesz do Wiezienia","NONE",0);
        Properties property32 = new Properties(32,55,"Frankfurt","RFN",600);
        Properties property33 = new Properties(33,55,"Kolonia","RFN",600);
        Properties property34 = new Properties(34,0,"BlueQuestionMark","NONE",0);
        Properties property35 = new Properties(35,60,"Bonn","RFN",640);
        Properties property36 = new Properties(36,50,"Linie Kolejowe Zachodnie","NONE",400);
        Properties property37 = new Properties(37,0,"RedQuestionMark","NONE",0);
        Properties property38 = new Properties(38,70,"Insbruck","Austria",700);
        Properties property39 = new Properties(39,200,"Podatek","NONE",0);
        Properties property40 = new Properties(40,100,"Wieden","Austria",800);

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
