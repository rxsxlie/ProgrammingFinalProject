package ss.Scrabble;

import java.util.*;

public class LetterBag {
    Map<Tile, Integer> lettersLeft = new HashMap<Tile, Integer>();
    Set<Tile> tiles = new HashSet<Tile>();

    public LetterBag(){
        this.createTiles();
        this.setLettersLeft();
    }

    public void setLettersLeft(){
        int[] startTileAmount = new int[]{9,2,2,4,12,2,2,2,8,2,2,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1,2};
        int i = 0;
        for(Tile t : tiles){
            lettersLeft.put(t, startTileAmount[i]);
            i++;
        }
    }

    public void createTiles(){
        int[] tileValues = new int[]{1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
        int i = 0;
        for(char alphabet = 'A'; alphabet <='Z'; alphabet++){
            Tile t = new Tile(alphabet, tileValues[i]);
            tiles.add(t);
            i++;
        }
        Tile blank = new Tile('b', 0);
        tiles.add(blank);
    }





}
