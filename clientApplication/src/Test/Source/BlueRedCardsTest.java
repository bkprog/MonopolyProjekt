package Test.Source;

import Source.BlueRedCards;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BlueRedCardsTest {

    @Test
    public void BlueRedCardsConstructorandgettest()
    {
        final BlueRedCards blueredcardstest =new BlueRedCards(1,"tekst karty",200,100,0,0,10);

        assertEquals(blueredcardstest.getCardId(),1);
        assertEquals(blueredcardstest.getCardText(),"tekst karty");
        assertEquals(blueredcardstest.getCashReward(),200);
        assertEquals(blueredcardstest.getCashFine(),100);
        assertEquals(blueredcardstest.getFieldsForward(),0);
        assertEquals(blueredcardstest.getFieldsBackward(),0);
        assertEquals(blueredcardstest.getDestinationField(),10);

    }
}
