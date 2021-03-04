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

    public void setNameProperty(String nameProperty){
        this.Name = nameProperty;
    }

    public String getNameProperty(){
        return this.Name;
    }

    public void setIDproperty(int IdPropertyrty){
        this.IDproperty = IdPropertyrty;
    }

    public int getIDproperty(){
        return this.IDproperty;
    }
}
