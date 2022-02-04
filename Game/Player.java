package ss.Scrabble.Game;

import java.util.List;

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
        this.rack.clear();
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
