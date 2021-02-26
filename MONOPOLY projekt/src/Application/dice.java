package Application;
import java.util.Random;

public class dice {
    int zmienna;

    public int throwfunction(){
        Random generator=new Random();
    zmienna=generator.nextInt(6)+1;
    return zmienna;
    }
}
