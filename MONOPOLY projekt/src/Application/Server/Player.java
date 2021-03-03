package Application.Server;

public class Player {
    private String name;
    private int playerNumber;
    private int cash;

    public Player(String playerName,int no,int c){
        this.name = playerName;
        this.playerNumber = no;
        this.cash = c;
    }

    public String getPlayerName(){
        return this.name;
    }

    public void setPlayerName(String newName){
        this.name = newName;
    }

    public int getCash(){
        return this.cash;
    }

    public void setCash(int newCash){
        this.cash = newCash;
    }

    public int getPlayerNumber(){
        return this.playerNumber;
    }

    public void setPlayerNumber(int newPlayerNumber){
        this.playerNumber = newPlayerNumber;
    }
}
