package Application.ServerApplication;

public class Player {
    int playerId;
    static String playerName;
    int money;

    public Player(String Name,int playerId){
        this.playerId = playerId;
        playerName = Name;
        this.money = 1500;
    }
}
