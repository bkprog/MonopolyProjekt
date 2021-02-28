package Application.ServerApplication;

public class Player {
    int playerId;
    static String playerName;
    int money;
    boolean isPalyerInJail = false;

    public Player(String Name,int playerId){
        this.playerId = playerId;
        playerName = Name;
        this.money = 1500;
    }
}
