package Test.Source;

import Source.Properties;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PropertiesTest {



    @Test
    public void PropertiesConstructorandGetTest(){
        final Properties PropertiesTest1= new Properties(10,100,"nazwa","kielce",200,10,20,30,40);

        assertEquals(PropertiesTest1.getIDproperty(),10);
        assertEquals(PropertiesTest1.getPaymentForStay(),100);
        assertEquals(PropertiesTest1.getNameProperty(),"nazwa");
        assertEquals(PropertiesTest1.getCountryName(),"kielce");
        assertEquals(PropertiesTest1.getBuyCost(),200);
        assertEquals(PropertiesTest1.getLvl1(),10);
        assertEquals(PropertiesTest1.getLvl2(),20);
        assertEquals(PropertiesTest1.getLvl3(),30);
        assertEquals(PropertiesTest1.getLvl4(),40);
    }

    @Test

    public void IfbuyableHousePropertyTest(){
        final Properties PropertiesTest2= new Properties(10,100,"Linie kolejowe","kielce",200,10,20,30,40);
        final Properties PropertiesTest3= new Properties(10,100,"nazwa","kielce",200,10,20,30,40);
        assertFalse(PropertiesTest2.ifBuyableHouseproperty());
        assertTrue(PropertiesTest3.ifBuyableHouseproperty());
    }

}
