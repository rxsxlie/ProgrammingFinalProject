package ss.Scrabble;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LetterBag {
    Map<Tile, Integer> lettersLeft;
    Random rand = new Random();

    public LetterBag(){
        lettersLeft = this.fillLetterBag();
    }

    public Map<Tile,Integer> fillLetterBag(){
        Map<Tile, Integer> lettersLeft = new ConcurrentHashMap<Tile, Integer>();
        List<Tile> tiles = this.getAlphabetTiles();
        int[] startTileAmount = new int[]{9,2,2,4,12,2,2,2,8,2,2,4,2,6,8,2,1,6,4,6,4,2,2,1,2,1,2};
        int i = 0;
        for(Tile t : tiles){
            lettersLeft.put(t, startTileAmount[i]);
            i++;
        }
        return lettersLeft;
    }


    public List<Tile> getAlphabetTiles(){
        List<Tile> tiles = new ArrayList<>();
        int[] tileValues = new int[]{1,3,3,2,1,4,2,4,1,8,5,1,3,1,1,3,10,1,1,1,1,4,4,8,4,10};
        int i = 0;

        for(char alphabet = 'A'; alphabet <='Z'; alphabet++){
            Tile t = new Tile(alphabet, tileValues[i]);
            tiles.add(t);
            i++;
        }
        Tile blank = new Tile('!', 0);
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

    public List<Tile> getRandomTiles(int numTiles){
        List<Tile> randomTiles = new ArrayList<Tile>();
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
            if(t.getLetter() == tile.getLetter()){
                if (t.getValue() > 1) {
                    Tile temp = new Tile(t.getLetter(), t.getValue());
                    int left = t.getValue() - 1;
                    lettersLeft.remove(t);
                    lettersLeft.put(temp, left);
                } else if (t.getValue() == 1) {
                    lettersLeft.remove(t);
                } else {
                    System.out.println("Letter could not be removed from letter bag");
                }
            }
        }
    }

    public void putInLetterBag(Set<Tile> tiles){
        for(Tile returnedTile : tiles){
            for(Tile bagTile : this.lettersLeft.keySet()){
                if(returnedTile.getLetter() == bagTile.getLetter()){
                    int left = lettersLeft.get(bagTile) + 1;
                    lettersLeft.remove(bagTile);
                    lettersLeft.put(bagTile, left);
                }
            }
        }
    }
}
