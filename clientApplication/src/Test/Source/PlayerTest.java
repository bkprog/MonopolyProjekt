package Test.Source;
import Source.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PlayerTest {
    public void playerDefaultsettingsTest(){
        final Player playertest= new Player();

        assertFalse(playertest.getIsInJail());
        assertFalse(playertest.isBankroupt());
        assertEquals(playertest.getPropertyId(),0);

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

