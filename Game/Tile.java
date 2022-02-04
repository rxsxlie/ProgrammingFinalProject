package ss.Scrabble.Game;


public class Tile{
    private int value;
    private char letter;

    public Tile(char letter, int value){
        super();
        this.letter = letter;
        this.value = value;
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
