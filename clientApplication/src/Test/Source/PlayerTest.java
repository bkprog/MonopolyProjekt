
import Source.Player;
import org.junit.jupiter.api.Test;

public class PlayerTest {



    @Test
    public void playerDefaultsettingsTest(){
        final Player playertest= new Player();

        if(playertest.getIsInJail()==true){
            throw new IllegalStateException("Player is in jail before game start");
        }
        if(playertest.isBankroupt()==true){
            throw new IllegalStateException("Player is bankroupt before game start");
        }
        if(playertest.getPropertyId()!=0)
        {
            throw new IllegalStateException("Player PropertyID is not 0 on the start");
        }
    }
}
