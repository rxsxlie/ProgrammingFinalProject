package ss.Scrabble;

import java.util.Set;

public class Player {
    String name;
    Rack rack;

    public Player(String name){
        this.name = name;
        this.rack = new Rack();
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


}
