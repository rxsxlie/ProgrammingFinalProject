package ss.Scrabble;

import java.util.HashSet;
import java.util.Set;

public class Rack {
    int lettersOnRack;
    Set<Tile> tileSet;

    public Rack(){
        lettersOnRack = 7;
        this.tileSet = getEmptyTileSet();
    }

    public Set<Tile> getEmptyTileSet(){
        Set<Tile> emptyTileSet = new HashSet<Tile>();
        for(int i = 0; i < this.lettersOnRack; i++){
            Tile temp = new Tile(' ', 0);
            emptyTileSet.add(temp);
        }
        return emptyTileSet;
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

    public void removeTiles(Set<Tile> playedTiles){
        for(Tile playedTile : playedTiles) {
            this.tileSet.removeIf(rackTile -> rackTile.getLetter() == playedTile.getLetter());
        }
    }

    public Set<Tile> getCopy(){
        Set<Tile> copy = new HashSet<Tile>();
        for(Tile t : this.tileSet){
            Tile copyT = new Tile(t.getLetter(), t.getValue());
            copy.add(copyT);
        }
        return copy;
    }

    public String toString(){
        String out = "Your rack: \n";
        for (Tile t : tileSet){
            out += "- | " + t.getLetter() + " | -";
        }
        return out;
    }
}
