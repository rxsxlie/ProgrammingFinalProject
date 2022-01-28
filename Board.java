package ss.Scrabble;

import ss.week4.tictactoe.Mark;

import java.util.Arrays;

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
                numbering[i] = ("+---++---++---++---++---++---++---++---++---++---++---++---++---++---++---+");
            }
        }
        return numbering;
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


    public boolean hasWordSpaceHorizontal(int row, int col, String word) {
        for(int i = col; i < (col+word.length()); i++){
            if(!isEmptyField(row, i) || !isField(row, i)){
                return false;
            }
        }
        return true;
    }

    public boolean hasWordSpaceVertical(int row, int col, String word) {
        for(int i = row; i < (row+word.length()); i++){
            if(!isEmptyField(i, col) || !isField(i, col)){
                return false;
            }
        }
        return true;
    }

    public void setWordHorizontal(int row, int col, String word){
        char[] wordArray = word.toCharArray();
        int i = 0;
        if(this.hasWordSpaceHorizontal(row, col, word)) {
            for (char letter : wordArray) {
                setField(row, (col + i), letter);
                i++;
            }
        } else {
            System.out.println("There is no space for this word on the board");
        }
    }

    public void setWordVertical(int row, int col, String word){
        char[] wordArray = word.toCharArray();
        int i = 0;
        if(this.hasWordSpaceVertical(row, col, word)) {
            for (char letter : wordArray) {
                setField((row + i), col, letter);
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
        String s = "";
        for (int i = 0; i < DIM; i++) {
            String row = "";
            for (int j = 0; j < DIM; j++) {
                row = row + " " + getField(i, j).toString() + " ";
                if (j < DIM - 1) {
                    row = row + "|";
                }
            }
            s = s + row + DELIM + NUMBERING[i * 2];
            if (i < DIM - 1) {
                s = s + "\n" + LINE + DELIM + NUMBERING[i * 2 + 1] + "\n";
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
