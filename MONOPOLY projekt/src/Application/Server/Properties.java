package Application.Server;

public class Properties {
    private String Name;
    private int paymentForStay;
    private int IDproperty;
    private int buyCost;
    private String countryName;
    private int ownerID = 0;

    public Properties(int IDproperty,int paymentForStay, String nameProperty,String countryName,int buyCost){
        this.IDproperty = IDproperty;
        this.Name = nameProperty;
        this.paymentForStay = paymentForStay;
        this.countryName = countryName;
        this.buyCost = buyCost;
    }

    public String getNameProperty(){
        return this.Name;
    }

    public int getPaymentForStay(){
        return this.paymentForStay;
    }

    public int getIDproperty(){
        return this.IDproperty;
    }

    public int getBuyCost(){
        return this.buyCost;
    }

    public String getCountryName(){
        return this.countryName;
    }

    public int getOwnerID(){
        return this.ownerID;
    }

    public void setIDproperty(int IdPropertyrty){
        this.IDproperty = IdPropertyrty;
    }

    public void setNameProperty(String nameProperty){
        this.Name = nameProperty;
    }

}
