package ss.Scrabble;

/**
 * Board for the Tic Tac Toe game. Module 2 lab assignment.
 *
 * @author Theo Ruys en Arend Rensink
 * @version $Revision: 1.4 $
 */
public class Board {
    public static final int DIM = 15;
    private static final String[] NUMBERING = createNumbering();
    private static final String LINE = NUMBERING[1];
    private static final String DELIM = "     ";


    private String[] board = NUMBERING;
    private int numTiles = 100;

    /**
     * The DIM by DIM fields of the Tic Tac Toe board. See NUMBERING for the
     * coding of the fields.
     * @invariant there are always DIM*DIM fields
     * @invariant all fields are either Mark.EMPTY, Mark.XX or Mark.OO
     */
    private Cell[][] cells;

    // -- Constructors -----------------------------------------------

    /**
     * Creates an empty board.
     * @ensures all fields are EMPTY
     */
    public Board() {
        this.cells = new Cell[DIM][DIM];
        for (int row = 0; row < DIM; row++){
            for (int col = 0; col < DIM; col++){
                cells[row][col] = new Cell(Cell.CellValue.NORMAL);
            }
        }
        setDefaultPremiumFields();
        reset();
    }

    public static String createNumberingLine(int startPoint){
        String line = "";
        for(int i = startPoint; i < startPoint + 15; i++){
            line += ("+ " + i + " +");
        }
        return line;
    }

    public static String[] createNumbering(){
        String[] numbering = new String[DIM*DIM-1];
        int startPoint = 1;
        for(int i = 0; i < numbering.length; i++){
            if(i%2 == 0){
                numbering[i] = createNumberingLine(startPoint);
                startPoint += 15;
            } else {
                numbering[i] = ("__________________________________________________________________________");
            }
        }
        return numbering;
    }

    public Cell[][] getCells(){
        return this.cells;
    }

    /**
     * Creates a deep copy of this field.
     * @ensures the result is a new object, so not this object
     * @ensures the values of all fields of the copy match the ones of this Board
     */
    public Board deepCopy() {
        Board board = new Board();
        Board boardCopy = board;
        return boardCopy;
    }

    /**
     * Returns true if index is a valid index of a field on the board.
     * @ensures a positive result when the index is between 0 and DIM*DIM
     * @return true if 0 <= index < DIM*DIM
     */
    public boolean isField(int row, int col) {
        return row < DIM && col < DIM && row >= 0 && col >= 0;
    }

    /**
     * Returns the content of the field i.
     * @requires i to be a valid field
     * @ensures the result to be either EMPTY, XX or OO
     * @param row the number of the field (see NUMBERING)
     * @return the mark on the field
     */
    public Cell getField(int row, int col) {
        if(isField(row, col)){
            return this.cells[row][col];
        } else {
            return null;
        }
    }

    public void setPremiumField(int row, int col, Cell.CellValue cellValue){
        this.cells[row-1][col-1] = new Cell(cellValue);
    }

