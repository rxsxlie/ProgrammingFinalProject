package ss.Scrabble.Client;
import ss.Scrabble.*;
import ss.Scrabble.Game.*;
import ss.Scrabble.Game.Dictionary;
import ss.Scrabble.Server.ClientHandler;

import java.util.*;
public class ClientGame {
    Board board;
    ArrayList<Player> players = new ArrayList<>();
    LetterBag letterBag;
    Dictionary dict;
    List<String> playedWords;
    Set<String> dictionary;
    Util util;
    Map<Character, Integer> alphToInt = new HashMap<Character, Integer>();
    private boolean over = false;

    public ClientGame(ArrayList<String> players) {

        for (String name: players) {
            this.players.add(new Player(name));
        }
        this.board = new Board();
        this.letterBag = new LetterBag();
        this.dict = new Dictionary();
        this.playedWords = new ArrayList<String>();
        util = new Util(this.letterBag, this.board);
    }

    //    Given a player name, get the player object
    public Player getPlayerByName(String name) {
        for (Player player: this.players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }

    public void fillAlphToInt(){
        int i = 0;
        for(char c = 'A'; c <= 'O'; c++){
            this.alphToInt.put(c, i);
            i++;
        }
    }

    public void setRack(String player, String letters) {
        Player p = getPlayerByName(player);
        ArrayList<Tile> tiles = new ArrayList<>();
        char[] charLetters = letters.toCharArray();

        for (char c : charLetters) {
            tiles.add(new Tile(c, 1));
        }
        p.setRack(tiles);
    }

    public void removeFromRack(String name, String letters) {
        Player p = getPlayerByName(name);
        ArrayList<Tile> tiles = new ArrayList<>();
        char[] charLetters = letters.toCharArray();

        for (char c : charLetters) {
            tiles.add(new Tile(c, 1));
        }
        p.getRack().removeTiles(tiles);
    }

    public void updateRack(String name, String letters) {
        Player p = getPlayerByName(name);
        ArrayList<Tile> tiles = new ArrayList<>();
        char[] charLetters = letters.toCharArray();

        for (char c : charLetters) {
            tiles.add(new Tile(c, 1));
        }
        p.getRack().putNewTilesOnRack(tiles);

    }

    //    Start some game objects
    public void start() {
        this.dictionary = this.dict.getDictionary();
        fillAlphToInt();
    }

    private List<String> getNewWords(List<String> oldWords, List<String> newWords){
        List<String> words = new ArrayList<>();
        for(String newWord : newWords){
            if(!oldWords.contains(newWord)){
                words.add(newWord);
            }
        }
        return words;
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
                return false;
            }
        }
        return true;
    }

    public boolean isInDictionary(String word){
        for(String dictEnrty : this.dictionary){
            if(dictEnrty.equals(word)){
                return true;
            }
        }
        return false;
    }

    public String setMove(String move) {
        String[] commands = Protocol.parseAll(move);
        String pos = commands[0];
        String orientation = commands[1];
        String wordToPlay = commands[2];
        int row = getRowCol(pos)[0];
        int col = getRowCol(pos)[1];

        String toRemove = "";

        if (orientation.equals("H")) {
            toRemove = this.board.setWordHorizontal(row, col, wordToPlay);
        } else if (orientation.equals("V")) {
            toRemove = this.board.setWordVertical(row, col, wordToPlay);
        }

        return toRemove;


    }

    public Protocol.Error validMove(String playerName, String move) {

        String[] commands = Protocol.parseAll(move);
        String pos = commands[0];
        String orientation = commands[1];
        String word = commands[2];
        int row = getRowCol(pos)[0];
        int col = getRowCol(pos)[1];

        Player player = getPlayerByName(playerName);

        List<String> oldWords = this.board.getWordsOnBoard();
        List<Tile> copyRack = player.getRack().getCopy();
        Board copyBoard = this.board.deepCopy();

        if (orientation.equals("H") && copyBoard.isValidWordSpaceHorizontal(row, col, word)) {
            List<Tile> tilesNeededToPlay = util.getWordTiles(copyBoard.getLettersToPlayHorizontal(row, col, word));
            if (!hasAllTiles(tilesNeededToPlay, copyRack, player)) {
                return Protocol.Error.E008;
            }
            copyBoard.setWordHorizontal(row, col, word);
            List<String> words = copyBoard.getWordsOnBoard();
            List<String> newWords = getNewWords(oldWords, words);
            for(String w : newWords){
                if(!isInDictionary(w)){
                    return Protocol.Error.E006;
                }
            }
            return Protocol.Error.NoError;
        } else if (orientation.equals("V") && copyBoard.isValidWordSpaceVertical(row, col, word)) {
            List<Tile> tilesNeededToPlay = util.getWordTiles(copyBoard.getLettersToPlayVertical(row, col, word));
            if (!hasAllTiles(tilesNeededToPlay, copyRack, player)) {
                return Protocol.Error.E008;
            }
            copyBoard.setWordVertical(row, col, word);
            List<String> words = copyBoard.getWordsOnBoard();
            List<String> newWords = getNewWords(oldWords, words);
            for(String w : newWords){
                if(!isInDictionary(w)){
                    return Protocol.Error.E006;
                }
            }
            return Protocol.Error.NoError;
        } else {
            return Protocol.Error.E003;
        }
    }

    private int[] getRowCol(String pos) {
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
        int[] res = new int[2];
        res[0] = row;
        res[1] = col;

        return res;

    }


}
