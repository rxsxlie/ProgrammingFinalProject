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

    public void setName(String name){
        this.name = name;
    }

    public Rack getRack(){
        return this.rack;
    }

    public void setRack(Set<Tile> newTiles){
        this.rack.putNewTilesOnRack(newTiles);
    }

    public String rackToString(){
        Set<Tile> tilesOnRack = this.rack.getTileSet();
        String res = "";
        for(Tile t : tilesOnRack){
            res += (" - " + t.getLetter() + " - ");
        }
        return res + "\n";
    }

    public void addPoints(int points){
        this.score += points;
    }


}
