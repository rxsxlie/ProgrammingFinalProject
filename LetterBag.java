package ss.Scrabble;

import java.util.*;

public class LetterBag {
    Map<Tile, Integer> lettersLeft;
    Random rand = new Random();

    public LetterBag(){
        lettersLeft = this.setInitLetters();
    }

    public Map<Tile,Integer> setInitLetters(){
        Map<Tile, Integer> lettersLeft = new HashMap<Tile, Integer>();
        Set<Tile> tiles = this.createTiles();
        int[] startTileAmount = new int[]{9,2,2,4,12,2,2,2,8,2,2,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1,2};
        int i = 0;
        for(Tile t : tiles){
            lettersLeft.put(t, startTileAmount[i]);
            i++;
        }
        return lettersLeft;
    }


    public Set<Tile> createTiles(){
        Set<Tile> tiles = new HashSet<Tile>();
        int[] tileValues = new int[]{1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
        int i = 0;

        for(char alphabet = 'A'; alphabet <='Z'; alphabet++){
            Tile t = new Tile(alphabet, tileValues[i]);
            tiles.add(t);
            i++;
        }
        Tile blank = new Tile('b', 0);
        tiles.add(blank);
        return tiles;
    }


    public int numTilesLeft(){
        int count = 0;
        for(Tile t : this.lettersLeft.keySet()){
            count += t.getValue();
        }
        return count;
    }

    public Set<Tile> getRandomTiles(int numTiles){
        Set<Tile> randomTiles = new HashSet<Tile>();
        while(randomTiles.size() < numTiles){
            int randI = rand.nextInt(this.lettersLeft.keySet().size());
            int i = 0;
            for(Tile t : this.lettersLeft.keySet()){
                if(i == randI && t.getValue() != 0){
                    randomTiles.add(t);
                    removeLetterFromLetterBag(t);
                }
                i++;
            }
        }
        return randomTiles;
    }

    public void removeLetterFromLetterBag(Tile tile){
        for(Tile t : this.lettersLeft.keySet()){
            if(t == tile){
                if (t.getValue() > 1) {
                    lettersLeft.put(t, t.getValue() - 1);
                } else if (t.getValue() == 1) {
                    lettersLeft.remove(t);
                } else {
                    System.out.println("Letter could not be removed from letter bag");
                }
            }
        }
    }
}
