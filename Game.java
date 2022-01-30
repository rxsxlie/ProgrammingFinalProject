package ss.Scrabble;

import ss.utils.TextIO;

import javax.sound.midi.Soundbank;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Game {
    Board board;
    Player[] players;
    LetterBag letterBag;
    Dictionary dict;
    Set<String> dictionary;
    Util util;
    Map<Character, Integer> alphToInt = new HashMap<Character, Integer>();

    public Game(Player[] players) throws IOException {
        int i = 0;
        this.players = players;
        this.board = new Board();
        this.letterBag = new LetterBag();
        this.dict = new Dictionary();
        util = new Util(this.letterBag, this.board);
    }

    public void play() throws IOException {
        start();
        while(this.letterBag.numTilesLeft() != 0){
            for(Player player : players){
                System.out.println(this.board.toString());
                System.out.println("It is " + player.getName() + " 's turn now!");
                System.out.println(player.getRack().toString());
                turn(player);
                System.out.println(player.getName() + " now has " + player.getScore() + " points.");
            }
        }
        System.out.println("End game");
    }

    public void start() throws IOException {
        this.dictionary = this.dict.getDictionary();
        fillAlphToInt();
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
                if(isInDictionary(input[4])) {
                    this.playWord(input[2], input[3], input[4], p);
                    giveNewTilesToPlayer(input[4], p);
                }
            } else if(move.equals("SWAP")){
                String lettersToRemove = swapLetters(input[2]);
                giveNewTilesToPlayer(lettersToRemove, p);
            }
        }
    }

    public void giveNewTilesToPlayer(String playedWord, Player p){
//        p.getRack().removeTiles(util.getWordTiles(playedWord));
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
        boolean hasAllTiles = true;
        char[] startPos = pos.toCharArray();
        char c = startPos[0];
        int col = alphToInt.get(c)-1;
        String r = "";
        int row;
        if(startPos.length > 2){
            r += startPos[1]+startPos[2];
        } else {
            r += startPos[1];
        }
        row = Integer.parseInt(r);
        Set<Tile> copyRack = p.getRack().getCopy();
        if(orientation.equals("H") && this.board.isValidWordSpaceHorizontal(row, col, word)){
            String tilesToPlay = this.board.getTilesToPlayHorizontal(row, col, word);
            Set<Tile> tilesToPlaySet = util.getWordTiles(tilesToPlay);
            hasAllTiles = hasAllTiles(tilesToPlaySet, copyRack, p);
            if(hasAllTiles){
                this.board.setWordHorizontal(row, col, word);
                p.addPoints(util.getWordValueOnBoardHorizontal(row, col, word));
                p.getRack().removeTiles(tilesToPlaySet);
            }
        } else if(orientation.equals("V") && this.board.isValidWordSpaceVertical(row, col, word)){
            String tilesToPlay = this.board.getTilesToPlayVertical(row, col, word);
            Set<Tile> tilesToPlaySet = util.getWordTiles(tilesToPlay);
            hasAllTiles = hasAllTiles(tilesToPlaySet, copyRack, p);
            if(hasAllTiles){
                this.board.setWordVertical(row, col, word);
                p.addPoints(util.getWordValueOnBoardVertical(row, col, word));
            }
        }
    }

    private boolean hasAllTiles(Set<Tile> tilesToPlaySet, Set<Tile> copyRack, Player p) {
        for(Tile wordTile : tilesToPlaySet) {
            boolean tileRemoved = false;
            for (Tile playersTile : p.getRack().getTileSet()) {
                if (wordTile.getLetter() == playersTile.getLetter()) {
                    copyRack.remove(playersTile);
                    tileRemoved = true;
                }
            }
            if (!tileRemoved) {
                return false;
            }
        }
        return true;
    }

    public String[] getUserInput(Player p){
        System.out.println(p.getName() + ", it's your turn to play a word. \n Please give it in the format: MAKEMOVE WORD/SWAP (COLROW H/V WORD)/ABC!");
        String input = (String) (TextIO.getln());
        String[] inputVals =  input.split(" ");
        return inputVals;
    }

    public boolean isInDictionary(String word){
        for(String dictEnrty : this.dictionary){
            if(dictEnrty.equals(word)){
                return true;
            }
        }
        System.out.println("This word is not in the dictionary");
        return false;
    }

    public void fillAlphToInt(){
        int i = 0;
        for(char c = 'A'; c < 'O'; c++){
            this.alphToInt.put(c, i);
            i++;
        }
    }


}
