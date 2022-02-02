package ss.Scrabble;

import java.util.Set;

public class Player {
    String name;
    Rack rack;
    int score;

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

    public void setRack(Set<Tile> newTiles){
        this.rack.putNewTilesOnRack(newTiles);
        Set<Tile> empty = this.rack.getEmptyTileSet();
        this.rack.removeTiles(empty);
    }

    public void addPoints(int points){
        this.score += points;
    }

    public int getScore(){
        return this.score;
    }


}
