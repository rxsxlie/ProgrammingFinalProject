package ss.Scrabble;

import ss.utils.TextIO;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Game {
    Board board;
    Player[] players;
    LetterBag letterBag;
    Dictionary dictionary;
    Util util;
    Map<Character, Integer> alphToInt = new HashMap<Character, Integer>();

    public Game(String[] playerNames){
        this.players = new Player[playerNames.length];
        int i = 0;
        for(Player p : players){
            p = new Player(playerNames[i]);
            i++;
        }
        this.board = new Board();
        this.letterBag = new LetterBag();
        this.dictionary = new Dictionary();
        util = new Util(this.letterBag, this.board);
    }

    public void play() throws IOException {
        start();
        while(this.letterBag.numTilesLeft() != 0){
            for(Player p : players){
                turn(p);
            }
        }
        System.out.println("End game");

    }

    public void start() throws IOException {
        this.dictionary = (Dictionary) dictionary.getDictionary();
        for(Player p : players){
            Set<Tile> initTiles = letterBag.getRandomTiles(7);
            p.setRack(initTiles);
        }
    }

    public void turn(Player p){
        String[] input = getUserInput(p);
        String makemove = input[0];
        String move = input[1];
        if(makemove.equals("MAKEMOVE")){
            if(move.equals("WORD")){
                this.playWord(input[2], input[3], input[4], p);
                giveNewTilesToPlayer(input[4], p);
            } else if(move.equals("SWAP")){
                String lettersToRemove = swapLetters(input[2]);
                giveNewTilesToPlayer(lettersToRemove, p);
            }
        }
    }

    public void giveNewTilesToPlayer(String playedWord, Player p){
        p.getRack().removeTiles(util.getWordTiles(playedWord));
        if(this.letterBag.numTilesLeft() >= playedWord.length()){
            p.getRack().putNewTilesOnRack(letterBag.getRandomTiles(playedWord.length()));
        } else {
            p.getRack().putNewTilesOnRack(letterBag.getRandomTiles(letterBag.numTilesLeft()));
        }
    }

    public String swapLetters(String letters){
        char[] letterArr = letters.toCharArray();
        String lettersToRemove = "";
        int i = 0;
        while(letterArr[i] == '!'){
            lettersToRemove += letterArr[i];
            i++;
        }
        return lettersToRemove;
    }

    public void playWord(String pos, String orientation, String word, Player p){
        char[] startPos = pos.toCharArray();
        char c = startPos[0];
        int col = alphToInt.get(c);
        String r = "";
        int row;
        if(startPos.length > 2){
            r += startPos[1]+startPos[2];
        } else {
            r += startPos[1];
        }
        row = Integer.parseInt(r);
        if(orientation.equals("H") && this.board.hasWordSpaceHorizontal(row, col, word)){
            this.board.setWordHorizontal(row, col, word);
            p.addPoints(util.getWordValueOnBoardHorizontal(row, col, word));
        } else if(orientation.equals("V") && this.board.hasWordSpaceVertical(row, col, word)){
            this.board.setWordVertical(row, col, word);
            p.addPoints(util.getWordValueOnBoardVertical(row, col, word));
        }
    }

    public String[] getUserInput(Player p){
        System.out.println(p.getName() + ", it's your turn to play a word. \n Please give it in the format: MAKEMOVE WORD/SWAP (COLROW H/V WORD)/ABC!");
        String input = (String) (TextIO.getln());
        String[] inputVals =  input.split(" ");
        return inputVals;
    }

    public boolean isInDictionary(String word){
        return false;
    }


}
