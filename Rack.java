package ss.Scrabble;

import java.util.Set;

public class Rack {
    int lettersOnRack;
    Set<Tile> tileSet;

    public Rack(){
        lettersOnRack = 7;
    }

    public Set<Tile> getTileSet(){
        return this.tileSet;
    }

    public int getLettersOnRack(){
        return this.tileSet.size();
    }

    public void putNewTilesOnRack(Set<Tile> newTiles){
        if(newTiles.size()<=7){
            this.tileSet.addAll(newTiles);
        }
    }

    public void removeTiles(Set<Tile> word){
        for(Tile t : word){
            this.tileSet.remove(t);
        }
    }
}
