package ss.Scrabble;

import java.util.HashSet;
import java.util.Set;

public class Util {
    LetterBag letterBag;
    Board board;
    Set<Tile> tilesSet;

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

    public Set<Tile> getWordTiles(String word){
        char[] letters = word.toCharArray();
        Set<Tile> tiles = letterBag.getAlphabetTiles();
        Set<Tile> wordTiles = new HashSet<Tile>();
        for(int i = 0; i < letters.length; i++){
            for(Tile t : tiles){
                if(letters[i] == t.getLetter()){
                    wordTiles.add(t);
                }
            }
        }
        return wordTiles;
    }

    public int getWordValueOnBoardHorizontal(int row, int col, String word){
        int count = 0;
        int dwCount = 0;
        int twCount = 0;
        Set<Tile> wordTiles = getWordTiles(word);
        for(int i = col; i < (col+word.length()); i++){
            Tile t = wordTiles.iterator().next();
            if(this.board.getCells()[row][i].getCellValue() == Cell.CellValue.DOUBLE_LETTER){
                count += (t.getValue()*2);
            } else if (this.board.getCells()[row][i].getCellValue() == Cell.CellValue.TRIPLE_LETTER){
                count += (t.getValue()*3);
            } else if (this.board.getCells()[row][i].getCellValue() == Cell.CellValue.DOUBLE_WORD){
                dwCount += 1;
                count += t.getValue();
            } else if (this.board.getCells()[row][i].getCellValue() == Cell.CellValue.TRIPLE_WORD) {
                twCount += 1;
                count += t.getValue();
            } else {
                count += t.getValue();
            }
        }
        return count * 2 * 3 * dwCount * twCount;
    }

    public int getWordValueOnBoardVertical(int row, int col, String word){
        int count = 0;
        int dwCount = 0;
        int twCount = 0;
        Set<Tile> wordTiles = getWordTiles(word);
        for(int i = row; i < (row+word.length()); i++){
            Tile t = wordTiles.iterator().next();
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
        }
        return count * 2 * 3 * dwCount * twCount;
    }
}
