package Application.ServerApplication;

import java.util.Random;

public class Dice {
    int random;

    public int throwFunction(){
        Random generator=new Random();
        random = generator.nextInt(6)+1;
        return random;
    }
}
