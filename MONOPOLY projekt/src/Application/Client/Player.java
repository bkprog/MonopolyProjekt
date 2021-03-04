package Application.Client;

public class Player {
    private String name;
    private int playerNumber;
    private int cash;
    boolean isInJail = false;
    int propertyId = 0;

    public void setPropertyId(int propertyId){
        this.propertyId = propertyId;
    }

    public int getPropertyId(){
        return this.propertyId;
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