    public void setDefaultPremiumFields(){
//        Triple word value
        setPremiumField(1,1, Cell.CellValue.TRIPLE_WORD);
        setPremiumField(1,8, Cell.CellValue.TRIPLE_WORD);
        setPremiumField(1,15, Cell.CellValue.TRIPLE_WORD);
        setPremiumField(8,1, Cell.CellValue.TRIPLE_WORD);
        setPremiumField(8,15, Cell.CellValue.TRIPLE_WORD);
        setPremiumField(15,1, Cell.CellValue.TRIPLE_WORD);
        setPremiumField(15,8, Cell.CellValue.TRIPLE_WORD);
        setPremiumField(15,15, Cell.CellValue.TRIPLE_WORD);
//        Double word value
        setPremiumField(2,2, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(2,14, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(3,3, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(3,13, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(4, 4, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(4,12, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(5,5, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(5,11, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(11,5, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(11,11, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(12,4, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(12,12, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(13,3, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(13,13, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(14,2, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(14,14, Cell.CellValue.DOUBLE_WORD);
        setPremiumField(8,8, Cell.CellValue.DOUBLE_WORD);
//        Double letter value
        setPremiumField(1,4, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(1,12, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(3,7, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(3,9, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(4,1, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(4,8, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(4,15, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(7,3, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(7,7, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(7,9, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(7,13, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(8,4, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(8,12, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(9,3, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(9,7, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(9,9, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(9,13, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(12,1, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(12,8, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(12,15, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(13,7, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(13,9, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(15,4, Cell.CellValue.DOUBLE_LETTER);
        setPremiumField(15,12, Cell.CellValue.DOUBLE_LETTER);
//      Tripple letter value
        setPremiumField(2,6, Cell.CellValue.TRIPLE_LETTER);
        setPremiumField(2,10, Cell.CellValue.TRIPLE_LETTER);
        setPremiumField(6,2, Cell.CellValue.TRIPLE_LETTER);
        setPremiumField(6,6, Cell.CellValue.TRIPLE_LETTER);
        setPremiumField(6,10, Cell.CellValue.TRIPLE_LETTER);
        setPremiumField(6,14, Cell.CellValue.TRIPLE_LETTER);
        setPremiumField(10,2, Cell.CellValue.TRIPLE_LETTER);
        setPremiumField( 10,6, Cell.CellValue.TRIPLE_LETTER);
        setPremiumField(10,10, Cell.CellValue.TRIPLE_LETTER);
        setPremiumField(10,14, Cell.CellValue.TRIPLE_LETTER);
        setPremiumField(14,6, Cell.CellValue.TRIPLE_LETTER);
        setPremiumField(14,10, Cell.CellValue.TRIPLE_LETTER);
    }

    /**
     * Returns true if the field referred to by the (row,col) pair it empty.
     * @requires (row, col) to be a valid field
     * @ensures true when the Mark at (row, col) is EMPTY
     * @param row the row of the field
     * @param col the column of the field
     * @return true if the field is empty
     */
    public boolean isEmptyField(int row, int col) {
        if(isField(row, col)){
            return this.cells[row][col].getLetter() == ' ';
        }
        return false;
    }

    /**
     * Tests if the whole board is full.
     * @ensures true if all fields are occupied
     * @return true if all fields are occupied
     */
    public boolean noTilesLeft() {
        return numTiles == ' ';
    }

    /**
     * Returns true if the game is over. The game is over when there is a winner
     * or the whole board is full.
     * @ensures true if the board is full or when there is a winner
     * @return true if the game is over
     */
    public boolean gameOver() {
        return noTilesLeft();
    }


    public boolean isValidWordSpaceHorizontal(int row, int col, String word) {
        char[] letters = word.toCharArray();
        boolean overlaps = false;
        int lettersI = 0;
        if(isBoardEmpty()){
            for(int i = col; i < (col+word.length()); i++){
                if(!isField(row, i)){
                    System.out.println("Space is out of scope of the board");
                    return false;
                }
                if(row == 7 && i == 7){
                    System.out.println("Overlapping H8!");
                    overlaps = true;
                }
                lettersI++;
            }
        } else {
        for(int i = col; i < (col+word.length()); i++){
            if(!isField(row, i)){
                return false;
            }
            if(!isEmptyField(row, i)){
                if(getField(row, i).getLetter() == letters[lettersI]){
                    overlaps = true;
                }
            }
            lettersI++;
        }
        }
        return overlaps;
    }

    public boolean isValidWordSpaceVertical(int row, int col, String word) {
        char[] letters = word.toCharArray();
        boolean overlaps = false;
        int lettersI = 0;
        if(isBoardEmpty()){
            for(int i = col; i < (col+word.length()); i++){
                if(!isField(row, i)){
                    System.out.println("Space is out of scope of the board");
                    return false;
                }
                if(row == 7 && i == 7){
                    overlaps = true;
                    System.out.println("Overlapping H8!");
                }
                lettersI++;
            }
        } else {
            for (int i = row; i < (row + word.length()); i++) {
                if (!isField(i, col)) {
                    return false;
                }
                if (!isEmptyField(i, col)) {
                    if (getField(i, col).getLetter() == letters[lettersI]) {
                        overlaps = true;
                    }
                }
                lettersI++;
            }
        }
        return overlaps;
    }

    public String getLettersToPlayHorizontal(int row, int col, String word){
        char[] wordArray = word.toCharArray();
        int i = 0;
        String lettersToPlay = "";
        for (char letter : wordArray) {
            if (getField(row, (col + i)).getLetter() == ' ') {
                lettersToPlay += letter;
            }
            i++;
        }
        System.out.println(lettersToPlay);
        return lettersToPlay;
    }

    public String getLettersToPlayVertical(int row, int col, String word){
        char[] wordArray = word.toCharArray();
        int i = 0;
        String lettersToPlay = "";
        for (char letter : wordArray) {
            if (getField((row+i), col).getLetter() == ' ') {
                lettersToPlay += letter;
            }
            i++;
        }
        System.out.println(lettersToPlay);
        return lettersToPlay;
    }

    public void setWordHorizontal(int row, int col, String word){
        char[] wordArray = word.toCharArray();
        int i = 0;
        for (char letter : wordArray) {
            setField(row, (col + i), letter);
            i++;
        }
    }

    public boolean isBoardEmpty(){
        for(Cell[] cs : this.cells){
            for(Cell c : cs){
                if(c.getLetter() != ' '){
                    return false;
                }
            }
        }
        return true;
    }

    public void setWordVertical(int row, int col, String word){
        char[] wordArray = word.toCharArray();
        int i = 0;
        if(this.isValidWordSpaceVertical(row, col, word)) {
            for (char letter : wordArray) {
                if(getField((row+i), col).getLetter() == ' ') {
                    setField((row + i), col, letter);
                }
                i++;
            }
        } else {
            System.out.println("There is no space for this word on the board");
        }
    }


    /**
     * Checks if the mark m has won. A mark wins if it controls at
     * least one row, column or diagonal.
     * @requires m to be either XX or OO
     * @ensures true when m has a row, column or diagonal
     * @return true if the mark has won
     */
    public boolean isWinner() {
        return false;
    }

    /**
     * Returns true if the game has a winner. This is the case when one of the
     * marks controls at least one row, column or diagonal.
     * @ensures true when either XX or OO has won
     * @return true if the student has a winner.
     */
    public boolean hasWinner() {
        return false;
    }

    /**
     * Returns a String representation of this board. In addition to the current
     * situation, the String also shows the numbering of the fields.
     *
     * @return the game situation as String
     */
    public String toString() {
        String s = "   A    B    C    D    E    F    G    H    I    J    K    L    M    N    O    \n" + "__________________________________________________________________________ \n";
        for (int i = 0; i < DIM; i++) {
            String row = String.valueOf(i+1);
            for (int j = 0; j < DIM; j++) {
                row = row + " " + getField(i, j).toString() + "  ";
                if (j < DIM - 1) {
                    row = row + "|";
                }
            }
            s = s + row + DELIM;
            if (i < DIM - 1) {
                s = s + "\n" + LINE + DELIM + "\n";
            }
        }
        return s;
    }

    /**
     * Empties all fields of this board (i.e., let them refer to the value
     * Mark.EMPTY).
     * @ensures all fields are EMPTY
     */
    public void reset() {
        for(int row = 0; row < DIM; row++){
            for(int col= 0; col < DIM; col++){
                this.cells[row][col].setLetter(' ');
            }
        }
    }

    /**
     * Sets the content of field i to the mark m.
     * @requires i to be a valid field
     * @ensures field i to be set to Mark m
     */
    public void setField(int row, int col, char c) {
        this.cells[row][col].setLetter(c);
    }


}