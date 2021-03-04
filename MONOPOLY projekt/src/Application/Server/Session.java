package Application.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class Session extends Thread {
    private ArrayList<Socket> socketPlayers;
    private ArrayList<Player> playersList;
    private Socket client;
    int playerNumber;
    int numberOfPlayersInGame;
    int playersReady = 0;

    public Session(Socket client, ArrayList<Socket> socketArray,int np,int numberOfPlayers,ArrayList<Player> playersList){
        this.client = client;
        this.socketPlayers = socketArray;
        playerNumber = np;
        numberOfPlayersInGame = numberOfPlayers;
        this.playersList = playersList;
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

}
