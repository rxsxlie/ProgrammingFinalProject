package ss.Scrabble.Game;

import java.util.ArrayList;
import java.util.List;

public class Rack {
    int lettersOnRack;
    List<Tile> tileSet;

    public Rack(){
        lettersOnRack = 7;
        this.tileSet = getEmptyTileSet();
    }

    public List<Tile> getEmptyTileSet(){
        List<Tile> emptyTileSet = new ArrayList<Tile>(this.lettersOnRack);
        for(int i = 0; i < this.lettersOnRack; i++){
            Tile temp = new Tile(' ', 0);
            emptyTileSet.add(temp);
        }
        return emptyTileSet;
    }

    public List<Tile> getTileSet(){
        return this.tileSet;
    }

    public int getLettersOnRack(){
        return this.tileSet.size();
    }

    public void putNewTilesOnRack(List<Tile> newTiles){
        if(newTiles.size()<=lettersOnRack){
            this.tileSet.addAll(newTiles);
        }
    }

    public void removeTiles(List<Tile> playedTiles){
        for(Tile playedTile : playedTiles) {
            this.tileSet.removeIf(rackTile -> rackTile.getLetter() == playedTile.getLetter());
        }
    }

    public List<Tile> getCopy(){
        List<Tile> copy = new ArrayList<>();
        for(Tile t : this.tileSet){
            Tile copyT = new Tile(t.getLetter(), t.getValue());
            copy.add(copyT);
        }
        return copy;
    }

    public String toString(){
        String out = "Your rack: \n";
        for (Tile t : this.tileSet){
            out += "- | " + t.getLetter() + " | -";
        }
        return out;
    }

    public String getLetters(){
        String out = "";
        for (Tile t : this.tileSet){
            out += t.getLetter();
        }
        return out;
    }
}
