package Application.Sources;

public class BlueRedCards {
    private String cardText;
    private int cash;
    private int destinationField;

    public BlueRedCards(String cardText,int cash,int destinationField){
        this.cardText = cardText;
        this.cash = cash;
        this.destinationField = destinationField;
    }

    public int getCash() {
        return this.cash;
    }

    public String getCardText() {
        return this.cardText;
    }

    public int getDestinationField() {
        return this.destinationField;
    }
}
