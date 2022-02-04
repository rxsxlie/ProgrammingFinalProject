package ss.Scrabble;

import jdk.swing.interop.SwingInterOpUtils;
import ss.utils.TextIO;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class Game {
    Board board;
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<ClientHandler> playerHandlers;
    LetterBag letterBag;
    Dictionary dict;
    List<String> playedWords;
    Set<String> dictionary;
    Util util;
    Map<Character, Integer> alphToInt = new HashMap<Character, Integer>();
    private boolean over = false;

    public Game(ArrayList<ClientHandler> playerHandlers) {
        this.playerHandlers = playerHandlers;
        for (ClientHandler clientHandler: this.playerHandlers) {
            this.players.add(new Player(clientHandler.getName()));
        }

        this.board = new Board();
        this.letterBag = new LetterBag();
        this.dict = new Dictionary();
        this.playedWords = new ArrayList<String>();
        util = new Util(this.letterBag, this.board);
    }

    public Player getPlayerByName(String name) {
        for (Player player: this.players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public void play() throws IOException {
//        start();
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

    public void start() {
        this.dictionary = this.dict.getDictionary();
        fillAlphToInt();
        for(Player p : players){
            List<Tile> initTiles = letterBag.getRandomTiles(7);
            p.setRack(initTiles);
        }
    }

    public void turn(Player p){
        String[] input = getUserInput(p);
        String makemove = input[0];
        String move = input[1];
        boolean hasPlayed = false;
        if(makemove.equals("MAKEMOVE")){
            if(move.equals("WORD")){
                while(!hasPlayed) {
                    if(isInDictionary(input[4])) {
                        hasPlayed = this.playWord(input[2], input[3], input[4], p);
                        if(!hasPlayed){
                            turn(p);
                        } else {
                            return;
                        }
                    } else {
                        turn(p);
                    }
                }
            } else if(move.equals("SWAP")){
                while(!hasPlayed) {
                    hasPlayed = swapLetters(input[2], p);
                }
            }
        }
    }

    public void giveNewTilesToPlayer(int num, Player p){
        if(this.letterBag.numTilesLeft() >= num){
            p.getRack().putNewTilesOnRack(letterBag.getRandomTiles(num));
        } else {
            p.getRack().putNewTilesOnRack(letterBag.getRandomTiles(letterBag.numTilesLeft()));
        }
    }

    public boolean swapLetters(String letters, Player p){
        List<Tile> swapped = new ArrayList<>();
        char[] letterArr = letters.toCharArray();
        for(char letter : letterArr) {
            for (Tile rackTile : p.getRack().getTileSet()) {
                if (rackTile.getLetter() == letter) {
                    swapped.add(rackTile);
                    break;
                }
            }
        }
        for(Tile swap : swapped){
            p.getRack().getTileSet().remove(swap);
        }
        giveNewTilesToPlayer(letters.length(), p);
        System.out.println("New rack now looks like this : ");
        System.out.println(p.getRack().toString());
        return swapped.size() == letters.length();
    }

    public boolean playWord(String pos, String orientation, String word, Player p){
        boolean hasAllTiles = false;
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

        row = Integer.parseInt(r) - 1;
        List<Tile> copyRack = p.getRack().getCopy();

        if(orientation.equals("H") && this.board.isValidWordSpaceHorizontal(row, col, word)){
            List<String> oldWords = this.board.getWordsOnBoard();
            String playLetters = this.board.getLettersToPlayHorizontal(row, col, word);
            List<Tile> tilesToPlaySet = util.getWordTiles(playLetters);
            hasAllTiles = hasAllTiles(tilesToPlaySet, copyRack, p);
            if (hasAllTiles) {
                this.board.setWordHorizontal(row, col, word);
                List<String> words = this.board.getWordsOnBoard();
                List<String> newWords = getExtraPoints(word, oldWords, words);
                for(String w : newWords){
                    if(!isInDictionary(w)){
                        return false;
                    }
                }
                int extra = getExtraPoints(newWords);
                p.addPoints(util.getWordValueOnBoardHorizontal(row, col, word) + extra);
                p.getRack().removeTiles(tilesToPlaySet);
                giveNewTilesToPlayer(playLetters.length(), p);
                this.playedWords.add(word);
                return true;
            } else {
                return false;
            }
        } else if(orientation.equals("V") && this.board.isValidWordSpaceVertical(row, col, word)){
            List<String> oldWords = this.board.getWordsOnBoard();
            String playLetters = this.board.getLettersToPlayVertical(row, col, word);
            List<Tile> tilesToPlaySet = util.getWordTiles(playLetters);
            hasAllTiles = hasAllTiles(tilesToPlaySet, copyRack, p);
            if (hasAllTiles) {
                this.board.setWordVertical(row, col, word);
                List<String> words = this.board.getWordsOnBoard();
                System.out.println("WORDS: " + words);
                List<String> newWords = getExtraPoints(word, oldWords, words);
                System.out.println("NEWWORDS: " + newWords);

                for(String w : newWords){
                    if(!isInDictionary(w)){
                        return false;
                    }
                }
                int extra = getExtraPoints(newWords);
                p.addPoints(util.getWordValueOnBoardVertical(row, col, word) + extra);
                p.getRack().removeTiles(tilesToPlaySet);
                giveNewTilesToPlayer(playLetters.length(), p);
                this.playedWords.add(word);
                return true;
            } else {
                return false;
            }
        } else {
            System.out.println("This is not a valid space for your word.");
            return false;
        }
    }

    public List<String> getExtraPoints(String word, List<String> oldWords, List<String> newWords){
        List<String> words = new ArrayList<>();
        for(String newWord : newWords){
            if(!oldWords.contains(newWord) && newWord != word){
                words.add(newWord);
            }
        }
        return words;
    }

    public int getExtraPoints(List<String> words){
        int extraPoints = 0;
        for(String w : words){
            if(isInDictionary(w)) {
                extraPoints += util.getWordValue(w);
            } else {
                break;
            }
        }
        return extraPoints;
    }

    private boolean hasAllTiles(List<Tile> tilesToPlaySet, List<Tile> copyRack, Player p) {
        for(Tile wordTile : tilesToPlaySet) {
            boolean tileRemoved = false;
            for (Tile playersTile : p.getRack().getTileSet()) {
                if (wordTile.getLetter() == playersTile.getLetter()) {
                    copyRack.remove(playersTile);
                    tileRemoved = true;
                }
            }
            if (!tileRemoved) {
                System.out.println(p.getName() + " does not have all tiles.");
                return false;
            }
        }
        System.out.println(p.getName() + " has all tiles!");
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
        System.out.println(word + " is not in the dictionary.");
        return false;
    }

    public void fillAlphToInt(){
        int i = 0;
        for(char c = 'A'; c <= 'O'; c++){
            this.alphToInt.put(c, i);
            i++;
        }
    }


    public boolean isOver() {
//        TODO: actually check
        return this.over;
    }

    public void makeMove(String name, String m) {
//        TODO: make move
        System.out.println("I am the game and a move has been played by " + name);
    }

    public boolean validMove(String m) {
//        TODO: do check valid move
        return true;
    }

    public boolean swap(String name, String rest) {
//        TODO: swap with the give player
//        TODO: notify the player with the new boys
        System.out.println("Swap by "+ name);
        return false;
    }

    public void skip(String name) {
//        TODO: notify all the players about it
    }
}
