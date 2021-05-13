
import Source.Player;
import org.junit.jupiter.api.Test;

public class PlayerTest {



    @Test
    public void playerDefaultsettingsTest(){
        final Player playertest= new Player();

        if(playertest.getIsInJail()){
            throw new IllegalStateException("Player is in jail before game start");
        }
        if(playertest.isBankroupt()){
            throw new IllegalStateException("Player is bankroupt before game start");
        }
        if(playertest.getPropertyId()!=0)
        {
            throw new IllegalStateException("Player PropertyID is not 0 on the start");
        }
    }

    @Test
    public void PlayerSetandgetTest(){
        final Player playertest= new Player();

        String nameTest="Janusz";
        int TestplayerNumber=1;
        int Testcash=1000;
        boolean TestisInJail = false;
        int TestpropertyId = 0;


        playertest.setPlayerName(nameTest);
        playertest.setPlayerNumber(TestplayerNumber);
        playertest.setCash(Testcash);
        playertest.setInJail(TestisInJail);
        playertest.setPropertyId(TestpropertyId);
        playertest.makeBankropt();

        if(playertest.getPlayerName()!=nameTest){
            throw new IllegalStateException("wrong name setting");
        }
        if(playertest.getPlayerNumber()!=TestplayerNumber){
            throw new IllegalStateException("wrong player number setting");
        }
        if(playertest.getCash()!=Testcash){
            throw new IllegalStateException("wrong cash setting");
        }
        if(playertest.getIsInJail()!=TestisInJail){
            throw new IllegalStateException("wrong jail status setting");
        }
        if(playertest.getPropertyId()!=TestpropertyId){
            throw new IllegalStateException("wrong name setting");
        }
        if(playertest.isBankroupt()==false){
            throw new IllegalStateException("making bancroput doesn't work");
        }
    }
}


