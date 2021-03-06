package ss.Scrabble;

import java.util.*;
import ss.Scrabble.Game.*;


public class Util {
    LetterBag letterBag;
    Board board;
    List<Tile> tilesSet;

    public Util(LetterBag letterBag, Board board){
        this.letterBag = letterBag;
        this.tilesSet = letterBag.getAlphabetTiles();
        this.board = board;
    }

    public int getWordValue(String word){
        int count = 0;
        char[] letters = word.toCharArray();
        for (char letter : letters) {
            for (Tile t : tilesSet) {
                if (t.getLetter() == letter) {
                    count += t.getValue();
                }
            }
        }
        return count;
    }

    public List<Tile> getWordTiles(String word){
        char[] letters = word.toCharArray();
        List<Tile> tiles = letterBag.getAlphabetTiles();
        List<Tile> wordTilesList = new ArrayList<>(word.length());
        for(int i = 0; i < letters.length; i++){
            for(Tile t : tiles){
                if(letters[i] == t.getLetter()){
                    Tile temp = new Tile(letters[i], t.getValue());
                    wordTilesList.add(i, temp);
                }
            }
        }
        return wordTilesList;
    }

    public int getWordValueOnBoardHorizontal(int row, int col, String word){
        int count = 0;
        int dwCount = 0;
        int twCount = 0;
        int letterIdx = 0;
        List<Tile> wordTiles = getWordTiles(word);
        for(int i = col; i < (col+word.length()); i++){
            Tile t = wordTiles.get(letterIdx);
            if(this.board.getCells()[row][i].getCellValue() == Cell.CellValue.DOUBLE_LETTER){
                count += (t.getValue()*2);
            } else if (this.board.getCells()[row][i].getCellValue() == Cell.CellValue.TRIPLE_LETTER){
                count += (t.getValue()*3);
            } else if (this.board.getCells()[row][i].getCellValue() == Cell.CellValue.DOUBLE_WORD){
                dwCount++;
                count += t.getValue();
            } else if (this.board.getCells()[row][i].getCellValue() == Cell.CellValue.TRIPLE_WORD) {
                twCount++;
                count += t.getValue();
            } else {
                count += t.getValue();
            }
            letterIdx ++;
        }
        return (int) (count * Math.pow(2, dwCount) * Math.pow(3, twCount));
    }

    public int getWordValueOnBoardVertical(int row, int col, String word){
        int count = 0;
        int dwCount = 0;
        int twCount = 0;
        int letterIdx = 0;
        List<Tile> wordTiles = getWordTiles(word);
        for(int i = row; i < (row+word.length()); i++){
            Tile t = wordTiles.get(letterIdx);
            if(this.board.getCells()[i][col].getCellValue() == Cell.CellValue.DOUBLE_LETTER){
                count += (t.getValue()*2);
            } else if (this.board.getCells()[i][col].getCellValue() == Cell.CellValue.TRIPLE_LETTER){
                count += (t.getValue()*3);
            } else if (this.board.getCells()[i][col].getCellValue() == Cell.CellValue.DOUBLE_WORD){
                dwCount += 1;
                count += t.getValue();
            } else if (this.board.getCells()[i][col].getCellValue() == Cell.CellValue.TRIPLE_WORD) {
                twCount += 1;
                count += t.getValue();
            } else {
                count += t.getValue();
            }
            letterIdx++;
        }
        return (int) (count * Math.pow(2, dwCount) * Math.pow(3, twCount));
    }
}
