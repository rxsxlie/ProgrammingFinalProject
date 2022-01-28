package ss.Scrabble;

import java.util.HashMap;
import java.util.Map;

public class Tile {
    private int value;
    private char letter;

    public Tile(int value, char letter){
        this.value = value;
        this.letter = letter;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public char getLetter() {
        return letter;
    }

    public void setLetter(char letter) {
        this.letter = letter;
    }
}
