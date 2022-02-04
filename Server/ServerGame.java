package ss.Scrabble.Server;

import ss.Scrabble.*;
import ss.Scrabble.Game.*;
import ss.Scrabble.Game.Dictionary;

import java.util.*;

public class ServerGame {

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

    public ServerGame(ArrayList<ClientHandler> playerHandlers) {
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

//    Start some game objects
    public void start() {
        this.dictionary = this.dict.getDictionary();
        fillAlphToInt();
        for(Player p : players){
            List<Tile> initTiles = letterBag.getRandomTiles(7);
            p.setRack(initTiles);
        }
    }

//    Return the string rep. of new tiles, internally also put them in the rack
    public String giveNewTilesToPlayer(int num, Player p){
        StringBuilder res = new StringBuilder();
        List<Tile> newTiles = null;
        if(this.letterBag.numTilesLeft() >= num){
            newTiles = letterBag.getRandomTiles(num);
            p.getRack().putNewTilesOnRack(newTiles);
        } else {
            newTiles = letterBag.getRandomTiles(letterBag.numTilesLeft());
            p.getRack().putNewTilesOnRack(newTiles);
        }
        for (Tile boi : newTiles) {
            res.append(boi.getLetter());
        }
        p.newLetters = res.toString();
        return res.toString();
    }

//    Swap, return the string rep. of swapped
    public String swapLetters(String letters, Player p){
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
        return giveNewTilesToPlayer(letters.length(), p);
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

    private int getExtraPoints(List<String> words){
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
                return false;
            }
        }
        return true;
    }

    public boolean isInDictionary(String word){
//        TODO: maybe use contains
        for(String dictEnrty : this.dictionary){
            if(dictEnrty.equals(word)){
                return true;
            }
        }
        return false;
    }

    public Protocol.Error playMove(String name, String move) {

        Player player = getPlayerByName(name);
        String[] commands = Protocol.parseAll(move);
        String pos = commands[1];
        String orientation = commands[2];
        String wordToPlay = commands[3];
        int row = getRowCol(pos)[0];
        int col = getRowCol(pos)[1];
        List<String> wordsOnBoard = this.board.getWordsOnBoard();

        if (!validMove(pos, orientation, wordToPlay, name).equals(Protocol.Error.NoError)) {return validMove(pos, orientation, wordToPlay, name);}

        String playLetters = "";
        if (orientation.equals("H")) {
            playLetters = this.board.getLettersToPlayHorizontal(row, col, wordToPlay);
            this.board.setWordHorizontal(row, col, wordToPlay);
        } else {
            playLetters = this.board.getLettersToPlayVertical(row, col, wordToPlay);
            this.board.setWordVertical(row, col, wordToPlay);
        }
        List<String> newWords = getNewWords(wordsOnBoard, this.board.getWordsOnBoard());
        newWords.remove(wordToPlay);
        int extra = getExtraPoints(newWords);
        player.addPoints(util.getWordValueOnBoardHorizontal(row, col, wordToPlay) + extra);
        player.getRack().removeTiles(util.getWordTiles(playLetters));
        giveNewTilesToPlayer(playLetters.length(), player);

        return Protocol.Error.NoError;
    }

    public boolean isOver() {
        return this.over;
    }

    public Protocol.Error makeMove(String name, String m) {
        Protocol.Error e = playMove(name, m);



//        TODO: check if the game should be over

        return e;
    }

    public Protocol.Error validMove(String pos, String orientation, String word, String playerName) {

        Player player = getPlayerByName(playerName);

        List<String> oldWords = this.board.getWordsOnBoard();
        List<Tile> copyRack = player.getRack().getCopy();
        Board copyBoard = this.board.deepCopy();

        int row = getRowCol(pos)[0];
        int col = getRowCol(pos)[1];

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

    public boolean swap(String name, String rest) {
//        TODO: swap with the give player
//        TODO: notify the player with the new boys
        return false;
    }

    public void skip(String name) {
//        TODO: notify all the players about it
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
