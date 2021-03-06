package ss.Scrabble;


public class Cell {

    public enum CellValue{
        START, DOUBLE_LETTER, TRIPLE_LETTER, DOUBLE_WORD, TRIPLE_WORD, NORMAL;
    }

    CellValue cellValue;
    Tile tile;

    public Cell(CellValue cellValue){
        this.cellValue = cellValue;
        this.tile = new Tile(' ', 0);
    }

    public char getLetter(){
        return this.tile.getLetter();
    }

    public void setLetter(char letter){
        this.tile.setLetter(letter);
    }

    public CellValue getCellValue(){
        return this.cellValue;
    }

    public String toString(){
        return String.valueOf(this.tile.getLetter());
    }
}

