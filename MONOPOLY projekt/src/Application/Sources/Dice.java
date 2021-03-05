package Application.Sources;

import java.util.Random;

public class Dice {
    private int dice;

    public Dice(){
        this.dice = throwfunction();
    }

    public int throwfunction(){
        Random generator = new Random();
        dice = generator.nextInt(6)+1;
        return dice;
    }

    public int getDice(){
        return this.dice;
    }
}
