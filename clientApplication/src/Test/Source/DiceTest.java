package Test.Source;

import Source.Dice;
import org.junit.jupiter.api.Test;

public class DiceTest {
    private int result;
    private int result2;

    @Test
    public void throwfunctionTest(){
        final Dice dicetest= new Dice();
        result= dicetest.throwfunction();
        System.out.println(result);
        if(result!=1 && result!=2 &&result!=3 && result!=4 &&result!=5 && result!=6)
        {
            throw new IllegalStateException("Dice throwing result is not in 1-6");
        }
    }


    @Test
    public void throwQuestionMarkCardTest(){
        final Dice dicetest2= new Dice();
        result2= dicetest2.throwQuestionMarkCard();
        System.out.println(result2);
        if(result2<0 || result2>16)
        {
            throw new IllegalStateException("Dice throwing result is not in 1-16");
        }
    }
}