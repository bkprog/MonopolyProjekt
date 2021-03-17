package Application.Sources;

public class BlueRedCards {
    private int CardId;
    private String cardText;
    private int cashReward;
    private int cashFine;
    private int fieldsForward;
    private int fieldsBackward;
    private int destinationField;

    public BlueRedCards(int cardId,String cardText,int cashReward,int cashFine,int fieldsForward,int fieldsBackward,int destinationField){
        this.CardId = cardId;
        this.cardText = cardText;
        this.cashReward = cashReward;
        this.cashFine = cashFine;
        this.fieldsForward = fieldsForward;
        this.fieldsBackward = fieldsBackward;
        this.destinationField = destinationField;
    }

    public int getCardId(){
        return this.CardId;
    }

    public int getCashReward() {
        return this.cashReward;
    }

    public int getCashFine(){
        return this.cashFine;
    }

    public int getFieldsForward(){
        return this.fieldsForward;
    }

    public int getFieldsBackward(){
        return this.fieldsBackward;
    }

    public String getCardText() {
        return this.cardText;
    }

    public int getDestinationField() {
        return this.destinationField;
    }
}
