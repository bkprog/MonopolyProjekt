package Application.Client2;

public class Properties2 {
    private String Name;
    private int paymentForStay;
    private int IDproperty;
    private int buyCost;
    private String countryName;
    private int ownerID = 0;

    public Properties2(){
        this.IDproperty = 0;
        this.Name = " ";
        this.paymentForStay = 0;
        this.countryName = " ";
        this.buyCost = 0;
    }

    public Properties2(int IDproperty,int paymentForStay, String nameProperty,String countryName,int buyCost){
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

    public void setNameProperty(String nameProperty){
        this.Name = nameProperty;
    }

    public void setPaymentForStay(int paymentForStay){
        this.paymentForStay = paymentForStay;
    }

    public void setIDproperty(int IdPropertyrty){
        this.IDproperty = IdPropertyrty;
    }

    public void setBuyCost(int buyCost){
        this.buyCost = buyCost;
    }

    public void setCountryName(String countryName){
        this.countryName = countryName;
    }

    public void setOwnerID(int ownerID){
        this.ownerID = ownerID;
    }

}
