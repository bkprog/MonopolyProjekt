package Application;

public class field {
    int fieldNubmer;
    String FieldName;
    private int countryNumber;
    private int price;
    private int housePrice;

    public int getPrice(){
        return this.price;
    }

    public int getHousePrice(){
        return this.housePrice;
    }

    public field(String name){
        FieldName = name;
    }



}
