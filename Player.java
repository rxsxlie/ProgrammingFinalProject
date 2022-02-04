package ss.Scrabble;

import java.util.List;
import java.util.Set;

public class Player {
    String name;
    Rack rack;
    int score;
    public String newLetters = "";

    public Player(String name){
        this.name = name;
        this.rack = new Rack();
        this.score = 0;
    }

    public String getName(){
        return this.name;
    }

    public Rack getRack(){
        return this.rack;
    }

    public void setRack(List<Tile> newTiles){
        //remove place holders
        List<Tile> empty = this.rack.getEmptyTileSet();
        this.rack.removeTiles(empty);
        //append init tiles
        this.rack.putNewTilesOnRack(newTiles);
    }

    public void addPoints(int points){
        this.score += points;
    }

    public int getScore(){
        return this.score;
    }


}
