package ss.Scrabble.Game;

import java.util.ArrayList;
import java.util.List;

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
        Board copy = new Board();
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                char letter = this.cells[i][j].getLetter();
                copy.cells[i][j].setLetter(letter);
            }
        }
        return copy;
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
                    return false;
                }
                if(row == 7 && i == 7){
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
            if(this.cells[row-1][col].getLetter() != ' '){
                overlaps = true;
            }
            if(this.cells[row+1][col].getLetter() != ' '){
                overlaps = true;
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
                    return false;
                }
                if(row == 7 && i == 7){
                    overlaps = true;
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
                if(this.cells[row][col-1].getLetter() != ' '){
                    overlaps = true;
                }
                if(this.cells[row][col+1].getLetter() != ' '){
                    overlaps = true;
                }
                lettersI++;
            }
        }
        return overlaps;
    }

    public List<String> getWordsOnBoard(){
        List<String> words = new ArrayList<>();
        //get horizontal words
        for(int row = 0; row < DIM; row++){
            String wordH = "";
            for (int col = 0; col < DIM; col++){
                if(this.cells[row][col].getLetter() != ' '){
                    wordH += this.cells[row][col].getLetter();
                    if(wordH.length() < 2) {
                        continue;
                    }
                    if(isField(row, col+1) && this.cells[row][col+1].getLetter() == ' '){
                        words.add(wordH);
                        wordH = "";
                    } else if (!isField(row,  col+1)){
                        words.add(wordH);
                    }
                }
            }
        }
        //get vertical
        for(int col = 0; col < DIM; col++){
            String wordV = "";
            for(int row = 0; row < DIM; row++){
                if(this.cells[row][col].getLetter() != ' '){
                    wordV += this.cells[row][col].getLetter();
                    if(wordV.length() < 2) {
                        continue;
                    }
                    if(isField(row+1, col) && this.cells[row+1][col].getLetter() == ' '){
                        words.add(wordV);
                        wordV = "";
                    } else if (!isField(row+1,  col)){
                        words.add(wordV);
                    }
                }
            }
        }
        return words;
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
        return lettersToPlay;
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

//    public List<String> getAdjacentWordsHorizontal(int row, int col, String word){
//        char[] wordArray = word.toCharArray();
//        List<String> adjacentWords = new ArrayList<>();
//        for(int i = 0; i < (wordArray.length); i++){
//            List<Character> wordUp = new ArrayList<>();
//            List<Character> wordDown = new ArrayList<>();
//            if(cells[row-1][col+i].getLetter() != ' '){
//                int upIdx = 0;
//                while(cells[row-1-upIdx][col+i].getLetter() != ' ') {
//                    wordUp.add(0, cells[row - upIdx][col+1+i].getLetter());
//                    upIdx++;
//                }
//            }
//            if(cells[row+1][col+i].getLetter() != ' '){
//                int downIdx = 0;
//                while(cells[row+1+downIdx][col+i].getLetter() != ' ') {
//                    wordDown.add((wordDown.size()), cells[row+1+downIdx][col+i].getLetter());
//                    downIdx++;
//                }
//            }
//            String up= "";
//            String down = "";
//            for(Character c : wordUp){
//                up+=c;
//            }
//            for(Character c : wordDown){
//                down+=c;
//            }
//            adjacentWords.add(up);
//            adjacentWords.add(down);
//            adjacentWords.add(up + wordArray[i] + down);
//        }
//        return adjacentWords;
//    }


//    public List<String> getAdjacentWordsVertical(int row, int col, String word){
//        char[] wordArray = word.toCharArray();
//
//        List<String> adjacentWords = new ArrayList<>();
//        for(int i = 0; i < wordArray.length; i++){
//            List<Character> wordLeft = new ArrayList<>();
//            List<Character> wordRight = new ArrayList<>();
//            if(cells[row+i][col-1].getLetter() != ' '){
//                int leftIdx = 0;
//                while(cells[row+i][col-1-leftIdx].getLetter() != ' ') {
//                    wordLeft.add(0, cells[row+i][col-1-leftIdx].getLetter());
//                    leftIdx++;
//                }
//            }
//            if(cells[row+i][col+1].getLetter() != ' '){
//                int rightIdx = 0;
//                while(cells[row+i][col+1+rightIdx].getLetter() != ' ') {
//                    wordRight.add((wordRight.size()), cells[row+i][col+1+rightIdx].getLetter());
//                    rightIdx++;
//                }
//            }
//            String left= "";
//            String right = "";
//            for(Character c : wordLeft){
//                left+=c;
//            }
//            for(Character c : wordRight){
//                right+=c;
//            }
//            adjacentWords.add(left);
//            adjacentWords.add(right);
//            adjacentWords.add(left + wordArray[i] + right);
//        }
//        return adjacentWords;
//    }

    public String setWordHorizontal(int row, int col, String word){
        String toRemove = getLettersToPlayHorizontal(row, col ,word);
        char[] wordArray = word.toCharArray();
        int i = 0;
        for (char letter : wordArray) {
            setField(row, (col + i), letter);
            i++;
        }

        return toRemove;
    }

    public String setWordVertical(int row, int col, String word){
        String toRemove = getLettersToPlayVertical(row, col ,word);
        char[] wordArray = word.toCharArray();
        int i = 0;

        for (char letter : wordArray) {
            if(getField((row+i), col).getLetter() == ' ') {
                setField((row + i), col, letter);
            }
            i++;
        }

        return toRemove;
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