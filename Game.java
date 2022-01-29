package ss.Scrabble;

import ss.utils.TextIO;

import java.util.Set;

public class Game {
    Board board;
    Player[] players;
    LetterBag letterBag;

    public Game(String[] playerNames){
        this.players = new Player[playerNames.length];
        int i = 0;
        for(Player p : players){
            p = new Player(playerNames[i]);
            i++;
        }
        this.board = new Board();
        this.letterBag = new LetterBag();
    }

    public void start(){
        board.setDefaultPremiumFields();
        for(Player p : players){
            Set<Tile> initTiles = letterBag.getRandomTiles(7);
            p.setRack(initTiles);
        }
    }

    public void turn(){

    }

    public String[] getUserInput(Player p){
        System.out.println(p.getName() + ", it's your turn to play a word. \n Please give it in the format: word h/v row col");
        String input = (String) (TextIO.getln());
        String[] inputVals =  input.split(" ");
        return inputVals;
    }
}
