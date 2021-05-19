package Test.Source;
import Source.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlayerTest {
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

        assertEquals(playertest.getPlayerName(),nameTest);

        assertEquals(playertest.getPlayerNumber(),TestplayerNumber);
        assertEquals(playertest.getCash(),Testcash);
        assertEquals(playertest.getIsInJail(),TestisInJail);
        assertEquals(playertest.getPropertyId(),TestpropertyId);
        assertTrue(playertest.isBankroupt());
    }
}

